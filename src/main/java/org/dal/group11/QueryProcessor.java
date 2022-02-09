package org.dal.group11;

import java.io.*;
import java.time.LocalDateTime;
import java.sql.SQLOutput;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor {

    DBOperations dbOperations;
    Validator validator;

    public void determineQueryType(String inputString, String path,String userName) throws IOException {
        validator = new Validator();
        dbOperations = new DBOperations();
        logQuery(userName, inputString);
        if (inputString.contains("SELECT")) {
            validateSelect(inputString);
        }
        else if(inputString.contains("CREATE DATABASE")){
            validateCreateDatabase(inputString,userName);
        }
        else if (inputString.contains("CREATE TABLE")) {
            validateCreateTable(inputString,path,userName);
        } else if (inputString.contains("INSERT INTO")) {
            validateInsert(inputString,userName);
        } else if (inputString.contains("ALTER TABLE")) {
            validateAlter(inputString);
        } else if (inputString.contains("UPDATE")) {
            validateUpdate(inputString);
        } else if (inputString.contains("DELETE FROM")) {
            validateDelete(inputString);
        } else if (inputString.contains("DROP TABLE")) {
            validateDrop(inputString);
        }else if(inputString.contains("CREATE DUMPS")){
            createDumps(userName, path);
        }
        else if(inputString.contains("GENERATE ERD")){
            createErd(userName);
        }
        else {
            System.out.println("QueryProcessor.determineQueryType: Unknown Command.");

        }
    }



    public void createErd(String userName) throws IOException {
        ErdGeneration erdGeneration = new ErdGeneration();
        erdGeneration.generateErd(userName);
    }

    private void createDumps(String userName, String path) {
        Dumps dumps = new Dumps(userName);
        dumps.createDump();
    }

    public void validateSelect(String inputString) throws FileNotFoundException {
      System.out.println("QueryProcessor.validateSelect");
      String[] keywords = inputString.split(" ");
      if(keywords[0].equals("SELECT")){
          SelectQuery selectQuery = new SelectQuery();
          selectQuery.setSelectString(inputString);
          if(!inputString.contains("WHERE")){
              final Pattern patternSelect = Pattern.compile("^SELECT \\* FROM .*", Pattern.CASE_INSENSITIVE);
              final Matcher matcherSelect = patternSelect.matcher(inputString);
              System.out.println("patternSelect without Where clause: "+matcherSelect.matches());
              selectQuery.setTableName(inputString.substring(inputString.indexOf("FROM")+5));
          } else {
              final Pattern patternSelectWhere = Pattern.compile("^SELECT \\* FROM [a-zA-Z]+ WHERE .*", Pattern.CASE_INSENSITIVE);
              final Matcher matcherSelectWhere = patternSelectWhere.matcher(inputString);
              System.out.println("patternSelect with Where clause: "+matcherSelectWhere.matches());
              selectQuery.setConditions(inputString.substring(inputString.indexOf("WHERE")+6));
              selectQuery.setTableName(inputString.substring(inputString.indexOf("FROM")+5, inputString.indexOf("WHERE")-1));
          }
          selectQuery.setAttributes(inputString.substring(inputString.indexOf("SELECT")+7, inputString.indexOf("FROM")-1));
          dbOperations.selectTable(selectQuery);
          //System.out.println(selectQuery);


      } else {
          System.out.println("Invalid SELECT query.");

      }
    }

    public String validateInsert(String inputString,String userName) throws IOException {
        if(inputString.indexOf("INSERT INTO")==0){
            String tableName = inputString.split(" ")[2];
            String columnName=inputString.split(" ")[3];
            String value = inputString.substring(inputString.indexOf("VALUES")+8, inputString.length()-1);
            System.out.println("tableName"+tableName);
            System.out.println("values"+value);
            System.out.println(columnName);
            dbOperations.insertTable(tableName, value,columnName,userName);
            return "INSERT INTO";
        } else {
            System.out.println("QueryProcessor.validateInsert: Kindly enter the command in correct format.");
            return "QueryProcessor.validateInsert: Kindly enter the command in correct format.";
        }
    }

    public void validateCreateTable(String inputString, String path,String userName) {
        try {
            if(path.equals("")){
                System.out.println("Select a database to create a table. (HINT: USE <databasename>)");
                LocalDateTime time = LocalDateTime.now();
                FileWriter fileWriter = new FileWriter("./logs/query.lg",true);
                BufferedWriter br = new BufferedWriter(fileWriter);
                br.write("\n"+time+" : "+"Select a database to create a table. (HINT: USE <databasename>)");
                br.close();
                fileWriter.close();
            }
            else {
                if (inputString.indexOf("CREATE TABLE") == 0) {
                    String tableName = inputString.split(" ")[2];
                    String columns = inputString.substring(inputString.indexOf("(") + 1, inputString.length() - 1);
                    //System.out.println("QueryProcessor.validateCreateTable.tableName: " + tableName);
                    //System.out.println("QueryProcessor.validateCreateTable.columns: " + columns);
                    dbOperations.createTable(tableName, columns, path,userName);
                } else {
                    System.out.println("QueryProcessor.validateCreateTable: Kindly enter the command in correct format.");
                    LocalDateTime time = LocalDateTime.now();
                    FileWriter fileWriter = new FileWriter("./logs/query.lg",true);
                    BufferedWriter br = new BufferedWriter(fileWriter);
                    br.write("\n"+time+" : "+"QueryProcessor.validateCreateTable: Kindly enter the command in correct format.");
                    br.close();
                    fileWriter.close();
                }
            }
        }catch(Exception e){
            System.out.println("QueryProcessor.validateCreateTable: Kindly enter the command in correct format.");
        }
    }

    public void validateAlter(String inputString) {
      System.out.println("validateAlter");
    }

    public void validateUpdate(String inputString) {
      System.out.println("validateUpdate");
    }

    public void validateDelete(String inputString) {
        if(inputString.indexOf("DELETE FROM")==0){
            DeleteQuery deleteQuery = new DeleteQuery();
            deleteQuery.setDeleteString(inputString);
            if(!inputString.contains("WHERE")){
                deleteQuery.setTableName(inputString.substring(inputString.indexOf("FROM")+5));
            } else {
                deleteQuery.setConditions(inputString.substring(inputString.indexOf("WHERE")+6));
                deleteQuery.setTableName(inputString.substring(inputString.indexOf("FROM")+5, inputString.indexOf("WHERE")-1));
            }
            System.out.println(deleteQuery);
            dbOperations.deleteData(deleteQuery);
        } else {
            System.out.println("Enter a valid DELETE query.");
        }
    }

    public void validateDrop(String inputString) {
        if(inputString.indexOf("DROP TABLE")==0){
            String tableToDelete = inputString.substring(inputString.indexOf("DROP TABLE")+11);
            System.out.println("Dropping Table: "+tableToDelete);
            dbOperations.dropTable(tableToDelete);
        } else {
            System.out.println("Enter a valid DELETE query.");
        }
    }


    public void validateCreateDatabase(String inputString,String userName){
       try {
           System.out.println(inputString);
           String databaseName = inputString.substring(16);
           String path = UserInteraction.databasePath+"/";
           System.out.println(databaseName);
           dbOperations.createDatabase(databaseName,path,userName);
       }
       catch (Exception e){
           System.out.println("Try creating database with other name");
       }


    }

    public String validateUseDatabase(String inputString) throws IOException {
        //System.out.println(inputSttring);
        String dbName=inputString.substring(4);
        System.out.println(dbName);
        String path=UserInteraction.databasePath+"/"+dbName;
        System.out.println(path);
        File f1 = new File(path);
        if(f1.exists()) {
            UserInteraction.databasePath = path;
            LocalDateTime time = LocalDateTime.now();
            FileWriter fileWriter = new FileWriter("./logs/events.lg",true);
            BufferedWriter br = new BufferedWriter(fileWriter);
            br.write("\n"+time+" : "+dbName+ " - is selected");
            br.close();
            fileWriter.close();
            return path;
        }
        else{
            System.out.println("Database does not exists!");
            LocalDateTime time = LocalDateTime.now();
            File queryLog = new File("./logs/query.lg");
            FileWriter logWriter = new FileWriter(queryLog,true);
            BufferedWriter br = new BufferedWriter(logWriter);
            br.write("\n"+time+" : "+"Database does not exists");
            br.close();
            logWriter.close();
            return "";
        }

        }

        public void logQuery(String username, String log) throws IOException {
            LocalDateTime time = LocalDateTime.now();
            File queryLog = new File("./logs/query.lg");
            FileWriter logWriter = new FileWriter(queryLog,true);
            BufferedWriter br = new BufferedWriter(logWriter);
            br.write("\n"+time+" : "+"Query by "+username+"-"+log);
            br.close();
            logWriter.close();
        }
    }
