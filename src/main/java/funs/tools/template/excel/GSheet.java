package funs.tools.template.excel;

public interface GSheet extends Iterable<GRow> {

	String getName();

	GRow getRow(int rowIndex);
	
	int getRowNum();
}
