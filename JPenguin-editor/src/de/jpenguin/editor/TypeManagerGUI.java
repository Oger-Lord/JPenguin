/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TypeManager.java
 *
 * Created on 21.01.2012, 16:15:41
 */
package de.jpenguin.editor;

import de.jpenguin.editor.xml.TypeXMLManager;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
/**
 *
 * @author Karsten
 */
public class TypeManagerGUI extends javax.swing.JFrame {

    private boolean popupOpen=false;

    // Variables declaration - do not modify  
    
    private javax.swing.JPanel[] jPanel = new javax.swing.JPanel[10];
    private javax.swing.JList[] jList = new javax.swing.JList[10];
    private javax.swing.JTable[] jTable = new javax.swing.JTable[10];
    private javax.swing.JScrollPane[] jScrollPane1 = new javax.swing.JScrollPane[10];
    private javax.swing.JScrollPane[] jScrollPane2 = new javax.swing.JScrollPane[10];
    
    
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemCopy;
    private javax.swing.JMenuItem jMenuItemCreateNew;
    private javax.swing.JMenuItem jMenuItemDelete;
    private javax.swing.JMenuItem jMenuItemRename;
    
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JTabbedPane jTabbedPane1;
    
    // End of variables declaration                   
   private TypeXMLManager typeXML;
   private Editor editor;
   
   private String test;
   
   
    /** Creates new form TypeManager */
    public TypeManagerGUI(Editor editor) {
        this.editor=editor;
        initComponents();
    }
                 
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemCreateNew = new javax.swing.JMenuItem();
        jMenuItemRename = new javax.swing.JMenuItem();
        jMenuItemCopy = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();  


        jPopupMenu1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPopupMenu1FocusLost(evt);
            }
        });

        jMenuItemCreateNew.setLabel("New");
        jMenuItemCreateNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemCreateNewMouseReleased(evt);
            }
        });
        jMenuItemCreateNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCreateNewActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemCreateNew);

        jMenuItemRename.setLabel("Rename");
        jMenuItemRename.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemRenameMouseReleased(evt);
            }
        });
        jMenuItemRename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRenameActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemRename);

        jMenuItemCopy.setLabel("Copy");
        jMenuItemCopy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemCopyMouseReleased(evt);
            }
        });
        jPopupMenu1.add(jMenuItemCopy);

        jMenuItemDelete.setLabel("Delete");
        jMenuItemDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemDeleteMouseReleased(evt);
            }
        });
        jPopupMenu1.add(jMenuItemDelete);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 478, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }                      

    
private void jTableMouseClicked(java.awt.event.MouseEvent evt) { 
    int id = jTabbedPane1.getSelectedIndex();
    String name =jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
    
    if(evt.getClickCount() == 2)
    {
        int row = jTable[id].getSelectedRow();
        //int column = jTable.getSelectedColumn();
       // System.out.println(jTable.getValueAt(row, column));
        
        String str = JOptionPane.showInputDialog(null, "Edit the value", ""+jTable[id].getValueAt(row, 1));
        if(str != null)
        {
            typeXML.getXMLFile(name).changeTable(jList[id].getSelectedValue()+"", row, str);
            jTable[id].setValueAt(str, row, 1);
            editor.getEditorApplication().updateType(name,(String)jList[id].getSelectedValue(),typeXML.getXMLFile(name).getKeyName(row));
        }
    }
}                                   

private void formMouseClicked(java.awt.event.MouseEvent evt) {                                  
    
}                                 

private void jMenuItemRenameActionPerformed(java.awt.event.ActionEvent evt) {                                                
// TODO add your handling code here:
}                                               

private void jPopupMenu1FocusLost(java.awt.event.FocusEvent evt) {                                      
    //jPopupMenu1.setVisible(false);
}                                     

private void jMenuItemCreateNewActionPerformed(java.awt.event.ActionEvent evt) {                                                   
// TODO add your handling code here:
}                                                  

private void jListMouseReleased(java.awt.event.MouseEvent evt) {                                    

    if(evt.getButton() == 1)
    {
        updateTable();
    
      if(popupOpen){
       jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
       popupOpen=false;
    }
         
    }
    else 
    {

        popupOpen=true;
        try
 	{
            Robot robot = new java.awt.Robot();
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }catch (AWTException ae) { System.out.println(ae); }
        
    }
    
}                                   

