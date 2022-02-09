package org.dal.group11;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class UserManagement {

    String userName;
    String password;
    String confirmedPassword;
    String securityAnswerOne;
    String securityAnswerTwo;
    String securityAnswerThree;
    String userManagementDirectoryPath;
    String userManagementCredentialPath;
    String logPath;
    String general_Logpath;

    File general_LogpathFile;
    FileWriter general_LogFileWriter;

    Scanner scn;

    DateFormat dateFormat =new SimpleDateFormat("dd/MM/yy HH:mm:ss");
    Calendar calobj = Calendar.getInstance();

    public UserManagement() {
        this.userName = "";
        this.password = "";
        this.confirmedPassword = "";
        this.securityAnswerOne = "";
        this.securityAnswerTwo = "";
        this.securityAnswerThree = "";
        this.userManagementDirectoryPath = "userdata";
        this.userManagementCredentialPath = userManagementDirectoryPath + "\\" + "usr.crd";
        this.logPath = "logs";
        this.general_Logpath = logPath + "\\general.lg";
        this.general_LogpathFile = new File(general_Logpath);
        try {
            this.general_LogFileWriter = new FileWriter(general_LogpathFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.scn = new Scanner(System.in);
    }

    public void homeView() throws IOException {

        try {
            this.general_LogFileWriter = new FileWriter(general_LogpathFile, true);
            general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Program started.");
            general_LogFileWriter.write( "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int option = 0;

        System.out.println("**** Database Management System ****");

        System.out.println("Please enter the correct option:");
        System.out.println("1. Login");
        System.out.println("2. Create User");

        System.out.println("Enter the choice:");
        option = scn.nextInt();

        switch (option){
            case 1:
                login();
                break;
            case 2:
                signup();
                break;
            default:
                System.out.println("You have entered the wrong input!");
        }

        try {
            this.general_LogFileWriter = new FileWriter(general_LogpathFile, true);
            general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Program exited.");
            general_LogFileWriter.write( "\n");
            general_LogFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean signup(){

        try {
            this.general_LogFileWriter = new FileWriter(general_LogpathFile, true);
            general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Signup attempt.");
            general_LogFileWriter.write( "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        reinitialization();
        System.out.println("Signup Wizard:");
        System.out.println("");
        System.out.println("Please enter the following information:");
        System.out.println("");

        System.out.println("Please enter a unique username:");
        scn.nextLine();
        userName = scn.nextLine();
        while (!userNameVerification(userName)){
            System.out.println("Username entered already exists, Please enter again:");
            userName = scn.nextLine();
        }
        System.out.println("Enter the password:");
        password = scn.nextLine();
        System.out.println("Confirm the password by re-entering it:");
        confirmedPassword = scn.nextLine();
        while (!this.password.equals(confirmedPassword) ){
            System.out.println("Password and confirmed password is not matching, Please enter again:");
            System.out.println("Enter the password:");
            password = scn.nextLine();
            System.out.println("Confirm the password by re-entering it:");
            confirmedPassword = scn.nextLine();
        }

        System.out.println("Answer the following questions for setting up multi-factored authentication:");
        System.out.println("");
        System.out.println("What’s your favorite movie?");
        securityAnswerOne = scn.next();
        System.out.println("");
        System.out.println("What was your first car?");
        securityAnswerTwo = scn.next();
        System.out.println("");
        System.out.println("What city were you born in?");
        securityAnswerThree = scn.next();

        storeUserCredential();

        try {
            general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": User with username: " + this.userName +" created successfully.");
            general_LogFileWriter.write( "\n");
            general_LogFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Successfully created the user!");

        return true;
    }

    public boolean login() throws IOException {

        try {
            this.general_LogFileWriter = new FileWriter(general_LogpathFile, true);
            general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Login attempt.");
            general_LogFileWriter.write( "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean resultFlag = true;

        reinitialization();
        System.out.println("Login Wizard:");
        System.out.println("");
        System.out.println("Please enter the following information:");
        System.out.println("");

        System.out.println("Please enter the username:");
        scn.nextLine();
        userName = scn.nextLine();
        System.out.println("Enter the password:");
        password = scn.nextLine();
        resultFlag = verifyCredential();
        if (!resultFlag){
            System.out.println("Credentials provided are invalid.");
            try {
                general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Failed login attempt, wrong credentials were provided!");
                general_LogFileWriter.write( "\n");
                general_LogFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultFlag = false;
            return resultFlag;
        }
        System.out.println("What’s your favorite movie?");
        securityAnswerOne = scn.next();
        System.out.println("");
        System.out.println("What was your first car?");
        securityAnswerTwo = scn.next();
        System.out.println("");
        System.out.println("What city were you born in?");
        securityAnswerThree = scn.next();
        resultFlag = mfa();
        if (!resultFlag){
            System.out.println("Answer provided for security questions are invalid.");
            try {
                general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Failed login attempt, Right credentials were provided but failed at MFA!");
                general_LogFileWriter.write( "\n");
                general_LogFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultFlag = false;
            return resultFlag;
        }

        try {
            general_LogFileWriter.write( dateFormat.format(calobj.getTime()) + ": Successful login attempt for User with username: " + this.userName);
            general_LogFileWriter.write( "\n");
            general_LogFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Successfully LoggedIn!");
        // TODO: REDIRECT TO QUERY_PROCESSOR
         UserInteraction userInteraction = new UserInteraction();
         userInteraction.userInteractions(userName);

        return resultFlag;
    }

    public String toHashedString(String stringToBeHashed){

        String hashedString = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash =messageDigest.digest(stringToBeHashed.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashByteStringBuilder = new StringBuilder();
            for (byte b : hash) {
                hashByteStringBuilder.append(String.format("%02x", b));
            }



            hashedString = hashByteStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }

        return hashedString;
    }

    private boolean storeUserCredential(){

        boolean resultFlag = false;
        this.userManagementDirectoryPath = "userdata";
        String userDirectoryPath = userManagementDirectoryPath + "\\" + this.userName;
        File userManagementDirectory = new File(userManagementDirectoryPath);
        File userDirectory = new File(userDirectoryPath);
        FileWriter fileWriter = null;
        String userData = this.userName + ":" + toHashedString(this.password) + ":" + this.securityAnswerOne + ":" + this.securityAnswerTwo + ":" + this.securityAnswerThree;
        if(!userManagementDirectory.exists()){
            userManagementDirectory.mkdir();
        }else{
            userDirectory.mkdir();
        }

        try {
            fileWriter = new FileWriter(userManagementCredentialPath, true);
            File userCredFile = new File(userManagementCredentialPath);
            if(userCredFile.length() != 0){
                fileWriter.write("\n");
            }
            fileWriter.write(userData);
            resultFlag = true;
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resultFlag;
    }

    private void reinitialization(){
        this.userName = "";
        this.password = "";
        this.confirmedPassword = "";
        this.securityAnswerOne = "";
        this.securityAnswerTwo = "";
        this.securityAnswerThree = "";
    }

    public boolean userNameVerification(String userName){

        boolean resultFlag = true;
        String eachLine = "";

        if (userName.equals("")){
            return resultFlag;
        }

        if (userName.charAt(0) == ' '){
            return resultFlag;
        }

        try {
            FileReader fileReader = new FileReader(userManagementCredentialPath);
            int eachCharacter = ' ';
            while ((eachCharacter = fileReader.read()) != -1){
                if(eachCharacter != 13){
                    eachLine = eachLine + (char)eachCharacter;
                }else{
                    String[] userAttributes = eachLine.split(":");
                    if(userAttributes[0].equals(userName)){
                        resultFlag = false;
                        return resultFlag;
                    }
                    eachLine = "";
                }
            }

            String[] userAttributes = eachLine.split(":");
            if(userAttributes[0].equals(userName)){
                resultFlag = false;
                return resultFlag;
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return resultFlag;
    }

    private boolean verifyCredential() {
        boolean resultFlag = false;
        String eachLine = "";

        if (userName.equals("") || password.equals("")){
            return resultFlag;
        }

        if (userName.charAt(0) == ' ' || password.charAt(0) == ' '){
            return resultFlag;
        }

        try {
            FileReader fileReader = new FileReader(userManagementCredentialPath);
            int eachCharacter = 0;
            while ((eachCharacter = fileReader.read()) != -1){
                if(eachCharacter != 10){
                    eachLine = eachLine + (char)eachCharacter;
                }else{
                    String[] userAttributes = eachLine.split(":");
                    if(userAttributes[0].equals(userName)){
                        if (toHashedString(this.password).equals(userAttributes[1])){
                            resultFlag = true;
                            return resultFlag;
                        }
                    }
                    eachLine = "";
                }
            }

            String[] userAttributes = eachLine.split(":");
            if(userAttributes[0].equals(userName)){
                if (toHashedString(this.password).equals(userAttributes[1])){
                    resultFlag = true;
                    return resultFlag;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return resultFlag;
    }

    private boolean mfa() {
        boolean resultFlag = false;

        String eachLine = "";
        try {
            FileReader fileReader = new FileReader(userManagementCredentialPath);
            int eachCharacter = 0;
            while ((eachCharacter = fileReader.read()) != -1){
                if(eachCharacter != 10){
                    if (eachCharacter != 13){
                        eachLine = eachLine + (char)eachCharacter;
                    }
                }else{
                    String[] userAttributes = eachLine.split(":");
                    if(userAttributes[0].equals(userName)){
                        if (userAttributes[2].equals(this.securityAnswerOne)){
                            if (userAttributes[3].equals(this.securityAnswerTwo)){
                                if(userAttributes[4].equals(this.securityAnswerThree)){
                                    resultFlag = true;
                                    return resultFlag;
                                }
                            }
                        }
                    }
                    eachLine = "";
                }
            }

            String[] userAttributes = eachLine.split(":");
            if(userAttributes[0].equals(userName)){
                if (userAttributes[2].equals(this.securityAnswerOne)){
                    if (userAttributes[3].equals(this.securityAnswerTwo)){
                        if(userAttributes[4].equals(this.securityAnswerThree)){
                            resultFlag = true;
                            return resultFlag;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resultFlag;
    }

}
