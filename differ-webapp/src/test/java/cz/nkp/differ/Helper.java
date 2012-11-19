package cz.nkp.differ;

import cz.nkp.differ.compare.io.ImageProcessor;
import cz.nkp.differ.compare.metadata.MetadataExtractors;
import cz.nkp.differ.dao.ImageDAO;
import cz.nkp.differ.dao.UserDAO;
import cz.nkp.differ.images.ImageLoader;
import cz.nkp.differ.io.ImageManager;
import cz.nkp.differ.user.UserManager;
import java.io.IOException;
import java.io.InputStream;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author xrosecky
 */
public class Helper {

    protected static ApplicationContext context = getApplicationContext();

    protected static ApplicationContext getApplicationContext() {
	ApplicationContext ctx = null;
	String applicationContextFileLocation = System.getProperty("application.context");
	if (applicationContextFileLocation != null) {
	    ctx = new FileSystemXmlApplicationContext(applicationContextFileLocation);
	} else {
	    ctx = new ClassPathXmlApplicationContext("config.xml");
	    DataSource ds = (DataSource) ctx.getBean("dataSource");
	    InputStream is = ClassLoader.class.getResourceAsStream("/create_db.sql");
	    String sql = "";
	    try {
		sql = IOUtils.toString(is);
	    } catch (IOException ioe) {
		ioe.printStackTrace();
	    }
	    JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
	    for (String str : sql.split(";")) {
		jdbcTemplate.execute(str);
	    }
	}
	return ctx;
    }

    public static ImageDAO getImageDAO() {
	return (ImageDAO) context.getBean("imageDAO");
    }

    public static UserDAO getUserDAO() {
	return (UserDAO) context.getBean("userDAO");
    }

    public static ImageManager getImageManager() {
	return (ImageManager) context.getBean("imageManager");
    }

    public static UserManager getUserManager() {
	return (UserManager) context.getBean("userManager");
    }

    public static ImageLoader getImageLoader() {
	return (ImageLoader) context.getBean("imageLoaderFactory");
    }

    public static ImageProcessor getImageProcessor() {
	return (ImageProcessor) context.getBean("imageProcessor");
    }

    public static MetadataExtractors getMetadataExtractors() {
	return (MetadataExtractors) context.getBean("metadataExtractors");
    }
}
