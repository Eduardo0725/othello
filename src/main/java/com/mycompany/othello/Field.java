package com.mycompany.othello;

/**
 * @author EDUARDO ANDRADE CARVALHO - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS  - RA: 125111350221
 * THIAGO REIS CARDOSO              - RA: 125111366586
 * RENATO RIBEIRO MELO FILHO        - RA: 125111370411
 * PITER MALHEIROS FANTI            - RA: 125111353595
 * VICTÓRIA SOUZA DIAS              - RA: 12523157176
 */

//Classe que define o tamanho do tabuleiro por meio de uma matriz definindo-a como 8x8
public class Field
{
    private String[][] field; 
    public static final int FIELDSIZE = 8; 
    private boolean firstTurn = true; //Define o jogador atual que inicia o jogo, sendo TRUE as peças brancas e FALSE as peças pretas

    /*Cria o tabuleiro como uma matriz de tamanho fieldsize x fieldsize que no caso foi definida como 8, portanto a matriz 8x8,
     *depois  define qual jogador começa a rodada inicial e por fim faz um loop FOR para definir as posições i,j de cada campo 
     *da matriz do tabuleiro e retorna a matriz field usada para registrar as posições e salvar o jogo em outros métodos
     */
    public String[][] setField(String file)
    {
        String[] tempField = MyLibrary.getStringArray(file);
        field = new String[FIELDSIZE][FIELDSIZE]; 
        
        String turn = tempField[0];
        firstTurn = Boolean.parseBoolean(turn);
        
        for(int i = 0; i < FIELDSIZE; i++)
        {
            for(int j = 0; j < FIELDSIZE; j++)
            {
                field[i][j] = tempField[i + 1].substring(j, j + 1);
            }
        }
        
        return field;
    }
    
    /*Retorna o turno de quem deve jogar a seguir após a definição posicional do tabuleiro, ou seja,
     *se o jogo for uma partida em andamento, carregada, esse retorno de valor indica qual dos jogadores
     *será o próximo nessa rodada, ou também após cada rodada o campo sendo atualizado
     */
    public boolean getFirstTurn()
    {
        return firstTurn;
    }
    
}
