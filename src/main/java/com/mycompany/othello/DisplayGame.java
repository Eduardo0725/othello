//Pacote othello, cujo qual estamos utilizando
package com.mycompany.othello; 
// importação da biblioteca awt para criação de interface gráfica em Containers
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author EDUARDO ANDRADE CARVALHO - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS  - RA: 125111350221
 * THIAGO REIS CARDOSO              - RA: 125111366586
 * RENATO RIBEIRO MELO FILHO        - RA: 125111370411
 * PITER MALHEIROS FANTI            - RA: 125111353595
 * VICTÓRIA SOUZA DIAS              - RA: 12523157176
 */

/*
 * Classe para criar display da pontuação, dos botões 
 * e ícones em geral das peças, tabuleiro e outros
 * por meio da extensão JFrame da AWT e Swing importadas anteriormente
 * usando os containeres, cria as janelas do jogo
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

    //Construtor da classe DisplayGame com os métodos de componentes e de configurações iniciais que a compõem
    public DisplayGame() {
        createComponents();

        prepareConfigurations();
    }
    
    /*
     *Prepara as configurações iniciais do display do jogo, isso é,torna a janela visível,
     *habilita fechamento da janela, define o tamanho da mesma, centraliza a janela, etc
     */
    private void prepareConfigurations() {
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    
    public void showDisplay() {
        this.setVisible(true);
    }
    
    public void hiddenDisplay() {
        this.setVisible(false);
    }
    
    /*
     *Cria os componentes em forma de métodos que serão mostrados no jogo:
     * o placar, os botões e o botão de salvar
     */
    private void createComponents() {
        createPanel();
        
        createScoreboard();
        createButtons();
        createSaveButton();
        createBackButton();
    }
    
    /*Cria o painel com seu tamanho de borda e o layout pronto para 
     *receber os containeres dos demais itens de display.
     */
    private void createPanel() {
        JPanel panel = new JPanel();
        int borderSize = 15;

        panel.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize)); //define tamanhos de borda para o painel

        this.setContentPane(panel);	
        FlowLayout layout = new FlowLayout();			//Cria o layout do painel
        layout.setAlignment(FlowLayout.LEFT);			//Alinha o painel à esquerda
        this.getContentPane().setLayout(layout);
    }
    
    /*Cria o botão de salvamento alinhando-o à esquerda, definindo seu container
     *depois define no botão JButton a identifição "Salvar" e adiciona o evento de 
     *salvar o jogo, chamando-o da classe Game ao apertar o botão
     */
    private void createSaveButton() {
    	FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        
    	containerSaveButton = new Container();
    	containerSaveButton.setLayout(layout);
    	
    	JButton saveButton = new JButton("Salvar");
    	saveButton.addActionListener((ActionEvent evt) -> {
            Game.saveGame();								//Ao receber o evento de ser selecionado, chama o método de salvamento da classe Game
    	});
    	containerSaveButton.add(saveButton);
    	
    	this.getContentPane().add(containerSaveButton);   	
    }
    
    //Cria o botão de sair da partida, salvando-a automaticamente e retornando ao menu principal do jogo
    private void createBackButton() {
    	JButton backButton = new JButton("Sair");
    	backButton.addActionListener((ActionEvent evt) -> {
            Game.backToMenuAndSaveGame();								//Ao receber o evento de ser selecionado, chama o método que volta para o menu
    	});
    	containerSaveButton.add(backButton);
    }

    //Cria a tabela de score para cada jogador, define a fonte e tamanho da escrita
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
    
    //Atualiza a pontuação de cada jogador
    public void setScore(boolean curPlayer, String name, int score) {
        int index = curPlayer ? 0 : 1;
        
        scores[index].setText(name + ": " + score);
        this.getContentPane().repaint();
    }
    
    //Cria os botões do tabuleiro 8x8 habilitando a interação para posicionar as peças
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
                
//               hiddenBackgroundButton(button);
                
                buttons[i][j] = button;
                containerButtons.add(buttons[i][j]);
            }
        }
        
        this.getContentPane().add(containerButtons);
    }
    
    //Método para ocultar background dos botões
    private void hiddenBackgroundButton(JButton button) {
      button.setOpaque(false);				
      button.setContentAreaFilled(false);	
      button.setBorderPainted(false);	
    }

    //Método de colocar os ícones das peças de cada cor e de movimentos válidos com a interface gráfica
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
    
    //Remove ícones quando campo não tiver recebido movimentos deixando apenas os botões do tabuleiro
    public void removeIcon(int x, int y) {
        buttons[x][y].setIcon(null);
    }
    
    //Permite selecionar os botões do tabuleiro, se a jogada é válida na classe Game, permitirá receber o movimento
    public void setEventButton(int x, int y, ActionListener actionListener) {
        buttons[x][y].addActionListener(actionListener);
    }

}
