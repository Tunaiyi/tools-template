package funs.tools.template;

import funs.tools.template.excel.GCell;
import funs.tools.template.excel.GRow;
import funs.tools.template.excel.GSheet;
import funs.tools.template.formula.FormulaType;
import funs.tools.template.formula.MvelFormulaFactory;
import funs.tools.template.util.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelTemplateSheet implements TemplateSheet {

    private static final Logger LOGGER = LoggerFactory.getLogger("ConfigParser");

    private static final String ROOT_ITEM_LIST_KEY = "itemList";
    private static final String PROFILE_KEY = "profile";

    private List<TemplateSheetConfig> configs = new ArrayList<>();

    private GSheet sheet;

    private List<Key> keyList = new ArrayList<>();

    private List<TemplateAttribute> attributeList = new ArrayList<>();

    public ExcelTemplateSheet(GSheet sheet) {
        this.sheet = sheet;
        this.parse(this.sheet);
    }

    @Override
    public String getName() {
        return this.sheet.getName();
    }

    @Override
    public boolean needOutPut() {
        return true;
    }

    @Override
    public List<TemplateSheetConfig> getConfigs() {
        return configs;
    }

    @Override
    public Map<String, Object> getAttributeMap(String profile) {
        Map<String, Object> rootAttribute = new HashMap<>();
        rootAttribute.put(ROOT_ITEM_LIST_KEY, this.getAttributeList(profile));
        rootAttribute.put(PROFILE_KEY, profile);
        return rootAttribute;
    }

    private void parseConfig(GRow row) {
        if (row.getCelNum() == 0)
            throw new NullPointerException(this.sheet.getName() + " configs row is null");
        for (int index = 0; index < row.getCelNum(); index++) {
            GCell cell = row.getCell(index);
            if (cell == null)
                continue;
            String json = cell.getContent();
            if (StringUtils.isBlank(json))
                continue;
            TemplateSheetConfig config = JSONUtils.toObject(json, TemplateSheetConfig.class);
            this.configs.add(config);
        }
        if (this.configs.isEmpty())
            throw new NullPointerException(this.sheet.getName() + " configs row is null");
    }

    protected void finish(GSheet sheet) {
        LOGGER.debug("???????????????{}", this.attributeList);
    }

    private void parseHead(GRow row) {
        for (GCell cell : row) {
            String head = cell.getContent();
            if (StringUtils.isBlank(head)) {
                this.keyList.add(Key.EMPTY_KEY);
            } else {
                this.keyList.add(Key.getKey(head));
            }
        }
    }

    private boolean validate(GRow row) {
        if (row == null || row.isEmpty()) {
            return false;
        }
        String cellValue = row.getCell(0).getContent();
        if (StringUtils.isBlank(cellValue)) {
            return false;
        }
        return true;
    }

    private void parseContent(GRow row) {
        try {
            LOGGER.debug("?????????{}???", row.getRowIndex());
            if (!this.validate(row)) {
                return;
            }
            TemplateAttribute attribute = new TemplateAttribute();
            int colNum = 0;
            for (Key key : this.keyList) {
                if (key.isEmpty()) {
                    colNum++;
                    continue;
                }
                String cellValue = null;
                GCell cell = row.getCell(colNum++);
                if (cell != null)
                    cellValue = StringUtils.trimToNull(cell.getContent());
                if (!key.isProfiles()) {
                    parseWithKey(attribute, key, null, row.getRowIndex(), cellValue);
                } else {
                    for (String profile : key.getProfiles()) {
                        parseWithKey(attribute, key, profile, colNum, cellValue);
                    }
                }
            }
            attribute.filter();
            this.attributeList.add(attribute);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        LOGGER.debug("?????????{}?????????", row.getRowIndex());
    }

    @SuppressWarnings("unchecked")
    private void parseWithKey(TemplateAttribute attribute, Key key, String profile, int rowIndex, String cellValue) {
        Key topKey = key;
        try {
            Map<String, Object> attr = attribute.loadOrCreate(profile);
            if (key.hasViceKey()) {
                while (key.hasViceKey()) {
                    Map<String, Object> tempAttr = null;
                    if (key.isPlurality()) {// ??????????????????
                        List<Map<String, Object>> list = (List<Map<String, Object>>) attr.get(key.getMainKey());
                        if (list == null) {// ????????????????????????
                            list = new ArrayList<>();
                            attr.put(key.getMainKey(), list);
                        }
                        if ((list.size() + 1) < key.getIndex()) {
                            throw new RuntimeException(
                                    MessageFormat.format("?????????{0}???????????????", key.getMainKey() + "$" + key.getIndex()));
                        }
                        if (list.size() >= key.getIndex()) {// ????????????????????????Map
                            tempAttr = list.get(key.getIndex() - 1);
                        } else {
                            tempAttr = new HashMap<>();
                            list.add(tempAttr);
                        }
                    } else {
                        tempAttr = (Map<String, Object>) attr.get(key.getMainKey());
                        if (tempAttr == null) {
                            tempAttr = new HashMap<>();
                            attr.put(key.getMainKey(), tempAttr);
                        }
                    }
                    attr = tempAttr;
                    key = key.getViceKey();
                }
            }
            if (attr == null) {// ??????????????????????????????key??????key?????????????????????????????????
                return;
            }
            if (key.isPlurality()) {// ??????
                List<Object> list = (List<Object>) attr.get(key.getMainKey());
                if (list == null) {
                    list = new ArrayList<>();
                    attr.put(key.getMainKey(), list);
                }
                if (StringUtils.isNotBlank(cellValue)) {
                    list.add(this.formatValue(cellValue));
                }
            } else {
                attr.put(key.getMainKey(), this.formatValue(cellValue));
            }
        } catch (Throwable e) {
            String error = "?????????" + rowIndex + "??? key : " + topKey + " [" + profile + "]????????????";
            LOGGER.error(error);
            throw new RuntimeException(error, e);
        }
    }

    private Object formatValue(String value) {
        if (value != null && value.startsWith("@")) {
            return MvelFormulaFactory.create(value.replaceFirst("@", ""), FormulaType.EXPRESSION).createFormula()
                    .execute(Object.class);
        }
        return value;
    }

    private List<Map<String, Object>> getAttributeList(String profile) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TemplateAttribute attribute : this.attributeList) {
            Map<String, Object> map = attribute.getAttribute(profile);
            if (map != null)
                list.add(map);
        }
        return list;
    }

    protected void parse(GCell cell) {
    }

    protected void parse(GSheet sheet) {
        for (GRow row : sheet) {
            this.parse(row);
        }
        this.finish(sheet);
    }

    protected void parse(GRow row) {
        if (row.getRowIndex() == 0) {
            this.parseConfig(row);
        } else if (row.getRowIndex() == 2) {
            this.parseHead(row);
        } else if (row.getRowIndex() > 2) {
            this.parseContent(row);
        }
    }

    public static void main(String[] args) {
        System.out.println("??".toCharArray());
    }

}
