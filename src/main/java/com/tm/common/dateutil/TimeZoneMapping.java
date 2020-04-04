package com.tm.common.dateutil;

public class TimeZoneMapping {
    private String standardName;
    private String DisplayName;
    private String zoneId;

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public TimeZoneMapping(String standardName, String zoneId, String displayName) {
        this.standardName = standardName;
        DisplayName = displayName;
        this.zoneId = zoneId;
    }

    public TimeZoneMapping() {

    }

    @Override
    public String toString() {
        return "TimeZoneMapping{" +
                "standardName='" + standardName + '\'' +
                ", DisplayName='" + DisplayName + '\'' +
                ", zoneId='" + zoneId + '\'' +
                '}';
    }
}
