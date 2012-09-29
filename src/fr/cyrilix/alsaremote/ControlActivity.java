package fr.cyrilix.alsaremote;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import fr.cyrilix.alsaremote.mixer.AlsaMixer;
import fr.cyrilix.alsaremote.mixer.AlsaMixerSshRemote;
import fr.cyrilix.alsaremote.mixer.MixerControl;

@TargetApi(11)
public class ControlActivity extends Activity {

    private final AlsaMixerSshRemote alsaMixer = new AlsaMixerSshRemote();

    private final SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
        private final AlsaMixer alsaMixer = new AlsaMixerSshRemote();

        /**
         * @see android.widget.SeekBar.OnSeekBarChangeListener#onStopTrackingTouch(android.widget.SeekBar)
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}

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
            if (!fromUser) return;

            Log.d(INPUT_METHOD_SERVICE, "Modification du volume: " + progress);
            TextView label = (TextView) ((LinearLayout) seekBar.getParent()).getChildAt(1);
            try {
                alsaMixer.updateControle(new MixerControl(label.getText().toString(), String.valueOf(progress)));
            } catch (IOException e) {
                Log.e("ControlActivity", e.getMessage(), e);
            }

        }
    };

    /**
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        networkInfo.isConnected();

        LinearLayout mixersLayout = (LinearLayout) findViewById(R.id.mixersLayout);

        AsyncTask<Object, Object, List<MixerControl>> task = alsaMixer.execute((Object) null);
        try {
            for (MixerControl mixer : task.get(10, TimeUnit.MINUTES)) {
                LinearLayout mixerLayout = new LinearLayout(this);
                mixerLayout.setOrientation(LinearLayout.VERTICAL);
                mixerLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                mixerLayout.setVisibility(View.VISIBLE);

                TextView mixerName = new TextView(this);
                mixerName.setText(mixer.getName());
                mixerName.setVisibility(View.VISIBLE);

                SeekBar seekBar = new SeekBar(this);
                seekBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                seekBar.setOnSeekBarChangeListener(onSeekBarChangeListener);
                seekBar.setVisibility(View.VISIBLE);
                seekBar.setRotation(270f);
                seekBar.setProgress(Integer.valueOf(mixer.getValue()));

                mixerLayout.addView(seekBar);
                mixerLayout.addView(mixerName);

                mixersLayout.addView(mixerLayout);

            }
        } catch (NumberFormatException e) {
            Log.e("ControlActivity", e.getMessage(), e);

        } catch (InterruptedException e) {
            Log.e("ControlActivity", e.getMessage(), e);

        } catch (ExecutionException e) {
            Log.e("ControlActivity", e.getMessage(), e);

        } catch (TimeoutException e) {
            Log.e("ControlActivity", e.getMessage(), e);

        }

    }

    /**
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_control, menu);
        return true;
    }

}
