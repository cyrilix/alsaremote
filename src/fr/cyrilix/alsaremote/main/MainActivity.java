package fr.cyrilix.alsaremote.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fr.cyrilix.alsaremote.R;
import fr.cyrilix.alsaremote.mixer.MixerActivity;

/**
 * @author Cyrille Nofficial
 * 
 */
public class MainActivity extends Activity {

    /**
     * Constructeur par défaut
     */
    public MainActivity() {}

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.mixer:
                intent.setClass(this, MixerActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.menu_settings:
                intent.setClass(this, SetPreferenceActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }

}
