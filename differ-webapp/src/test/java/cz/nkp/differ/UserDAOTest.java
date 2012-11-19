package cz.nkp.differ;

import cz.nkp.differ.model.User;
import cz.nkp.differ.dao.UserDAO;
import cz.nkp.differ.exceptions.UserDifferException;
import java.sql.SQLException;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class UserDAOTest extends Helper {

    public UserDAO userDAO;

    public UserDAOTest() throws SQLException {
	this.userDAO = Helper.getUserDAO();
    }

    @Test
    public void addUserTest() throws UserDifferException {
	User user = new User();
	user.setAdmin(true);
	user.setUserName("username");
	user.setPasswordHash("hash");
	user.setPasswordSalt("salt");
	user.setMail("user@example.com");
	userDAO.addUser(user);
	assert(user.getId() != 0);
	User user2 = userDAO.getUserByUserName("username");
	assert(user2.getId() != 0);
    }

}
