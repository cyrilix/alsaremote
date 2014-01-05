package fr.cyrilix.alsaremote.main;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

/**
 * Activity to manage preferences
 * 
 * @author Cyrille Nofficial
 * 
 */
public class SetPreferenceActivity extends Activity {

    /** Key preference for SSH port */
    public static final String PORT = "port";
    /** Key preference for SSH hostname */
    public static final String HOSTNAME = "hostname";
    /** Key preference for SSH username */
    public static final String USER = "user";
    /** Key preference for key SSH passphrase */
    public static final String PRIVATE_KEY_PASSPHRASE = "privateKeyPassphrase";
    /** Key preference for SSh private key */
    public static final String PRIVATE_KEY = "privateKey";
    /** Key preference for user password */
    public static final String PASSWORD = "mdp";

    /**
     * Constructor
     */
    public SetPreferenceActivity() {}

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void savePreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(HOSTNAME, sharedPreferences.getString(HOSTNAME, ""));
        editor.putString(PASSWORD, sharedPreferences.getString(PASSWORD, ""));
        editor.putInt(PORT, sharedPreferences.getInt(PORT, 22));
        editor.putString(PRIVATE_KEY, sharedPreferences.getString(PRIVATE_KEY, ""));
        editor.putString(PRIVATE_KEY_PASSPHRASE, sharedPreferences.getString(PRIVATE_KEY_PASSPHRASE, ""));
        editor.putString(USER, sharedPreferences.getString(USER, ""));
        editor.commit();
    }

    /**
     * @see android.app.Activity#onContextMenuClosed(android.view.Menu)
     */
    @Override
    public void onContextMenuClosed(Menu menu) {
        savePreferences();
        super.onContextMenuClosed(menu);
    }
}
