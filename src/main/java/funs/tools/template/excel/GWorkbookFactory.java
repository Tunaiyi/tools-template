package funs.tools.template.excel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import funs.tools.template.excel.poi.POIWorkbook;

public class GWorkbookFactory {

	private static final WorkbookType TEMP_WORKBOOK_TYPE = WorkbookType.POI;

	public static GWorkbook create(InputStream inputStream) throws IOException, InvalidFormatException {
		return create(inputStream, NoneSheetFilter.getInstance(), TEMP_WORKBOOK_TYPE);
	}

	public static GWorkbook create(InputStream inputStream, SheetFilter filter) throws IOException, InvalidFormatException {
		return create(inputStream, filter, TEMP_WORKBOOK_TYPE);
	}

	public static GWorkbook create(InputStream inputStream, SheetFilter filter, WorkbookType type) throws IOException, InvalidFormatException {
		return new POIWorkbook(inputStream, filter);
	}

}
