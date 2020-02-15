package exercise.domain;

import java.util.Objects;

public class UserPurchase {
    private int userId;
    private String name;
    private String email;
    private int adId;
    private String title;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPurchase)) return false;
        UserPurchase that = (UserPurchase) o;
        return userId == that.userId &&
                adId == that.adId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, adId);
    }

    @Override
    public String toString() {
        return "UserPurchase{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", adId='" + adId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
