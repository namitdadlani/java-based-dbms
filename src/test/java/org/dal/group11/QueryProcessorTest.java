package org.dal.group11;

import junit.framework.TestCase;

import java.io.FileNotFoundException;
import java.io.IOException;

public class QueryProcessorTest extends TestCase {

    public void testDetermineQueryType() throws FileNotFoundException {
        String inputString = "SELECT * FROM EMPLOYEE";
        QueryProcessor qp = new QueryProcessor();
        qp.validateSelect(inputString);
    }

    public void testDetermineQueryType2() throws IOException {
        String inputString = "CREATE TABLE Persons ( PersonID int, LastName varchar(255), FirstName varchar(255), Address varchar(255), City varchar(255) )";
        QueryProcessor qp = new QueryProcessor();
        qp.determineQueryType(inputString,"","");
    }

    public void testDetermineQueryType3() throws IOException {
        String inputString = "INSERT INTO mytable (column1, column2, column3) VALUES (value1, value2, value3)";
        QueryProcessor qp = new QueryProcessor();
        qp.determineQueryType(inputString,"","");
    }

    public void testDetermineQueryType4() throws IOException {
        String inputString = "CREATE TABLE * FROM  EMPLOYEE";
        QueryProcessor qp = new QueryProcessor();
        qp.determineQueryType(inputString,"","");
    }

    public void testDetermineQueryType5() throws IOException {
        String inputString = "CREATE * FROM TABLE EMPLOYEE";
        QueryProcessor qp = new QueryProcessor();
        qp.determineQueryType(inputString,"","");
    }


}