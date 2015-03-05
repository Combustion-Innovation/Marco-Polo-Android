package com.ci.marcopolo;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Alex on 2/25/15.
 */
public class MarcoPoloData {
    public final static String TAG = "MarcoPoloData";

    private JSONObject marcoPoloData;
    private JSONObject mp;
    private JSONArray blocked;
    private JSONArray marcos;
    private JSONArray polos;
    private JSONObject friends;
    private JSONObject contacts;

    private int totalCount;
    private int poloCount;
    private int marcoCount;
    private int friendCount;

    private void getCounts() {
        poloCount = polos.length();
        marcoCount = marcos.length();
        friendCount = 0;
        try {
            if (!friends.has("empty")) {
                for (char initial = 'A'; initial <= 'Z'; initial++) {
                    JSONArray initialGroup = friends.getJSONArray(initial + "");
                    for (int i = 0; i < initialGroup.length(); i++) {
                        friendCount++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return;
        }
        totalCount = poloCount + marcoCount + friendCount;
    }

    public MarcoPoloData(String data) {
        try {
            marcoPoloData = new JSONObject(data);
            mp = marcoPoloData.getJSONObject("mp");
            blocked = marcoPoloData.getJSONArray("blocked");
            marcos = marcoPoloData.getJSONArray("marcos");
            polos = marcoPoloData.getJSONArray("polos");
            friends = marcoPoloData.getJSONObject("friends");
            contacts = marcoPoloData.getJSONObject("contacts");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        getCounts();
    }

    public MarcoPoloData(JSONObject data) {
        try {
            marcoPoloData = data;
            mp = marcoPoloData.getJSONObject("mp");
            blocked = marcoPoloData.getJSONArray("blocked");
            marcos = marcoPoloData.getJSONArray("marcos");
            polos = marcoPoloData.getJSONArray("polos");
            friends = marcoPoloData.getJSONObject("friends");
            contacts = marcoPoloData.getJSONObject("contacts");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        getCounts();
    }

    public JSONObject get(int index) {
        if (index > getTotalCount()) return null;

        //Log.d(TAG, "getting index " + index);
        //Log.d(TAG, "poloCount: " + poloCount);
        //Log.d(TAG, "marcoCount: " + marcoCount);
        //Log.d(TAG, "friendCount: " + friendCount);
        try {
            int effectiveIndex = index;
            if (effectiveIndex < poloCount) {
                JSONObject cell = polos.getJSONObject(index);
                cell.put("type", "POLO");
                return cell;
            }

            effectiveIndex -= poloCount;
            //Log.d(TAG, "effective index: " + effectiveIndex);
            if (effectiveIndex < marcoCount) {
                JSONObject cell = marcos.getJSONObject(effectiveIndex);
                cell.put("type", "MARCO");
                return cell;
            }

            effectiveIndex -= marcoCount;
            //Log.d(TAG, "effective index: " + effectiveIndex);
            for (char initial = 'A'; initial <= 'Z'; initial++) {
                JSONArray initialGroup = friends.getJSONArray(initial + "");
                int initialGroupLength = initialGroup.length();
                if (effectiveIndex < initialGroupLength) {
                    JSONObject cell = initialGroup.getJSONObject(effectiveIndex);
                    cell.put("type", "FRIEND");
                    return cell;
                } else {
                    effectiveIndex -= initialGroupLength;
                    //Log.d(TAG, "effective index: " + effectiveIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public JSONObject getPolo(String username) {
        for (int i = 0; i < poloCount; i++) {
            try {
                JSONObject currentPolo = polos.getJSONObject(i);
                String currentUsername = currentPolo.getString("username");
                if (currentUsername.equals(username)) {
                    return currentPolo;
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return null;
            }
        }
        return null;
    }

    public JSONObject getMarco(String username) {
        for (int i = 0; i < marcoCount; i++) {
            try {
                JSONObject currentMarco = marcos.getJSONObject(i);
                String currentUsername = currentMarco.getString("username");
                if (currentUsername.equals(username)) {
                    return currentMarco;
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
                return null;
            }
        }
        return null;
    }

    public JSONObject getMarcoPoloData() {
        return marcoPoloData;
    }

    public JSONObject getMp() {
        return mp;
    }

    public JSONArray getBlocked() {
        return blocked;
    }

    public JSONArray getMarcos() {
        return marcos;
    }

    public JSONArray getPolos() {
        return polos;
    }

    public JSONObject getFriends() {
        return friends;
    }

    public JSONObject getContacts() {
        return contacts;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getPoloCount() {
        return poloCount;
    }

    public int getMarcoCount() {
        return marcoCount;
    }

    public int getFriendCount() {
        return friendCount;
    }
}
