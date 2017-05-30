package funs.tools.template.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XMLFactory {

    public static XMLTable createTable(InputStream input) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line = null;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            XMLMapConverter xmlMapConverter = new XMLMapConverter();
            XStream xStream = new XStream(new DomDriver());
            xStream.alias("table", XMLTable.class);
            xStream.registerLocalConverter(XMLTable.class, "attributeMap", xmlMapConverter);
            //		System.out.println(JSONUtils.toJson(table));
            return (XMLTable) xStream.fromXML(builder.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
