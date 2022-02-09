package org.dal.group11;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class DBOperations {
           //user            db              table           column        data
    HashMap<String, HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>>> user = new HashMap<>();
    HashMap<String, HashMap<String, ArrayList<String>>> table = new HashMap<>();
    public void createDatabase(String databaseName, String path,String userName) throws IOException {
        File f1= new File(path+databaseName);
        System.out.println(path+databaseName);
        boolean bool = f1.mkdir();
        if(bool){
            System.out.println("Database: " +databaseName+ " is created successfully");
            FileWriter metadata = new FileWriter(path+"UserMetadata.metadata",true);
            PrintWriter pr = new PrintWriter(metadata);
            HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> db = new HashMap<>();
            db.put(databaseName,null);
            user.put(userName,db);
            pr.write(String.valueOf("\n"+user));
            pr.close();
            metadata.close();



        }else{
            System.out.println("Database already exists");
        }


    }

    public void createTable(String tableName, String columns, String path,String userName) throws IOException {
       if(path.equals("")){
           System.out.println("Select a Database before creating a Table! ");
       }
       else{
           System.out.println(path);
           String[] dbName  = path.split("/");
           String databaseName = dbName[3];
           HashMap<String,ArrayList<String>> columnData = new HashMap<>();
           String[] columnValues = columns.split(",");

           ArrayList<String> output = new ArrayList<>();
           for(int i=0;i<columnValues.length-1;i++) {
               //System.out.println(columnValues[i]);
               //output.add(columnValues[i]);
               String[] finalColumns=columnValues[i].split(" ");
               for(int j=0;j<finalColumns.length;j=j+2){
                   output.add(finalColumns[j]);
               }
           }
           String PK ="#"+columnValues[columnValues.length-1];
           System.out.println(PK);
           String datatype = "";
           for(int i=0;i<columnValues.length-1;i++){
               String temp = "("+columnValues[i]+")";
               datatype=datatype+temp;
           }



           for(int i=0;i<output.size();i=i+1) {
               System.out.println(output.get(i));
               columnData.put(output.get(i),null);
           }
           table.put(tableName,columnData);
           HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> db = new HashMap<>();
           db.put(databaseName,table);
           user.put(userName,db);
           System.out.println(table);
           path=path+"/"+tableName+".ngsg11";
           File file= new File(path);
           //System.out.println("get"+file.getPath());
           boolean bool = file.createNewFile();

           System.out.println(bool);
           if(bool){
               System.out.println("Table: " +tableName+ " is created successfully");
               FileWriter tableWriter = new FileWriter(path);
               FileWriter metadataWriter = new FileWriter("./userdata/"+userName+"/"+databaseName+"/TableMetadata.metadata",true);
               FileWriter logWriter = new FileWriter("./logs/events.lg",true);
               BufferedWriter br = new BufferedWriter(logWriter);
               PrintWriter pr = new PrintWriter(metadataWriter);
               pr.write("\n"+String.valueOf(user)+"*"+datatype+"*"+PK);
               tableWriter.write(String.valueOf(table)+"*"+datatype+"*"+PK);
               LocalDateTime time = LocalDateTime.now();
               br.write("\n"+time+" : "+tableName+" is created in datasbase: "+databaseName);
               tableWriter.close();
               pr.close();
               metadataWriter.close();
               br.close();
               logWriter.close();
           }else{
               System.out.println("Table already exists");
               LocalDateTime time = LocalDateTime.now();
               FileWriter fileWriter = new FileWriter("./logs/query.lg",true);
               BufferedWriter br = new BufferedWriter(fileWriter);
               br.write("\n"+time+" : "+"Table already exists");
               br.close();
               fileWriter.close();
           }
       }

    }

    public void insertTable(String tableName, String value,String columns,String userName) throws IOException {

            System.out.println(tableName);
            System.out.println(UserInteraction.databasePath);

           String[] dbName  = UserInteraction.databasePath.split("/");
           String databaseName = dbName[3];
            //System.out.println("value"+value);
            //System.out.println(columns);
            String[] query = columns.split("\\(|,|\\)");
            ArrayList<String> columnData = new ArrayList<>();
            for (String word : query) {
                //System.out.println(word);
                columnData.add(word);
            }
             columnData.removeAll(Arrays.asList("", null));
            HashMap<String,ArrayList<String>> column = new HashMap<>();
            for (int i = 0; i < columnData.size(); i++) {
                  // System.out.println("column: "+columnData.get(i));
                    String[] rowSplit=value.split(",");

                    ArrayList<String> temp = new ArrayList<>();
                   //System.out.println(rowSplit[i]);
                    temp.add(rowSplit[i]);
                    column.put(columnData.get(i),temp);
                   // System.out.println(column);



                   table.put(tableName,column);
                   HashMap<String,HashMap<String, HashMap<String, ArrayList<String>>>> db = new HashMap<>();
                   db.put(databaseName,table);
                   user.put(userName,db);
            }
        FileWriter metadataWriter = new FileWriter("./userdata/"+userName+"/"+databaseName+"/"+tableName+"Metadata.metadata",true);
        PrintWriter pr = new PrintWriter(metadataWriter);
        pr.write("\n"+String.valueOf(user));
        pr.close();
        metadataWriter.close();



        File tableFile = new File(UserInteraction.databasePath + "/" + tableName + ".ngsg11");
        Scanner tableReader = new Scanner(tableFile);
        String data = "";
        while (tableReader.hasNextLine()) {
            data = tableReader.nextLine();
            //System.out.println(data);
        }
        if(data.substring(16,20).equals("null")) {
            FileWriter tableWriter = new FileWriter(UserInteraction.databasePath + "/" + tableName + ".ngsg11",true);
            BufferedWriter br = new BufferedWriter(tableWriter);
            br.write(String.valueOf("\n"+table));
            br.close();
            tableWriter.close();

        }
        else{
            FileWriter fr = new FileWriter(UserInteraction.databasePath + "/" + tableName + ".ngsg11", true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(":"+String.valueOf(table));
            br.close();
            fr.close();
        }

            System.out.println(table);

        }


    public void selectTable(SelectQuery selectQuery) throws FileNotFoundException {
        try {
            String tableName = selectQuery.tableName;
            System.out.println(tableName);
            File table = new File(UserInteraction.databasePath + "/" + tableName + ".ngsg11");
            Scanner myReader = new Scanner(table);
            String data = "";
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                // System.out.println(data);
            }
            String[] tableData = data.split(":");
            ArrayList<String> output = new ArrayList<>();
            for (String word : tableData) {
                System.out.println(word);
                output.add(word);
            }
            ArrayList<String> finalOutput = new ArrayList<>();
            for (int i = 0; i < output.size(); i++) {
                String[] columnData = output.get(i).split("=|\\{|\\],|\\[|\\]|\\}");
                for (String word : columnData) {
                    //System.out.println(word);
                    finalOutput.add(word);
                }
            }
            finalOutput.removeAll(Arrays.asList("", null));
            ArrayList<String> columns = new ArrayList<>();
            String[] columnData = output.get(0).split("=|\\{|\\],|\\[|\\]|\\}");
            for (int i = 3; i < columnData.length; i = i + 3) {
                columns.add(columnData[i]);
            }
            int counter=0;
            System.out.println("---------------------" + "Table Name: " + finalOutput.get(0) + "---------------------");
            for (int i = 0; i < columns.size(); i++) {
                System.out.print("  | " + columns.get(i) + "     |  ");
            }
            System.out.println("\n");
            for (int i = 2; i < finalOutput.size(); i = i + 5) {

                System.out.print("  " + finalOutput.get(i));
                System.out.print("\t");
                System.out.print("       " + finalOutput.get(i + 2));
                System.out.println("\n");
                counter++;

            }
            LocalDateTime time = LocalDateTime.now();
            FileWriter fileWriter = new FileWriter("./logs/events.lg",true);
            BufferedWriter br = new BufferedWriter(fileWriter);
            br.write("\n"+time+" : "+counter+" rows returned from table "+tableName);
            br.close();
            fileWriter.close();
        }catch(Exception e){
            System.out.println("Select Database Before Fetching Data");
        }
    }

    public void deleteData (DeleteQuery deleteQuery){
        //deleteQuery.tableName
        //deleteQuery.conditions
    }

    public void dropTable(String tableToDelete) {

    }
}
