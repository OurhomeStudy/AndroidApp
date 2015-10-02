package Items;

/**
 * Created by Dong_Gyo on 2015. 10. 2..
 */
public class Review {

    private String name;
    private String content;
    private int reviewstar;

    public Review(String name, String content, int reviewstar) {
        this.name = name;
        this.content = content;
        this.reviewstar = reviewstar;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public void setReviewstar (int reviewstar) {
        this.reviewstar = reviewstar;
    }

    public String getName () {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getReviewstar() {
        return reviewstar;
    }
}
