package baomoi.huuutri.com.Model;

/**
 * Created by Thanh Trung on 29/05/2019.
 */

public class Content {
    String text;
    String img;

    public Content(String text, String img) {
        this.text = text;
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public String getImg() {
        return img;
    }

}
