package cz.nkp.differ;

import cz.nkp.differ.io.ProfileManager;
import org.junit.Test;

/**
 *
 * @author xrosecky
 */
public class ProfileManagerTest {

    @Test
    public void loadProfilesTest() {
        ProfileManager pf = Helper.getProfileManager();
        for (String name : pf.getProfiles()) {
            //Profile profile = pf.getProfileByName(name);
        }
    }
}
