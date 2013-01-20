package cz.nkp.differ.user;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import cz.nkp.differ.dao.UserDAO;
import cz.nkp.differ.dao.UserDAOImpl;
import cz.nkp.differ.exceptions.UserDifferException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.log4j.Logger;

import cz.nkp.differ.model.User;
import cz.nkp.differ.util.GeneralMacros;
import org.apache.commons.codec.binary.Base64;

/**
 * Class for application wide user authentication.
 * @author Joshua Mabrey
 * May 4, 2012
 */
public class UserManager {

    private static Logger LOGGER = Logger.getLogger(UserManager.class);
    private static UserManager _instance = null;
    private static final String PASSWORD_HASH_ALGORITHM_NAME = "SHA-1";
    private static String currentUser = null;
    protected UserDAO userDAO;
    protected MessageDigest passwordHashDigest;

    public UserManager() {
	init();
    }

    public UserManager(UserDAO userDAO) {
	init();
	this.userDAO = userDAO;
    }

    protected final void init() {
	try {
	    this.passwordHashDigest = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM_NAME);
	} catch (NoSuchAlgorithmException e) {
	    LOGGER.error("Unable to create MessageDigest. Algorithm: " + PASSWORD_HASH_ALGORITHM_NAME);
	}
    }

    public UserDAO getUserDAO() {
	return userDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
	this.userDAO = userDAO;
    }

    public static UserManager getInstance() {
	if (_instance == null) {
	    MysqlDataSource ds = new MysqlDataSource();
	    ds.setUser("differ");
	    ds.setPassword("differ");
	    ds.setServerName("localhost");
	    ds.setDatabaseName("differ");
	    _instance = new UserManager(new UserDAOImpl(ds));
	}
	return _instance;
    }

    /**
     * Checks the supplied username and password against the user database.
     *
     * TODO:Implement
     * @param username
     * @param userSuppliedPassword
     * @return
     */
    public User attemptLogin(String username, String userSuppliedPassword) throws UserDifferException {
	User user = userDAO.getUserByUserName(username);
	if (user == null) {
	    throw new UserDifferException(UserDifferException.ErrorCode.BAD_PASSWORD_OR_USERNAME, "User does not exists.");
	}
	String userSuppliedPasswordHash = getHashedPassword(userSuppliedPassword.toCharArray(), decode(user.getPasswordSalt()).getBytes());
	if (user.getPasswordHash() == null) {
	    throw new NullPointerException("user.getPasswordHash() is null");
	}
	if (decode(user.getPasswordHash()).equals(userSuppliedPasswordHash)) {
	    currentUser = username;
	    LOGGER.trace("Logged in user: " + currentUser);
	    return user;
	}
	throw new UserDifferException(UserDifferException.ErrorCode.BAD_PASSWORD_OR_USERNAME, "Bad password.");
    }

    // FIXME:TODO
    public synchronized User attemptAnonymousLogin() {
	return null;
    }

    public synchronized String getLoggedInUser() {
	if (currentUser == null) {
	    LOGGER.warn("No user logged in!");
	}
	return currentUser;
    }

    public synchronized User registerUser(User user, String passwordPlaintext) throws UserDifferException {
	System.err.println("Registering new user, username: "+ user.getUserName() + " password: " + passwordPlaintext);
	if (GeneralMacros.containsNull(user.getUserName(), passwordPlaintext)) {
	    LOGGER.debug("Null username or password!");
	    throw new NullPointerException("Null username or password!");
	}
	String salt = getPasswordSalt();
	String hashedPassword = getHashedPassword(passwordPlaintext.toCharArray(), salt.getBytes());
	if (hashedPassword == null) {
	    throw new NullPointerException("hashedPassword is null!!!");
	}
	user.setPasswordHash(encode(hashedPassword));
	user.setPasswordSalt(encode(salt));
	userDAO.addUser(user);
	return user;
    }

    protected static String encode(String str) {
	return new String(Base64.encodeBase64(str.getBytes()));
    }

    protected static String decode(String str) {
	return new String(Base64.decodeBase64(str.getBytes()));
    }

    private String getPasswordSalt() {
	try {
	    SecureRandom saltGen = SecureRandom.getInstance("SHA1PRNG");
	    byte[] salt = new byte[64];
	    saltGen.nextBytes(salt);
	    return StringUtils.newStringUtf8(salt);
	} catch (NoSuchAlgorithmException e) {
	    LOGGER.error("Unable to find SHA1PRNG provider to generate salts.");
	    e.printStackTrace();
	    //TODO:Implement fallback
	}
	return null;
    }

    /**
     * Returns a byte array representing the hash of the given salted password.
     * @param saltedPassword
     * @return
     */
    private String getHashedPassword(char[] plaintextPassword, byte[] salt) {
	if (GeneralMacros.containsNull(passwordHashDigest, salt, plaintextPassword)) {
	    throw new NullPointerException("Failed to hash password becuase of null arguments.");
	}

	try {
	    KeySpec spec = new PBEKeySpec(plaintextPassword, salt, 2048, 160);
	    SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    byte[] bytes = f.generateSecret(spec).getEncoded();
	    return StringUtils.newStringUtf8(bytes);
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException("Unable to perform hash because algorithm is missing.", e);
	} catch (InvalidKeySpecException e) {
	    throw new RuntimeException("Unable to perform hash.", e);
	}
    }
}
