package com.example.erick.reportedecaos;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by Erick on 18/08/2017.
 */

public class Report {
    public String id;
    public String imageUrl;
    public String reason;
    public Location location;
    public String advice;
    public long timestamp;
    public User user;
    public List<String> tags;
    public boolean isConfirmed;
    public List<Boolean> votes;
    public boolean isFixed;
    public List<Comment> comments;

    public Report() {
        isConfirmed = false;
        isFixed = false;
    }

    public Report(String id, String imageUrl, String reason, Location location, String advice, int timestamp, User user, List<String> tags, boolean isConfirmed, List<Boolean> votes, boolean isFixed, List<Comment> comments) {

        this.id = id;
        this.imageUrl = imageUrl;
        this.reason = reason;
        this.location = location;
        this.advice = advice;
        this.timestamp = timestamp;
        this.user = user;
        this.tags = tags;
        this.isConfirmed = isConfirmed;
        this.votes = votes;
        this.isFixed = isFixed;
        this.comments = comments;
    }

    public static User castFirebaseUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.name = firebaseUser.getDisplayName();
        user.email = firebaseUser.getEmail();
        user.id = firebaseUser.getUid();
        user.photoUrl = firebaseUser.getPhotoUrl().toString();
        return user;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", reason='" + reason + '\'' +
                ", location=" + location +
                ", advice='" + advice + '\'' +
                ", timestamp=" + timestamp +
                ", user=" + user +
                ", tags=" + tags +
                ", isConfirmed=" + isConfirmed +
                ", votes=" + votes +
                ", isFixed=" + isFixed +
                ", comments=" + comments +
                '}';
    }
}
