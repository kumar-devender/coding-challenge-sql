package exercise.domain;

public class Purchase {
    private int adId;
    private String title;
    private int userId;

    public Purchase(int adId, String title, int userId) {
        this.adId = adId;
        this.title = title;
        this.userId = userId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int usetId) {
        this.userId = userId;
    }
}
