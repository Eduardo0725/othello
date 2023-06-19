package com.mycompany.othello;

/**
 * Contains methods to store and print the field
 * 
 * @author EDUARDO ANDRADE CARVALHO     - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS 		- RA: 125111350221
 * THIAGO REIS CARDOSO                	- RA: 125111366586
 * RENATO RIBEIRO MELO FILHO         	- RA: 125111370411
 * PITER MALHEIROS FANTI                - RA: 125111353595
 * VICTÓRIA SOUZA DIAS                 	- RA: 12523157176
 */
public class Field
{
    private String[][] field; //the field array
    public static final int FIELDSIZE = 8; //size of the field
    private boolean firstTurn = true; //stores the player, who has the first turn after the field was loaded

    /**
     * Sets the field
     * 
     * @param file
     * 
     * @return field contains a game field with players discs
     */
    public String[][] setField(String file)
    {
        String[] tempField = MyLibrary.getStringArray(file);
        field = new String[FIELDSIZE][FIELDSIZE]; //initialise the 2d field array
        
        //define who's turn it's supposed to be after the field loads
        String turn = tempField[0];
        firstTurn = Boolean.parseBoolean(turn);
        
        //transform a 1d field array into a 2d field array
        for(int i = 0; i < FIELDSIZE; i++)
        {
            for(int j = 0; j < FIELDSIZE; j++)
            {
                field[i][j] = tempField[i + 1].substring(j, j + 1);
            }
        }
        
        return field;
    }
    
    /**
     * Returns who's turn it's supposed to be after the field loads
     * 
     * @return firstTurn represents the player who's turn it will be
     */
    public boolean getFirstTurn()
    {
        return firstTurn;
    }
    
}
