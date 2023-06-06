package com.mycompany.othello;

import java.util.Scanner;
/**
 * Contains the main method & methods to display/process main menu
 */
public class Menu
{
    
    /**
     * Displays the main game menu and processes user main menu input
     */
    public void mainMenu()
    {
        Scanner menuOptionListener = new Scanner(System.in);
        String userChoice;
        
        System.out.println("\u000c");
        System.out.println("#######################################");
        System.out.println("#                                     #");
        System.out.println("# 1. Start a new game.                #");
        System.out.println("# 2. Load the game.                   #");
        System.out.println("# 3. Help                             #");
        System.out.println("# 4. Exit the program.                #");   
        System.out.println("#                                     #");
        System.out.println("#######################################");
        userChoice = menuOptionListener.nextLine();
        
        
        processUserChoice(userChoice);
    }
    
    /**
     * Processes user main menu input
     * 
     * @param userChoice represents user's menu choice
     */
    public void processUserChoice(String userChoice)
    {
        // Game game = new Game();
        switch(userChoice)
        {
            case "1" -> Game.startGame();
            case "2" -> Game.loadGame();
            case "3" -> Game.openHelp();
            case "4" -> Game.closeGame();
            default -> mainMenu();
        }
        mainMenu();
    }
    
    
}
