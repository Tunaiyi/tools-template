package funs.tools.template;

import java.util.List;
import java.util.Map;

public interface TemplateSheet {

	public String getName();

	public boolean needOutPut();

	public String getOutput();
	
	public List<String> getProfiles();

	public String getMvl();

	public Map<String, Object> getAttributeMap(String profile);

}
