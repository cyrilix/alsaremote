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

/**
 * Mixer alsa distant basé sur ssh
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AlsaMixerSshRemote implements AlsaMixer {

    private final AmixerParser amixerParser = new AmixerParser();
    private final SharedPreferences preferences;
    private final Context context;

    /**
     * Constructeur par défaut
     */
    public AlsaMixerSshRemote(Context context) {
        super();
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * @throws IOException
     * 
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#getControles()
     */
    @Override
    public List<MixerControl> getControles() throws IOException {
        AsyncTask<String, Object, String> sshRemote = new SshRemote(preferences);
        AsyncTask<String, Object, String> task = sshRemote.execute("amixer contents");
        try {
            return amixerParser.parse(task.get(3, TimeUnit.SECONDS));
        } catch (Exception e) {
            Log.e("AlsaMixer", e.getMessage(), e);
            throw new IOException(e.getMessage(), e);

        }
    }

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#updateControle(java.lang.String,
     *      int)
     */
    @Override
    public void updateControle(String mixerName, int value) throws IOException {
        AsyncTask<String, Object, String> sshRemote = new SshRemote(preferences);
        AsyncTask<String, Object, String> task = sshRemote.execute("amixer cset name='" + mixerName + "' " + value);
        try {
            task.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e("AlsaMixer", e.getMessage(), e);
            throw new IOException(e.getMessage(), e);

        }

    }
}
