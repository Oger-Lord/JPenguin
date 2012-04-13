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

import de.jpenguin.loader.PlayerDataManager;
import de.jpenguin.loader.PlayerData;

import com.jme3.math.ColorRGBA;
import java.awt.Color;

import java.util.ArrayList;
/**
 *
 * @author Karsten
 */
public class PlayerGUIGeneral extends javax.swing.JFrame {

    private javax.swing.JPanel jPanel;
    private javax.swing.JButton jButtonAddPlayer;
    private javax.swing.JButton jButtonSave;
    
    private PlayerDataManager playerDataManager;
    
    private PlayerLine[] playerLines;
    
    private int numberPlayer=0;
    
    
    
    
    private class PlayerLine
    {
        private javax.swing.JPanel jPanelLine;
        private javax.swing.JButton jButtonDelete;
        private javax.swing.JButton jButtonDown;
        private javax.swing.JButton jButtonUp;
        private javax.swing.JCheckBox jCheckBoxVisible;
        private javax.swing.JCheckBox jCheckBoxNeutral;
        private javax.swing.JLabel jLabelId;
        private javax.swing.JPanel jPanelColor;
        private javax.swing.JTextField jTextFieldId;
        private javax.swing.JTextField jTextFieldName;
        
        private PlayerData playerData;
        
