package com.mycompany.othello;

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * @author EDUARDO ANDRADE CARVALHO - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS  - RA: 125111350221
 * THIAGO REIS CARDOSO              - RA: 125111366586
 * RENATO RIBEIRO MELO FILHO        - RA: 125111370411
 * PITER MALHEIROS FANTI            - RA: 125111353595
 * VICTÓRIA SOUZA DIAS              - RA: 12523157176
 */

//Classe que armazena os processos e endereços dos arquivos que são chamados para compor a interface gráfica, criando nossa bilbioteca de endereços, ícones e imagens
public class MyLibrary
{
    private static final String PATH_DEFAULT = "blueprint/fieldBlu.txt";	//define o caminho padrão da blueprint de tabuleiro padrão
    
    public static void readwriter(String action, String file)
    {
        switch (action) {
            case "print":
                writeInFile(file);
                break;
            case "read":
                readFromFile(file);
                break;
            case "duplicate":
                duplicateFile(file);
                break;
        }
    }
    
    //Gravação de arquivo para montar a matriz do arquivo de jogo salvo
    public static void writeInFile(String fileName)
    {
       String userInput;
       
       try
       {
           FileOutputStream writeStream = new FileOutputStream(fileName);
           PrintWriter write = new PrintWriter(writeStream);
           Scanner doggie = new Scanner(System.in);
           
           userInput = doggie.nextLine();
           while(!userInput.equals(""))
           {
               write.println(userInput);
               userInput = doggie.nextLine();
           }
           
           write.close();
           doggie.close();
       }
       catch(IOException ex)
       {
           System.out.println("Não foi possível ler o arquivo.");
       }
    }
   
    //Leitura de arquivo de texto para carregamento, verificando sua existência e se seu arquivo pôde ser lido com êxito
    public static void readFromFile(String fileName)
    {
        if(checkFileExistence(fileName))
        {
           BufferedReader read = null;
           
           try
           {
               FileReader readStream = new FileReader(fileName);
               read = new BufferedReader(readStream);
               
               String line = read.readLine();
               while(line != null)
               {
                   System.out.println(line);
                   line = read.readLine();
               }
           }
           catch(IOException ex)
           {
               System.out.println("Algo deu errado!");
           }
           finally
           {
               try
               {
                   read.close();
               }
               catch(IOException ex)
               {
                   System.out.println("Não foi possível fechar o arquivo");
               }
           } 
        }
        else
        {
           System.out.println("Arquivo não existe.");
           readFromFile(getNewFileName());
        }
    }
    
    //Verificação de nome de arquivo duplicado
    public static void duplicateFile(String fileName)
    {
       boolean[] fileExistent = new boolean[2];
       String copiedLine;
       
       String[] file = fileName.split("<>"); 
       
       fileExistent[1] = checkFileExistence(file[1]);
       
       if(fileExistent[1])
       {
           BufferedReader read = null;
           PrintWriter write = null;
           
           try
           {
               FileReader readStream = new FileReader(file[1]);
               read = new BufferedReader(readStream);
               
               FileOutputStream writeStream = new FileOutputStream(file[0]);
               write = new PrintWriter(writeStream);
               
               copiedLine = read.readLine();
               while(copiedLine != null)
               {
                   write.println(copiedLine);
                   copiedLine = read.readLine();
               }
           }
           catch(IOException ex)
           {
               System.out.println("Arquivo não pôde ser lido");
           }
           finally
           {
               try
               {
                   read.close();
               }
               catch(IOException ex)
               {
                   System.out.println("Não foi possível fechar o arquivo");
               }
               if(write != null)
               {
                   write.close();
               }
           }
       }
       else
       {
           System.out.println("O arquivo de output não pôde ser lido.");
           String newNames = file[0] + getNewFileName();
           duplicateFile(newNames);
       }
    }
    
    //Salva lista de Arrays para gravando a matriz do arquivo
    public static void saveArrayList(ArrayList arrayToSave, String file)
    {
        try
        {
            FileOutputStream writeStream = new FileOutputStream(file);

            try (PrintWriter write = new PrintWriter(writeStream)) {
                for(int i = 0; i < arrayToSave.size(); i++)
                {
                    write.println(arrayToSave.get(i));
                }

                write.close();
            }
        }
        catch(IOException ex)
        {
            System.out.println("Algo deu errado.");
        }
    }
    
