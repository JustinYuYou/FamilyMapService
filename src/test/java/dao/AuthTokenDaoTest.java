package dao;

import databaseAccessException.DataAccessException;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDaoTest {
    private Database db;
    private AuthToken compareAuthToken;
    private AuthToken compareAuthToken2;

    @BeforeEach
    void setUp() throws Exception{
        db = new Database();
        compareAuthToken = new AuthToken("11111", "JustinYu117");
        compareAuthToken2 = new AuthToken("11112", "jj2324");
    }

    @AfterEach
    void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void findPass() throws Exception{
        AuthToken testAuthToken = null;
        try {
            Connection conn = db.openConnection();

            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            authTokenDao.insertAuthToken(compareAuthToken);

            testAuthToken = authTokenDao.findAuthToken(compareAuthToken.getAuthToken());
            db.closeConnection(true);

        } catch (DataAccessException e){
            db.closeConnection(false);
        }

        assertNotNull(testAuthToken);

        assertEquals(testAuthToken.getAuthToken(), compareAuthToken.getAuthToken());
        assertEquals(testAuthToken.getAssociatedUsername(), compareAuthToken.getAssociatedUsername());
    }

    @Test
    public void findFail() throws Exception{
        AuthToken testAuthToken = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);

            authTokenDao.insertAuthToken(compareAuthToken);
            testAuthToken = authTokenDao.findAuthToken("1111");

            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(testAuthToken);
    }

    @Test
    public void insertAuthToken() throws Exception {

        AuthToken testAuthToken = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);

            authTokenDao.insertAuthToken(compareAuthToken);

            testAuthToken = authTokenDao.findAuthToken(compareAuthToken.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(testAuthToken);

        assertEquals(compareAuthToken, testAuthToken);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);

            authTokenDao.insertAuthToken(compareAuthToken);

            authTokenDao.insertAuthToken(compareAuthToken);
            db.closeConnection(true);
        } catch (DataAccessException e) {

            db.closeConnection(false);
            didItWork = false;
        }

        assertFalse(didItWork);

        AuthToken testAuthToken = compareAuthToken;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);

            testAuthToken = authTokenDao.findAuthToken(compareAuthToken.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that testAuthToken is indeed null
        assertNull(testAuthToken);

        //Test the user has the same username
        boolean work = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            authTokenDao.insertAuthToken(compareAuthToken);

            compareAuthToken2.setAuthToken(compareAuthToken.getAuthToken());
            authTokenDao.insertAuthToken(compareAuthToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            work = false;
        }

        assertFalse(work);
    }

    @Test
    public void deletePass() throws Exception {
        Connection conn = null;

        AuthToken testAuthToken = null;
        try {
            conn = db.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            authTokenDao.insertAuthToken(compareAuthToken);
            authTokenDao.insertAuthToken(compareAuthToken2);

            authTokenDao.deleteAllAuthTokens();

            testAuthToken = authTokenDao.findAuthToken(compareAuthToken2.getAuthToken());
            db.closeConnection(true);

        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(testAuthToken);
    }
}