
import Model.Person;
import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Lab1Test {

    public void runSQL(){
        /**
         * problem1: Create a view called "firstname_lastname" in lab1.sql from the Person table that only has the firstname and lastname columns.
         * NOTE: This table should NOT have the id and age.
         */
        String sql = FileUtil.parseSQLFile("src/main/lab1.sql");

        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement s = connection.createStatement();
            s.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("problem1: " + e.getMessage() + '\n');
        }
    }

    @Test
    public void problemTest(){
        runSQL();

        List<Person> expectedResult = new ArrayList<>();
        List<Person> actualResult = new ArrayList<>();

        try {

            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM firstname_lastname;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                actualResult.add(new Person(rs.getString("firstname"), rs.getString("lastname")));
            }
            String sql2 = "SELECT firstname, lastname FROM Person;";
            ps = connection.prepareStatement(sql2);
            rs = ps.executeQuery();
            while(rs.next()){
                Person p = new Person();
                p.setFirstname(rs.getString("firstname"));
                p.setLastname(rs.getString("lastname"));
                expectedResult.add(p);
            }
        } catch (SQLException e) {
            System.out.println("problem1: " + e.getMessage() + '\n');
            Assert.fail();
        }
        Assert.assertTrue("The view should have the same amount of records as " +
                        "the original Person table. ",
                expectedResult.size() == actualResult.size());
        System.out.println(expectedResult);
        System.out.println(actualResult);
        for(int i = 0 ; i < expectedResult.size(); i++){
            boolean containsFlag = false;
            for(int j = 0; j < actualResult.size(); j++){
                if(expectedResult.get(i).getFirstname().equals(actualResult.get(j).getFirstname())
                && expectedResult.get(i).getLastname().equals(actualResult.get(j).getLastname())){
                    containsFlag = true;
                }
            }
            Assert.assertTrue("The view should contain all of the firstnames " +
                    "and lastnames of the Person table. Missing: "+expectedResult.get(i),
                    containsFlag);
        }
    }

    @Test
    public void noAgeTest(){
        runSQL();

        List<Person> expectedResult = new ArrayList<>();
        List<Person> actualResult = new ArrayList<>();

        try {

            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT age FROM firstname_lastname;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Assert.fail("The firstname_lastname view should not have the age column.");
        } catch (SQLException e) {

        }
    }
    @Test
    public void noIdTest(){
        runSQL();

        List<Person> expectedResult = new ArrayList<>();
        List<Person> actualResult = new ArrayList<>();

        try {

            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT id FROM firstname_lastname;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Assert.fail("The firstname_lastname view should not have the id column.");
        } catch (SQLException e) {

        }
    }


















    /**
     * The @Before annotation runs before every test so that way we create the tables required prior to running the test
     */
    @Before
    public void beforeEach(){
        try {

            Connection connection = ConnectionUtil.getConnection();

            //Write SQL logic here
            String sql1 = "CREATE TABLE Person (id SERIAL PRIMARY KEY, firstname varchar(100), lastname varchar(100), age int);";
            String sql2 = "INSERT INTO Person (firstname, lastname, age) VALUES ('Steve', 'Garcia', 23);";
            String sql3 = "INSERT INTO Person (firstname, lastname, age) VALUES ('Alexa', 'Smith', 40);";
            String sql4 = "INSERT INTO Person (firstname, lastname, age) VALUES ('Steve', 'Jones', 29);";
            String sql5 = "INSERT INTO Person (firstname, lastname, age) VALUES ('Brandon', 'Smith', 50);";
            String sql6 = "INSERT INTO Person (firstname, lastname, age) VALUES ('Adam', 'Jones', 61);";

            PreparedStatement ps = connection.prepareStatement(sql1 + sql2 + sql3 + sql4 + sql5 + sql6);

            ps.executeUpdate();

        } catch (SQLException e) {
        }
    }

    /**
     * The @After annotation runs after every test so that way we drop the tables to avoid conflicts in future tests
     */
    @After
    public void afterEach(){

        try {

            Connection connection = ConnectionUtil.getConnection();
            String sql = "DROP VIEW firstname_lastname;";
            String sql2 = "DROP TABLE Person;";

            PreparedStatement ps1 = connection.prepareStatement(sql);
            ps1.executeUpdate();
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps2.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
