/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TerrainPalette.java
 *
 * Created on 23.01.2012, 22:56:17
 */
package de.jpenguin.editor;

import de.jpenguin.editor.engine.EditorApplication;
import de.jpenguin.editor.object.ObjectManager;
import de.jpenguin.editor.terrain.TerrainManager;
import de.jpenguin.editor.xml.TypeXMLManager;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.Hashtable;
import java.util.Enumeration;

/**
 *
 * @author Karsten
 */
public class EditorPalette extends javax.swing.JFrame {

    private boolean popupOpen=false;
    
    /** Creates new form TerrainPalette */
    public EditorPalette(EditorApplication editorApp) {
        this.editorApp=editorApp;
        initComponents();
        
        
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemSetTexture = new javax.swing.JMenuItem();
        jMenuItemSetNormalMap = new javax.swing.JMenuItem();
        jMenuItemSetSize = new javax.swing.JMenuItem();
        buttonGroupPathing = new javax.swing.ButtonGroup();
        buttonGroupWater = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jRadioButtonLower = new javax.swing.JRadioButton();
        jRadioButtonRaise = new javax.swing.JRadioButton();
        jSliderSize = new javax.swing.JSlider();
        jSliderStrength = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jRadioButtonDraw = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListTextures = new javax.swing.JList();
        jRadioButtonSmooth = new javax.swing.JRadioButton();
        jRadioButtonLevel = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSliderPathingSize = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jComboBoxPathing = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jRadioButtonWaterAdd = new javax.swing.JRadioButton();
        jRadioButtonWaterRemove = new javax.swing.JRadioButton();
        jSliderWaterSize = new javax.swing.JSlider();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListWater = new javax.swing.JList();

        jMenuItemSetTexture.setText("Change Texture");
        jMenuItemSetTexture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemSetTextureMouseReleased(evt);
            }
        });
        jMenuItemSetTexture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetTextureActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemSetTexture);
        jMenuItemSetTexture.getAccessibleContext().setAccessibleName("set Texture");

        jMenuItemSetNormalMap.setText("Change NormalMap");
        jMenuItemSetNormalMap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemSetNormalMapMouseReleased(evt);
            }
        });
        jMenuItemSetNormalMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetNormalMapActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemSetNormalMap);

        jMenuItemSetSize.setText("set Size");
        jMenuItemSetSize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jMenuItemSetSizeMouseReleased(evt);
            }
        });
        jPopupMenu1.add(jMenuItemSetSize);
        jMenuItemSetSize.getAccessibleContext().setAccessibleName("jMenuItemSetSize");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Tools");
        setAlwaysOnTop(true);
        setResizable(false);

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jLabel2.setText("Size");

        buttonGroup.add(jRadioButtonLower);
        jRadioButtonLower.setText("Lower");
        jRadioButtonLower.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonLowerActionPerformed(evt);
            }
        });

        buttonGroup.add(jRadioButtonRaise);
        jRadioButtonRaise.setSelected(true);
        jRadioButtonRaise.setText("Raise");
        jRadioButtonRaise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonRaiseActionPerformed(evt);
            }
        });

        jLabel1.setText("Strength");

        buttonGroup.add(jRadioButtonDraw);
        jRadioButtonDraw.setText("Draw");

        jListTextures.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jListTexturesMouseReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jListTextures);

        buttonGroup.add(jRadioButtonSmooth);
        jRadioButtonSmooth.setText("Smooth");

        buttonGroup.add(jRadioButtonLevel);
        jRadioButtonLevel.setText("Level");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButtonSmooth)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel2))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSliderStrength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSliderSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jRadioButtonRaise, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jRadioButtonDraw))
                            .addGap(14, 14, 14)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jRadioButtonLevel)
                                .addComponent(jRadioButtonLower))
                            .addGap(14, 14, 14)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSliderSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSliderStrength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonRaise)
                    .addComponent(jRadioButtonLower))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonSmooth)
                    .addComponent(jRadioButtonLevel))
                .addGap(15, 15, 15)
                .addComponent(jRadioButtonDraw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Terrain", jPanel1);

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jList1MousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(420, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Objects", jPanel2);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroupPathing.add(jRadioButton1);
        jRadioButton1.setText("Add");
        jPanel3.add(jRadioButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        buttonGroupPathing.add(jRadioButton2);
        jRadioButton2.setText("Remove");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jRadioButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, -1, -1));
        jPanel3.add(jSliderPathingSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel3.setText("Size");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        jComboBoxPathing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ground", "Building", "Air" }));
        jPanel3.add(jComboBoxPathing, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 200, -1));

        jTabbedPane1.addTab("Pathing", jPanel3);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        buttonGroupWater.add(jRadioButtonWaterAdd);
        jRadioButtonWaterAdd.setSelected(true);
        jRadioButtonWaterAdd.setText("Add");
        jPanel4.add(jRadioButtonWaterAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        buttonGroupWater.add(jRadioButtonWaterRemove);
        jRadioButtonWaterRemove.setText("Remove");
        jRadioButtonWaterRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonWaterRemoveActionPerformed(evt);
            }
        });
        jPanel4.add(jRadioButtonWaterRemove, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, -1, -1));
        jPanel4.add(jSliderWaterSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, -1, -1));

        jLabel4.setText("Size");
        jPanel4.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        jListWater.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jListWaterMouseReleased(evt);
            }
        });
        jScrollPane3.setViewportView(jListWater);

        jPanel4.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 210, 190));

        jTabbedPane1.addTab("Water", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jRadioButtonRaiseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonRaiseActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRadioButtonRaiseActionPerformed

private void jRadioButtonLowerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonLowerActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRadioButtonLowerActionPerformed

private void jList1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList1MousePressed
   // String modelPath = typeXML.getXMLFile("DoodadType").getValue(jList1.getSelectedValue()+"", "model");
   // Float modelSize = Float.parseFloat(typeXML.getXMLFile("DoodadType").getValue(jList1.getSelectedValue()+"", "size"));
    editorApp.getObjectManager().setModelBrush(jList1.getSelectedValue()+"");
}//GEN-LAST:event_jList1MousePressed

