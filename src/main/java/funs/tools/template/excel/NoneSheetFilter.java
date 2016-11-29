package funs.tools.template.excel;

public class NoneSheetFilter implements SheetFilter {
	
	private static final NoneSheetFilter INSTANCE = new NoneSheetFilter();
	
	private NoneSheetFilter(){}
	
	public static NoneSheetFilter getInstance() {
		return INSTANCE;
	}

	@Override
	public boolean accept(String sheetName) {
		return true;
	}

	

}
