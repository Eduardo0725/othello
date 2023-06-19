package com.mycompany.othello;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Contains methods regarding the flow of the game
 *
 * @author EDUARDO ANDRADE CARVALHO     - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS 		- RA: 125111350221
 * THIAGO REIS CARDOSO                	- RA: 125111366586
 * RENATO RIBEIRO MELO FILHO         	- RA: 125111370411
 * PITER MALHEIROS FANTI                - RA: 125111353595
 * VICTÓRIA SOUZA DIAS                 	- RA: 12523157176
 */
class Game
{
    private static final int[] scores = {
        0, // White
        0  // Black
    };
    private static final String DEFAULTFIELD = "blueprint/fieldBlu.txt"; //the path to the field blueprint
    
    private static Field fieldObj = new Field();
    private static String[][] field;
    private static boolean curPlayer = true; //true - current player is white, false - current player is black
    private static boolean canChangePlayer = true;
    private static Scanner regScan = new Scanner(System.in); //scanner that is used throughout the program
    private static String winner = "UNDEFINED";  //as the game ends, stores the winner's colour
    private static String currentFileGame = null;
    
    public static DisplayGame displayGame = new DisplayGame();
    public static HashMap<String, Boolean> movesLeft = new HashMap();
    
    interface ForEachField {  
        public void ForEachField(int x, int y);
    }

    /**
     * Default constructor, initializes class fields
     * 
     */
    public static void resetValues()
    {
        fieldObj = new Field();
        regScan = new Scanner(System.in);
        curPlayer = true; //sets current player to white
        winner = "UNDEFINED"; //sets winned to undefined as the game has just begun
        displayGame = new DisplayGame();
        resetScores();
    }
    
    /**
     * Starts the game loading the game field, settings starting discs and sets the first player to white
     * 
     */
    public static void startGame()
    {
    	currentFileGame = null;
    	
        field = fieldObj.setField(DEFAULTFIELD);
        curPlayer = fieldObj.getFirstTurn();
        gameFlow();
    }
    
    /**
     * Loads the game from file selected by user
     * 
     */
    public static void loadGame()
    {        
        String[] fileArray = MyLibrary.getFileNamesInFolder("savedGames/"); //splits passed file names into separate paths
        
        if(fileArray.length == 1 && fileArray[0].equals(""))
        {
            System.out.println("No available save files... (press ENTER to get back to the main menu)"); //happens if there are no available files in that folder
            regScan.nextLine();
            return;
        }

        System.out.println("Available save files:");    
        
        for(int i = 0; i < fileArray.length; i++)       //loops through the array
        {                                               
            String temp = fileArray[i];                 //stores the file path into a variable
            temp = temp.replace("savedGames\\", "");    //cuts the unnecessary parts of the path (folder name and
            temp = temp.replace(".txt", "");            //.txt format)
            System.out.println(temp);                   //prints off the formatted file name
        }
        
        //gets user input, adds the folder name and the .txt format and gets a field array from the field object, sets current player according to the save file
        System.out.println("\nWhich save file to load:");  
        String fileToLoad = regScan.nextLine();
        fileToLoad = "savedGames/" + fileToLoad + ".txt";
        System.out.println(fileToLoad);
        field = fieldObj.setField(fileToLoad);
        curPlayer = fieldObj.getFirstTurn();
        gameFlow();

    }
    
    public static void loadFileGame(String fileToLoad) {
        System.out.println("fileToLoad: " + fileToLoad);
        currentFileGame = fileToLoad;
        field = fieldObj.setField(fileToLoad);
        curPlayer = fieldObj.getFirstTurn();
        gameFlow();
    }
    
