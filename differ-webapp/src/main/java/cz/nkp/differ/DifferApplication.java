package cz.nkp.differ;

import com.vaadin.terminal.ExternalResource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Window;
import cz.nkp.differ.configuration.GoogleAnalyticsConfiguration;

import cz.nkp.differ.gui.windows.MainDifferWindow;
import cz.nkp.differ.io.ImageManager;
import cz.nkp.differ.io.ResultManager;
import cz.nkp.differ.model.User;
import cz.nkp.differ.user.UserManager;
import eu.livotov.tpt.TPTApplication;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vaadin.googleanalytics.tracking.GoogleAnalyticsTracker;

/**
 * The main Application instance, responsible for setting global settings, such as locale, theme, and the root window for the GUI.
 * This class also allows any code to fetch the current Application instance in a thread-safe way.
 * @author Joshua Mabrey
 * Mar 30, 2012
 */
@SuppressWarnings("serial")
public class DifferApplication extends TPTApplication {

    /* static class members */
    protected static UserManager userManager = null;
    protected static ImageManager imageManager = null;
    protected static ResultManager resultManager = null;
    protected static ApplicationContext applicationContext = null;
    protected static MainDifferWindow mainDifferWindow = null;
    protected static GoogleAnalyticsTracker gaTracker = null;
    /* session variables */
    private User loggedUser = null;

    /*
     * We dont need an X server running on a display to do graphics operations. May be slower on some machines.
     * TODO: examine a switching option for this setting
     */
    static {
	System.setProperty("java.awt.headless", "true");
    }

    /**
     * Called by the server to run the application and begin the session
     * FIXME: move same parts to firstApplicationStartup()
     */
    @Override
    public void applicationInit() {
	//Setup Apache Log4j Configuration
	BasicConfigurator.configure();

	//BouncyCastle Setup
	Security.addProvider(new BouncyCastleProvider());

	setTheme(DIFFER_THEME_NAME);//Set to custom differ theme
	LOGGER.trace("Loaded Vaadin theme: " + DIFFER_THEME_NAME);

	//Get Application Context
	WebApplicationContext context = (WebApplicationContext) getContext();

	//Set Context Locale to Browser Locale
	Locale locale = context.getBrowser().getLocale();
	setLocale(locale);
	LOGGER.trace("Session Locale: " + locale.getDisplayName());

	//Add this as a listener to the context transaction event pump
	context.addTransactionListener(this);

	ServletContext servletContext = ((WebApplicationContext) this.getContext()).getHttpSession().getServletContext();
        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	userManager = (UserManager) applicationContext.getBean("userManager");
	imageManager = (ImageManager) applicationContext.getBean("imageManager");
	resultManager = (ResultManager) applicationContext.getBean("resultManager");
        
        String resultsPath = "/tmp/differ/" + userManager.getLoggedInUser() + "/results";
        new File(resultsPath).mkdirs(); //create results folder if doesn't exist
        resultManager.setDirectory(resultsPath);
        GoogleAnalyticsConfiguration gaConf = (GoogleAnalyticsConfiguration)
                applicationContext.getBean("googleAnalyticsConfiguration");

	mainDifferWindow = new MainDifferWindow();
	mainDifferWindow.setSizeUndefined();
	setMainWindow(mainDifferWindow);
        gaTracker = new GoogleAnalyticsTracker(gaConf.getTrackerId(), gaConf.getDomainName());
        mainDifferWindow.addComponent(gaTracker);
        gaTracker.trackPageview("/");
    }

    @Override
    public void firstApplicationStartup() {
    }

    @Override
    public Window getWindow(String name) {
	Window window = super.getWindow(name);
	if (window == null) {
	    window = new MainDifferWindow();
	    window.setSizeUndefined();
	    window.setName(name);
	    addWindow(window);
	    window.open(new ExternalResource(window.getURL()));
	}
	return window;
    }

    public User getLoggedUser() {
	return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
	this.loggedUser = loggedUser;
    }

    public static ImageManager getImageManager() {
	return imageManager;
    }

    public static UserManager getUserManager() {
	return userManager;
    }

    public static ResultManager getResultManager() {
	return resultManager;
    }
    
    public static GoogleAnalyticsTracker getGATracker() {
        return gaTracker;
    }

    public static ApplicationContext getApplicationContext() {
	return applicationContext;
    }

    public static DifferApplication getCurrentApplication() {
	return (DifferApplication) TPTApplication.getCurrentApplication();
    }

    public static Window getMainApplicationWindow() {
        return mainDifferWindow;
    }
    /*
    public static File getHomeDirectory() {
	if (differHome == null) {
	    differHome = System.getProperty("user.home");
	    differHome += File.separatorChar + ".differ";
	    LOGGER.trace("Differ Home Directory: " + differHome);

	    //If the home directory doesnt exist create it
	    File differHomeFile = new File(differHome);
	    if (!differHomeFile.exists()) {
		differHomeFile.mkdir();
	    }

	    //Same with the plugin subdirectory
	    File differHomeFilePluginDirectory = new File(differHomeFile, "plugins");
	    if (!differHomeFilePluginDirectory.exists()) {
		differHomeFilePluginDirectory.mkdir();
	    }

	    //Same with users subdirectory
	    File differHomeFileUsersDirectory = new File(differHomeFile, "users");
	    if (!differHomeFileUsersDirectory.exists()) {
		differHomeFileUsersDirectory.mkdir();
	    }

	    //Same with logs subdirectory
	    File differHomeLogsDirectory = new File(differHomeFile, "logs");
	    if (!differHomeLogsDirectory.exists()) {
		differHomeLogsDirectory.mkdir();
	    }

	}

	File homeDir = new File(differHome);

	if (!homeDir.exists()) {
	    LOGGER.error("Differ home directory unable to be created at " + homeDir.getAbsolutePath());
	}

	return homeDir;
    }
    */ 

    public float getScreenWidth() {
	return getMainWindow().getWidth();
    }
    private static String differHome;
    private static final String DIFFER_THEME_NAME = "differ";
    private static Logger LOGGER = Logger.getLogger(DifferApplication.class);
}
