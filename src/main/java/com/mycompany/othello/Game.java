package com.mycompany.othello;

import java.util.HashMap;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * @author EDUARDO ANDRADE CARVALHO - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS  - RA: 125111350221
 * THIAGO REIS CARDOSO              - RA: 125111366586
 * RENATO RIBEIRO MELO FILHO        - RA: 125111370411
 * PITER MALHEIROS FANTI            - RA: 125111353595
 * VICTÓRIA SOUZA DIAS              - RA: 12523157176
 */

/*Classe que processa o fluxo do jogo, nela iniciamos a matriz posicional do tabuleiro pelo DEFAULTFIELD com a blueprint de um tabuleiro
 *inicial com as peças brancas e pretas intercaladas no meio da matriz e com os outros campos zerados, depoisdefinimos o objeto de 
 *fieldObj como o tabuleiro e o field como suas posições, seguindo pelo indicador de jogador atual e o de troca de jogador para após um movimento.
 *curPlayer TRUE define a vez das peças brancas jogarem, FALSE das peças pretas.
 */
class Game
{
    private static final int[] scores = {
        0, // Peças brancas marcadas como W de "White"
        0  // Peças pretas  marcadas como B de "Black"
    };
    private static final String DEFAULTFIELD = "blueprint/fieldBlu.txt"; //Caminho para a blueprint de início de partida
    
    private static Field fieldObj = new Field();
    private static String[][] field;
    private static boolean curPlayer = true; 
    private static boolean canChangePlayer = true;
    private static String winner = "UNDEFINED";  
    private static String currentFileGame = null;
    
    public static DisplayGame displayGame = new DisplayGame();			//Cria a interface gráfica definida na classe DisplayGame();
    public static HashMap<String, Boolean> movesLeft = new HashMap();
    
    interface ForEachField {  
        public void ForEachField(int x, int y);
    }

    /*Inicializa os parâmetros da classe, criando novo tabuleiro, novo scanner para receber os movimentos,
     *definindo o jogador atual (TRUE peças brancas e FALSE peças pretas), e dando Display no jogo com a 
     *criação das janelas definidas em DisplayGame e zerando as pontuações.
     */
    public static void resetValues()
    {
        fieldObj = new Field();
        curPlayer = true; 
        winner = "UNDEFINED"; 
        displayGame = new DisplayGame();
        resetScores();
    }
    
    /*Inicia o jogo novo conforme classe Menu processando a escolha de jogo do usuário
     * iniciando novo tabuleiro padrão, definindo o jogador do novo turno e deixando nulo 
     * o currentFileGame para poder-se criar um novo arquivo de jogo salvo
     */
    public static void startGame()
    {
        displayGame.showDisplay();
    	currentFileGame = null;
    	
        field = fieldObj.setField(DEFAULTFIELD); //Monta tabuleiro conforme blueprint de padrão de partida
        curPlayer = fieldObj.getFirstTurn();	 //Define o primeiro turno
        gameFlow();
    }
    
    /*Carrega o jogo salvo em formato de texto retomando as posições da matriz do tabuleiro com suas respectivas peças e 
     * definindo o turno do jogador atual, salvando o nome do arquivo no currentFileGame para que caso seja salvo novamente
     * sobrescreva o arquivo ao invés de criar novos para a mesma partida
     */
    public static void loadFileGame(String fileToLoad) {
        displayGame.showDisplay();
        System.out.println("fileToLoad: " + fileToLoad);
        currentFileGame = fileToLoad;
        field = fieldObj.setField(fileToLoad);
        curPlayer = fieldObj.getFirstTurn();
        gameFlow();
    }
    
