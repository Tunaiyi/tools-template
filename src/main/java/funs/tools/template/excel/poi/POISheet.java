package funs.tools.template.excel.poi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import funs.tools.template.excel.GRow;
import funs.tools.template.excel.GSheet;

public class POISheet implements GSheet {
	
	private String sheetName;

	private List<GRow> rowList;
	
	protected POISheet(Sheet sheet) {
		this.sheetName = sheet.getSheetName();
		List<GRow> rowList = new ArrayList<GRow>();
		/*for(Row row : sheet) {
			rowList.add(new POIRow(row));
		}*/
		for(int rowIndex=0, lastRowNum=sheet.getLastRowNum(); rowIndex <= lastRowNum; rowIndex ++) {
			Row row = sheet.getRow(rowIndex);
			if(row == null) {
				rowList.add(new POIRow(rowIndex));
			} else {
				rowList.add(new POIRow(row));
			}
		}
		this.rowList = rowList;
	}

	@Override
	public Iterator<GRow> iterator() {
		return rowList.iterator();
	}

	@Override
	public String getName() {
		return sheetName;
	}

	@Override
	public GRow getRow(int rowIndex) {
		return rowIndex >= getRowNum() ? null : rowList.get(rowIndex);
	}

	@Override
	public int getRowNum() {
		return rowList.size();
	}

}
