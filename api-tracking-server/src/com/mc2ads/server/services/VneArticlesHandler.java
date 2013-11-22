package com.mc2ads.server.services;



import com.mc2ads.manager.VideoManagerImpl;
import com.mc2ads.model.Video;
import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;
import com.mc2ads.utils.ParamUtil;
import java.util.ArrayList;
import java.util.List;

@BaseRestHandler(uri="vne-article")
public class VneArticlesHandler extends BaseServiceHandler
{

    public VneArticlesHandler()
    {
    }

    @MethodRestHandler
    public String getServiceName()
    {
        return getClass().getName();
    }

    @MethodRestHandler
    public List<Video> relatedVideos()
    {
        long id = ParamUtil.getLong(request, "id", 0L);
        int dbid = ParamUtil.getInt(request, "dbid", 0);
        return videoManagerImpl.getRelatedVideos(dbid, id);
    }

    @MethodRestHandler
    public List<Long> relatedVideoIds()
    {
        long id = ParamUtil.getLong(request, "id", 0L);
        int dbid = ParamUtil.getInt(request, "dbid", 0);
        return videoManagerImpl.getRelatedVideoIds(dbid, id);
    }

    @MethodRestHandler
    public List<Video> getVideoDetails()
    {
        String ids[] = ParamUtil.getString(request, "ids", "").split(",");
        int dbid = ParamUtil.getInt(request, "dbid", 0);
        List<Long> idss = new ArrayList<Long>();
        for(int i = 0; i < ids.length; i++)
        {
            long id = Long.parseLong(ids[i]);
            idss.add(Long.valueOf(id));
        }

        try
        {
            return videoManagerImpl.getVideoDetails(dbid, idss);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return new ArrayList<Video>(0);
    }

    static final VideoManagerImpl videoManagerImpl = new VideoManagerImpl();

}
