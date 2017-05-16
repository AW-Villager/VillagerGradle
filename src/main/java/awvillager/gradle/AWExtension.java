package awvillager.gradle;

public class AWExtension {

    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAIWolfVersion() {
        if (version == null) {
            throw new RuntimeException(
                    "Not found version for aiwolf from build.gradle . "
                            + "Add String to build.gradle [ aiwolf.version={version} ]");
        }
        return version.split("-")[0];
    }

    public String getVillagerVersion() {
        if (version == null) {
            throw new RuntimeException(
                    "Not found version for aiwolf from build.gradle . "
                            + "Add String to build.gradle [ aiwolf.version={version} ]");
        }
        return version.split("-")[1];
    }

}
