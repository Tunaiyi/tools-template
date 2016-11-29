package funs.tools.template.excel.poi;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FormulaEvaluatorFactory {

	public static FormulaEvaluator create(Workbook wb) {
		if(wb instanceof XSSFWorkbook) {
			return new XSSFFormulaEvaluator((XSSFWorkbook)wb);
		}
		if(wb instanceof HSSFWorkbook) {
			return new HSSFFormulaEvaluator((HSSFWorkbook)wb);
		}
		throw new RuntimeException("wb 不是 XSSFWorkbook or HSSFWorkbook");
	}
}
