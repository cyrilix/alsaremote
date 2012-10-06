package fr.cyrilix.alsaremote;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import fr.cyrilix.alsaremote.mixer.AlsaMixer;
import fr.cyrilix.alsaremote.mixer.AlsaMixerSshRemote;
import fr.cyrilix.alsaremote.mixer.MixerControl;

public class ControlActivity extends Activity {

    private AlsaMixer alsaMixer;

    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {

        /**
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.d(INPUT_METHOD_SERVICE, "Modification du volume: " + seekBar.getProgress());
            TextView label = (TextView) ((LinearLayout) seekBar.getParent()).getChildAt(1);
            try {
                alsaMixer.updateControle(label.getText().toString(), seekBar.getProgress());
            } catch (IOException e) {
                Log.e("ControlActivity", e.getMessage(), e);
                displayError(e);
            }
        }

        /**
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onStartTrackingTouch(android.widget.SeekBar)
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        /**
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onProgressChanged(android.widget.SeekBar,
         *      int, boolean)
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // if (!fromUser) return;
            //
            // Log.d(INPUT_METHOD_SERVICE, "Modification du volume: " +
            // progress);
            // TextView label = (TextView) ((LinearLayout)
            // seekBar.getParent()).getChildAt(1);
            // try {
            // alsaMixer.updateControle(label.getText().toString(), progress);
            // } catch (IOException e) {
            // Log.e("ControlActivity", e.getMessage(), e);
            // }
        }
    };

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alsaMixer = new AlsaMixerSshRemote(getApplicationContext());
        setContentView(R.layout.activity_control);

        loadMixer();

    }

    /**
     * 
     */
    public void loadMixer() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        networkInfo.isConnected();

        LinearLayout mixersLayout = (LinearLayout) findViewById(R.id.mixersLayout);

        try {
            for (MixerControl mixer : alsaMixer.getControles()) {
                LinearLayout mixerLayout = new LinearLayout(this);
                mixerLayout.setOrientation(LinearLayout.VERTICAL);
                mixerLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
                mixerLayout.setVisibility(View.VISIBLE);

                TextView mixerName = new TextView(this);
                mixerName.setText(mixer.getName());
                mixerName.setVisibility(View.VISIBLE);

                SeekBar seekBar = new SeekBar(this);
                seekBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
                seekBar.setRotation(270f);
                seekBar.setProgress(Integer.valueOf(mixer.getValue()));
                seekBar.setMax(Integer.parseInt(mixer.getMaxValue()));
                seekBar.setVisibility(View.VISIBLE);

                mixerLayout.addView(seekBar);
                mixerLayout.addView(mixerName);

                mixersLayout.addView(mixerLayout);

            }

        } catch (IOException e) {
            Log.e("ControlActivity", e.getMessage(), e);
            displayError(e);
        }
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

        switch (item.getItemId()) {

            case R.id.menu_settings:
                Intent intent = new Intent();
                intent.setClass(ControlActivity.this, SetPreferenceActivity.class);
                startActivityForResult(intent, 0);
                break;

        }
        return true;
    }

    /**
     * @see android.app.Activity#onRestart()
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        loadMixer();
    }
}