    //Leitura da lista de arrays para guardar valores da matriz
    public static String[] getStringArray(String file)
    {
        boolean existence = checkFileExistence(file);
        String[] strArray = null;
        ArrayList<String> next = new ArrayList();
        
        if (existence)
        {
            try
            {
                FileReader readStream = new FileReader(file);
                BufferedReader read = new BufferedReader(readStream);

                String nextLine = read.readLine();
                
                int i = 0;
                while(nextLine != null)
                {
                    i++;
                    next.add(nextLine);
                    nextLine = read.readLine();
                }
                
                strArray = new String[i];
                for(int x = 0; x < strArray.length; x++)
                {
                    strArray[x] = next.get(x);
                }
                
                closeFile(read);
            }
            catch(IOException ex)
            {
                System.out.println("Arquivo não pôde ser lido.");
            }

            return strArray;
        }

        System.out.println("Arquivo inexistente. Carregando arquivo padrão.");

        try
        {
            createFileDefaultIfNotExists();

            strArray = getStringArray(PATH_DEFAULT);
        }
        catch (IOException e)
        {
            System.out.println("Algo deu errado.");
        }

        return strArray;
    }
    
    //Cria arquivo padrão de tabuleiro caso não exista (para receber blueprint)
    public static void createFileDefaultIfNotExists() throws IOException {
        File newFile = new File(PATH_DEFAULT);
        
        if (newFile.exists())
        {
            return;
        }
        
        createFolderDefaultIfNotExists();
     
        newFile.createNewFile();
        System.out.println("File created: " + newFile.getPath());

        ArrayList<String> data = getInitialData();

        saveArrayList(data, PATH_DEFAULT);
    }

    //Pega dados iniciais do arquivo de salvamento para geração do tabuleiro base de início de jogo (blueprint)
    public static ArrayList<String> getInitialData() {
        ArrayList<String> data = new ArrayList();

        data.add("true");
        data.add("00000000");
        data.add("00000000");
        data.add("00000000");
        data.add("000WB000");
        data.add("000BW000");
        data.add("00000000");
        data.add("00000000");
        data.add("00000000");
        
        return data;
    }
    
    //Cria diretório padrão para blueprint
    public static void createFolderDefaultIfNotExists() {
        File folder = new File("blueprint");
        
        if (!folder.exists() && folder.mkdir()) {
            System.out.println("Diretório blueprint foi criado");
        }
    }
    
    public static void closeFile(BufferedReader read) {
        try
        {
            read.close();
        }
        catch(IOException ex)
        {
            System.out.println("Não foi possível fechar o arquivo.");
        }
    }
   
    //Teste de nome de arquivo pelo console (backend)
    public static String getNewFileName()
    {
       Scanner newFileNameScan = new Scanner(System.in);
       String newFileName;
       System.out.println("Por favor, insira o nome correto do arquivo:");
       newFileName = newFileNameScan.nextLine();
       return newFileName;
    }

    /**
     * Checks if the given file exists
     * 
     * @param file the file to check existence of
     * @return existence represents the existence of the file
     */
    public static boolean checkFileExistence(String file)
    {
       return new File(file).exists();
    }

    //Definir nome de arquivo para o jogo salvo pelo botão de salvar
    public static String[] getFileNamesInFolder(String path)
    {      
        if (!checkFileExistence(path))
        {
            if (!path.equals("savedGames/"))
            {
                System.out.println("Not exists folder: " + path);
                return new String[0];
            }

            createFolder(path);
        }
        
        File folder = new File(path);
        File[] fileArray = folder.listFiles();
        String[] filenames = new String[fileArray.length];
        
        for (int i= 0; i < fileArray.length; i++)
        {
        	filenames[i] = fileArray[i].getPath();
        }
        
        return filenames;
    }
    
    //Cria diretório de jogos salvos
    public static boolean createFolder(String path) {
        return new File(path).mkdir();
    }
    
    //Pega o nome dos arquivos salvos no diretório
    public static boolean hasGameFileSaved() {
        return getFileNamesInFolder("savedGames/").length > 0;
    }
    
    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        
        if (file.exists() && file.delete())
        {
            System.out.println("File " + filename + " deleted.");
            return true;
        }
        
        return false;
    }
    
}
