package com.mehboob.hunzabykea.ui.models;

import android.location.Location;

public class ExistingLocations {

    private String locationTitle;
    private LocationModel location;

    public ExistingLocations(String locationTitle, LocationModel location) {
        this.locationTitle = locationTitle;
        this.location = location;

    }

    public ExistingLocations() {
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public LocationModel getLocation() {
        return location;
    }

    public void setLocation(LocationModel location) {
        this.location = location;
    }
}