    /**
     * Saves the game with the filename selected by user into a savedGames folder of the program
     * 
     */
    public static void saveGame()
    {
        ArrayList<String> tempField = new ArrayList();

        //gets the name of the file to save game to
        String file = currentFileGame;
        if (file == null) {
        	String[] files = MyLibrary.getFileNamesInFolder("savedGames/");
            file = "savedGames/file" + (files.length + 1) + ".txt";
        };
                    
        String currentPlayer = String.valueOf(curPlayer);   //stores current player's colour
        tempField.add(currentPlayer);                       //adds current player as the first field to the arrayList
        
        //stores the whole field into an array
        for (String[] field1 : field) {
            String tempLine = "";

            //transforms the row of values of a 2d array into a single string
            for (int j = 0; j < field[0].length; j++) {
                tempLine += field1[j];
            }

            tempField.add(tempLine); //adds this string to the array
        }
                
        //saves the current player's colour and field into a file
        MyLibrary.saveArrayList(tempField,file);
        currentFileGame = file;
        System.out.println("Game successfully saved. Filename: " + file);
        
        JOptionPane.showMessageDialog(null, "Jogo salvo com sucesso!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Sets and updates the counters for the number of white/black discs on the board
     * 
     */
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
    
    private static void resetScores() {
        scores[0] = 0;
        scores[1] = 0;
    }

    /**
     * Exits the game
     * 
     */
    public static void closeGame()
    {
        System.exit(0);
    }
    
    public static void gameFlow() {
        loadFunctionsInButtons();
        reloadButtons();
        displayCounters();
    }
    
    public static void setPart(int x, int y) {
        String userInput = convertNumberToCaracterPosition(y) + (x + 1);
        
        System.out.println("Position: " + userInput);

        if(!checkTurnInput(x, y) || !checkMoveLegal(userInput))
        {
            System.out.println("INVALID MOVE");
            return;
        }

        displayGame.setIconWithType(x, y, curPlayer ? "w" : "b");

        curPlayer = !curPlayer;

        reloadButtons();
        displayCounters();
    }
    
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
    
    /**
     * Updates and prints the counters for the number of white/black discs on the board
     */
    public static void displayCounters()
    {
        setCounters();
    }

    public static boolean checkTurnInput(int x, int y)
    {
        if (x < 0 || x > Field.FIELDSIZE || y < 0 || y > Field.FIELDSIZE) {
            System.out.println("INVALID LETTER");

            return false;
        }

        return true;
    }
    
    /**
     * Checks if the user's turn was legal according to the game rules
     * 
     * @param place represents the user turn input
     * @return moveLegal tells if the move was legal or not
     */
    public static boolean checkMoveLegal(String place)
    {
        String letter = place.substring(0,1).toLowerCase();
        int num = Integer.parseInt(place.substring(1,2));
        int letterNum = convertCaracterToNumberPosition(letter);
        String playerColour = curPlayer ? "W" : "B"; //sets the current player's colour

        //gets the coordinate value from the secon part of the string
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

    /**
     * if player is trying to put a disc onto an empty tile, check if the move is legal, else move is not legal
     */
    public static boolean canPutDisc(String position)
    {
        return position.equals("0");
    }
    
    /**
     * Finds and returns a list of available moves for the current player
     * 
     * @return moves the list of legals move available for the current player
     */
    public static ArrayList<String> getAvailableMoves()
    {
        ArrayList<String> moves = new ArrayList(); //stores the available moves
        String playerColour = curPlayer ? "W" : "B";
        int num; //stores a vertical coordinate of the line displayed to user
        boolean[] directions = new boolean[8]; //used to check if the appropriate lines form in different directions

        //loops through each tile on the field (field array), checking if the lines form
        for(int tempV = 0; tempV < field.length; tempV++)
        {
            for(int tempH = 0; tempH < field[0].length; tempH++)
            {
                //if the line is unoccupied by discs, placing a disc on it would be a valid move, else does nothing
                if(field[tempV][tempH].equals("0"))
                {
                    //checks if placing a disc on that tile would make a line form for each direction
                    directions = getDirectionsOfPosition(tempV, tempH, playerColour, "check");
                    
                    //loops through the directions and checks if line is formed in any of these directions thus making a move legal
                    for(int i = 0; i < directions.length; i++)
                    {
                        //if the move is legal, transform it in a format, understandable to user
                        if(directions[i] == true)
                        {
                            //the horizontal coordinate transformed into letter
                            //assign a letter value to a horizontal coordinate
                            String letterH = convertNumberToCaracterPosition(tempH);
                            num = tempV + 1;  //increase a vertical coordinate by one (the first line on the field presented to user is 1st line, but in the field array it's 0th)
                            moves.add(letterH+num); //stores a move
                            break;
                        }
                    }
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Checks if user's turn will form a valid line of discs that can be flipped
     * 
     * @param vertPos represents the vertical position of the tile to be checked
     * @param horPos represents the horizontal position of the tile to be checked
     * @param direction represents the direction in which to check if the line will be formed
     * @param playerColor represents the symbol of the current player ("W" for white, "B" for black)
     * @param action tells the method whether to check if both the move is legal and flip the discs, or just check if the move is legal
     * 
     * @return lineFormed represents the legality of the move
     */
    public static boolean checkValidLineFormed(int vertPos, int horPos, String direction, String playerColor, String action)    
    {
        boolean lineFormed = false; //value that is being returned
        int infinity = 0; //is always equal to 0
        
        //makes the method work properly and each time it's called to start from the beginning (sometimes, it beginned from the middle without the loop, i don't know why).
        while(infinity == 0)
        {
            int deltaH = 0; //distance by which horizontal coordinate will be changed each time
            int deltaV = 0; //distance by which vertical coordinate will be changed each time
            int tempH = horPos; //temporary tile horizontal position
            int tempV = vertPos;//temporary tile vertical position
            
            //sets the delta variables according to the direction of the line checked

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

            //if the next tile in the line is within the field bounds and is opposite player's colour, start checking the line, else no valid line can be formed in that direction
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
                //while the tiles are within the bonds, check the tiles
                while(tempV >= 0 && tempV < field.length && tempH >= 0 && tempH < field[0].length)
                {
                    //if the next tile of the line is empty, no valid lime can be formed in that direction, else if next tile contains an opponent's disc, a valid line was formed
                    //and then break out of loop, if neither of these two happened, check the next tile
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
            
            //if line was formed and the command to the method was equal to 'domove', flip discs
            if(lineFormed == true && action.equals("domove"))
            {
                //go backwards, subtracting delta variables from temporary variables until the coordinates are equal to the line beginning coordinates
                while(tempV != vertPos || tempH != horPos)
                {
                    field[tempV][tempH] = playerColor; //flips the disc
                    
                    tempH -= deltaH;
                    tempV -= deltaV;
                }

                field[vertPos][horPos] = playerColor; // put a new disc on the appropriate tile of the board
            }

            return lineFormed;
        }

        return lineFormed;
    }
    
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
    
    /**
     * Ends the game stating the winner and getting beck to the main menu
     * 
     */
    public static void endGame()
    {
        winner = getWinner();
        
        if(winner.equals("tie"))
        {
            System.out.println("It's a tie!");
        }
        else
        {
            System.out.println("Winner: " + winner + "!");
        }
    }
}
