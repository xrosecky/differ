package cz.nkp.differ.dao;

import cz.nkp.differ.exceptions.UserDifferException;
import cz.nkp.differ.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

/**
 *
 * @author xrosecky
 */
public class UserDAOImpl implements UserDAO {

    private static String ADD_USER = "INSERT INTO users(admin, username, password_hash, password_salt, mail) VALUES(?, ?, ?, ?, ?)";
    private static String GET_USER_BY_USERNAME = "SELECT id, username, password_hash, password_salt, mail, admin FROM users WHERE username = ?";
    private JdbcTemplate jdbcTemplate;

    public UserDAOImpl() {
    }

    public UserDAOImpl(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addUser(final User user) throws UserDifferException {
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	try {
	    jdbcTemplate.update(new PreparedStatementCreator() {

		@Override
		public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
		    PreparedStatement preparedStatement = conn.prepareStatement(ADD_USER, Statement.RETURN_GENERATED_KEYS);
		    preparedStatement.setBoolean(1, user.isAdmin());
		    preparedStatement.setString(2, user.getUserName());
		    preparedStatement.setString(3, user.getPasswordHash());
		    preparedStatement.setString(4, user.getPasswordSalt());
		    preparedStatement.setString(5, user.getMail());
		    return preparedStatement;
		}
	    }, keyHolder);
	} catch (DataIntegrityViolationException ex) {
	    throw new UserDifferException(UserDifferException.ErrorCode.USER_ALREADY_EXISTS, "User already exists!", ex);
	}
	user.setId(keyHolder.getKey().intValue());
    }

    @Override
    public User getUserByUserName(String userName) throws UserDifferException {
	try {
	    return (User) jdbcTemplate.queryForObject(GET_USER_BY_USERNAME, new Object[]{userName}, new UserMapper());
	} catch (EmptyResultDataAccessException ex) {
	    return null;
	}
    }

    public class UserMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
	    User user = new User();
	    user.setId(rs.getInt("id"));
	    user.setAdmin(rs.getBoolean("admin"));
	    user.setUserName(rs.getString("username"));
	    user.setPasswordHash(rs.getString("password_hash"));
	    user.setPasswordSalt(rs.getString("password_salt"));
	    user.setMail(rs.getString("mail"));
	    return user;
	}
    }
}
