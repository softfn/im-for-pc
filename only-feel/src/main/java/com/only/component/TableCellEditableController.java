package com.only.component;

public interface TableCellEditableController {

    /**
     * 单元格是否可编辑
     *
     * @param rowInModel TableModel中的行号
     * @param columnInModel TableModel中的列号
     * @return 是否可编辑
     */
    public boolean isCellEditable(int rowInModel, int columnInModel);
}
