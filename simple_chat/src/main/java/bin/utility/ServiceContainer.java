package bin.utility;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServiceContainer {
	private static HashMap<String, Class<?>> map;
	private static Logger log = Logger.getLogger(ServiceContainer.class);

	public static void initialize(String path) {
		map = new HashMap<String, Class<?>>();
		File file = new File(path);

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			NodeList nl = doc.getElementsByTagName("services");
			Element node = (Element) nl.item(0);
			NodeList params = node.getElementsByTagName("service");
			for (int i = 0; i < params.getLength(); i++) {
				Element param = (Element) params.item(i);
				String key = param.getAttribute("id");
				String className = param.getAttribute("class");
				Class<?> c;
				try {
					c = Class.forName(className);
					map.put(key, c);
				} catch (ClassNotFoundException e) {
					log.error("could not find the service's class \"" + className + "\", which the id is \"" + key + "\"");
				}
			}
			log.info("intialized the service container successful");
		} catch (SAXException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		} catch (IOException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		} catch (ParserConfigurationException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		} catch (NullPointerException e) {
			log.error("parse\"" + path + "\" failed,error message:\"" + e.getMessage() + "\"");
		}
	}

	public static Object getService(String id) {
		if (map == null) {
			throw new NullPointerException("service container has not be initailized");
		}
		Class<?> c = map.get(id);
		if (c != null) {
			try {
				return c.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
