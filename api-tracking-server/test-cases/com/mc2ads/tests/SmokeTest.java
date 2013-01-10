package com.mc2ads.tests;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import com.mc2ads.utils.HttpClientUtil;


public class SmokeTest {

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Test
    @PerfTest(invocations = 1000, threads = 20)
    @Required(max = 10000, average = 7000)
    public void testWhiteboardRecentDrawings() throws Exception {    	
    	String html = HttpClientUtil.executeGet("http://google.com");
    	    	
        Thread.sleep(200);
    }

}