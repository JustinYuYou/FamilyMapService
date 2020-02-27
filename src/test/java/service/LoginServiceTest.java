package service;

import dao.Database;
import dao.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import response.LoginResponse;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    private Database db;

    private User testUser;
    private UserDao userDao;


    @BeforeEach
    void setUp() throws Exception{
        testUser = new User("sheila", "Sheila_Parker",
                "parker", "sheila@parker.com",
                "Sheila", "Parker",
                "f");
        db = new Database();
        try(Connection conn = db.openConnection()){
        userDao = new UserDao(conn);
        userDao.insertUser(testUser);
        db.closeConnection(true);}
        catch(Exception e){
            e.printStackTrace();
            db.closeConnection(false);
        }
    }

    @AfterEach
    void tearDown() throws Exception{
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loginPass() throws Exception {
        LoginService loginTest = new LoginService();

        LoginRequest r = new LoginRequest(testUser.getUserName(), testUser.getPassword());
        LoginResponse loginResponse = loginTest.login(r);

        assertNotNull(loginResponse.getAuthToken());
        assertEquals(loginResponse.getUserName(), testUser.getUserName());
        assertEquals(loginResponse.getPersonID(), testUser.getPersonID());
    }

    @Test
    public void loginFail() throws Exception {

        LoginService loginTest = new LoginService();

        LoginRequest r = new LoginRequest("dfgdfger", testUser.getPassword());
        LoginResponse loginResponse = loginTest.login(r);

        assertFalse(loginResponse.isSuccess());
        assertEquals(loginResponse.getMessage(), "User does not exist");

    }

}