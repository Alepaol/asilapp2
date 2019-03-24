package asilapp.sms.com.chatassistant.model;

public class User {
    private String uid;
    private String email;
    private String firebaseToken;

    public User(){

    }

    public User(String uid, String email, String firebaseToken){
        this.uid = uid;
        this.email = email;
        this.firebaseToken = firebaseToken;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public String getUid() {
        return uid;
    }
}