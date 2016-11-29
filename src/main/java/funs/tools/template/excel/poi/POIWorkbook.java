package funs.tools.template.excel.poi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import funs.tools.template.excel.GSheet;
import funs.tools.template.excel.GWorkbook;
import funs.tools.template.excel.NoneSheetFilter;
import funs.tools.template.excel.SheetFilter;

public class POIWorkbook implements GWorkbook {

	private Map<String, GSheet> nameSheetMap;

	public POIWorkbook(InputStream inputStream, SheetFilter filter) throws InvalidFormatException, IOException {
		this(WorkbookFactory.create(inputStream), filter);
	}

	public POIWorkbook(InputStream inputStream) throws InvalidFormatException, IOException {
		this(WorkbookFactory.create(inputStream));
	}

	public POIWorkbook(Workbook workbook) {
		this(workbook, NoneSheetFilter.getInstance());
	}

	public POIWorkbook(Workbook workbook, SheetFilter filter) {
		Map<String, GSheet> nameSheetMap = new HashMap<String, GSheet>();
		int sheetNum = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			if (filter.accept(workbook.getSheetName(i))) {
				GSheet sheet = new POISheet(workbook.getSheetAt(i));
				nameSheetMap.put(sheet.getName(), sheet);
			}
		}
		this.nameSheetMap = nameSheetMap;
	}

	@Override
	public Iterator<GSheet> iterator() {
		return nameSheetMap.values().iterator();
	}

	@Override
	public Collection<String> getSheetNames() {
		return Collections.unmodifiableSet(nameSheetMap.keySet());
	}

	@Override
	public GSheet getSheet(String sheetName) {
		return nameSheetMap.get(sheetName);
	}

	@Override
	public Collection<GSheet> getAllSheet() {
		return nameSheetMap.values();
	}
}
