package com.mycompany.othello;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * Contains methods regarding the flow of the game
 * 
 * @author Aleksejs Loginovs
 */
class Game
{
    private static final String DEFAULTFIELD = "blueprint/fieldBlu.txt"; //the path to the field blueprint
    private static Field fieldObj = new Field();
    private static String[][] field;
    private static int black = 0; //number of black discs on the board
    private static int white = 0; //number of white discs on the board
    private static boolean curPlayer = true; //true - current player is white, false - current player is black
    private static Scanner regScan = new Scanner(System.in); //scanner that is used throughout the program
    private static String winner = "UNDEFINED";  //as the game ends, stores the winner's colour
    private static boolean surrender = false; //if someone decides to surrender, becomes true and stops the loop in gameFlow()
    public static DisplayGame displayGame = new DisplayGame();
    
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
        black = 0;    //sets counter values
        white = 0;    //    to 0
        curPlayer = true; //sets current player to white
        surrender = false;
        winner = "UNDEFINED"; //sets winned to undefined as the game has just begun
        displayGame = new DisplayGame();
    }
    
    /**
     * Starts the game loading the game field, settings starting discs and sets the first player to white
     * 
     */
    public static void startGame()
    {
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
        String files = MyLibrary.getFileNamesInFolder("savedGames/"); //stores the names of files in the 'savedGames' folder
        String[] fileArray = files.split("#"); //splits passed file names into separate paths
        
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
        field = fieldObj.setField(fileToLoad);
        curPlayer = fieldObj.getFirstTurn();
        gameFlow();
    }
    
    /**
     * Saves the game with the filename selected by user into a savedGames folder of the program
     * 
     * 
     */
    public static void saveGame()
    {
        String file;
        ArrayList<String> tempField = new ArrayList();

        //gets the name of the file to save game to
        System.out.println("Enter the name of the file you want to save your game in:");
        file = regScan.nextLine();
            
            
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
            
        file = "savedGames/" + file + ".txt";
        
        //saves the current player's colour and field into a file
        MyLibrary.saveArrayList(tempField,file);
        System.out.println("Game successfully saved. (press ENTER)");
        
        regScan.nextLine();
    }
    
    /**
     * Sets and updates the counters for the number of white/black discs on the board
     * 
     */
    public static void setCounters()
    {
        int b = 0;
        int w = 0;

        //scans the field updating the counters
        for (String[] field1 : field) {
            
            for (int j = 0; j < field[0].length; j++) {
                
                if (field1[j].equals("B")) {
                    b++;
                } else if (field1[j].equals("W")) {
                    w++;
                }
            }
        }

        black = b;
        white = w;
    }
    
    /**
     * Exits the game
     * 
     */
    public static void closeGame()
    {
        System.exit(0);
    }
    
    /**
     * Opens the help text file and prints it on the screen
     * 
     */
    public static void openHelp()
    {
        System.out.println("\u000c");
        MyLibrary.readwriter("read", "help/help.txt");
        System.out.println("(PRESS ENTER)");
        regScan.nextLine();
    }
    
    /**
     * Represents the flow of the game, getting user turn inputs, processing them etc.
     * 
     */
