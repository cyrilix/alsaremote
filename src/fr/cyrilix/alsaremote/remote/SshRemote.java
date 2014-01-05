package fr.cyrilix.alsaremote.remote;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

import fr.cyrilix.alsaremote.main.SetPreferenceActivity;
import fr.cyrilix.alsaremote.tools.AsyncTaskResult;

/**
 * Interface to run command over ssh
 * 
 * @author Cyrille Nofficial
 * 
 */
public class SshRemote extends AsyncTask<String, Object, AsyncTaskResult<String>> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SshRemote.class);
    private final SharedPreferences sharedPreferences;

    /**
     * SSH logger
     * 
     * @author Cyrille Nofficial
     * 
     */
    public static class Logger implements com.jcraft.jsch.Logger {

        /**
         * @see com.jcraft.jsch.Logger#isEnabled(int)
         */
        @Override
        public boolean isEnabled(int level) {
            return true;
        }

        /**
         * @see com.jcraft.jsch.Logger#log(int, java.lang.String)
         */
        @Override
        public void log(int level, String message) {
            Log.d("SSH", message);
        }
    }

    /**
     * Constructor
     * 
     * @param sharedPreferences
     */
    public SshRemote(SharedPreferences sharedPreferences) {
        super();
        this.sharedPreferences = sharedPreferences;
        JSch.setLogger(new Logger());
    }

    private String runCmd(String cmd) throws IOException {
        JSch jsch = new JSch();

        com.jcraft.jsch.Session session;
        JSch.setConfig("StrictHostKeyChecking", "no");

        try {
            jsch.addIdentity(
                    Environment.getExternalStorageDirectory() + File.separator
                            + sharedPreferences.getString(SetPreferenceActivity.PRIVATE_KEY, ""),
                    sharedPreferences.getString(SetPreferenceActivity.PRIVATE_KEY_PASSPHRASE, ""));
            session = jsch.getSession(sharedPreferences.getString(SetPreferenceActivity.USER, ""),
                    sharedPreferences.getString(SetPreferenceActivity.HOSTNAME, ""),
                    Integer.parseInt(sharedPreferences.getString(SetPreferenceActivity.PORT, "22")));
            session.setPassword(sharedPreferences.getString(SetPreferenceActivity.PASSWORD, ""));

            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setInputStream(null);

            try {
                channel.setCommand(cmd);
                channel.run();

                Log.d("SSH", "Commande exécutée");

                InputStream inputStream = channel.getInputStream();
                channel.connect();
                return inputStreamToString(inputStream, "UTF-8");
            } finally {

                try {
                    channel.disconnect();

                } finally {
                    session.disconnect();
                }
            }

        } catch (JSchException e) {
            Log.e("SSH", e.getMessage(), e);
            throw new IOException(e.getMessage());

        }
    }

    private String inputStreamToString(InputStream inputStream, String encoding) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding));
        try {
            StringBuilder content = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null)
                content.append(line).append('\n');

            return content.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * @see android.os.AsyncTask#doInBackground(String[])
     */
    @Override
    protected AsyncTaskResult<String> doInBackground(String... params) {
        try {
            return new AsyncTaskResult<String>(runCmd(params[0]));
        } catch (Exception e) {
            LOGGER.error(MarkerFactory.getMarker("SSH"), e.getMessage(), e);
            return new AsyncTaskResult<String>(e);
        }
    }
}
