package com.mc2ads.manager;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mc2ads.db.SqlDbConfigs;
import com.mc2ads.model.Video;

public class VideoManagerImpl {

	public VideoManagerImpl() {
	}

	public List<Long> getRelatedVideoIds(int dbid, long id) {
		Connection dbConnection;
		CallableStatement cs;
		String query;
		List<Long> ids;
		dbConnection = null;
		cs = null;
		ResultSet rs = null;
		query = "{call GET_RESULT_ASSOCIATION(?,?,?)}";
		ids = new ArrayList<Long>();
		try {			
			dbConnection = SqlDbConfigs.load("db_oracle_video").getConnection();
			cs = dbConnection.prepareCall(query);
			cs.setInt(1, dbid);
			cs.setLong(2, id);
			cs.registerOutParameter(3, -10);
			cs.execute();
			for (rs = (ResultSet) cs.getObject(3); rs.next(); ids.add(new Long(
					rs.getString(1))))
				;

		} catch (SQLException e) {
			logger.error(e);
		} finally {
			try {
				if (cs != null)
					cs.close();
				if (dbConnection != null)
					dbConnection.close();
			} catch (SQLException sqlexception) {}
		}
		

		return ids;
	}

	public List<Video> getVideoDetails(int dbid, List<Long> ids){
		Connection connect = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Video> videos = new ArrayList<Video>();
		String sids2;

		if (ids.isEmpty())
			return videos;
		StringBuilder sids = new StringBuilder();
		for (int i = 0; i < ids.size(); i++) {
			long id = ids.get(i);
			Video video = (Video) videoCachePool.getIfPresent(Long.valueOf(id));
			if (video != null) {
				videos.add(video);
				logger.info((new StringBuilder("from cache ")).append(
						video.getTitle()).toString());
			} else {
				sids.append(id).append(",");
			}
		}

		sids2 = sids.toString();
		int li = sids2.lastIndexOf(",");
		if (li > 0)
			sids2 = sids2.substring(0, li);
		logger.info(sids2);
		if (sids2.isEmpty())
			return videos;
		try {
			if (dbid == 1) {
				connect = SqlDbConfigs.load("db_mysql_video_nhacso").getConnection();
				String sql = "CALL sp_plugin_getVideoByStr(?)";
				ps = connect.prepareStatement(sql);
			} else {
				connect = SqlDbConfigs.load("db_mysql_video").getConnection();
				String sql = "CALL sp_plugin_getArticlesByStr(?)";
				ps = connect.prepareStatement(sql);
			}
			ps.setString(1, sids2);
			rs = ps.executeQuery();
			while( rs.next()){ 
				videos.add(videoMapping(dbid, rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (connect != null)
					connect.close();
			} catch (SQLException e) {	}
		}
		return videos;
	}

	Video videoMapping(int dbid, ResultSet rs) throws SQLException {
		String pkname = "article_id";
		if (dbid == 1)
			pkname = "VideoID";
		long article_id = rs.getLong(pkname);
		String ck = (new StringBuilder(String.valueOf(dbid))).append("-").append(article_id).toString();
		Video video = (Video) videoCachePool.getIfPresent(ck);
		if (video == null) {
			if (dbid == 1) {
				String title = rs.getString("VideoName");
				String thumbnail_url = (new StringBuilder(rs.getString("URLThumb"))).append("/").append(rs.getString("Thumbnail")).toString();
				if (thumbnail_url.trim().isEmpty())
					thumbnail_url = "http://vnexpress.net/Images/video-vne.jpg";
				Date date = new Date(rs.getLong("CreateDate") * 1000L);
				int pageview = rs.getInt("pageview");
				video = new Video(article_id, title, thumbnail_url, date,
						pageview);
			} else {
				String title = rs.getString("title");
				String thumbnail_url = rs.getString("thumbnail_url");
				if (thumbnail_url == null)
					thumbnail_url = "http://vnexpress.net/Images/video-vne.jpg";
				if (thumbnail_url.trim().isEmpty())
					thumbnail_url = "http://vnexpress.net/Images/video-vne.jpg";
				Date date = new Date(rs.getLong("creation_time") * 1000L);
				int pageview = rs.getInt("pageview");
				video = new Video(article_id, title, thumbnail_url, date, pageview);
			}
			videoCachePool.put(ck, video);
		}
		return video;
	}

	public List<Video> getRelatedVideos(int dbid, long id) {
		try {
			List<Long> ids = getRelatedVideoIds(dbid, id);
			return getVideoDetails(dbid, ids);
		} catch (Exception e) {
			logger.error(e);
		}
		return new ArrayList<Video>(0);
	}

	public static void main(String args[]) throws Exception {
		VideoManagerImpl impl = new VideoManagerImpl();
		String ids = "1921666,1921664,1921662";
	}

	static Logger logger = Logger.getLogger(VideoManagerImpl.class);
	static final LoadingCache<String, Video> videoCachePool;

	static {
		CacheLoader<String, Video> loader = new CacheLoader<String, Video>() {
			public Video load(String key) throws Exception {
				return new Video();
			}
		};
		videoCachePool = CacheBuilder.newBuilder()
				.expireAfterWrite(1L, TimeUnit.HOURS).build(loader);
	}
}