    /*Salva o jogo conforme disposiçãp da matriz do tabuleiro e vez do jogador atual.
     *Também garante o nome padrão "file" + o número do arquivo novo atual, mas garantindoo que caso o jogo seja
     *carregado ou já tenha sido salvo, será sobrescrito e não criado um novo
     */
    public static void saveGame()
    {
        ArrayList<String> tempField = new ArrayList();

        String file = currentFileGame;
        if (file == null) {
            String[] files = MyLibrary.getFileNamesInFolder("savedGames/");
            file = "savedGames/file" + (files.length + 1) + ".txt";
        };
                    
        String currentPlayer = String.valueOf(curPlayer);   
        tempField.add(currentPlayer);                       
        
        for (String[] field1 : field) {
            String tempLine = "";

            for (int j = 0; j < field[0].length; j++) {
                tempLine += field1[j];
            }

            tempField.add(tempLine); 
        }
                
        MyLibrary.saveArrayList(tempField,file);
        currentFileGame = file;
        System.out.println("Jogo salvo com sucesso. Filename: " + file);
        
        JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Define os contadores de peças brancas e pretas contando quantas ocorrências de cada há na matriz a cada rodada
    public static void setCounters()
    {
        resetScores();
        
        forEachField((x, y) -> {
            if (field[x][y].equals("B")) {
                scores[1]++;
            } else if (field[x][y].equals("W")) {
                scores[0]++;
            }
        });
        
        displayGame.setScore(true, "White", scores[0]);
        displayGame.setScore(false, "Black", scores[1]);
    }
    
    //Redefine a pontuação para 0
    private static void resetScores() {
        scores[0] = 0;
        scores[1] = 0;
    }

    //Fecha o jogo saindo do sistema
    public static void closeGame()
    {
        System.exit(0);
    }
    
    //Fluxo do jogo que carrega funções dos botões recebendo novas posições de peças na matriz e renovando os contadores
    public static void gameFlow() {
        loadFunctionsInButtons();
        reloadButtons();
        displayCounters();
    }
    
    /*Processa se o movimento é válido para gravar a posição da nova jogada do jogador atual na matriz e 
     * depois trocar o jogador para a próxima rodada dando display no console apenas para controle do backend do processo
     */
    public static void setPart(int x, int y) {
        String userInput = convertNumberToCaracterPosition(y) + (x + 1);
        
        System.out.println("Posição: " + userInput);				//Backend dando display no console

        if(!checkTurnInput(x, y) || !checkMoveLegal(userInput))
        {
            System.out.println("Movimento inválido");				//Backend dando display no console
            return;
        }

        displayGame.setIconWithType(x, y, curPlayer ? "w" : "b");	//Define a peça naquele campo da matriz selecionado pelo jogador

        curPlayer = !curPlayer;										//Troca a vez do jogador

        reloadButtons();											//Recarrega os botões
        displayCounters();											//Redefine os contadores a cada jogada
    }
    
    //Processa o backend da matriz para entendermos quais posições estão sendo alteradas no console antes da implementação da interface gráfica
    private static String convertNumberToCaracterPosition(int numberPosition) {
        switch (numberPosition) {
            case 0:
                return "a";
            case 1:
                return "b";
            case 2:
                return "c";
            case 3:
                return "d";
            case 4:
                return "e";
            case 5:
                return "f";
            case 6:
                return "g";
            case 7:
                return "h";
        }
        
        return "UNDEFINED";
    }
    
    //Processa o backend da matriz para entendermos quais posições estão sendo alteradas no console antes da implementação da interface gráfica
    private static int convertCaracterToNumberPosition(String charPosition) {
        switch (charPosition) {
            case "a": 
                return 0;
            case "b": 
                return 1;
            case "c": 
                return 2;
            case "d": 
                return 3;
            case "e": 
                return 4;
            case "f": 
                return 5;
            case "g": 
                return 6;
            case "h": 
                return 7;
        }
        
        return 10000;
    }

    //Recarrega os botões considerando os novos valores da matriz do tabuleiro
    private static void reloadButtons() {
        forEachField((x, y) -> {
            if (field[x][y].equals("0")) {
                displayGame.removeIcon(x, y);
            } else {
                displayGame.setIconWithType(x, y, field[x][y]);
            }
        });
        
        loadLegalMovePositions();
    }

    public static void forEachField(ForEachField forEachFieldCallback) {
        for(int x = 0; x < Field.FIELDSIZE; x++)
        {
            for(int y = 0; y < Field.FIELDSIZE; y++)
            {
                forEachFieldCallback.ForEachField(x, y);
            }
        }
    }
    
    //Carrega jogadas válidas para indicar possibilidade se se colocar uma peça a cada rodada e atualizar na matriz
    private static void loadLegalMovePositions() {
        ArrayList<String> moves = getAvailableMoves();
        
        if (moves.isEmpty())
        {
            if (canChangePlayer)
            {
                curPlayer = !curPlayer;
                
                canChangePlayer = false;
                loadLegalMovePositions();
                canChangePlayer = true;
                
                return;
            }
            
            System.out.println("Without moves.");
            endGame();

            return;
        }

        moves.forEach((position) -> {
            String[] positions = position.split("");

            int y = convertCaracterToNumberPosition(positions[0]);
            int x = Integer.parseInt(positions[1]) - 1;

            displayGame.setIconWithType(x, y, "legal_move");
        });
    }
    
    //Carrega as funções dos botões para atualizar a matriz do tabuleiro com a interface gráfica 
    private static void loadFunctionsInButtons() {
        for(int i = 0; i < field.length; i++)
        {
            for(int j = 0; j < field.length; j++)
            {
                JButton btn = displayGame.buttons[i][j];
                btn.setActionCommand(i + "-" + j);
                btn.addActionListener((ActionEvent evt) -> {
                    JButton button = (JButton) evt.getSource();
                    String[] position = button.getActionCommand().split("-");
                    
                    setPart(Integer.parseInt(position[0]), Integer.parseInt(position[1]));
                });
            }
        }
    }
    
    //Mostra os contadores
    public static void displayCounters()
    {
        setCounters();
    }
    
    //Verifica se o campo selecionado é válido
    public static boolean checkTurnInput(int x, int y)
    {
        if (x < 0 || x > Field.FIELDSIZE || y < 0 || y > Field.FIELDSIZE) {
            System.out.println("Campo inválido");

            return false;
        }

        return true;
    }
    
    //Verifica se o movimento é válido e registra caso seja
    public static boolean checkMoveLegal(String place)
    {
        String letter = place.substring(0,1).toLowerCase();
        int num = Integer.parseInt(place.substring(1,2));
        int letterNum = convertCaracterToNumberPosition(letter);
        String playerColour = curPlayer ? "W" : "B"; 			//Define qual jogador fez o movimento

        num -= 1;

        if(canPutDisc(field[num][letterNum]))
        {
            boolean[] directions = getDirectionsOfPosition(num, letterNum, playerColour, "domove");

            for(int i = 0; i < directions.length; i++)
            {
                if(directions[i] == true)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean[] getDirectionsOfPosition(int num, int letterNum, String playerColour, String action)
    {
        boolean[] directions = new boolean[8]; //a boolean value that tells if the line was formed in a certain direction

        directions[0] = checkValidLineFormed(num, letterNum, "up", playerColour, action);
        directions[1] = checkValidLineFormed(num, letterNum, "dUpRight", playerColour, action);
        directions[2] = checkValidLineFormed(num, letterNum, "right", playerColour, action);
        directions[3] = checkValidLineFormed(num, letterNum, "dDownRight", playerColour, action);
        directions[4] = checkValidLineFormed(num, letterNum, "down", playerColour, action);
        directions[5] = checkValidLineFormed(num, letterNum, "dDownLeft", playerColour, action);
        directions[6] = checkValidLineFormed(num, letterNum, "left", playerColour, action);
        directions[7] = checkValidLineFormed(num, letterNum, "dUpLeft", playerColour, action);

        return directions;
    }

    
    public static boolean canPutDisc(String position)
    {
        return position.equals("0");
    }
    
    //Pega movimentos válidos para indicar a cada rodada
    public static ArrayList<String> getAvailableMoves()
    {
        ArrayList<String> moves = new ArrayList(); //stores the available moves
        String playerColour = curPlayer ? "W" : "B";
        int num; 
        boolean[] directions = new boolean[8]; 

        
        for(int tempV = 0; tempV < field.length; tempV++)
        {
            for(int tempH = 0; tempH < field[0].length; tempH++)
            {                
                if(field[tempV][tempH].equals("0"))
                {                    
                    directions = getDirectionsOfPosition(tempV, tempH, playerColour, "check");
                                       
                    for(int i = 0; i < directions.length; i++)
                    {
                        if(directions[i] == true)
                        {
                            String letterH = convertNumberToCaracterPosition(tempH);
                            num = tempV + 1;  
                            moves.add(letterH+num); 
                            break;
                        }
                    }
                }
            }
        }
        
        return moves;
    }
    
    //Verifica se é formada a linha que valida o movimento
    public static boolean checkValidLineFormed(int vertPos, int horPos, String direction, String playerColor, String action)    
    {
        boolean lineFormed = false; 
        int infinity = 0; 
                
        while(infinity == 0)
        {
            int deltaH = 0; 
            int deltaV = 0; 
            int tempH = horPos; 
            int tempV = vertPos;
            
             switch (direction) {
                case "up":
                    deltaV = -1;
                    break;
                case "right":
                    deltaH = 1;
                    break;
                case "down":
                    deltaV = 1;
                    break;
                case "left":
                    deltaH = -1;
                    break;
                case "dDownLeft":
                    deltaH = -1;
                    deltaV = -1;
                    break;
                case "dUpRight":
                    deltaH = 1;
                    deltaV = -1;
                    break;
                case "dDownRight":
                    deltaH = 1;
                    deltaV = 1;
                    break;
                case "dUpLeft":
                    deltaH = -1;
                    deltaV = 1;
                    break;
                default:
                    break;
            }
            
            tempH += deltaH;
            tempV += deltaV;

            if(
                true
                && tempH >= 0 
                && tempV >= 0 
                && tempH < field[0].length 
                && tempV < field.length 
                && !field[tempV][tempH].equals(playerColor) 
                && !field[tempV][tempH].equals("0")
            )
            {                
                while(tempV >= 0 && tempV < field.length && tempH >= 0 && tempH < field[0].length)
                {
                    if(field[tempV][tempH].equals("0"))
                    {
                        lineFormed = false;
                        break;
                    }
                    
                    if(field[tempV][tempH].equals(playerColor))
                    {
                        lineFormed = true;
                        break;
                    }

                    tempV += deltaV;
                    tempH += deltaH;
                }
                    
            }
            //Se a linha for formada, altera as peças do meio para a do jogador ativo
            if(lineFormed == true && action.equals("domove"))
            {
                while(tempV != vertPos || tempH != horPos)
                {
                    field[tempV][tempH] = playerColor; 
                    
                    tempH -= deltaH;
                    tempV -= deltaV;
                }

                field[vertPos][horPos] = playerColor; 
            }

            return lineFormed;
        }

        return lineFormed;
    }
    
    //Verifica qual o vencedor ao final do jogo
    public static String getWinner() {
        if(scores[1] > scores[0])
        {
            return "black";
        }

        if(scores[0] > scores[1])
        {
            return "white";
        }

        return "tie";
    }
    
    //
    public static void endGame()
    {
        winner = getWinner();
        String message;
        
        if(winner.equals("tie"))
        {
            System.out.println("Empate!");
            message = "Empate!";
        }
        else
        {
            System.out.println("Vencedor: " + winner + "!");
            message = "O " + winner + " ganhou!";
        }
        
        JOptionPane.showMessageDialog(null, message, "Fim de jogo!", JOptionPane.INFORMATION_MESSAGE);
        
        deleteCurrentFileIfExists();
        backToMenu();
    }
    
    private static void deleteCurrentFileIfExists() {
        if (currentFileGame != null && MyLibrary.deleteFile(currentFileGame))
        {
            System.out.print("File " + currentFileGame + " deleted.");
        }
    }
    
    public static void backToMenuAndSaveGame() {
        if (currentFileGame == null)
        {
            Object[] buttons = {"Sim", "Não"};

            int option = JOptionPane.showOptionDialog(
                    null, 
                    "Deseja salvar o jogo?", 
                    "Atenção", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    buttons, 
                    buttons[0]
            );

            if (option == JOptionPane.YES_OPTION)
            {
                Game.saveGame();
            }
        }
        else
        {
            Game.saveGame();
        }

        backToMenu();
    }
    
    public static void backToMenu() {
        displayGame.hiddenDisplay();
        Menu.openMenu();
    }

}
