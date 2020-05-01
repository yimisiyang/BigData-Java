package pojo;

public class Upgrade {
    private String upUnit;
    private String upJX;
    private String updevice;
    private String updescribe;
    private String updateVersion;

    public String getUpUnit() {
        return upUnit;
    }

    public void setUpUnit(String upUnit) {
        this.upUnit = upUnit;
    }

    public String getUpJX() {
        return upJX;
    }

    public void setUpJX(String upJX) {
        this.upJX = upJX;
    }

    public String getUpdevice() {
        return updevice;
    }

    public void setUpdevice(String updevice) {
        this.updevice = updevice;
    }

    public String getUpdescribe() {
        return updescribe;
    }

    public void setUpdescribe(String updescribe) {
        this.updescribe = updescribe;
    }

    public String getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(String updateVersion) {
        this.updateVersion = updateVersion;
    }

    public Upgrade(String upUnit, String upJX, String updevice, String updescribe, String updateVersion) {
        this.upUnit = upUnit;
        this.upJX = upJX;
        this.updevice = updevice;
        this.updescribe = updescribe;
        this.updateVersion = updateVersion;
    }

    public Upgrade() {
    }
}
