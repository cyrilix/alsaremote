package fr.cyrilix.alsaremote.main;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import fr.cyrilix.alsaremote.R;

/**
 * @author Cyrille Nofficial
 * 
 */
public class Preferences extends PreferenceFragment {

    /**
     * Constructeur par défaut
     */
    public Preferences() {}

    /**
     * @see android.preference.PreferenceFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

    }

}
