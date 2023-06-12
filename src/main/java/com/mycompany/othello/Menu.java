package com.mycompany.othello;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Contains the main method & methods to display/process main menu
 */
public class Menu extends JFrame {

    public static Menu menuFrame;
    
    private JPanel panel;
    private Container containerButtons;
    private Container containerGameFiles;
    private JComboBox gameFiles;
    private JButton buttonStartFileGame;
    private JButton buttonBackFileGame;
    private JButton buttonChooseFileGame;

    private final ArrayList<JButton> buttons = new ArrayList();
    
    public static void main()
    {
        menuFrame = new Menu();

        menuFrame.createComponents();
        menuFrame.prepareConfigurations();

        menuFrame.setVisible(true);
    }
    
    public static void processUserChoice(String userChoice)
    {
        menuFrame.setVisible(false);

        switch(userChoice)
        {
            case "startGame":
                Game.startGame();
                break;
            case "loadGame":
                changeGameFileVisible(true);
                break;
            case "closeGame":
                Game.closeGame();
                break;
            default:
                menuFrame.setVisible(true);
        }
    }
    
    private static void changeGameFileVisible(boolean show) {
        Container containerToRemove = !show ? menuFrame.containerGameFiles : menuFrame.containerButtons;
        Container containerToAdd = show ? menuFrame.containerGameFiles : menuFrame.containerButtons;
        
        menuFrame.getContentPane().remove(containerToRemove);
        menuFrame.getContentPane().add(containerToAdd);
        
        menuFrame.getContentPane().revalidate();
        menuFrame.getContentPane().repaint();
        
        menuFrame.setVisible(true);
    }
    
    private void prepareConfigurations() {
        menuFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        menuFrame.setLocationRelativeTo(null);
        menuFrame.pack();
//        menuFrame.setExtendedState(MAXIMIZED_BOTH);
//        menuFrame.setResizable(false);
    }
    
    private void createComponents() {
        createPanel();
        
        createTitle();
        createButtons();
        
        createFileGameComponent();
    }
    
    private void createFileGameComponent() {
        containerGameFiles = new Container();
        containerGameFiles.setLayout(new GridLayout(5, 1, 0, 15));
        
        createFilenameListComboBox();
        createButtonStartGame();
        createButtonBackFileGame();
        createButtonChooseFileGame();
    }
    
    private void createFilenameListComboBox() {
        JLabel title = new JLabel("Selecione um arquivo de jogo:");
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        containerGameFiles.add(title);
        
        String[] files = MyLibrary.getFileNamesInFolder("savedGames/").split("#");
        gameFiles = new JComboBox(files);
        containerGameFiles.add(gameFiles);
    }
    
    private void createButtonStartGame() {
        buttonStartFileGame = new JButton("Carregar arquivo selecionado");
        buttonStartFileGame.addActionListener((ActionEvent e) -> {
            String filename = gameFiles.getSelectedItem().toString();

            if (!filename.isBlank()) {
                menuFrame.setVisible(false);
                Game.loadFileGame(filename);
            }
        });
        containerGameFiles.add(buttonStartFileGame);
    }
    
    private void createButtonBackFileGame() {
        buttonBackFileGame = new JButton("Voltar");
        buttonBackFileGame.addActionListener((ActionEvent e) -> {
            changeGameFileVisible(false);
        });
        containerGameFiles.add(buttonBackFileGame);
    }
    
    private void createButtonChooseFileGame() {
        buttonChooseFileGame = new JButton("Escolher arquivo");
        buttonChooseFileGame.addActionListener((ActionEvent e) -> {
            openFile();
        });
        containerGameFiles.add(buttonChooseFileGame);
    }
    
    private static void openFile() {
        JFileChooser chooser = new JFileChooser();
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            
            menuFrame.setVisible(false);
            Game.loadFileGame(file.getPath());
        }
    }
    
    private void createPanel() {
        panel = new JPanel();
        int borderSize = 50;

        panel.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));

        menuFrame.setContentPane(menuFrame.panel);
        menuFrame.getContentPane().setLayout(new GridLayout(2, 1));
    }
    
    private void createButtons() {
        createButtonSaveGame();
        createButtonLoadGame();
        createButtonCloseGame();

        loadButtons();
    }
    
    private void createTitle() {
        JLabel title = new JLabel("Othello");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font(null, Font.BOLD, 30));
        
        Container containerTitle = new Container();
        containerTitle.setLayout(new GridLayout(1, 1));
        containerTitle.add(title);
        
        menuFrame.getContentPane().add(containerTitle);
    }
    
    private void loadButtons() {
        containerButtons = new Container();
        GridLayout layout = new GridLayout(menuFrame.buttons.size(), 1, 0, 15);
        containerButtons.setLayout(layout);

        menuFrame.buttons.forEach((button) -> {
            containerButtons.add(button);
        });

        menuFrame.getContentPane().add(containerButtons);
    }
    
    private void createButtonSaveGame() {
        menuFrame.buttons.add(createButton("Iniciar novo jogo", "startGame"));
    }

    private void createButtonLoadGame() {
        JButton btn = createButton("Carregar jogo", "loadGame");
        
        if (!MyLibrary.hasGameFileSaved()) {
            disableButton(btn);
        }
        
        menuFrame.buttons.add(btn);
    }

    private void createButtonCloseGame() {
        menuFrame.buttons.add(createButton("Sair", "closeGame"));
    }
    
    private JButton createButton(String text, String actionName) {
        JButton btn = new JButton(text);

        btn = prepareEventButton(btn, actionName);
        btn = changeButtonSize(btn);

        return btn;
    }
    
    private JButton prepareEventButton(JButton btn, String actionName) {
        btn.setActionCommand(actionName);
        btn.addActionListener((ActionEvent evt) -> {
            JButton button = (JButton) evt.getSource();
            processUserChoice(button.getActionCommand());
        });
        
        return btn;
    }
    
    private JButton changeButtonSize(JButton btn) {
        int btnHeight = 25;
        int btnWidth = 100;

        btn.setBorder(new EmptyBorder(btnHeight, btnWidth, btnHeight, btnWidth));

        return btn;
    }
    
    private JButton disableButton(JButton btn) {
        btn.setOpaque(true);
        btn.setEnabled(false);
        btn.repaint();
        
        return btn;
    }
    
}
