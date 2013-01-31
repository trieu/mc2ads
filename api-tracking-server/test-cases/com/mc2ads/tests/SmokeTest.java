package com.mc2ads.tests;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import com.mc2ads.utils.HttpClientUtil;


public class SmokeTest {

    @Rule
    public ContiPerfRule i = new ContiPerfRule();
    
    //static final String testurl = "http://localhost:10001/log/track/html?fosp_aid=1&url=http://vnexpress.net";
    static final String testurl = "http://localhost:10001/vne-article/relatedVideos/json?dbid=2&id=111";

    @Test
    @PerfTest(invocations = 5000, threads = 1000)
    @Required(max = 21000, average = 3000)
    public void smokeTesttLogTracking() throws Exception {    	
    	String html = HttpClientUtil.executeGet(testurl, false);
/*    	AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    	Future<Response> f = asyncHttpClient.prepareGet(testurl).execute();
    	Response r = f.get();
    	String html = r.getResponseBody();*/
    	System.out.println(html);
    	Assert.assertTrue(html.contains("aaaa"));
        Thread.sleep(100);
    }

}