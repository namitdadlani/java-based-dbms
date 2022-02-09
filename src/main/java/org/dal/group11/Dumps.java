package org.dal.group11;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Dumps {

    String userName;
    String path;
    Scanner scn;
    String DUMP_LOCATION;
    DateFormat dateFormat;
    Calendar calobj;

    public Dumps(String userName) {
        this.userName = userName;
        this.path = "./userdata/" + userName;
        this.scn = new Scanner(System.in);
        DUMP_LOCATION = "Dumps";
        dateFormat =new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        calobj = Calendar.getInstance();
    }

    public void createDump(){

        System.out.println("Please enter the name of the Dump folder:");
        String dumpFoldername = scn.nextLine();

        dumpFoldername = DUMP_LOCATION + "\\" + dumpFoldername;

        System.out.println(dumpFoldername);

        File dumpFolder = new File(dumpFoldername);
        System.out.println(dumpFolder.mkdir());

        String fileContent = "";
        String dumpHeading = "-- DUMPS --";
        String userNameForDumps = "-- User: " + userName + " --";

        File userDirectory = new File(path);

        String[] listOfFiles = userDirectory.list();
        List<String> listOfDirectories = new LinkedList<>();

        for (String fileName: listOfFiles
             ) {
            if (!fileName.contains(".")){
                listOfDirectories.add(fileName);
            }
        }

        for (String dir: listOfDirectories
             ) {

            File finalDump = new File(dumpFoldername + "/" + dir + ".txt");
            FileWriter dumpWriter = null;
            try {
                dumpWriter = new FileWriter(finalDump);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String currentDB = dir;

            String currentpath = path + "/" + currentDB;

            File currentDbFile = new File(currentpath);

            String[] filesInCurretDirectory = currentDbFile.list();
            List<String> metaDataFiles = new LinkedList<>();

            if(filesInCurretDirectory.length !=0){
                for (String f: filesInCurretDirectory
                     ) {
                    if(f.contains(".metadata")){
                        metaDataFiles.add(f);
                    }
                }
            }

            if (metaDataFiles.contains("TableMetadata.metadata")){
                String metaPath = currentpath + "/TableMetadata.metadata";
                File metaFile = new File(metaPath);

                if(metaFile.length() != 0){
                    try {
                        FileReader dumpReader = new FileReader(metaFile);
                        int fileChar = 0;

                        while ((fileChar = dumpReader.read())!=-1){
                            fileContent += (char)fileChar;
                        }

                        dumpWriter.write(dumpHeading);
                        dumpWriter.write("\n");
                        dumpWriter.write(userNameForDumps);
                        dumpWriter.write("\n");
                        dumpWriter.write("\n");
                        dumpWriter.write(fileContent);
                        dumpWriter.write("\n");

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            for (String f: metaDataFiles
                 ) {
                if(!f.equals("TableMetadata.metadata")){
                    String currentFileName = f;
                    String metaPath =  currentpath + "/" + currentFileName;
                    File metaFile = new File(metaPath);

                    String metaFileContent = "";

                    if(metaFile.length() != 0){
                        try {
                            FileReader dumpReader = new FileReader(metaFile);
                            int fileChar = 0;

                            while ((fileChar = dumpReader.read())!=-1){
                                metaFileContent += (char)fileChar;
                            }

                            dumpWriter.write("\n");
                            dumpWriter.write(metaFileContent);
                            dumpWriter.write("\n");

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                dumpWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
