package Items;

import java.util.ArrayList;

/**
 * Created by Dong_Gyo on 15. 8. 3..
 */
public class Shop {

    private String shopname;
    private String address;
    private String telno;
    private String category;
    private ArrayList<Menu> menus;

    public Shop (String shopname){
        this.shopname = shopname;
    }

    public void setCategory(String tmp) {
        category = tmp;
    }

    public String getCategory() {
        return category;
    }

    public String getShopname () {

        return shopname;
    }

    public void setShopname (String tmp){
        shopname = tmp;
    }

    public String  getAddress() {
        return address;
    }

    public void setAddress(String tmp) {
        address = tmp;
    }

    public String  getTelno() {
        return telno;
    }

    public void setTelno(String tmp) {
        telno = tmp;
    }

    public ArrayList<Menu> getMenus () {
        return menus;
    }

    public void setMenus (ArrayList<Menu> tmp) {
        menus = tmp;
    }
}
