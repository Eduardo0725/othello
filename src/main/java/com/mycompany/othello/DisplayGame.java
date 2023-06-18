/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.othello;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author eduardo
 */
public class DisplayGame extends JFrame {
    
    public JButton[][] buttons;
    
    private Container containerButtons;
    private Container containerScoreboard;
    private Container containerSaveButton;

    private final JLabel[] scores = {
        new JLabel(""),
        new JLabel("")
    };

    private final String iconWhite = "images/light.png";
    private final String iconBlack = "images/dark.png";
    private final String iconLegalMove = "images/legalMoveIcon.png";

    public DisplayGame() {
        createComponents();

        prepareConfigurations();
    }
    
    private void prepareConfigurations() {
        this.setVisible(true);
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
    }
    
    private void createComponents() {
        createPanel();
        
        createScoreboard();
        createButtons();
        createSaveButton();
    }
    
    private void createPanel() {
        JPanel panel = new JPanel();
        int borderSize = 15;

        panel.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));

        this.setContentPane(panel);
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        this.getContentPane().setLayout(layout);
    }
    
    private void createSaveButton() {
    	FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        
    	containerSaveButton = new Container();
    	containerSaveButton.setLayout(layout);
    	
    	JButton saveButton = new JButton("Salvar");
    	saveButton.addActionListener((ActionEvent evt) -> {
    		Game.saveGame();
    	});
    	containerSaveButton.add(saveButton);
    	
    	this.getContentPane().add(containerSaveButton);   	
    }

    private void createScoreboard() {
        containerScoreboard = new Container();
        containerScoreboard.setLayout(new GridLayout(1, 2));
        
        Font font = new Font(null, Font.BOLD, 18);
        scores[0].setFont(font);
        scores[1].setFont(font);
        
        scores[1].setBorder(new EmptyBorder(0, 20, 0, 0));
        
        containerScoreboard.add(scores[0]);
        containerScoreboard.add(scores[1]);
        
        this.getContentPane().add(containerScoreboard);
    }
    
    public void setScore(boolean curPlayer, String name, int score) {
        int index = curPlayer ? 0 : 1;
        
        scores[index].setText(name + ": " + score);
        this.getContentPane().repaint();
    }
    
    private void createButtons() {
        containerButtons = new Container();
        GridLayout layout = new GridLayout(Field.FIELDSIZE,Field.FIELDSIZE);
        containerButtons.setLayout(layout);
        
        buttons = new JButton[Field.FIELDSIZE][Field.FIELDSIZE];

        for(int i = 0; i < Field.FIELDSIZE; i++)
        {
            for(int j = 0; j < Field.FIELDSIZE; j++)
            {
                JButton button = new JButton();
                button.setName(i + "-" + j);
                
                button.setSize(25,25);
                
//                button.setOpaque(false);
//                button.setContentAreaFilled(false);
//                button.setBorderPainted(false);
                
                buttons[i][j] = button;
                containerButtons.add(buttons[i][j]);
            }
        }
        
        this.getContentPane().add(containerButtons);
    }

    public void setIconWithType(int x, int y, String iconType) {
        iconType = iconType.toLowerCase();
        String iconPath = "";
        
        switch (iconType) {
            case "white": 
            case "w":
                iconPath = iconWhite;
                break;
            case "black":
            case "b":
                iconPath = iconBlack;
                break;
            case "legal_move":
                iconPath = iconLegalMove;
                break;
        }
        
        if (iconPath.equals("")) {
            return;
        }
        
        Icon icon = new ImageIcon(iconPath);
        buttons[x][y].setIcon(icon);
    }
    
    public void removeIcon(int x, int y) {
        buttons[x][y].setIcon(null);
    }
    
    public void setEventButton(int x, int y, ActionListener actionListener) {
        buttons[x][y].addActionListener(actionListener);
    }

}
