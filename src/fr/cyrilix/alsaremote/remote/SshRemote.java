package fr.cyrilix.alsaremote.remote;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

/**
 * Mixer alsa distant basé sur ssh
 * 
 * @author Cyrille Nofficial
 * 
 */
public class SshRemote extends AsyncTask<String, Object, String> {

    private final SharedPreferences sharedPreferences;

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
     * Constructeur par défaut
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
                            + sharedPreferences.getString("privateKey", ""),
                    sharedPreferences.getString("privateKeyPassphrase", ""));
            session = jsch.getSession(sharedPreferences.getString("user", ""),
                    sharedPreferences.getString("hostname", ""), 22);

            // } else {
            session.setPassword(sharedPreferences.getString("mdp", ""));
            // }

            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setInputStream(null);

            try {
                channel.setCommand(cmd);
                channel.run();

                Log.d("SSH", "Commande exécutée");

                InputStream inputStream = channel.getInputStream();
                channel.connect();
                // String result = inputStreamToString(channel.getInputStream(),
                // "UTF-8");
                // Log.d("SSH", "Résultat de la commande ssh: " + result);
                return inputStreamToString(inputStream, "UTF-8");
            } finally {

                try {

                    // Log.e("SSH", inputStreamToString(channel.getErrStream(),
                    // "UTF-8"));
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

    /**
     * @param string
     * @return
     * @throws IOException
     */
    private byte[] loadPrivateKey(String string) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream keyFile = new FileInputStream(string);
        int bite = -1;
        while ((bite = keyFile.read()) != -1)
            byteArrayOutputStream.write(bite);

        keyFile.close();
        return byteArrayOutputStream.toByteArray();
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
    protected String doInBackground(String... params) {
        try {
            return runCmd(params[0]);
        } catch (Exception e) {
            Log.e("SSH", e.getMessage(), e);
            return "";
        }
    }
}
