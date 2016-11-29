package funs.tools.template.xml;

import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Xpp3DomDriver;

import funs.tools.template.util.JSONUtils;

public class TestXML {

	public static void main(String[] args) {
		XMLMapConverter xmlMapConverter = new XMLMapConverter();
		XStream xStream = new XStream(new Xpp3DomDriver());
		xStream.registerConverter(xmlMapConverter);
		xStream.alias("data", Map.class);
		String data = "<?xml version='1.0' encoding='UTF-8'?><data class='list'><item name='tom' sex='man' ></item><friends class='list'><name>Lily</name><name>Lucy</name><name>Sam</name></friends><local>shantou</local><phones class = 'list'><phone head='123' number='122233'></phone><phone head='123' number='122233'></phone><phone head='123' number='122233'></phone></phones></data>";
//		String data = "<?xml version='1.0' encoding='UTF-8'?><data><item name='tom' sex='man'><friends class='list'><name>Lily</name><name>Lucy</name><name>Sam</name></friends></item><item name='tom' sex='man'><friends class='list'><name>Lily</name><name>Lucy</name><name>Sam</name></friends></item><item name='tom' sex='man'><friends class='list'><name>Lily</name><name>Lucy</name><name>Sam</name></friends></item><item name='tom' sex='man'><friends class='list'><name>Lily</name><name>Lucy</name><name>Sam</name></friends></item></data>";
		System.out.println(JSONUtils.toJson(xStream.fromXML(data)));
	}

}
