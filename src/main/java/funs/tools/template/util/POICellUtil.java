package funs.tools.template.util;

import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;

import java.text.MessageFormat;

public class POICellUtil {

	private static final DataFormatter dataFormatter = new DataFormatter();

	public static String getContent(Cell cell) {
		if (cell == null) {
			return null;
		}
		String content = null;
		switch (cell.getCellType()) {
		case STRING:
			content = cell.getRichStringCellValue().getString();
			break;
		case NUMERIC:
			content = dataFormatter.formatCellValue(cell);
			break;
		case BOOLEAN:
			content = String.valueOf(cell.getBooleanCellValue());
			break;
		case ERROR:
			content = ErrorEval.getText(cell.getErrorCellValue());
			break;
		case BLANK:
			break;
		case FORMULA:
			switch (cell.getCachedFormulaResultType()) {
			case STRING:
				RichTextString str = cell.getRichStringCellValue();
				if (str != null && str.length() > 0) {
					content = str.toString();
				}
				break;
			case NUMERIC:
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
			case BOOLEAN:
				content = String.valueOf(cell.getBooleanCellValue());
				break;
			case ERROR:
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
