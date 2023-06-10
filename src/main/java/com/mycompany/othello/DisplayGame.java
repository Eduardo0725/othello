/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.othello;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author eduardo
 */
public class DisplayGame extends JFrame {
    
    public JButton[][] buttons;

    private final String iconWhite = "images/light.png";
    private final String iconBlack = "images/dark.png";
    private final String iconLegalMove = "images/legalMoveIcon.png";

    public DisplayGame() {
        createButtons();

        this.setVisible(true);
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
    }

    private void createButtons() {
        buttons = new JButton[Field.FIELDSIZE][Field.FIELDSIZE];

        GridLayout layout = new GridLayout(Field.FIELDSIZE,Field.FIELDSIZE);
        this.setLayout(layout);

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
                this.add(buttons[i][j]);
            }
        }
    }

    public void setIconWithType(int x, int y, String iconType) {
        iconType = iconType.toLowerCase();
        String iconPath = "";
        
        switch (iconType) {
            case "white" -> iconPath = iconWhite;
            case "w" -> iconPath = iconWhite;
            case "black" -> iconPath = iconBlack;
            case "b" -> iconPath = iconBlack;
            case "legal_move" -> iconPath = iconLegalMove;
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