private void jMenuItemRenameMouseReleased(java.awt.event.MouseEvent evt) {                                              
    int id = jTabbedPane1.getSelectedIndex();
    String name =jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
    
    if(jList[id].getSelectedValue() != null)
    {
        String oldname=(String)jList[id].getSelectedValue();
        String str = JOptionPane.showInputDialog(null, "New Name",jList[id].getSelectedValue());
        if(str != null)
        {
            if(typeXML.getXMLFile(name).renameElement(jList[id].getSelectedValue()+"",str))
            {
                updateList(id);
                jList[id].setSelectedValue(str,false);
                editor.getEditorApplication().renameType(name,oldname,str);
            }else{
                JOptionPane.showMessageDialog(null, "Error", "Id already exists!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}                                             

private void jMenuItemCopyMouseReleased(java.awt.event.MouseEvent evt) {                                            
    int id = jTabbedPane1.getSelectedIndex();
    String name =jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
    
   if(jList[id].getSelectedValue() != null)
        {
        String str = JOptionPane.showInputDialog(null, "Id",jList[id].getSelectedValue()+"1");
        if(str != null)
        {
            if(typeXML.getXMLFile(name).copyElement(jList[id].getSelectedValue()+"",str)){
            updateList(id);
            jList[id].setSelectedValue(str, true);
            editor.getEditorApplication().newType(name,str);
            }else{
                JOptionPane.showMessageDialog(null, "Error", "Id already exists!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}                                           

private void jMenuItemDeleteMouseReleased(java.awt.event.MouseEvent evt) {                                              
    int id = jTabbedPane1.getSelectedIndex();
    String name =jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
    
    if(jList[id].getSelectedValue() != null)
    {
        int i =JOptionPane.showConfirmDialog(null,"Do you want to delete the element?","",JOptionPane.YES_NO_OPTION);
        if(i == JOptionPane.YES_OPTION)
        {
            String selected = (String)jList[id].getSelectedValue();
            typeXML.getXMLFile(name).removeElement(jList[id].getSelectedValue()+"");
            updateList(id);
            
            editor.getEditorApplication().removeType(name,selected);
            
        }
    }
}                                             

private void jMenuItemCreateNewMouseReleased(java.awt.event.MouseEvent evt) {  
    int id = jTabbedPane1.getSelectedIndex();
    String name =jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
    
        String str = JOptionPane.showInputDialog(null, "Id","newId");
        if(str != null)
        {
            if(typeXML.getXMLFile(name).newElement(str))
            {
                updateList(id);
                jList[id].setSelectedValue(str, true);
                editor.getEditorApplication().newType(name,str);
            }else{
                JOptionPane.showMessageDialog(null, "Error", "Id already exists!", JOptionPane.ERROR_MESSAGE);
            }
        }
}                                                


   public void load(TypeXMLManager typeXML)
   {
      this.typeXML=typeXML;
      
      String array[] = typeXML.getElements();
      for(int i=0;i<array.length;i++)
      {
          addType(typeXML,array[i]);
      }
      
      jTabbedPane1.setSelectedIndex(0);
   }
   
   private void addType(TypeXMLManager typeXML, String name)
   {
        int id = jTabbedPane1.getTabCount();
       
        jPanel[id] = new javax.swing.JPanel();
        jScrollPane2[id] = new javax.swing.JScrollPane();
        jTable[id] = new javax.swing.JTable();
        jScrollPane1[id] = new javax.swing.JScrollPane();
        jList[id] = new javax.swing.JList();
        
        jTable[id].setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable[id].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableMouseClicked(evt);
            }
        });
        jScrollPane2[id].setViewportView(jTable[id]);
        jTable[id].getColumnModel().getColumn(0).setPreferredWidth(50);

        jList[id].setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList[id].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jListMouseReleased(evt);
            }
        });
        jScrollPane1[id].setViewportView(jList[id]);

        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel[id]);
        jPanel[id].setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1[id], javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2[id], javax.swing.GroupLayout.DEFAULT_SIZE, 575, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1[id], javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(jScrollPane2[id], 0, 0, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(name, jPanel[id]);
        updateList(id);
   }
   
   public void updateTable()
   {
       int id = jTabbedPane1.getSelectedIndex();
       String name =jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
       
        if(jList[id].getSelectedValue() != null)
        {
        jTable[id].setModel(new javax.swing.table.DefaultTableModel(
                typeXML.getXMLFile(name).getTable(jList[id].getSelectedValue()+""),
                new String [] {
                    "Name", "Value"
                }
            ) {
                boolean[] canEdit = new boolean [] {
                    false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return false;
                }
            });
        }
   }
   
   public void updateList(int id)
   {
       test =jTabbedPane1.getTitleAt(id);
       
       if(editor.getEditorApplication() !=null)
       {
           // editor.getEditorApplication().updateTools(typeXML);
       }
       
         jList[id].setModel(new javax.swing.DefaultListModel() {
            String[] strings = typeXML.getXMLFile(test).getElements();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
   }

}

