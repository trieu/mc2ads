package com.mc2ads.storm.analytics.topology;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class TestDbProcessingTopology {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestDbProcessingTopology.class);
	final static Map<String,Integer> counters = new HashMap<String,Integer>();
		
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
	
	public static class DbSeederSpout implements IRichSpout {

		private static final Logger LOG = LoggerFactory
				.getLogger(DbSeederSpout.class);
		
		private TopologyContext _context;
		private SpoutOutputCollector _collector;
		
		static int index = 0;
		final static int SIZE = 10;
		static Queue<String> bufferQueue = new ConcurrentLinkedQueue<String>();

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
			Connection conn = getDbConnection();	
			PreparedStatement pst = null;
			try {
				pst = conn.prepareStatement("SELECT position FROM students LIMIT "+ index + ","+SIZE);
				index+=SIZE;
				// execute the SQL
				ResultSet res = pst.executeQuery();
				while (res.next()) {
					String name = res.getString("position");
					bufferQueue.add(name);
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
					LOG.error("connection can't closed or statement can't closed! "
							+ e.getMessage());
				}
			}
		}

		public void nextTuple() {			
			if(bufferQueue.isEmpty()){
				seedBufferQueue();
				Utils.sleep(100);
			} else {
				String name = bufferQueue.poll();	
				if (name != null) {
					Values values = new Values();				
					values.add(name);
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
			declarer.declare(new Fields("name"));
		}

		public Map<String, Object> getComponentConfiguration() {
			return null;
		}
	}

	public static class DbRowProcessingBolt implements IRichBolt {
		private static final Logger LOG = LoggerFactory
				.getLogger(DbRowProcessingBolt.class);

		private TopologyContext _context;
		private OutputCollector _collector;
		
		static int total = 0;
		
		Integer id;
		String name;

		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			_context = context;
			_collector = collector;
		}

		public void execute(Tuple input) {
			LOG.info("input's size : " + input.size());			
			String name = input.getStringByField("name");	
			LOG.info("name is : " + name);
			total++;			
			_collector.ack(input);
			LOG.info("Total : " + total);
			
			if (!counters.containsKey(name)) {
				counters.put(name, 1);
			} else {
				Integer c = counters.get(name) + 1;
				counters.put(name, c);
			}
			//Firstly i have 99 results in my db, and i think if total is 99, counters will be the latest one and won't update.
			//Then the _collector will send the content of counters to aggregate the result of all four threads, but it doesn't work. Why?
			
			//if(total == 99){ 
				for(Map.Entry<String, Integer> entry : counters.entrySet()){
					_collector.emit(new Values(entry.getKey(),entry.getValue()));
				}				
			//}
		}

		public void cleanup() {
			
		}

		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("name","sum"));
		}

		public Map<String, Object> getComponentConfiguration() {
			return null;
		}
	}
	
	public static class DbAggregation implements IRichBolt{		
		private TopologyContext _context;
		private OutputCollector _collector;
		private Map<String,Integer> counters = new HashMap<String,Integer>();
		Integer id;
		String name;

		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {
			_context = context;
			_collector = collector;
			name = _context.getThisComponentId();
			id = _context.getThisTaskId();
		}

		public void execute(Tuple input) {
			counters.put(input.getValueByField("name").toString(), input.getIntegerByField("sum"));
			_collector.ack(input);
		}

		public void cleanup() {
			System.out.println("-- Movie Counter [" + name + "-" + id + "] --");
			for (Map.Entry<String, Integer> entry : counters.entrySet()) {
				System.out.println(entry.getKey() + " : " + entry.getValue());
			}
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
		//builder.setBolt("dbRowProcessing", new DbRowProcessingBolt(), 4).shuffleGrouping("seedNameFromDb");
		builder.setBolt("dbRowProcessing", new DbRowProcessingBolt(), 4).fieldsGrouping("seedNameFromDb", new Fields("name"));
		builder.setBolt("aggregation", new DbAggregation()).shuffleGrouping("dbRowProcessing");
		
		Config conf = new Config();
		conf.put(Config.STORM_LOCAL_DIR, "D:/storm-tmp");
		conf.put(Config.DEV_ZOOKEEPER_PATH, "D:/storm-tmp");
		//conf.setDebug(true);

		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("test", conf, builder.createTopology());
		Thread.sleep(12000);
		// cluster.killTopology("test");
		LOG.info("#### total DbRowProcessingBolt processed: " + DbRowProcessingBolt.total);
		cluster.shutdown();
	}

}
