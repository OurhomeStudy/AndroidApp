package Items;

/**
 * Created by Dong_Gyo on 15. 8. 3..
 */
public class Menu {

    private String menuname;
    private int price;

    public Menu (String menuname, int price) {

        this.menuname = menuname;
        this.price = price;
    }

    int getPrice () {
        return price;
    }

    String getMenuname() {
        return menuname;
    }

    void setMenuname (String tmp) {
        menuname = tmp;
    }

    void setPrice (int tmp) {
        price = tmp;
    }
}
