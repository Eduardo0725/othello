package com.mycompany.othello;

import java.awt.Container;
import java.awt.FlowLayout;
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
 * @author EDUARDO ANDRADE CARVALHO - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS  - RA: 125111350221
 * THIAGO REIS CARDOSO              - RA: 125111366586
 * RENATO RIBEIRO MELO FILHO        - RA: 125111370411
 * PITER MALHEIROS FANTI            - RA: 125111353595
 * VICTÓRIA SOUZA DIAS              - RA: 12523157176
 */

/*Classe responsável por criar o menu principal do jogo
 *define seu painel, os containeres de botões e arquivos salvos do jogo,
 *e os botões de opções do menu, para iniciar o jogo, carregar ou sair.
 */
public class Menu extends JFrame {

    public static Menu menuFrame;		//Objeto menuFrame, que inicia os componentes e configurações da janela de menu
    
    private JPanel panel;
    private Container containerButtons;
    private Container containerGameFiles;
    private JComboBox gameFiles;
    private JButton buttonStartFileGame;
    private JButton buttonBackFileGame;
    private JButton buttonChooseFileGame;

    private final ArrayList<JButton> buttons = new ArrayList();
    
    public static void main() {
        menuFrame = new Menu();

        menuFrame.createComponents();		//Método de preparação de componentes, como botões, título, e painel
        menuFrame.prepareConfigurations();	//Prepara configurações de fechamento, posicionamento e redimensionamento do painel

        menuFrame.setVisible(true);			//Deixa visível a janela de menu
    }
    
    //Processa a escolha de usuário entre as opções do menu: Iniciar Novo Jogo; Carregar Jogo; Sair do Jogo 
    public static void processUserChoice(String userChoice) {
        menuFrame.setVisible(false);

        switch(userChoice)			//Processa dependendo da escolha do botão selecionado, chamando a função respectiva da classe Game
        {
            case "startGame":
                Game.startGame();
                break;
            case "loadGame":
                changeGameFileVisible(true);	//Caso loadGame, torna visível lista de arquivos de jogo salvos para selecionar no menu de carregamento
                break;
            case "closeGame":
                Game.closeGame();
                break;
            default:
                menuFrame.setVisible(true);
        }
    }
    
    public static void openMenu() {
        changeGameFileVisible(false);
        menuFrame.setVisible(true);
    }
    
    /*Define quais Containeres serão removídos ou adicionados conforme arquivos e botões de containerGameFiles e containerButtons.
     *É ativado quando for carregar um jogo salvo, habilitando a lista de arquivos.txt para escolha do jogador retomar a partida
     *tornando-a visível.
     */
    private static void changeGameFileVisible(boolean show) {
        Container containerToRemove = !show ? menuFrame.containerGameFiles : menuFrame.containerButtons;
        Container containerToAdd = show ? menuFrame.containerGameFiles : menuFrame.containerButtons;
        
        menuFrame.getContentPane().remove(containerToRemove);
        menuFrame.getContentPane().add(containerToAdd);
        
        menuFrame.getContentPane().revalidate();
        menuFrame.getContentPane().repaint();
        
        if (show)
        {
            menuFrame.updateFilenameListInComboBox();
        }
        
        menuFrame.setVisible(true);
    }
    
    /*Prepara configurações do painel da janela de menu, habilitando fechamento, redimensionamento ou não,
     * minimização da janela e posição inicial da mesma.
     */
    private void prepareConfigurations() {
        menuFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        menuFrame.setLocationRelativeTo(null);
        menuFrame.pack();
        menuFrame.setExtendedState(MAXIMIZED_BOTH);
        menuFrame.setResizable(false);
    }
    
    /*Prepara componentes do painel da janela de menu, criando o painel, o título e botões além dos componentes de arquivo do jogo
     * que serão vistos apenas no menu de carregamento
     */
    private void createComponents() {
        createPanel();
        
        createTitle();
        createButtons();
        
        createFileGameComponent();
    }
    
    //Cria componentes do menu de carregamento de arquivo do jogo alterando o menu para as opções de carregamento
    private void createFileGameComponent() {
        containerGameFiles = new Container();
        containerGameFiles.setLayout(new GridLayout(5, 1, 0, 15));
        
        createFilenameListComboBox();
        createButtonConfirmLoadGame();
        createButtonBackFileGame();
        createButtonChooseFileGame();
    }
    
    private void updateFilenameListInComboBox() {
        String[] files = MyLibrary.getFileNamesInFolder("savedGames/");

        gameFiles.removeAllItems();

        for (int i = 0; i < files.length; i++)
        {
            gameFiles.addItem(files[i]);
        }
    }
    
    //Cria lista de arquivos salvos para o usuário selecionar o jogo salvo
    private void createFilenameListComboBox() {
        JLabel title = new JLabel("Selecione um arquivo de jogo:");
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        containerGameFiles.add(title);
        
        gameFiles = new JComboBox();
        
        containerGameFiles.add(gameFiles);
    }
    