private void jMenuItemSetTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetTextureActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jMenuItemSetTextureActionPerformed

private void jMenuItemSetSizeMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemSetSizeMouseReleased
    TerrainManager terrainManager = editorApp.getTerrainManager();
    
    String str = JOptionPane.showInputDialog(null, "Edit size", ""+terrainManager.getTextureSize(jListTextures.getSelectedIndex()));
    if(str != null)
    {
        terrainManager.setTextureSize(jListTextures.getSelectedIndex(),Double.parseDouble(str));
    }
}//GEN-LAST:event_jMenuItemSetSizeMouseReleased

private void jMenuItemSetTextureMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemSetTextureMouseReleased
  
    File dir = new File(Editor.getPath()+"assets/Textures/Terrain/");

    FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() == false && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"));
        }
    };
    File[] files = dir.listFiles(fileFilter);
    String[] array = new String[files.length];
    
    for(int i=0;i<files.length;i++)
    {
        array[i] = files[i].getName();
    }
    
    String str = (String) JOptionPane.showInputDialog(null, 
        "Select a Texture",
        "Texture",
        JOptionPane.QUESTION_MESSAGE, 
        null, 
        array, 
        array[0]);
    
    if(str != null)
    {
        TerrainManager terrainManager = editorApp.getTerrainManager();
        terrainManager.setTexture(jListTextures.getSelectedIndex(),"Textures/Terrain/"+str);
    }
}//GEN-LAST:event_jMenuItemSetTextureMouseReleased

private void jListTexturesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListTexturesMouseReleased
        if(evt.getButton() != 1)
        {
            popupOpen=true;
            try
            {
                Robot robot = new java.awt.Robot();
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
             }catch (AWTException ae) { System.out.println(ae); }
            
            
        }else{
        
    if(popupOpen){
       jPopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
       popupOpen=false;
    }}

}//GEN-LAST:event_jListTexturesMouseReleased

private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
    String title=jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
    editorApp.activate(title);
}//GEN-LAST:event_jTabbedPane1StateChanged

private void jMenuItemSetNormalMapMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItemSetNormalMapMouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_jMenuItemSetNormalMapMouseReleased

private void jMenuItemSetNormalMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSetNormalMapActionPerformed
    File dir = new File(Editor.getPath()+"assets/Textures/Terrain/");

    FileFilter fileFilter = new FileFilter() {
        public boolean accept(File file) {
            return file.isDirectory() == false && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"));
        }
    };
    File[] files = dir.listFiles(fileFilter);
    String[] array = new String[files.length+1];
    
    array[0] = "<none>";
    
    for(int i=1;i<files.length;i++)
    {
        array[i] = files[i].getName();
    }
    
    String str = (String) JOptionPane.showInputDialog(null, 
        "Select a Texture",
        "Texture",
        JOptionPane.QUESTION_MESSAGE, 
        null, 
        array, 
        array[0]);
    
    if(str != null)
    {
        TerrainManager terrainManager = editorApp.getTerrainManager();
        
        if(str.equals("<none>"))
        {
            terrainManager.setTextureNormalMap(jListTextures.getSelectedIndex(),null);
        }else{
            terrainManager.setTextureNormalMap(jListTextures.getSelectedIndex(),"Textures/Terrain/"+str);
        }
    }
}//GEN-LAST:event_jMenuItemSetNormalMapActionPerformed

