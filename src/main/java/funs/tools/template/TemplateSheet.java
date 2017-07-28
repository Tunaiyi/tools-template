package funs.tools.template;

import java.util.List;
import java.util.Map;

public interface TemplateSheet {

	String getName();

	boolean needOutPut();

	List<TemplateSheetConfig> getConfigs();

	Map<String, Object> getAttributeMap(String profile);

}
