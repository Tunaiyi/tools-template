package funs.tools.template.util;

import java.text.MessageFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;

public class POICellUtil {

	private static final DataFormatter dataFormatter = new DataFormatter();

	public static String getContent(Cell cell) {
		if (cell == null) {
			return null;
		}
		String content = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			content = cell.getRichStringCellValue().getString();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			content = dataFormatter.formatCellValue(cell);
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			content = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			content = ErrorEval.getText(cell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_FORMULA:
			switch (cell.getCachedFormulaResultType()) {
			case Cell.CELL_TYPE_STRING:
				RichTextString str = cell.getRichStringCellValue();
				if (str != null && str.length() > 0) {
					content = str.toString();
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				CellStyle style = cell.getCellStyle();
				if (style == null) {
					content = String.valueOf(cell.getNumericCellValue());
				} else {
					content =
							dataFormatter.formatRawCellContents(
									cell.getNumericCellValue(),
									style.getDataFormat(),
									style.getDataFormatString()
									);
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				content = String.valueOf(cell.getBooleanCellValue());
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				content = ErrorEval.getText(cell.getErrorCellValue());
				break;

			}
			break;
		default:
			throw new RuntimeException(MessageFormat.format("{0}的{1}行{2}列的单元格错误：Unexpected cell type ({3})", cell.getSheet().getSheetName()
					, cell.getRowIndex() + 1, cell.getColumnIndex() + 1, cell.getCellType()));
		}
		return content;
	}
}
