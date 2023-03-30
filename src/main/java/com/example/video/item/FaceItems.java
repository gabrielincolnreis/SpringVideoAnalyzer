package com.example.video.item;

public class FaceItems {

    // Represents a model that stores labels detected in a video
    private String ageRange;
    private String beard;
    private String eyeglasses;
    private String eyesOpen;
    private String mustache;
    private String smile;

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getBeard() {
        return beard;
    }

    public void setBeard(String beard) {
        this.beard = beard;
    }

    public String getEyeglasses() {
        return eyeglasses;
    }

    public void setEyeglasses(String eyeglasses) {
        this.eyeglasses = eyeglasses;
    }

    public String getEyesOpen() {
        return eyesOpen;
    }

    public void setEyesOpen(String eyesOpen) {
        this.eyesOpen = eyesOpen;
    }

    public String getMustache() {
        return mustache;
    }

    public void setMustache(String mustache) {
        this.mustache = mustache;
    }

    public String getSmile() {
        return smile;
    }

    public void setSmile(String smile) {
        this.smile = smile;
    }
}
