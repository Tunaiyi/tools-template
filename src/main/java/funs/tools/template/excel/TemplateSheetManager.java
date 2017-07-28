package funs.tools.template.excel;

import funs.tools.template.ExcelTemplateSheet;
import funs.tools.template.GlobalTemplateSheet;
import funs.tools.template.SheetSrcType;
import funs.tools.template.TemplateSheet;
import funs.tools.template.xml.XMLFactory;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TemplateSheetManager {

    public static final String GLOBAL_SHEET_NAME = "global.mvl";

    private Map<String, TemplateSheet> templateSheetMap = new HashMap<String, TemplateSheet>();

    //	public TemplateSheetManager(String path, SheetFilter filter) throws Exception {
    //		InputStream in = null;
    //		try {
    //			in = new FileInputStream(path);
    //			workbook = GWorkbookFactory.create(in, filter);
    //			initTemplateSheet();
    //		} catch (FileNotFoundException e) {
    //			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    //			if (in == null) {
    //				throw e;
    //			}
    //			workbook = GWorkbookFactory.create(in, filter);
    //		} finally {
    //			IOUtils.closeQuietly(in);
    //		}
    //	}

    public TemplateSheetManager(String name, InputStream in, SheetSrcType type, SheetFilter filter) throws Exception {
        if (type == SheetSrcType.EXCEL) {
            GWorkbook workbook = GWorkbookFactory.create(in, filter);
            initTemplateSheet(workbook);
        } else if (type == SheetSrcType.XML) {
            TemplateSheet templateSheet = XMLFactory.createTable(name, in);
            this.templateSheetMap.put(templateSheet.getName(), templateSheet);
        }
    }

    private void initTemplateSheet(GWorkbook workbook) {
        for (GSheet sheet : workbook.getAllSheet()) {
            TemplateSheet templateSheet;
            if (sheet.getName().equals(TemplateSheetManager.GLOBAL_SHEET_NAME)) {
                templateSheet = new GlobalTemplateSheet(sheet);
            } else {
                templateSheet = new ExcelTemplateSheet(sheet);
            }
            this.templateSheetMap.put(sheet.getName(), templateSheet);
        }
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
