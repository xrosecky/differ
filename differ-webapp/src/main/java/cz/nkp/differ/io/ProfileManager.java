package cz.nkp.differ.io;

import cz.nkp.differ.model.Profile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 *
 * @author xrosecky
 */
public class ProfileManager {

    private File profileDirectory;

    public List<String> getProfiles() {
        List<String> profiles = new ArrayList<String>();
        for (File file : profileDirectory.listFiles()) {
            if (file.isFile() && file.canRead()) {
                profiles.add(file.getName());
            }
        }
        return profiles;
    }

    public Map<String, String> getProfileByName(String name) {
        File file = new File(profileDirectory, name);
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            Map<String, String> result = new HashMap<String, String>();
            for (Object object : properties.keySet()) {
                String key = (String) object;
                String value = properties.getProperty(key);
                result.put(key, value);
            }
            return result;
        } catch (FileNotFoundException fnfe) {
            return null;
        } catch (IOException ioe) {
            throw new IllegalArgumentException(String.format("IO error when reading file: %s does not exists", file.getAbsolutePath()), ioe);
        }
    }

    public void saveProfile(String name, Map<String, String> profile) {
        Properties props = new Properties();
        for (Entry<String, String> entry : profile.entrySet()) {
            props.setProperty(entry.getKey(), entry.getValue());
        }
        File file = new File(profileDirectory, name);
        try {
            props.store(new FileOutputStream(file), null);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public void deleteProfile(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (name.equals("")) {
            throw new IllegalArgumentException("name is empty string!");
        }
        File file = new File(profileDirectory, name);
        file.delete();
    }

    public File getProfileDirectory() {
        return profileDirectory;
    }

    public void setProfileDirectory(File profileDirectory) {
        this.profileDirectory = profileDirectory;
    }

    public Map<String, List<String>> getProfileTemplate() {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        InputStream is = ProfileManager.class.getResourceAsStream("/jpeg2000_profile.properties");
        Properties props = new Properties();
        try {
            props.load(is);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        for (Object key : props.keySet()) {
            String name = (String) key;
            String value = props.getProperty(name);
            List<String> values = Arrays.asList(value.split(","));
            result.put(name, values);
        }
        return result;
    }
}