    //Cria o botão de carregar o jogo selecionado do menu de carregamento de arquivo
    private void createButtonConfirmLoadGame() {
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
    
    //Cria o botão de voltar ao menu principal
    private void createButtonBackFileGame() {
        buttonBackFileGame = new JButton("Voltar");
        buttonBackFileGame.addActionListener((ActionEvent e) -> {
            changeGameFileVisible(false);
        });
        containerGameFiles.add(buttonBackFileGame);
    }
    
    //Cria o botão de confirmar o carregamento do jogo selecionado
    private void createButtonChooseFileGame() {
        buttonChooseFileGame = new JButton("Escolher arquivo");
        buttonChooseFileGame.addActionListener((ActionEvent e) -> {
            openFile();
        });
        containerGameFiles.add(buttonChooseFileGame);
    }
    
    //Processo de carregamento do arquivo de jogo selecionado após confirmar o arquivo
    private static void openFile() {
        JFileChooser chooser = new JFileChooser();
        
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            
            menuFrame.setVisible(false);
            Game.loadFileGame(file.getPath());
        }
    }
    
    //Cria o painel chamado no método createComponents() para a janela do menu
    private void createPanel() {
        panel = new JPanel();
        int borderSize = 50;

        panel.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));

        menuFrame.setContentPane(menuFrame.panel);
        menuFrame.getContentPane().setLayout(new GridLayout(2, 1));
    }
    
    //Cria os botões chamados no método createComponents() para opções do menu principal
    private void createButtons() {
        createButtonStartGame();
        createButtonLoadGame();
        createButtonCloseGame();

        loadButtons();
    }
    
    //Cria o título do menu principal, o nome do jogo "Othello", com alinhammento central e fonte Bold em tamanho 30
    private void createTitle() {
        JLabel title = new JLabel("Othello");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font(null, Font.BOLD, 30));
        
        Container containerTitle = new Container();
        containerTitle.setLayout(new GridLayout(1, 1));
        containerTitle.add(title);
        
        menuFrame.getContentPane().add(containerTitle);
    }
    
    //Carrega os botões do Menu principal criados anteriormente, para mostrá-los na janela do menu
    private void loadButtons() {
        containerButtons = new Container();
        GridLayout layout = new GridLayout(1, 3, 0, 15);
        containerButtons.setLayout(layout);
        
        Container[] containers = new Container[menuFrame.buttons.size()];
        Container containerMain = new Container();
        containerMain.setLayout(new GridLayout(3, 1, 0, 15));
        
        containerMain.setBounds(0, 0, 100, 20);
        
        for (int i = 0; i < menuFrame.buttons.size(); i++) {
            containers[i] = new Container();
            containers[i].setLayout(new GridLayout(1, 1));
            containers[i].add(menuFrame.buttons.get(i));
            containerMain.add(containers[i]);
        }
        
        Container containerTest = new Container();
        containerTest.setLayout(new FlowLayout());
        containerTest.add(containerMain);
        
        containerButtons.add(new Container());
        containerButtons.add(containerTest);
        containerButtons.add(new Container());

        menuFrame.getContentPane().add(containerButtons);
    }
    
    //Define botão de iniciar o jogo chamado no menu principal antes da partida
    private void createButtonStartGame() {
        menuFrame.buttons.add(createButton("Iniciar novo jogo", "startGame"));
    }

    //Define botão de carregar o jogo que leva ao menu de carregamento de arquivos de jogo salvos
    private void createButtonLoadGame() {
        JButton btn = createButton("Carregar jogo", "loadGame");
        
        if (!MyLibrary.hasGameFileSaved()) {
            disableButton(btn);
        }
        
        menuFrame.buttons.add(btn);
    }

    //Define botão de sair do jogo no menu principal
    private void createButtonCloseGame() {
        menuFrame.buttons.add(createButton("Sair", "closeGame"));
    }
    
    //Prepara eventos de botões, indicando o que cada botão faz conforme valores retornados pelo JButton selecionado no menu
    private JButton createButton(String text, String actionName) {
        JButton btn = new JButton(text);

        btn = prepareEventButton(btn, actionName);
        btn = changeButtonSize(btn);

        return btn;
    }
    
    //Dimensiona altura, largura e bordas do botão retornando um botão
    private JButton prepareEventButton(JButton btn, String actionName) {
        btn.setActionCommand(actionName);
        btn.addActionListener((ActionEvent evt) -> {
            JButton button = (JButton) evt.getSource();
            processUserChoice(button.getActionCommand());
        });
        
        return btn;
    }
    
    //Desabilita botões na troca de menu principal para de carregamento e no processo inverso
    private JButton changeButtonSize(JButton btn) {
        int btnHeight = 20;
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
