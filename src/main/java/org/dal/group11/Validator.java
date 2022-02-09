package org.dal.group11;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    List<String> OPERATIONS = new ArrayList<>();

    public Validator(){
        OPERATIONS.add("SELECT");
        OPERATIONS.add("CREATE TABLE");
        OPERATIONS.add("ALTER TABLE");
        OPERATIONS.add("UPDATE");
        OPERATIONS.add("DELETE");
        OPERATIONS.add("DROP");
    }

    public boolean validateOtherKeywords(String inputString, String command){
        for(String commandType : OPERATIONS){
            if(inputString.contains(commandType) && !commandType.equals(command)){
                return false;
            }
        }
        return true;
    }
}
