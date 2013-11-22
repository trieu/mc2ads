package com.mc2ads.tests;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import kafka.api.FetchRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import org.junit.Test;

import com.google.common.base.Stopwatch;

public class TestKafkaAsConsumer {

     public static String messageToString(MessageAndOffset msg){
		StringBuffer response = new StringBuffer();
		Charset charset = Charset.forName("UTF-8");
		response.append( charset.decode( msg.message().payload() ) );
		return response.toString();
	}

	@Test
	public void consumeData() {
		final Stopwatch stopwatch = new Stopwatch().start();
		try {
			SimpleConsumer consumer = new SimpleConsumer("localhost", 9092, 100000, 2048000);

			System.out.println("Testing single fetch");
			long offset = 536870947;
			int total = 0;
			while(true)
			{
				System.out.println("fetch at offset: " + offset);
				FetchRequest fetchRequest = new FetchRequest("clicks",	0, offset, 2048000);

				// get the message set from the consumer and print them out
				ByteBufferMessageSet messages = consumer.fetch(fetchRequest);

				int c = 0;
				for (MessageAndOffset msg : messages) {
                                        String log = messageToString(msg);
					System.out.println("offset "+ offset + " consumed: " +  log);
					// advance the offset after consuming each message
					offset = msg.offset();
					c++;
				}
				consumer.close();
				total += c;
				if(c == 0){
					break;
				}
			}
			long processTime = stopwatch.elapsedTime(TimeUnit.MILLISECONDS);
			System.out.println("\n kafka message fetched:" + total + " processing Time(MILLISECONDS): " + processTime);
			double oneM_mls = ((1000000 * processTime) / total)/1000;
			System.out.println("If fetching 1.000.000 messages, it will take (minutes) " + (oneM_mls/60) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

