package com.mc2ads.tests;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadXMLFile {

	public static void main(String argv[]) {

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean classpathentry = false;
	

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {

					//System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("classpathentry")) {
						classpathentry = true;
						String kind = attributes.getValue("kind");
						if(kind.equalsIgnoreCase("lib")){
							String path = attributes.getValue("path");
							System.out.println(path + " ; ");	
						}						
					}
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					//System.out.println("End Element :" + qName);
				}

				public void characters(char ch[], int start, int length)
						throws SAXException {
					if (classpathentry) {						
						classpathentry = false;
					}
				}

			};

			saxParser.parse(".classpath", handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}