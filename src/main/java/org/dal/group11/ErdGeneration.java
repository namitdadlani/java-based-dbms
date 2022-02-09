package org.dal.group11;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ErdGeneration {

    public void generateErd(String username) throws IOException {
        String[] dbName  = UserInteraction.databasePath.split("/");
        String databaseName = dbName[3];
        File tableMetaData = new File(UserInteraction.databasePath+"/"+"TableMetadata.metadata");
        Scanner myReader = new Scanner(tableMetaData);
        String data = "";
        ArrayList<String> output = new ArrayList<>();
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
            output.add(data);
        }
        ArrayList<String> keys = new ArrayList<>();
        for(int i=0;i<output.size();i++){
            String[] keyData = output.get(i).split("#");
            for(int j=0;j<keyData.length;j++){
                keys.add(keyData[j]);
            }
        }

        HashMap<String,String> primaryKeys = new HashMap<>();
        HashMap<String,String> foriegnKeys = new HashMap<>();
        for(int i=0;i<keys.size();i++){
           // System.out.println(keys.get(i));
            if(keys.get(i).startsWith("PRIMARY KEY")){
                primaryKeys.put(keys.get(i-1),keys.get(i));
            }
        }
        for(int i=0;i<keys.size();i++){
            //System.out.println(keys.get(i));
            if(keys.get(i).startsWith("FOREIGN KEY")){
                foriegnKeys.put(keys.get(i-1),keys.get(i));
            }
        }
       // System.out.println(foriegnKeys);
       // System.out.println(primaryKeys);
        int counter = 0;
        System.out.println("ERD relationships for database: "+databaseName+"----");
        for (Map.Entry<String,String> entry : foriegnKeys.entrySet())
        {
            for(Map.Entry<String,String> pk: primaryKeys.entrySet())
              System.out.println(pk.getKey()+"----------------->" + entry.getKey());
              counter++;
    }
        LocalDateTime time = LocalDateTime.now();
        FileWriter fileWriter = new FileWriter("./logs/events.lg");
        BufferedWriter br = new BufferedWriter(fileWriter);
        br.write("\n"+time+" : "+counter+" relationships found");
        br.close();
        fileWriter.close();



    }

}
