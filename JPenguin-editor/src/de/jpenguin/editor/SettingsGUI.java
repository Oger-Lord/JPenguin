/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SettingsGUI.java
 *
 * Created on 27.02.2012, 13:32:46
 */
package de.jpenguin.editor;

import de.jpenguin.editor.settings.*;
import java.awt.Color;
import javax.swing.JColorChooser;

import com.jme3.math.ColorRGBA;
/**
 *
 * @author Karsten
 */
public class SettingsGUI extends javax.swing.JFrame {

    private SettingManager settingManager;
    
    /** Creates new form SettingsGUI */
    public SettingsGUI(Editor editor) {
        settingManager = editor.getEditorApplication().getSettingManager();
        initComponents();
        load();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFogColor = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldLightIntensity = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldFogDensity = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jCheckBoxFog = new javax.swing.JCheckBox();
        jCheckBoxLight = new javax.swing.JCheckBox();
        jPanelLightColor = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jCheckBoxShadow = new javax.swing.JCheckBox();
        jTextFieldShadowIntensity = new javax.swing.JTextField();
        jTextFieldShadowDirection = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldLightDirection = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldFogDistance = new javax.swing.JTextField();

        setTitle("Settings");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanelFogColor.setBackground(new java.awt.Color(200, 180, 150));
        jPanelFogColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelFogColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelFogColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelFogColorLayout = new javax.swing.GroupLayout(jPanelFogColor);
        jPanelFogColor.setLayout(jPanelFogColorLayout);
        jPanelFogColorLayout.setHorizontalGroup(
            jPanelFogColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 113, Short.MAX_VALUE)
        );
        jPanelFogColorLayout.setVerticalGroup(
            jPanelFogColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelFogColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 57, -1, -1));

        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });
        getContentPane().add(jButtonSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 306, -1, -1));

        jLabel2.setText("Color");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(19, 37, -1, -1));
        getContentPane().add(jTextFieldLightIntensity, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 93, -1));

        jLabel3.setText("Distance");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        jTextFieldFogDensity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldFogDensityActionPerformed(evt);
            }
        });
        getContentPane().add(jTextFieldFogDensity, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 93, -1));

        jLabel4.setText("Density");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        jCheckBoxFog.setText("Fog");
        getContentPane().add(jCheckBoxFog, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jCheckBoxLight.setText("Ambiente Light");
        getContentPane().add(jCheckBoxLight, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 7, -1, -1));

        jPanelLightColor.setBackground(new java.awt.Color(200, 180, 150));
        jPanelLightColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelLightColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelLightColorMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelLightColorLayout = new javax.swing.GroupLayout(jPanelLightColor);
        jPanelLightColor.setLayout(jPanelLightColorLayout);
        jPanelLightColorLayout.setHorizontalGroup(
            jPanelLightColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
        );
        jPanelLightColorLayout.setVerticalGroup(
            jPanelLightColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        getContentPane().add(jPanelLightColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 57, -1, -1));

        jLabel5.setText("Color");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(172, 35, -1, -1));

        jCheckBoxShadow.setText("Shadow");
        getContentPane().add(jCheckBoxShadow, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 7, -1, -1));
        getContentPane().add(jTextFieldShadowIntensity, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 57, 96, -1));

        jTextFieldShadowDirection.setEnabled(false);
        getContentPane().add(jTextFieldShadowDirection, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 108, 96, -1));

        jLabel6.setText("Direct Light");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, -1, -1));

        jLabel7.setText("Vector Direction");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 96, -1));

        jTextFieldLightDirection.setEnabled(false);
        getContentPane().add(jTextFieldLightDirection, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 109, -1));

        jLabel8.setText("Vector Direction");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 83, 96, -1));

        jLabel9.setText("Intensity");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 100, 96, -1));

        jLabel10.setText("Intensity");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 37, 96, -1));
        getContentPane().add(jTextFieldFogDistance, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 93, -1));

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-477)/2, (screenSize.height-373)/2, 477, 373);
    }// </editor-fold>//GEN-END:initComponents

private void jTextFieldFogDensityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldFogDensityActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_jTextFieldFogDensityActionPerformed

private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
    save();
}//GEN-LAST:event_jButtonSaveActionPerformed

private void jPanelFogColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelFogColorMouseClicked
    Color newColor = JColorChooser.showDialog(
                     null,
                     "Choose Color",
                     jPanelFogColor.getBackground());
    if(newColor != null)
    {
        jPanelFogColor.setBackground(newColor);
    }
}//GEN-LAST:event_jPanelFogColorMouseClicked

private void jPanelLightColorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelLightColorMouseClicked
    Color newColor = JColorChooser.showDialog(
                     null,
                     "Choose Color",
                     jPanelLightColor.getBackground());
    if(newColor != null)
    {
        jPanelLightColor.setBackground(newColor);
    }
}//GEN-LAST:event_jPanelLightColorMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSave;
    private javax.swing.JCheckBox jCheckBoxFog;
    private javax.swing.JCheckBox jCheckBoxLight;
    private javax.swing.JCheckBox jCheckBoxShadow;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanelFogColor;
    private javax.swing.JPanel jPanelLightColor;
    private javax.swing.JTextField jTextFieldFogDensity;
    private javax.swing.JTextField jTextFieldFogDistance;
    private javax.swing.JTextField jTextFieldLightDirection;
    private javax.swing.JTextField jTextFieldLightIntensity;
    private javax.swing.JTextField jTextFieldShadowDirection;
    private javax.swing.JTextField jTextFieldShadowIntensity;
    // End of variables declaration//GEN-END:variables


    public void load()
    {
        //Fog
        jCheckBoxFog.setSelected(settingManager.isFogIsActive());
        jTextFieldFogDensity.setText(settingManager.getFogDensity()+"");
        jTextFieldFogDistance.setText(settingManager.getFogDistance()+"");
        jPanelFogColor.setBackground(convertColor(settingManager.getFogColor()));
        
        //Light
        jCheckBoxLight.setSelected(settingManager.isAmbientLightIsActive());
        jPanelLightColor.setBackground(convertColor(settingManager.getLightColor()));
        jTextFieldLightDirection.setText(settingManager.getLightVector());
        jTextFieldLightIntensity.setText(settingManager.getAmbientLightIntensity()+"");
        
        //Shadow
        jCheckBoxShadow.setSelected(settingManager.isShadowIsActive());
        jTextFieldShadowIntensity.setText(settingManager.getShadowIntensity()+"");
    }
    
    
    public void save()
    {
        try{
        //Fog
        settingManager.setFogIsActive(jCheckBoxFog.isSelected());
        settingManager.setFogDensity(Float.parseFloat(jTextFieldFogDensity.getText()));
        settingManager.setFogDistance(Float.parseFloat(jTextFieldFogDistance.getText()));
        settingManager.setFogColor(convertColor(jPanelFogColor.getBackground()));
        
        //Light
        settingManager.setAmbientLightIsActive(jCheckBoxLight.isSelected());
        settingManager.setLightColor(convertColor(jPanelLightColor.getBackground()));
        settingManager.setLightVector(jTextFieldLightDirection.getText());
        settingManager.setAmbientLightIntensity(Float.parseFloat(jTextFieldLightIntensity.getText()));

        //Shadow
        settingManager.setShadowIsActive(jCheckBoxShadow.isSelected());
        settingManager.setShadowIntensity(Float.parseFloat(jTextFieldShadowIntensity.getText()));
        
        if(settingManager.isActive())
        {
            settingManager.activate(false);
            settingManager.activate(true);
        }
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