        public PlayerLine()
        {
            jPanelLine = new javax.swing.JPanel();
            
            jButtonUp = new javax.swing.JButton();
            jButtonDown = new javax.swing.JButton();
            jButtonDelete = new javax.swing.JButton();
            jLabelId = new javax.swing.JLabel();
            jTextFieldId = new javax.swing.JTextField();
            jPanelColor = new javax.swing.JPanel();
            jCheckBoxVisible = new javax.swing.JCheckBox();
            jTextFieldName = new javax.swing.JTextField();
            jCheckBoxNeutral = new javax.swing.JCheckBox();
            
            jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    deletePlayer(evt);
                }
            });
            
            jButtonDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    swapPlayer(evt,true);
                }
            });
            
            jButtonUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                    swapPlayer(evt,false);
                }
            });
            
            setId(numberPlayer);
            
            //jLabelId[numberPlayer].setBounds(10,10+numberPlayer*lineHeight,200,30);
            jCheckBoxVisible.setText("Visible");
            jButtonUp.setText("Up");
            jButtonDown.setText("Down");
            jButtonDelete.setText("Delete");
            jCheckBoxNeutral.setText("Neutral");
            
            jPanelColor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            jPanelColor.setSize(50, 50);
            
            jTextFieldId.setColumns(10);
            jTextFieldName.setColumns(20);
            
         //   jPanelLine[numberPlayer].setLayout(new  java.awt.BorderLayout(7,12));
            jPanelLine.setLocation(30, 15+30*numberPlayer);
            
            jPanelLine.add(jLabelId);
            jPanelLine.add(jTextFieldId);
            jPanelLine.add(jTextFieldName);
            jPanelLine.add(jPanelColor);
            jPanelLine.add(jCheckBoxNeutral);
            jPanelLine.add(jCheckBoxVisible);
            jPanelLine.add(jButtonUp);
            jPanelLine.add(jButtonDown);
            jPanelLine.add(jButtonDelete);
            
            jPanel.add(jPanelLine);
            
            numberPlayer++;
            validate();
        }
        
        public PlayerLine(PlayerData pData)
        {
            this();
            playerData=pData;
            jTextFieldId.setText(pData.getId());
            jTextFieldName.setText(pData.getName());
            jCheckBoxVisible.setSelected(pData.isVisible());
            jPanelColor.setBackground(convertColor(pData.getColor()));
        }
        
        public PlayerLine(PlayerDataManager pdm)
        {
            this();
            playerData = new PlayerData();
            pdm.getPlayer().add(playerData);
        }
        
        public void save()
        {
            playerData.setId(jTextFieldId.getText());
            playerData.setName(jTextFieldName.getText());
            playerData.setVisible(jCheckBoxVisible.isSelected());
            playerData.setColor(convertColor(jPanelColor.getBackground()));
        }            
        

        
        
        public void setId(int id)
        {
            jLabelId.setText((id+1)+".");
            jButtonUp.setActionCommand(id+"");
            jButtonDown.setActionCommand(id+"");
            jButtonDelete.setActionCommand(id+"");
            
            
            /*
            pdm.getPlayer().remove(playerData);
            
            if(pdm.getPlayer().indexOf(playerData) > id)
            {
                pdm.getPlayer().add(id,playerData);
            }else{
                pdm.getPlayer().add(id-1,playerData);
            }
             * 
             */
            
        }
        
        public void add(PlayerDataManager pdm)
        {
            pdm.getPlayer().add(playerData);
            jPanel.add(jPanelLine);
        }
        
        public void destroy(PlayerDataManager pdm)
        {
            pdm.getPlayer().remove(playerData);
            jPanel.remove(jPanelLine);
            validate();
        }
        
    }
    
    
    /** Creates new form PlayerGUI */
    public PlayerGUIGeneral(PlayerDataManager pdm) {
        super("Player Manager");
        
      //  colors[9] = new ColorRGBA(0.5f,0.7f,1f,1f);
      //  colors[10] = new ColorRGBA(0.1f,0.5f,0.35f,1f);
        playerDataManager = pdm;
        playerLines = new PlayerLine[50];
        
        setDefaultCloseOperation(javax.swing.JFrame.HIDE_ON_CLOSE);
        
        
        jPanel = new javax.swing.JPanel();
        jButtonAddPlayer = new javax.swing.JButton();
        jButtonAddPlayer.setBounds(600, 610, 150, 50);
        jButtonAddPlayer.setText("add Player");
        jButtonAddPlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPlayer();
            }
        });
        
        jButtonSave = new javax.swing.JButton();
        jButtonSave.setBounds(430, 610, 150, 50);
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save();
            }
        });
        
        jPanel.setLayout(new  java.awt.GridLayout(12,1));
        
        jPanel.setBounds(0,20,800, 590);
        getContentPane().add(jPanel);
        getContentPane().add(jButtonAddPlayer);
        getContentPane().add(jButtonSave);
        setLayout(new  java.awt.BorderLayout(5,5));
        
        
        pack();
        
        setSize(820,700);
        
        setVisible(true);
        
        setResizable(false);
        
        ArrayList<PlayerData> al = pdm.getPlayer();
        for(int i=0;i<al.size();i++)
        {
            playerLines[i] = new PlayerLine(al.get(i));
        }
    }
    
                        
    private void addPlayer() {
    
        if(numberPlayer!=50)
        {
            
            playerLines[numberPlayer] = new PlayerLine(playerDataManager);
            
          //  System.out.println("finished");
        }
    }     
    
    
    private void deletePlayer(java.awt.event.ActionEvent evt)
    {
        int id = Integer.parseInt(evt.getActionCommand());
        
        
        playerLines[id].destroy(playerDataManager);
        
        for(int i=id+1;i<numberPlayer;i++)
        {
            playerLines[i].setId(i-1);
            playerLines[i-1] = playerLines[i];
        }
        
         validate();
        
        numberPlayer--;
    }
    
    private void swapPlayer(java.awt.event.ActionEvent evt, boolean down)
    {
        int id = Integer.parseInt(evt.getActionCommand());
        
        if(down)
        {
            swapPlayerDown(id);
            
        }else{
            if(id==0){return;} 
            
            swapPlayerDown(id-1);
        }
        
    }
    
    private void swapPlayerDown(int id)
    {
       if(id==numberPlayer-1){return;}   
            
        playerLines[id].setId(id+1);
        playerLines[id+1].setId(id);
            
        PlayerLine pl = playerLines[id];
        playerLines[id] = playerLines[id+1];
        playerLines[id+1] = pl;
        
        for(int i=id;i<numberPlayer;i++)
        {
            playerLines[i].destroy(playerDataManager);
        }
        
         for(int i=id;i<numberPlayer;i++)
        {
            playerLines[i].add(playerDataManager);
        }
    }
    
    public void save()
    {
        for(int i=0;i<numberPlayer;i++)
        {
            playerLines[i].save();
        }
        
        PlayerDataManager.save(playerDataManager, Editor.getPath());
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
