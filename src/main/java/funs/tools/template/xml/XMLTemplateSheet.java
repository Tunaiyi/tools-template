package funs.tools.template.xml;

import funs.tools.template.TemplateSheet;
import funs.tools.template.TemplateSheetConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XMLTemplateSheet implements TemplateSheet {

    private List<TemplateSheetConfig> configs = new ArrayList<>();

    private Map<String, Object> attributeMap;

    private String name;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean needOutPut() {
        return true;
    }

    @Override
    public List<TemplateSheetConfig> getConfigs() {
        return configs;
    }

    public XMLTemplateSheet setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public Map<String, Object> getAttributeMap(String profile) {
        return Collections.unmodifiableMap(attributeMap);
    }


}
