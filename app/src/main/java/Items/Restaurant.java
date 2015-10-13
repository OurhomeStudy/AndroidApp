package Items;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Song on 2015-09-13.
 */
public class Restaurant {



    private int shopid;
    private String shopname;
    private String address_lotnum;
    private String address_street;
    private String telno;
    private String category;
    private String floor;
    private String detail;
    private String introduction;
    private ArrayList<Menu> menus;
    private LatLng latlng;
    private String type;
    private String homepg;

    public Restaurant(int shopid, String shopname, String ladd, String sadd, String floor, String telnum, String category, String type, String detail, String homepage, String introduct, String lat, String lng){

        setShopid(shopid);
        setShopname(shopname);
        setLAddress(ladd);
        setSAddress(sadd);
        setFloor(floor);
        setTelno(telnum);
        setCategory(category);
        setType(type);
        setDetail(detail);
        setHomepg(homepage);
        setIntroduction(introduct);
        setLatlng(lat, lng);
    }

    public Restaurant() { }

    public void setHomepg (String tmp) { homepg = tmp; }

    public String getHomepg () { return homepg; }

    public void setType (String tmp) { type = tmp; }

    public String getType () { return type; }

    public void setLatlng (String lat, String lng) { latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)); }

    public LatLng getLatlng () { return latlng; }

    public String getDetail () { return detail; }

    public void setDetail (String tmp) { detail = tmp; }

    public String getIntroduction () { return introduction; }

    public void setIntroduction (String tmp) { introduction = tmp; }

    public String getFloor () { return floor; }

    public void setFloor (String tmp) { floor = tmp; }

    public String getCategory() { return category; }

    public void setCategory(String tmp) { category = tmp; }

    public String getShopname () { return shopname; }

    public void setShopname (String tmp){ shopname = tmp; }

    public String getSAdress() { return address_street; }

    public void setSAddress(String tmp) {
        address_street = tmp;
    }

    public String  getLAddress() {
        return address_lotnum;
    }

    public void setLAddress(String tmp) {
        address_lotnum = tmp;
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




    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }
}
