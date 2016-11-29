package funs.tools.template.excel.poi;

import org.apache.poi.ss.usermodel.Cell;

import funs.tools.template.excel.GCell;
import funs.tools.template.util.POICellUtil;

public class POICell implements GCell {
	
	private Cell cell;
	
	private int rowIndex;
	private int columnIndex;
	
	protected POICell(Cell cell) {
		this.cell = cell;
		this.rowIndex = cell.getRowIndex();
		this.columnIndex = cell.getColumnIndex();
	}
	
	protected POICell(int rowIndex, int columnIndex) {
		this.rowIndex = rowIndex;
		this.columnIndex = columnIndex;
	}

	@Override
	public String getContent() {
		return POICellUtil.getContent(cell);
	}

	@Override
	public int getColumnIndex() {
		return this.columnIndex;
	}

	@Override
	public int getRowIndex() {
		return this.rowIndex;
	}

}
