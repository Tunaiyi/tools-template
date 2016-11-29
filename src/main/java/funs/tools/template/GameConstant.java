package funs.tools.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameConstant {

	public static final String ENCODING = "UTF-8";//System.getProperty("template.encoding", "UTF-8");

	public static final Map<String, String> SUFFIX_MAP;

	static {
		String suffixMapLists = System.getProperty("template.suffixMap", "xml:xml,pro:properties,cs:cs,as:as");
		Map<String, String> suffixMap = new HashMap<String, String>();
		String[] suffixMapStrs = suffixMapLists.split(",");
		for (String suffixMapStr : suffixMapStrs) {
			String[] suffix = suffixMapStr.split(":");
			suffixMap.put(suffix[0], suffix[1]);
		}
		SUFFIX_MAP = Collections.unmodifiableMap(suffixMap);
	}
}
