package fr.cyrilix.alsaremote;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

/**
 * @author Cyrille Nofficial
 * 
 */
public abstract class AbstractMainActivity extends Activity {

    /**
     * Constructeur par défaut
     */
    protected AbstractMainActivity() {}

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_control, menu);
        return true;
    }

    /**
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.mixer:
                intent.setClass(this, ControlActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.radio:
                intent.setClass(this, RadioActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.menu_settings:
                intent.setClass(this, SetPreferenceActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }

    /**
     * @param e
     */
    public void displayError(IOException e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setMessage(e.getLocalizedMessage());
        builder.setTitle("Connexion");
        builder.create();
    }
}
