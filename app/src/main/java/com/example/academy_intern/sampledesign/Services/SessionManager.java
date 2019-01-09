package com.example.academy_intern.sampledesign.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager
{
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    public static final String PREF_NAME = "EntreHivePref";

    // All Shared Preferences Keys
    public static final String IS_LOGGED_IN = "IsLoggedIn";

    public static final String HAS_SEEN_SLIDESHOW = "HasSeenSlideshow";

    // User id (make variable public to access from outside)
    public static final String KEY_USER_ID = "userId";

    // User role (make variable public to access from outside)
    public static final String USER_ROLE = "User_role";

    // Constructor
    public SessionManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int userId, boolean userRole, int balance)
    {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGGED_IN, true);

        // Storing user id in pref
        editor.putInt(KEY_USER_ID, userId);

        // Storing user role in pref
        editor.putBoolean(USER_ROLE, userRole);

        // Storing user balance in pref
        editor.putInt("Balance", balance);

        // commit changes
        editor.apply();
    }

    public void setUserBalance(int balance)
    {
        editor.putInt("Balance", balance);

        editor.apply();
    }


    /**
     * Clear session details
     * */
    public void logoutUser()
    {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    // Get user id
    public int getLoggedInId(){
        return pref.getInt(KEY_USER_ID, 0);
    }

    // Get user user role
    public boolean getUserRole(){
        return pref.getBoolean(USER_ROLE, false);
    }

    public int getUserBalance(){
        return pref.getInt("Balance", 0);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(HAS_SEEN_SLIDESHOW, isFirstTime);
        editor.commit();
    }

    public boolean firstTimeLaunch(){
        return pref.getBoolean(HAS_SEEN_SLIDESHOW, true);
    }
}