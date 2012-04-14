/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DoodadGUI.java
 *
 * Created on 01.03.2012, 22:04:50
 */
package de.jpenguin.editor.object;

import de.jpenguin.editor.object.Doodad;
import java.awt.Color;
import javax.swing.JColorChooser;
import com.jme3.math.ColorRGBA;

/**
 *
 * @author Karsten
 */
public class DoodadGUI extends javax.swing.JFrame {

    /** Creates new form DoodadGUI */
    public DoodadGUI() {
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

        jButtonSave = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldSize = new javax.swing.JTextField();
        jTextFieldRotation = new javax.swing.JTextField();
        jTextFieldHeight = new javax.swing.JTextField();
        jPanelColor = new javax.swing.JPanel();
        jCheckBoxSize = new javax.swing.JCheckBox();
        jCheckBoxRotation = new javax.swing.JCheckBox();
        jCheckBoxHeight = new javax.swing.JCheckBox();
        jCheckBoxColor = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jTextFieldRotationB = new javax.swing.JTextField();
        jCheckBoxRotationSpecial = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldRotationC = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 300, -1, -1));

        jLabel1.setText("Size");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jLabel2.setText("Rotation");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel3.setText("Height");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 200, -1, -1));

        jLabel4.setText("Color");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, -1, -1));
        getContentPane().add(jTextFieldSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 100, -1));
        getContentPane().add(jTextFieldRotation, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 80, 100, -1));
        getContentPane().add(jTextFieldHeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 100, -1));

        jPanelColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelColorLayout = new javax.swing.GroupLayout(jPanelColor);
        jPanelColor.setLayout(jPanelColorLayout);
        jPanelColorLayout.setHorizontalGroup(
            jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanelColorLayout.setVerticalGroup(
            jPanelColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 19, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, -1, -1));

        jCheckBoxSize.setText("Individual");
        getContentPane().add(jCheckBoxSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 30, -1, -1));

        jCheckBoxRotation.setText("Individual");
        getContentPane().add(jCheckBoxRotation, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 80, -1, -1));

        jCheckBoxHeight.setText("Individual");
        getContentPane().add(jCheckBoxHeight, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 200, -1, -1));

        jCheckBoxColor.setText("Individual");
        getContentPane().add(jCheckBoxColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 250, -1, -1));

        jLabel5.setText("RotationB");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));
        getContentPane().add(jTextFieldRotationB, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 100, -1));

        jCheckBoxRotationSpecial.setText("Individual");
        getContentPane().add(jCheckBoxRotationSpecial, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, -1, -1));

        jLabel6.setText("RotationC");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 160, -1, -1));
        getContentPane().add(jTextFieldRotationC, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 100, -1));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-340)/2, (screenSize.height-394)/2, 340, 394);
    }// </editor-fold>//GEN-END:initComponents

private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
    save();
    dispose();
}//GEN-LAST:event_jButtonSaveActionPerformed

private void jPanelColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColorMouseClicked
    Color newColor = JColorChooser.showDialog(
                     null,
                     "Choose Color",
                     jPanelColor.getBackground());
    if(newColor != null)
    {
        jPanelColor.setBackground(newColor);
    }
}//GEN-LAST:event_jPanelColorMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxColor;
    private javax.swing.JCheckBox jCheckBoxHeight;
    private javax.swing.JCheckBox jCheckBoxRotation;
    private javax.swing.JCheckBox jCheckBoxRotationSpecial;
    private javax.swing.JCheckBox jCheckBoxSize;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanelColor;
    private javax.swing.JTextField jTextFieldHeight;
    private javax.swing.JTextField jTextFieldRotation;
    private javax.swing.JTextField jTextFieldRotationB;
    private javax.swing.JTextField jTextFieldRotationC;
    private javax.swing.JTextField jTextFieldSize;
    // End of variables declaration//GEN-END:variables
    private Doodad doodad;
    
    public void load(Doodad d)
    {
        doodad=d;
        
        //Height
        jTextFieldHeight.setText(doodad.getHeight()+"");
        jCheckBoxHeight.setSelected(doodad.isHighLocked());
        
        //rotation
        jTextFieldRotation.setText(doodad.getRotation()+"");
        jCheckBoxRotation.setSelected(doodad.isRotationLocked());
        
        //rotation Special
        jTextFieldRotationB.setText(doodad.getRotationB()+"");
        jTextFieldRotationC.setText(doodad.getRotationC()+"");
        jCheckBoxRotationSpecial.setSelected(doodad.isRotationSpecialLocked());
        
        //Size
        jTextFieldSize.setText(doodad.getSize()+"");
        jCheckBoxSize.setSelected(doodad.isSizeLocked());
        
        //Color
        jPanelColor.setBackground(convertColor(doodad.getColorRGBA()));
        jCheckBoxColor.setSelected(doodad.isColorLocked());
    }
    
    
    public void save()
    {
        try{
            //Height
            doodad.setHeight(Float.parseFloat(jTextFieldHeight.getText()));
            doodad.setHighLocked(jCheckBoxHeight.isSelected());

            //rotation
            doodad.setRotation(Float.parseFloat(jTextFieldRotation.getText()));
            doodad.setRotationLocked(jCheckBoxRotation.isSelected());
            
            //rotation Special
            doodad.setRotationB(Float.parseFloat(jTextFieldRotationB.getText()));
            doodad.setRotationC(Float.parseFloat(jTextFieldRotationC.getText()));
            doodad.setRotationSpecialLocked(jCheckBoxRotationSpecial.isSelected());

            //Size
            doodad.setSize(Float.parseFloat(jTextFieldSize.getText()));
            doodad.setSizeLocked(jCheckBoxSize.isSelected());

            //Color
            doodad.setColorRGBA(convertColor(jPanelColor.getBackground()));
            doodad.setColorLocked(jCheckBoxColor.isSelected());
            
            doodad.updateSpatial();
        }catch(Exception e){}
    }
    
    
    public ColorRGBA convertColor(Color c)
    {
        return new ColorRGBA((float)(c.getRed())/255,((float)c.getGreen())/255,((float)c.getBlue())/255,0.5f);
    }
    
    public Color convertColor(ColorRGBA rgba)
    {
        return new Color(rgba.getRed(),rgba.getGreen(),rgba.getBlue());
    }

}
