package cz.nkp.differ.dao;

import cz.nkp.differ.model.Image;
import cz.nkp.differ.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

/**
 *
 * @author xrosecky
 */
public class ImageDAOImpl implements ImageDAO {

    private static String ADD_IMAGE = "INSERT INTO images(filename, unique_name, owner_id, size, shared) VALUES(?, ?, ?, ?, ?)";
    private static String GET_IMAGES_BY_USER = "SELECT id, filename, unique_name, owner_id, size, shared FROM images WHERE owner_id = ?";
    private static String GET_SHARED_IMAGES = "SELECT id, filename, unique_name, owner_id, size, shared FROM images WHERE shared =true";
    private static String DELETE_IMAGE_BY_ID = "DELETE FROM images WHERE id = ?";
    private static String UPDATE_IMAGE = "UPDATE images SET filename = ?, owner_id =?, shared = ? WHERE id = ?";
    private JdbcTemplate jdbcTemplate;

    public ImageDAOImpl(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ImageDAOImpl() {
    }

    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addImage(final Image image) {
	GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	jdbcTemplate.update(new PreparedStatementCreator() {

	    @Override
	    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(ADD_IMAGE, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, image.getFileName());
		preparedStatement.setString(2, image.getUniqueName());
		preparedStatement.setInt(3, image.getOwnerId());
		preparedStatement.setInt(4, image.getSize());
		preparedStatement.setBoolean(5, image.isShared());
		return preparedStatement;
	    }
	}, keyHolder);
	image.setId(keyHolder.getKey().intValue());
    }

    @Override
    public void updateImage(final Image image) {
	jdbcTemplate.update(new PreparedStatementCreator() {

	    @Override
	    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_IMAGE);
		preparedStatement.setString(1, image.getFileName());
		preparedStatement.setInt(2, image.getOwnerId());
		preparedStatement.setBoolean(3, image.isShared());
		preparedStatement.setInt(4, image.getId());
		return preparedStatement;
	    }
	});
    }

    @Override
    public List<Image> getImagesForUser(User user) {
	return jdbcTemplate.query(GET_IMAGES_BY_USER, new Object[]{user.getId()}, new ImageMapper());
    }

    @Override
    public List<Image> getSharedImages() {
	return jdbcTemplate.query(GET_SHARED_IMAGES, new ImageMapper());
    }

    @Override
    public void deleteImage(Image image) {
	jdbcTemplate.update(DELETE_IMAGE_BY_ID, new Object[]{image.getId()});
    }

    public class ImageMapper implements RowMapper<Image> {

	@Override
	public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
	    Image image = new Image();
	    image.setId(rs.getInt("id"));
	    image.setFileName(rs.getString(2));
	    image.setUniqueName(rs.getString(3));
	    image.setOwnerId(rs.getInt(4));
	    image.setSize(rs.getInt(5));
	    image.setShared(rs.getBoolean(6));
	    return image;
	}
    }
}
