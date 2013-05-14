package com.mc2ads.server.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.ProducerData;
import kafka.producer.ProducerConfig;

import com.mc2ads.kafka.KafkaProperties;
import com.mc2ads.model.UrlTrackingPayload;
import com.mc2ads.server.BaseServiceHandler;
import com.mc2ads.server.annotations.BaseRestHandler;
import com.mc2ads.server.annotations.MethodRestHandler;
import com.mc2ads.utils.Log;
import com.mc2ads.utils.ParamUtil;
import com.mc2ads.utils.TemplateUtils;

@BaseRestHandler(uri = "log")
public class LogHandler extends BaseServiceHandler {

	private final kafka.javaapi.producer.Producer<Integer, String> producer;
	private final String topic;
	private final Properties props = new Properties();

	public LogHandler() {
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("zk.connect", "localhost:2181");
		// Use random partitioner. Don't need the key type. Just set it to
		// Integer.
		// The message is of type String.
		producer = new kafka.javaapi.producer.Producer<Integer, String>(
				new ProducerConfig(props));
		this.topic = KafkaProperties.topicUrlTracking;
	}

	@Override
	@MethodRestHandler
	public String getServiceName() {
		return this.getClass().getName();
	}

	final static String RS = "1";

	int c = 0;
	@MethodRestHandler
	public String track() {

		final String url = ParamUtil.getString(request, "url");
		final String title = ParamUtil.getString(request, "title", "");
		final String keywords = ParamUtil.getString(request, "keywords", "");
		final String categories = ParamUtil.getString(request, "categories", "");

		//System.out.println(HashUtil.hashUrl(url));

		final String fosp_aid = ParamUtil.getString(request, "fosp_aid");

		// TODO submit raw data to storm for real-time computation

		System.out.println("-------------------------------");
		c++;
		Log.println(" - ",c, url, title, keywords, fosp_aid, categories);		
	    producer.send(new ProducerData<Integer, String>(topic, new UrlTrackingPayload(request).toJson()));
	    producer.close();
	    
		System.out.println("-------------------------------");
		return RS;
	}

	@MethodRestHandler
	public String stats() {

		this.response.setContentType("text/html");

		Map<String, Object> dataModel = new HashMap<String, Object>();
		dataModel.put("request", this.request);
		dataModel.put("response", this.response);
		String tpl_name = ParamUtil.getString(request, "tpl_name", "");
		if (tpl_name.isEmpty()) {
			return "";
		}
		return TemplateUtils.processModel(dataModel, "", tpl_name + ".ftl");
	}


}
