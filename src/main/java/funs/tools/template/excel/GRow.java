package funs.tools.template.excel;

public interface GRow extends Iterable<GCell>{

	/**
	 * 获取行索引
	 * @return
	 */
    int getRowIndex();
    
    /**
     * 根据列索引获取单元格
     * @param cellIndex
     * @return
     */
    GCell getCell(int cellIndex);
    
    /**
     * 获取列数
     * @return
     */
    int getCelNum();
    
    /**
     * 是否是空行
     * @return
     */
    boolean isEmpty();
    
}
