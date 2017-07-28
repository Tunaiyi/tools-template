package funs.tools.template.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import funs.tools.template.TemplateSheetConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class XMLFactory {

    public static XMLTemplateSheet createTable(String name, InputStream input) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line.trim());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            XMLMapConverter xmlMapConverter = new XMLMapConverter();
            XStream xStream = new XStream(new DomDriver());
            xStream.alias("table", XMLTemplateSheet.class);
            xStream.alias("config", TemplateSheetConfig.class);
            xStream.registerLocalConverter(XMLTemplateSheet.class, "attributeMap", xmlMapConverter);
            //		System.out.println(JSONUtils.toJson(table));
            XMLTemplateSheet sheet = (XMLTemplateSheet) xStream.fromXML(builder.toString());
            sheet.setName(name);
            return sheet;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