//    public static void gameFlow()
//    {
//        //variables that determine if the first move was already made           
//        boolean wasFirstMove = false;   
//        
//        boolean skippedTurn = false; //determines of the previous turn was skipped due to lack of moves
//        boolean validMove;   //determines if user's input was 'grammatically' correct and can be further processed by the program
//        boolean moveLegal = false;   //determines if the user's input was a legal move, allowed by the game rules
//        String userInput;
//        int infinite = 0; //while it's equal to 0, loop runs. it always is equal to 0
//        boolean gameFinished = false; //determines if the game was finished or not
//        
//        HashMap<String, Boolean> movesLeft = new HashMap();
//        ArrayList<String> movesList; //stores the available moves for the current player
//        boolean moves;      //determines if there are available moves for the current player
//        
//        //temporary variables used to check if the previous&current move were both skipped
//        boolean one;
//        boolean two;
//        
//        //the same as the two variables above, but not temporary
//        movesLeft.put("White", true);
//        movesLeft.put("Black", true);
//        
//        do
//        {
//            String colour;
//            String opposColour;
//            
//            //if first move was made, update variable
//            if(!wasFirstMove)
//            {
//                wasFirstMove = true;
//            }
//            
//            //if both the first move was made and it was legal,  OR  if previous turn was skipped, change the current player and reset skippedTUrn variable
//            if((wasFirstMove && moveLegal) || skippedTurn)
//            {
//                curPlayer = !curPlayer;
//                skippedTurn = false;
//                moveLegal = false;
//            }
//            
//            //store current and opposite players' colours
//            if(curPlayer)
//            {
//                colour = "White";
//                opposColour = "Black";
//            }
//            else
//            {
//                colour = "Black";
//                opposColour = "White";;
//            }
//                
//            //print the field, update counters, print counters and store available moves
//            System.out.println("\u000c");
//            fieldObj.printField();
//            reloadButtons();
//            displayCounters();
//            movesList = getAvailableMoves();
//            
//            //if there are available moves - print them, it there are no available moves, print an appropriate message and skip the turn
//            if(movesList.isEmpty())
//            {
//                moves = false;
//                skippedTurn = true;
//                System.out.println(colour + " ,you don't have any moves left. Skipping turn. (press ENTER)");
//                regScan.nextLine();
//            }
//            else
//            {
//                moves = true;
//                int moveCount = 0; //stores the number of printed moves not to print more than 5 moves in a line, to make it more clear
//                System.out.println("Available moves: (" + movesList.size() + ")"); //also prints the number of available moves
//
//                for(int i = 0; i < movesList.size(); i++)
//                {
//                    System.out.print(movesList.get(i)+"\t"); //prints the move
//                    moveCount++;
//                    
//                    //if line contains 5 moves, print a blank line and reset the number of moves printed
//                    if(moveCount == 5)
//                    {
//                        System.out.println();
//                        moveCount = 0;
//                    }
//                }
//                System.out.println("\n");
//            }
//            
//            //store if there were moves left for the current player
//            movesLeft.put(colour, moves);
//            
//            //if there were moves left for the current player, ask him to make a move
//            if(movesLeft.get(colour))
//            {
//                System.out.println(colour + " player's turn:");
//                userInput = regScan.nextLine();
//                
//                //if the user writes 'menu', open the menu, else he makes a move -> check if the input was correct and the move tha tuser is trying to make is legal
//                if(userInput.equals("menu"))
//                {
//                    inGameMenu();
//                    moveLegal = false;
//                }
//                else
//                {
//                    validMove = checkTurnInput(userInput); //move is 'gramatically' correct
//                    if(validMove)
//                    {
//                        moveLegal = checkMoveLegal(userInput); //move is legal according to the game rules
//                    }
//                }
//                movesLeft.put(opposColour, true); //sets the previous player turn to the default value (not skipped)
//            }
//            
//            //if both previous player's turn and current player's turn was skipped, end the game
//            one = movesLeft.get(colour);
//            two = movesLeft.get(opposColour);
//            if(one == false && two == false)
//            {
//                gameFinished = true;
//            }
//        }
//        while(infinite == 0 && surrender == false && gameFinished == false);
//        
//        //if the game was finished, define the winner and print it
//        if(gameFinished)
//        {
//            endGame();
//        }
//
//        //reset the values of the variables
//        surrender = false;
//    }

    public static void gameFlow() {
        //print the field, update counters, print counters and store available moves
        System.out.println("\u000c");
        fieldObj.printField();
        loadFunctionsInButtons();
        reloadButtons();
        displayCounters();
    }
    
    public static HashMap<String, Boolean> movesLeft = new HashMap();
    
