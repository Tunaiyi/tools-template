package funs.tools.template.excel;

public interface GCell {

	/**
	 * 返回单元格内容
	 * @return
	 */
	String getContent();
	
	/**
	 * 返回列索引，基于0
	 * @return
	 */
    int getColumnIndex();

    /**
     * 返回含索引，基于0
     * @return
     */
    int getRowIndex();
    
}
