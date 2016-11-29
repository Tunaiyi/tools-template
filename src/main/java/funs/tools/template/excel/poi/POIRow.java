package funs.tools.template.excel.poi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import funs.tools.template.excel.GCell;
import funs.tools.template.excel.GRow;

public class POIRow implements GRow {

	private int rowIndex;

	private List<GCell> cellList;

	public POIRow(Row row) {
		this.rowIndex = row.getRowNum();
		List<GCell> gcellList = new ArrayList<GCell>();
		
		//***下面方法跳过空列
		/*for (Cell cell : row) {
			gcellList.add(new POICell(cell, evaluator));
		}*/
		for(int i=0; i< row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if(cell == null) {
				gcellList.add(new POICell(this.rowIndex, i));
			} else {
				gcellList.add(new POICell(row.getCell(i)));
			}
		}
		this.cellList = gcellList;
	}
	
	public POIRow(int rowIndex) {
		this.rowIndex = rowIndex;
		this.cellList = Collections.emptyList();
	}

	@Override
	public Iterator<GCell> iterator() {
		return cellList.iterator();
	}

	@Override
	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public GCell getCell(int cellIndex) {
		return cellIndex >= getCelNum() ? null : cellList.get(cellIndex);
	}

	@Override
	public int getCelNum() {
		return cellList.size();
	}

	@Override
	public boolean isEmpty() {
		return cellList.size() == 0;
	}

}
