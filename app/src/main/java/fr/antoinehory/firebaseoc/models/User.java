package fr.antoinehory.firebaseoc.models;

import androidx.annotation.Nullable;

public class User {
    private String uid;
    private String username;
    private Boolean isMentor;
    @Nullable
    private String urlPicture;

    public User(){

    }

    public User(String uid, String username, @Nullable String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.isMentor = false;
        this.urlPicture = urlPicture;
    }

    // GETTERS
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public Boolean getIsMentor() {
        return isMentor;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    // SETTERS
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIsMentor(Boolean mentor) {
        isMentor = mentor;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
