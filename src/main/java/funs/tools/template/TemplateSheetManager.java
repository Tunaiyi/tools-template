package funs.tools.template;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import funs.tools.template.excel.GSheet;
import funs.tools.template.excel.GWorkbook;
import funs.tools.template.excel.GWorkbookFactory;
import funs.tools.template.excel.SheetFilter;

public class TemplateSheetManager {

	public static final String GLOBAL_SHEET_NAME = "global.mvl";

	private GWorkbook workbook;

	private Map<String, TemplateSheet> templateSheetMap = new HashMap<String, TemplateSheet>();

	public TemplateSheetManager(String path, SheetFilter filter) throws Exception {
		InputStream in = null;
		try {
			in = new FileInputStream(path);
			workbook = GWorkbookFactory.create(in, filter);
			initTemplateSheet();
		} catch (FileNotFoundException e) {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			if (in == null) {
				throw e;
			}
			workbook = GWorkbookFactory.create(in, filter);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public TemplateSheetManager(InputStream in, SheetFilter filter) throws Exception {
		workbook = GWorkbookFactory.create(in, filter);
		initTemplateSheet();
	}

	private void initTemplateSheet() {
		for (GSheet sheet : workbook.getAllSheet()) {
			TemplateSheet templateSheet = null;
			if (sheet.getName().equals(TemplateSheetManager.GLOBAL_SHEET_NAME)) {
				templateSheet = new GlobalTemplateSheet(sheet);
			} else {
				templateSheet = new ConfigTableTemplateSheet(sheet);
			}
			this.templateSheetMap.put(sheet.getName(), templateSheet);
		}
	}

	public GWorkbook getWorkbook() {
		return workbook;
	}

	public Collection<TemplateSheet> getTemplateSheetList() {
		return Collections.unmodifiableCollection(templateSheetMap.values());
	}

	public TemplateSheet getTemplateSheetBy(String name) {
		return this.templateSheetMap.get(name);
	}

	//	/**
	//	 * 处理每行
	//	 * 
	//	 * @param sheetName
	//	 * @param parser
	//	 */
	//	private void parse(String sheetName, Parser parser) {
	//		GSheet sheet = workbook.getSheet(sheetName);
	//		if (sheet != null) {
	//			parser.parse(sheet);
	//		}
	//	}

}
