package hu.eke.colorlist;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by szugyi on 18/11/16.
 */

public class Storage {
    private static final String PREF_NAME = "hu.eke.colorlist";
    private static final String EMAIL = "email";

    private SharedPreferences pref;

    public Storage(Context ctx){
        this.pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getEmail(){
        return pref.getString(EMAIL, null);
    }

    public void setEmail(String email){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("email", email);
        editor.commit();
    }

    public void clear(){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
