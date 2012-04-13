/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PlayerGUI.java
 *
 * Created on 20.03.2012, 15:50:32
 */
package de.jpenguin.editor;

import de.jpenguin.loader.PlayerDataMapManager;
import de.jpenguin.loader.PlayerDataMap;
import de.jpenguin.loader.PlayerDataManager;
import de.jpenguin.loader.PlayerData;

import com.jme3.asset.AssetManager;

import com.jme3.math.ColorRGBA;
import java.awt.Color;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class PlayerGUI extends javax.swing.JFrame {

    private javax.swing.JPanel jPanel;
    private javax.swing.JButton jButtonSave;
    
    private PlayerDataMapManager playerDataMapManager;
    
    private PlayerLine[] playerLines;
    
    private String[] controllerNames;
    
    private int numberPlayer=0;
    
    
    
    
    private class PlayerLine
    {
        private javax.swing.JPanel jPanelLine;
        
        private javax.swing.JLabel jLabelId;
        private javax.swing.JPanel jPanelColor;
        private javax.swing.JTextField jTextFieldName;
        private javax.swing.JComboBox jComboBoxController;
        
        private PlayerDataMap playerDataMap;
        private PlayerData playerData;
        
        public PlayerLine(PlayerData pData)
        {
            playerData = pData;
            
            jPanelLine = new javax.swing.JPanel();
            
            jLabelId = new javax.swing.JLabel();
            jPanelColor = new javax.swing.JPanel();
            jTextFieldName = new javax.swing.JTextField();
            jComboBoxController = new javax.swing.JComboBox(controllerNames);
            
            jLabelId.setText((numberPlayer+1)+".");
            
            //jLabelId[numberPlayer].setBounds(10,10+numberPlayer*lineHeight,200,30);
            
            jPanelColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jPanelColor.setSize(50, 50);
            
            jTextFieldName.setColumns(20);
            jTextFieldName.setText(pData.getName());
            
            playerDataMap = playerDataMapManager.getPlayer().get(pData.getId());
            if(playerDataMap != null)
            {
                jComboBoxController.setSelectedIndex(playerDataMap.getController());
            }else{
                playerDataMap = new PlayerDataMap();
                playerDataMapManager.getPlayer().put(pData.getId(),playerDataMap);
            }
            
            
         //   jPanelLine[numberPlayer].setLayout(new  java.awt.BorderLayout(7,12));
            jPanelLine.setLocation(30, 15+30*numberPlayer);
            
            jPanelLine.add(jLabelId);
            jPanelLine.add(jTextFieldName);
            jPanelLine.add(jPanelColor);
            jPanelLine.add(jComboBoxController);

            
            jPanel.add(jPanelLine);
            
            numberPlayer++;
            validate();
            
            
            jTextFieldName.setText(pData.getName());
            jPanelColor.setBackground(convertColor(pData.getColor()));
        }
        
        public void save()
        {
            if(jTextFieldName.getText().equals(playerData.getName())==false)
            {
                playerDataMap.setName(jTextFieldName.getText());
            }
            
            playerDataMap.setController(jComboBoxController.getSelectedIndex());
        }            



        
    }
    
    
    /** Creates new form PlayerGUI */
    public PlayerGUI(AssetManager assetManager,PlayerDataManager pdm) {
        super("Player Manager");
        
        playerDataMapManager = PlayerDataMapManager.load(assetManager,Editor.getMap());

        playerLines = new PlayerLine[50];
        
        controllerNames = new String[3];
        controllerNames[0] = "None";
        controllerNames[1] = "Computer";
        controllerNames[2] = "User";
        
        setDefaultCloseOperation(javax.swing.JFrame.HIDE_ON_CLOSE);
        
        
        jButtonSave = new javax.swing.JButton();
        jButtonSave.setBounds(600, 610, 150, 50);
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save();
            }
        });
        
        jPanel = new javax.swing.JPanel();
        jPanel.setLayout(new  java.awt.GridLayout(12,1));
        
        jPanel.setBounds(0,20,800, 590);
        getContentPane().add(jPanel);
        getContentPane().add(jButtonSave);
        setLayout(new  java.awt.BorderLayout(5,5));
        
        
        pack();
        
        setSize(820,700);
        
        setVisible(true);
        
        setResizable(false);
        
        ArrayList<PlayerData> al = pdm.getPlayer();
        for(int i=0;i<al.size();i++)
        {
            if(al.get(i).isVisible())
            {
                playerLines[i] = new PlayerLine(al.get(i));
            }
            al.get(i).setVisible(false);
        }
    }
    
    
    public void save()
    {
        
        for(int i=0;i<numberPlayer;i++)
        {
            playerLines[i].save();
        }
        
        PlayerDataMapManager.save(playerDataMapManager, Editor.getMapPath());
         
    }
    
    public ColorRGBA convertColor(Color c)
    {
        return new ColorRGBA((float)(c.getRed())/255,((float)c.getGreen())/255,((float)c.getBlue())/255,1f);
    }
    
    public Color convertColor(ColorRGBA rgba)
    {
        return new Color(rgba.getRed(),rgba.getGreen(),rgba.getBlue());
    }
           
}
