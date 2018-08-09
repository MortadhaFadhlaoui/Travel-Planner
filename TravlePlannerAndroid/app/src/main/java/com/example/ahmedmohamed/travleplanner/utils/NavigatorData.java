package com.example.ahmedmohamed.travleplanner.utils;

/**
 * Created by AHMED Mohamed on 14/11/2017.
 */


import com.example.ahmedmohamed.travleplanner.Adapters.ArriveAdapter;
import com.example.ahmedmohamed.travleplanner.Entities.Nearby;
import com.example.ahmedmohamed.travleplanner.Entities.Pack;
import com.example.ahmedmohamed.travleplanner.Entities.Packmorta;
import com.example.ahmedmohamed.travleplanner.Entities.Pays;
import com.example.ahmedmohamed.travleplanner.Entities.Paysmorta;
import com.example.ahmedmohamed.travleplanner.Entities.Saved;
import com.example.ahmedmohamed.travleplanner.Entities.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Mortadhafff
 */
public class NavigatorData {
    public static <T> void removeDuplicate(List<T> list) {
        HashSet<T> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
    }
    public static String imageurl;

    private ArrayList<Paysmorta> payses;
    private ArrayList<Packmorta> packs;

    public ArrayList<Paysmorta> getPayses() {
        return payses;
    }

    public void setPayses(ArrayList<Paysmorta> payses) {
        this.payses = payses;
    }

    public ArrayList<Packmorta> getPaks() {
        return packs;
    }
    private Paysmorta pays;
    public void setPacks(ArrayList<Packmorta> packs) {
        this.packs = packs;
    }
    public void setPays(Paysmorta pays) {
        this.pays = pays;
    }
    public  Paysmorta getPays() {
        return pays;
    }
    private final static NavigatorData instance = new NavigatorData();
    private Nearby nearby;
    public static NavigatorData getInstance() {
        return instance;
    }
    private Saved saved;
    private User athuser;
    private String token;
    private Pack AddedPack;
    public static ArriveAdapter adapter;
    private static final String url="http://192.168.1.7:8000";

    public Pack getAddedPack() {
        return AddedPack;
    }
    public void setNearby(Nearby nearby) {
        this.nearby = nearby;
    }
    public  Nearby getNearby() {
        return nearby;
    }
    public void setAddedPack(Pack addedPack) {
        AddedPack = addedPack;
    }

    public String getUrl() {
        return url;
    }

    public Saved getSaved() {
        return saved;
    }

    public void setSaved(Saved saved) {
        this.saved = saved;
    }

    public void setUserlogedIn(User user) {
        this.athuser = user;
    }
    public void setUserToken(String token) {
        this.token = token;
    }

    public String getUserToken() {
        return token;
    }
    public User getUserlogedIn() {
        return athuser;
    }


}