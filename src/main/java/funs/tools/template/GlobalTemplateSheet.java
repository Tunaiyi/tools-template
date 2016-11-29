package funs.tools.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import funs.tools.template.excel.GCell;
import funs.tools.template.excel.GRow;
import funs.tools.template.excel.GSheet;

public class GlobalTemplateSheet implements TemplateSheet {

	private static final Logger LOGGER = LoggerFactory.getLogger("ConfigParser");

	private GSheet sheet;

	private Map<String, Object> attributeMap = new HashMap<>();

	public GlobalTemplateSheet(GSheet sheet) {
		this.sheet = sheet;
		this.parse(this.sheet);
	}

	@Override
	public String getName() {
		return this.sheet.getName();
	}

	@Override
	public String getOutput() {
		return null;
	}

	@Override
	public boolean needOutPut() {
		return false;
	}

	@Override
	public String getMvl() {
		return null;
	}

	@Override
	public Map<String, Object> getAttributeMap(String profile) {
		return new HashMap<>(attributeMap);
	}

	protected void finish(GSheet sheet) {
		LOGGER.debug("所有属性：{}", attributeMap);
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
		LOGGER.debug("处理第{}行", row.getRowIndex());
		if (!validate(row)) {
			return;
		}
		GCell keyCell = row.getCell(0);
		GCell valueCell = row.getCell(1);
		if (keyCell == null)
			return;
		this.attributeMap.put(keyCell.getContent(), valueCell == null ? null : valueCell.getContent());
		LOGGER.debug("处理第{}行结束,属性为{}", row.getRowIndex(), this.attributeMap);
	}

	protected void parse(GCell cell) {
	}

	@Override
	public List<String> getProfiles() {
		return Collections.emptyList();
	}

	protected void parse(GSheet sheet) {
		for (GRow row : sheet) {
			parse(row);
		}
		finish(sheet);
	}

	protected void parse(GRow row) {
		parseContent(row);
	}

	public static void main(String[] args) {
		System.out.println("×".toCharArray());
	}

}