private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRadioButton2ActionPerformed

private void jRadioButtonWaterRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonWaterRemoveActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jRadioButtonWaterRemoveActionPerformed

private void jListWaterMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListWaterMouseReleased
// TODO add your handling code here:
}//GEN-LAST:event_jListWaterMouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.ButtonGroup buttonGroupPathing;
    private javax.swing.ButtonGroup buttonGroupWater;
    private javax.swing.JComboBox jComboBoxPathing;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JList jListTextures;
    private javax.swing.JList jListWater;
    private javax.swing.JMenuItem jMenuItemSetNormalMap;
    private javax.swing.JMenuItem jMenuItemSetSize;
    private javax.swing.JMenuItem jMenuItemSetTexture;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButtonDraw;
    private javax.swing.JRadioButton jRadioButtonLevel;
    private javax.swing.JRadioButton jRadioButtonLower;
    private javax.swing.JRadioButton jRadioButtonRaise;
    private javax.swing.JRadioButton jRadioButtonSmooth;
    private javax.swing.JRadioButton jRadioButtonWaterAdd;
    private javax.swing.JRadioButton jRadioButtonWaterRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSlider jSliderPathingSize;
    private javax.swing.JSlider jSliderSize;
    private javax.swing.JSlider jSliderStrength;
    private javax.swing.JSlider jSliderWaterSize;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables

    private TypeXMLManager typeXML;
    private EditorApplication editorApp;
    
    public double getBrushSize()
    {
        return (jSliderSize.getValue()+1)/2;
    }
    
    public double getBrushStrength()
    {
        return jSliderStrength.getValue();
    }
    
    public String getBrushType()
    {
        if(jRadioButtonRaise.isSelected())
        {
            return "raise";
        }else if(jRadioButtonLower.isSelected()){
            return "lower";
        }else if(jRadioButtonDraw.isSelected()){
            return "draw";
        }else if(jRadioButtonSmooth.isSelected()){
            return "smooth";
        }else if(jRadioButtonLevel.isSelected()){
            return "level";
        }else{
            return "antidraw";
        }
    }
    /*
    public String getPalette()
    {
        if(jTabbedPane1.getSelectedIndex()==0)
        {
            return "terrain";
        }else if(jTabbedPane1.getSelectedIndex()==1)
            return "unit";
        else{
            return "pathing";
        }
    }
     * 
     */
    
    public String getPathingBrushType()
    {
        if(jRadioButton1.isSelected())
        {
            return "add";
        }
        return "remove";
    }
    
     public String getWaterBrushType()
    {
        if(jRadioButtonWaterAdd.isSelected())
        {
            return "add";
        }
        return "remove";
    }
    
    
    public double getPathingBrushSize()
    {
        return (jSliderPathingSize.getValue()+1)/2;
    }
   
    public int getPathingBrushMap()
    {
        return jComboBoxPathing.getSelectedIndex();
    }
    
    public double getWaterBrushSize()
    {
        return (jSliderWaterSize.getValue()+1)/2;
    }
        
  public void setWaterList(String[] array)
  {
      jListWater.setListData(array);
  }
  
  public int getWater()
  {
      return jListWater.getSelectedIndex();
  }
          
          
    public int getTexture()
    {
      //  System.out.println(jListTextures.getSelectedIndex());
        return jListTextures.getSelectedIndex();
    }
    
    public void addTypeList(TypeXMLManager tXML)
    {
        this.typeXML = tXML;
         jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = typeXML.getXMLFile("DoodadType").getElements();
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
    }

    public void setTextureList(BufferedImage[] array)
    {
        int selected = jListTextures.getSelectedIndex();
        jListTextures.setListData(array);
        jListTextures.setCellRenderer(new ImageRenderer());
        
        if(selected != -1)
        {
            jListTextures.setSelectedIndex(selected);
        }
      //  jListTextures.add
    }
    
    
    class ImageRenderer extends DefaultListCellRenderer
    {
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus)
    {
        // for default cell renderer behavior
        Component c = super.getListCellRendererComponent(list, value,
                                       index, isSelected, cellHasFocus);
        // set icon for cell image
        ((JLabel)c).setIcon(new ImageIcon((BufferedImage)value));
        ((JLabel)c).setText("");
        return c;
    }
}
    

}