//    public static void setPart(int x, int y)
//    {
//        //variables that determine if the first move was already made           
//        boolean wasFirstMove = false;   
//        
//        boolean skippedTurn = false; //determines of the previous turn was skipped due to lack of moves
//        boolean moveLegal = false;   //determines if the user's input was a legal move, allowed by the game rules
//        String userInput;
//
//        // HashMap<String, Boolean> movesLeft = new HashMap();
//        ArrayList<String> movesList; //stores the available moves for the current player
//        boolean moves;      //determines if there are available moves for the current player
//        
//        //temporary variables used to check if the previous&current move were both skipped
//        boolean one;
//        boolean two;
//        
//        //the same as the two variables above, but not temporary
//        if (movesLeft.isEmpty()) {
//            movesLeft.put("White", true);
//            movesLeft.put("Black", true);
//        }
//        
//        
//
//            String colour;
//            String opposColour;
//
//            //if first move was made, update variable
//            if(!wasFirstMove)
//            {
//                wasFirstMove = true;
//            }
//
//            //if both the first move was made and it was legal,  OR  if previous turn was skipped, change the current player and reset skippedTUrn variable
//            if((wasFirstMove && moveLegal) || skippedTurn)
//            {
//                curPlayer = !curPlayer;
//                skippedTurn = false;
//                moveLegal = false;
//            }
//
//            //store current and opposite players' colours
//            if(curPlayer)
//            {
//                colour = "White";
//                opposColour = "Black";
//            }
//            else
//            {
//                colour = "Black";
//                opposColour = "White";;
//            }
//
//            //print the field, update counters, print counters and store available moves
//            System.out.println("\u000c");
//            fieldObj.printField();
//            reloadButtons();
//            displayCounters();
//            movesList = getAvailableMoves();
//            
//            //if there are available moves - print them, it there are no available moves, print an appropriate message and skip the turn
//            if(movesList.isEmpty())
//            {
//                moves = false;
//                skippedTurn = true;
//                System.out.println(colour + " ,you don't have any moves left. Skipping turn. (press ENTER)");
//                regScan.nextLine();
//            }
//            else
//            {
//                moves = true;
//                int moveCount = 0; //stores the number of printed moves not to print more than 5 moves in a line, to make it more clear
//                System.out.println("Available moves: (" + movesList.size() + ")"); //also prints the number of available moves
//
//                for(int i = 0; i < movesList.size(); i++)
//                {
//                    System.out.print(movesList.get(i)+"\t"); //prints the move
//                    moveCount++;
//                    
//                    //if line contains 5 moves, print a blank line and reset the number of moves printed
//                    if(moveCount == 5)
//                    {
//                        System.out.println();
//                        moveCount = 0;
//                    }
//                }
//                System.out.println("\n");
//            }
//            
//            //store if there were moves left for the current player
//            movesLeft.put(colour, moves);
//            
//            //if there were moves left for the current player, ask him to make a move
//            if(movesLeft.get(colour))
//            {
//                System.out.println(colour + " player's turn:");
//                userInput = convertNumberToCaracterPosition(x) + y;
//                
//                //if the user writes 'menu', open the menu, else he makes a move -> check if the input was correct and the move tha tuser is trying to make is legal
////                if(userInput.equals("menu"))
////                {
////                    inGameMenu();
////                    moveLegal = false;
////                }
////                else
////                {
//                    if(checkTurnInput(userInput))
//                    {
//                        moveLegal = checkMoveLegal(userInput); //move is legal according to the game rules
//                    }
////                }
//                movesLeft.put(opposColour, true); //sets the previous player turn to the default value (not skipped)
//            }
//            
//            //if both previous player's turn and current player's turn was skipped, end the game
//            one = movesLeft.get(colour);
//            two = movesLeft.get(opposColour);
//            if(one == false && two == false)
//            {
//                movesLeft.put("White", true);
//                movesLeft.put("Black", true);
//
//                endGame();
//            }
//
//        //reset the values of the variables
//        surrender = false;
//    }
    
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
    }
    
    private static String convertNumberToCaracterPosition(int numberPosition) {
        return switch (numberPosition) {
            case 0 -> "a";
            case 1 -> "b";
            case 2 -> "c";
            case 3 -> "d";
            case 4 -> "e";
            case 5 -> "f";
            case 6 -> "g";
            case 7 -> "h";
            default -> "UNDEFINED";
        };
    }
    
    private static int convertCaracterToNumberPosition(String charPosition) {
        return switch (charPosition) {
            case "a" -> 0;
            case "b" -> 1;
            case "c" -> 2;
            case "d" -> 3;
            case "e" -> 4;
            case "f" -> 5;
            case "g" -> 6;
            case "h" -> 7;
            default -> 10000;
        };
    }

    private static void reloadButtons() {
        forEachField((i, j) -> {
            if (field[i][j].equals("0")) {
                displayGame.removeIcon(i, j);
            } else {
                displayGame.setIconWithType(i, j, field[i][j]);
            }
        });
        
        loadLegalMovePositions();
    }

    public static void forEachField(ForEachField forEachFieldCallback) {
        for(int i = 0; i < Field.FIELDSIZE; i++)
        {
            for(int j = 0; j < Field.FIELDSIZE; j++)
            {
                forEachFieldCallback.ForEachField(i, j);
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
    
    private static boolean canChangePlayer = true;
    
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
        System.out.println("\nWhite: " + white);
        System.out.println("Black: " + black + "\n\n");
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
                            String letterH = switch (tempH) {
                                case 0 -> "a";
                                case 1 -> "b";
                                case 2 -> "c";
                                case 3 -> "d";
                                case 4 -> "e";
                                case 5 -> "f";
                                case 6 -> "g";
                                case 7 -> "h";
                                default -> "UNDEFINED";
                            };
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
    
    /**
     * Displays the secondary menu and gets user menu input
     * 
     */
    public static void inGameMenu()
    {
        String input;
        System.out.println("\u000c");
        System.out.println("#######################################");
        System.out.println("#                                     #");
        System.out.println("# 1. Back to game                     #");
        System.out.println("# 2. Help                             #");
        System.out.println("# 3. Surrender                        #");
        System.out.println("# 4. Save the game                    #");
        System.out.println("# 5. Save and exit                    #"); 
        System.out.println("# 6. Exit the program                 #");
        System.out.println("#                                     #");
        System.out.println("#######################################");
        input = regScan.nextLine();
        
        processInGameMenu(input); 
    }
    
    /**
     * Processes user secondary menu input
     * 
     * @param input represents user secondary menu input
     */
    public static void processInGameMenu(String input)
    {
        switch(input)
        {
            case "1":   
                return;  //resumes the game
            case "2":   
                openHelp();
                break;
            case "3":   
                surrender(); //surrenders the game for the current player
                surrender = true;
                return;
            case "4":   
                saveGame(); //saves the game
                break;
            case "5":   
                saveGame(); //saves AND exits the game
                surrender = true;
                return;
            case "6":   
                surrender = true; // exits the game
                return;
            default:    
                inGameMenu(); //reopens the main menu if the user input was invalid
                break;
        }
    }
    
    /**
     * Surrenders the game, displaying the winner and setting the surrender variable's value so that it will break out of gameFlow infinite loop and return to main menu
     * 
     */
    public static void surrender()
    {
        surrender = true;
        String colour = curPlayer ? "Black" : "White";

        System.out.println("\u000c");
        fieldObj.printField();
        displayCounters();
        System.out.println(colour + " wins.\n(PRESS ENTER)");

        regScan.nextLine(); //waits until user presses enter to let him appreciate his victory and view the field
    }
    
    public static String getWinner() {
        if(black > white)
        {
            return "black";
        }

        if(white > black)
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
