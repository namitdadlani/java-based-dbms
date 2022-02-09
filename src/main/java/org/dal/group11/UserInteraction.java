package org.dal.group11;

import java.io.IOException;
import java.util.Scanner;

public class UserInteraction {

    public static String databasePath="";
    QueryProcessor queryProcessor = new QueryProcessor();
     public void userInteractions(String userName) throws IOException {
         databasePath="./userdata/"+userName;
         while (true) {
             System.out.println("Enter exit to quit! ");
             System.out.println("Enter query:");
             Scanner sc = new Scanner(System.in);
             String query = sc.nextLine();
             if (query.equals("exit")) {
                 System.exit(1);
             } else if (query.startsWith("USE")) {
                 try {
                     queryProcessor.logQuery(userName, query);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 databasePath = queryProcessor.validateUseDatabase(query);
                 //System.out.println("path"+databasePath);

             } else {

                 try {
                     queryProcessor.determineQueryType(query, databasePath,userName);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }

         }
     }
}
