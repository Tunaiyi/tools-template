package funs.tools.template.xml;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import funs.tools.template.TemplateSheet;
import funs.tools.template.TemplateSheetConfig;

public class XMLTable implements TemplateSheet {

	private TemplateSheetConfig config;

	private Map<String, Object> attributeMap;

	public TemplateSheetConfig getConfig() {
		return config;
	}

	@Override
	public String getName() {
		return "xml";
	}

	@Override
	public boolean needOutPut() {
		return true;
	}

	@Override
	public String getOutput() {
		return config.getOutput();
	}

	@Override
	public String getMvl() {
		return config.getMvl();
	}
	

	@Override
	public Map<String, Object> getAttributeMap(String profile) {
		return Collections.unmodifiableMap(attributeMap);
	}

	@Override
	public List<String> getProfiles() {
		return config.getProfiles();
	}

}
