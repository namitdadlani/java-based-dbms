package org.dal.group11;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class App
{


    public static void main( String[] args ) throws IOException, FileNotFoundException{

        HashMap<String, HashMap<String, List<HashMap<String, String>>>> dbMap = new LinkedHashMap<>();

        UserManagement userManagement = new UserManagement();
        userManagement.homeView();


    }
}
