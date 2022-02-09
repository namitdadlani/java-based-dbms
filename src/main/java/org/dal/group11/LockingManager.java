package org.dal.group11;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class LockingManager {
    HashMap<String, Integer> transactionList;
    boolean acquired;

    LockingManager() {
        transactionList = new HashMap<>();
        acquired=false;
    }

    synchronized public void acquireLock(String username, String databaseName) throws IOException {
        String databasePath="./userdata/"+username+"/"+databaseName+"/"+"locks.ngsg11";
        File lockFile = new File(databasePath);
        boolean exists = lockFile.exists();
        if(!exists) {
            writeLockFile(databasePath, username, false);
        } else {
            String currentUser = readLockFile(databasePath);
            while(!currentUser.equals("") && !currentUser.equals(username)){
                try{
                    wait();
                    break;
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            currentUser = readLockFile(databasePath);
            if(currentUser.equals("")) {
                writeLockFile(databasePath, username, false);
            }
        }
    }

    synchronized public void releaseLock(String username, String databaseName) throws IOException {
        String databasePath="./userdata/"+username+"/"+databaseName+"/"+"locks.ngsg11";
        acquired=false;
        writeLockFile(databasePath, "", false);
        notifyAll();
    }

    public String readLockFile(String databasePath) throws FileNotFoundException {
        String user = "";
        File lockFile = new File(databasePath);
        Scanner lockReader = new Scanner(lockFile);
        while (lockReader.hasNextLine()) {
            String userName = lockReader.nextLine();
            if(userName!=""){
                user=userName;
            }
        }
        lockReader.close();
        return user;
    }

    public void writeLockFile(String databasePath, String content, boolean append) throws IOException {
        FileWriter lockWriter = new FileWriter(databasePath,append);
        PrintWriter pr = new PrintWriter(lockWriter);
        pr.write(content);
        pr.close();
        lockWriter.close();
    }
}
