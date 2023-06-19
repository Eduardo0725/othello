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
 * @author EDUARDO ANDRADE CARVALHO     - RA: 125111371662
 * JHONATAS VIEIRA DA SILVA SANTOS 		- RA: 125111350221
 * THIAGO REIS CARDOSO                	- RA: 125111366586
 * RENATO RIBEIRO MELO FILHO         	- RA: 125111370411
 * PITER MALHEIROS FANTI                - RA: 125111353595
 * VICTÓRIA SOUZA DIAS                 	- RA: 12523157176
 */
public class MyLibrary
{
    private static final String PATH_DEFAULT = "blueprint/fieldBlu.txt";
    
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
           System.out.println("Could not read the file. Error code RW-wIF-1");
       }
    }
   
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
               System.out.println("Ooops, something went wrong! Error code: ML-rFF-1");
           }
           finally
           {
               try
               {
                   read.close();
               }
               catch(IOException ex)
               {
                   System.out.println("Could not close file");
               }
           } 
        }
        else
        {
           System.out.println("Such file doesn't exist.");
           readFromFile(getNewFileName());
        }
    }
    
    
    public static void duplicateFile(String fileName)
    {
       boolean[] fileExistent = new boolean[2];
       String copiedLine;
       
       String[] file = fileName.split("<>"); //INPUT THEN OUTPUT
       
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
               System.out.println("File could not be read. Error code ML-DF-1");
           }
           finally
           {
               try
               {
                   read.close();
               }
               catch(IOException ex)
               {
                   System.out.println("Could not close file! Error code ML-DF-1");
               }
               if(write != null)
               {
                   write.close();
               }
           }
       }
       else
       {
           System.out.println("The output file doesn't exist. Error code ML-DF-3.");
           String newNames = file[0] + getNewFileName();
           duplicateFile(newNames);
       }
    }
    
    
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
            System.out.println("Ooops, there was some kind of an error. Error code: ML-SAL-01.");
        }
    }
    
    
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
                System.out.println("Could not read file. Error code ML-GSA-1.");
            }

            return strArray;
        }

        System.out.println("Such file doesn't exist. Loading a default field instead.");

        try
        {
            createFileDefaultIfNotExists();

            strArray = getStringArray(PATH_DEFAULT);
        }
        catch (IOException e)
        {
            System.out.println("An error occurred.");
        }

        return strArray;
    }
    
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
    
    public static void createFolderDefaultIfNotExists() {
        File folder = new File("blueprint");
        
        if (!folder.exists() && folder.mkdir()) {
            System.out.println("Directory blueprint is created");
        }
    }
    
    public static void closeFile(BufferedReader read) {
        try
        {
            read.close();
        }
        catch(IOException ex)
        {
            System.out.println("Could not close the file... Error code ML-GSA-2.");
        }
    }
   
    
    public static String getNewFileName()
    {
       Scanner newFileNameScan = new Scanner(System.in);
       String newFileName;
       System.out.println("Please, enter the correct file name:");
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

    //Definir nome de arquivo para o jogo salvo
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
    
    public static boolean createFolder(String path) {
        return new File(path).mkdir();
    }
    
    public static boolean hasGameFileSaved() {
        return getFileNamesInFolder("savedGames/").length > 0;
    }
    
}
