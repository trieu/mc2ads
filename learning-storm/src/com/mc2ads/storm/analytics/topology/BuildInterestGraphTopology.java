package com.mc2ads.storm.analytics.topology;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.mc2ads.storm.analytics.utils.CrawlerUtil;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class BuildInterestGraphTopology {
	
	private static final Logger LOG = LoggerFactory.getLogger(BuildInterestGraphTopology.class);
	static Connection getDbConnection(){
		Connection conn = null;		
		String dbName = "i2tree";
		String host = "127.0.0.1";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(					
					"jdbc:mysql://"+host+":3306/"+ dbName + "?useUnicode=true&characterEncoding=UTF-8", "root", "");
		} catch (Exception e) {
			LOG.info("Connection can't open!");
		}
		return conn;
	}
	
	public static class DbRecord {
		long id;
		String name;
		String email;
		
		public DbRecord(long id, String name, String email) {
			super();
			this.id = id;
			this.name = name;
			this.email = email;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}		
	}
	
	
	public static class DbSeederSpout implements IRichSpout {

		private static final Logger LOG = LoggerFactory
				.getLogger(DbSeederSpout.class);
		
		private TopologyContext _context;
		private SpoutOutputCollector _collector;
		
		static int index = 0;
		final static int SIZE = 10;
		static Queue<DbRecord> bufferQueue = new ConcurrentLinkedQueue<DbRecord>();

		public void open(Map conf, TopologyContext context,
				SpoutOutputCollector collector) {
			_context = context;
			_collector = collector;
		}

		public void close() {
		}

		public void activate() {
		}

		public void deactivate() {
		}
		
		private void seedBufferQueue(){
			PreparedStatement pst = null;
			Connection conn = getDbConnection();
			if (conn == null) {
				LOG.error("Can not connect to mySQL database server");
				return;
			}
			try {
				pst = conn.prepareStatement("SELECT id,name,email FROM students LIMIT "+ index + ","+SIZE);
				index+=SIZE;
				// execute the SQL
				ResultSet res = pst.executeQuery();
				while (res.next()) {
					long id = res.getLong("id");
					String name = res.getString("name");
					String email = res.getString("email");
					bufferQueue.add(new DbRecord(id, name, email));
				}
			} catch (SQLException ex) {
				LOG.error("SQLException: " + ex.getMessage());
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}					
					conn.close();	
				} catch (SQLException e) {
					LOG.error("connection can't closed or statement can't closed! " + e.getMessage());
				}
			}
		}

		public void nextTuple() {			
			if(bufferQueue.isEmpty()){
				seedBufferQueue();
				Utils.sleep(100);
			} else {
				DbRecord record = bufferQueue.poll();	
				if (record != null) {
					Values values = new Values();				
					values.add(record.getId());
					values.add(record.getName());
					values.add(record.getEmail());
					_collector.emit(values);
				}
				Utils.sleep(50);
			}	
		}

		public void ack(Object msgId) {
		}

		public void fail(Object msgId) {
		}

		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("id","name","email"));
		}

		public Map<String, Object> getComponentConfiguration() {
			return null;
		}
	}

	public static class DbRowProcessingBolt implements IRichBolt {
		private static final Logger LOG = LoggerFactory.getLogger(DbRowProcessingBolt.class);

		private TopologyContext _context;
		private OutputCollector _collector;
		static int total = 0;

		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			_context = context;
			_collector = collector;
		}
		
		void setKeywordsFromCrawler(long id, String email){
			PreparedStatement pst = null;
			String keywords = new Gson().toJson(CrawlerUtil.guessKeywordsFromEmail(email));
			System.out.println();
			Connection conn = getDbConnection();
			if (conn == null) {
				LOG.error("Can not connect to mySQL database server");
				return;
			}
			try {
				String sql = "UPDATE students SET keywords = ? WHERE id = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, keywords);
				pst.setLong(2, id);
				
				// execute the SQL
				int res = pst.executeUpdate();
				System.out.println("### setKeywordsFromCrawler: " + res + ", keywords: " + keywords);
				
			} catch (SQLException ex) {
				LOG.error("SQLException: " + ex.getMessage());
			} finally {
				try {
					if (pst != null) {
						pst.close();
					}					
					conn.close();	
				} catch (SQLException e) {
					LOG.error("connection can't closed or statement can't closed! " + e.getMessage());
				}
			}
		}

		public void execute(Tuple input) {
			LOG.info("input's size : " + input.size());
			total++;
			long id = input.getLongByField("id");
			String name = input.getStringByField("name");			
			String email = input.getStringByField("email");
			LOG.info("name is : " + name + ", email is: " + email + ", id is: " + id);
			setKeywordsFromCrawler(id, email);
			_collector.ack(input);
		}

		public void cleanup() {
		}

		public void declareOutputFields(OutputFieldsDeclarer declarer) {
		}

		public Map<String, Object> getComponentConfiguration() {
			return null;
		}
	}

	
	public static void main(String[] args) throws Exception {
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("seedNameFromDb", new DbSeederSpout());
		builder.setBolt("dbRowProcessing", new DbRowProcessingBolt(), 4).shuffleGrouping("seedNameFromDb");

		Config conf = new Config();
		conf.setDebug(true);

		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("test", conf, builder.createTopology());
		Thread.sleep(12000);
		// cluster.killTopology("test");
		LOG.info("#### total DbRowProcessingBolt processed: " + DbRowProcessingBolt.total);
		cluster.shutdown();
	}

}
