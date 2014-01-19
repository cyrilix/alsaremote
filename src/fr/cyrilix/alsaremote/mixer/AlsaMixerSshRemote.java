package fr.cyrilix.alsaremote.mixer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import fr.cyrilix.alsaremote.remote.SshRemote;
import fr.cyrilix.alsaremote.tools.AsyncTaskResult;

/**
 * Remote alsa mixer based on ssh
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AlsaMixerSshRemote implements AlsaMixer {

    private final AmixerParser amixerParser = new AmixerParser();
    private final SharedPreferences preferences;

    /**
     * Constructor
     * 
     * @param context application context
     */
    public AlsaMixerSshRemote(Context context) {
        super();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * @throws IOException
     * 
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#getControles()
     */
    @Override
    public List<MixerControl> getControles() throws IOException {

        String cmd = "amixer contents";
        return amixerParser.parse(runCommand(cmd));
    }

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#updateControle(java.lang.String,
     *      int)
     */
    @Override
    public void updateControle(String mixerName, int value) throws IOException {

        String cmd = "amixer cset name='" + mixerName + "' " + value;
        runCommand(cmd);
    }

    private String runCommand(String cmd) throws IOException {
        AsyncTask<String, Object, AsyncTaskResult<String>> sshRemote = new SshRemote(preferences);
        AsyncTask<String, Object, AsyncTaskResult<String>> task = sshRemote.execute(cmd);
        AsyncTaskResult<String> result = null;
        try {
            result = task.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e("AlsaMixer", e.getMessage(), e);
            throw new IOException(e.getMessage(), e);
        }
        if (result.hasError()) {
            Exception e = result.getError();
            throw new IOException(e.getMessage(), e);
        }

        return result.getResult();
    }
}
