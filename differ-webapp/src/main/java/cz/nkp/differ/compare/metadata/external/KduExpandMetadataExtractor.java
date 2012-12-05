package cz.nkp.differ.compare.metadata.external;

import cz.nkp.differ.compare.metadata.ImageMetadata;
import cz.nkp.differ.compare.metadata.MetadataSource;
import cz.nkp.differ.io.ProfileManager;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author xrosecky
 */
public class KduExpandMetadataExtractor extends ExternalMetadataExtractor {

    private ProfileManager profileManager;

    @Override
    public List<ImageMetadata> getMetadata(File file) {
        List<ImageMetadata> result = super.getMetadata(file);
        for (String profileName : profileManager.getProfiles()) {
            StringBuilder comparison = new StringBuilder();
            boolean conform = true;
            Map<String, String> profile = profileManager.getProfileByName(profileName);
            for (ImageMetadata metadata : result) {
                String key = metadata.getKey();
                Object val = metadata.getValue();
                String profileVal = profile.get(key);
                if (profileVal != null) {
                    if (val.equals(profileVal)) {
                        comparison.append(String.format("%s: %s != %s", key, val, profileVal));
                    } else {
                        comparison.append(String.format("%s: %s == %s\n", key, val, profileVal));
                        conform = false;
                    }
                }
            }
            MetadataSource source = new MetadataSource(0, comparison.toString(), "", "profile");
            result.add(new ImageMetadata(profileName, (Object) conform, source));
        }
        return result;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }
}
