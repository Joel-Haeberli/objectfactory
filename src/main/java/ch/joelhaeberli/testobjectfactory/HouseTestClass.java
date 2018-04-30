package ch.joelhaeberli.testobjectfactory;

import java.time.LocalDateTime;
import java.util.Date;

public class HouseTestClass {

    private String housename;
    private int housenumber;
    private Date builddate;
    private LocalDateTime buildtimeanddate;

    public HouseTestClass() {
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public int getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(int housenumber) {
        this.housenumber = housenumber;
    }

    public Date getBuilddate() {
        return builddate;
    }

    public void setBuilddate(Date builddate) {
        this.builddate = builddate;
    }

    public LocalDateTime getBuildtimeanddate() {
        return buildtimeanddate;
    }

    public void setBuildtimeanddate(LocalDateTime buildtimeanddate) {
        this.buildtimeanddate = buildtimeanddate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("House{");
        sb.append("housename='").append(housename).append('\'');
        sb.append(", housenumber=").append(housenumber);
        sb.append(", builddate=").append(builddate);
        sb.append(", buildtimeanddate=").append(buildtimeanddate);
        sb.append('}');
        return sb.toString();
    }
}
