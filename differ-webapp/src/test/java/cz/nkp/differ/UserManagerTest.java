package cz.nkp.differ;

import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.model.User;
import cz.nkp.differ.user.UserManager;
import java.sql.SQLException;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class UserManagerTest {

    protected UserManager udc;

    public UserManagerTest() throws SQLException {
	this.udc = Helper.getUserManager();
    }

    @Test
    public void registerAndLoginUser() throws UserDifferException {
	User user = new User();
	user.setUserName("test2");
	udc.registerUser(user, "password2");
	assert(user.getId() != 0);
	udc.attemptLogin("test2", "password2");
	try {
	    udc.attemptLogin("test2", "bad_password");
	    assert(false);
	} catch (UserDifferException ex) {
	    assert(ex.getCode() == UserDifferException.ErrorCode.BAD_PASSWORD_OR_USERNAME);
	}
    }

}
