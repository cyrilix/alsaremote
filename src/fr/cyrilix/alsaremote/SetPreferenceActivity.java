package fr.cyrilix.alsaremote;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author Cyrille Nofficial
 * 
 */
public class SetPreferenceActivity extends Activity {
    /**
     * Constructeur par défaut
     */
    public SetPreferenceActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
    }
}
