package com.only.laf;

import com.only.OnlyCheckBoxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

public class OnlyTableColumnModel extends DefaultTableColumnModel implements ActionListener
{
    private static final long serialVersionUID = -8111507962954048460L;
    
    private List<TableColumn> currentColumns;
    
    private Map<TableColumn, CheckBoxMenuItem> initialMap;
    
    private JPopupMenu menu;

    public OnlyTableColumnModel(JPopupMenu menu)
    {
        super();
        currentColumns = new ArrayList<TableColumn>();
        initialMap = new LinkedHashMap<TableColumn, CheckBoxMenuItem>();
        this.menu = menu;
    }
    
    public List<TableColumn> getColumns(boolean includeHidden)
    {
        if(includeHidden)
        {
            return new ArrayList<TableColumn>(initialMap.keySet());
        }
        
        return Collections.list(getColumns());
    }

    public int getColumnCount(boolean includeHidden)
    {
        if(includeHidden)
        {
            return initialMap.size();
        }
        
        return getColumnCount();
    }
    
    public void removeColumn(TableColumn column)
    {
        currentColumns.remove(column);
        menu.remove(initialMap.remove(column));
        super.removeColumn(column);
    }
    
    public void addColumn(TableColumn column)
    {
        CheckBoxMenuItem item = createMenuItem(column);
        currentColumns.add(column);
        initialMap.put(column, item);
        menu.add(item);
        super.addColumn(column);
    }
    
    public void moveColumn(int columnIndex, int newIndex)
    {
        if(columnIndex != newIndex)
        {
            updateCurrentColumns(columnIndex, newIndex);
        }
        
        super.moveColumn(columnIndex, newIndex);
    }

    private void updateCurrentColumns(int oldIndex, int newIndex)
    {
        TableColumn movedColumn = tableColumns.elementAt(oldIndex);
        int oldPosition = currentColumns.indexOf(movedColumn);
        TableColumn targetColumn = tableColumns.elementAt(newIndex);
        int newPosition = currentColumns.indexOf(targetColumn);
        currentColumns.remove(oldPosition);
        currentColumns.add(newPosition, movedColumn);
    }
    
    private TableColumn getColumnIncludeHidden(Object identifier)
    {
        for(TableColumn column: initialMap.keySet())
        {
            if(column.getIdentifier().equals(identifier))
            {
                return column;
            }
        }
        
        return null;
    }
    
    public void setColumnVisible(Object identifier, boolean visible)
    {
        TableColumn column = getColumnIncludeHidden(identifier);
        
        if(column != null)
        {
            setColumnVisible(column, visible);
        }
    }
    
    public void setColumnVisible(TableColumn column, boolean visible)
    {
        if(visible)
        {
            super.addColumn(column);
            Integer addIndex = currentColumns.indexOf(column);
            
            for(int i = 0; i < (getColumnCount() - 1); i++)
            {
                TableColumn tableCol = getColumn(i);
                int actualPosition = currentColumns.indexOf(tableCol);
                
                if(actualPosition > addIndex)
                {
                    super.moveColumn(getColumnCount() - 1, i);
                    break;
                }
            }
        }
        else
        {
            super.removeColumn(column);
        }
        
        CheckBoxMenuItem menuItem = initialMap.get(column);
        
        if(menuItem.isSelected() != visible)
        {
            menuItem.removeActionListener(this);
            menuItem.setSelected(visible);
            menuItem.addActionListener(this);
        }
    }
    
    public boolean isColumnVisible(TableColumn column)
    {
        CheckBoxMenuItem item = initialMap.get(column);
        return item != null && item.isSelected();
    }
    
    public boolean isColumnVisible(Object identifier)
    {
        TableColumn column = getColumnIncludeHidden(identifier);
        return column != null? isColumnVisible(column): false;
    }
    
    private CheckBoxMenuItem createMenuItem(TableColumn column)
    {
        CheckBoxMenuItem item = new CheckBoxMenuItem(column.getIdentifier().toString(), column);
        item.setSelected(true);
        item.addActionListener(this);
        return item;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        CheckBoxMenuItem item = (CheckBoxMenuItem)e.getSource();
        setColumnVisible(item.getColumn(), item.isSelected());
    }
    
    private class CheckBoxMenuItem extends OnlyCheckBoxMenuItem
    {
        private static final long serialVersionUID = 2333247143034833883L;
        
        private TableColumn column;
        
        public CheckBoxMenuItem(String text, TableColumn column)
        {
            super(text);
            this.column = column;
        }

        public TableColumn getColumn()
        {
            return column;
        }
    }
}