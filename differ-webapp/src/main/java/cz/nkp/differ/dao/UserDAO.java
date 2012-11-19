package cz.nkp.differ.dao;

import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.model.User;

/**
 *
 * @author xrosecky
 */
public interface UserDAO {

    public User getUserByUserName(String userName) throws UserDifferException;
    public void addUser(User user) throws UserDifferException;

}
