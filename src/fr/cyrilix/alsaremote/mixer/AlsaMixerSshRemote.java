package fr.cyrilix.alsaremote.mixer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import net.schmizz.sshj.AndroidConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;
import net.schmizz.sshj.userauth.method.AuthKeyboardInteractive;
import net.schmizz.sshj.userauth.method.AuthMethod;
import net.schmizz.sshj.userauth.method.PasswordResponseProvider;
import net.schmizz.sshj.userauth.password.PasswordFinder;
import net.schmizz.sshj.userauth.password.Resource;
import android.os.AsyncTask;
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
public class AlsaMixerSshRemote extends AsyncTask<Object, Object, List<MixerControl>> implements AlsaMixer {

    private SSHClient sshClient;

    private final AmixerParser amixerParser = new AmixerParser();

    /**
     * Constructeur par défaut
     */
    public AlsaMixerSshRemote() {}

    /**
     * @throws IOException
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#getControles()
     */
    @Override
    public List<MixerControl> getControles() throws IOException {
        return amixerParser.parse(runCmd("amixer contents"));
    }

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#updateControle(fr.cyrilix.alsaremote.mixer.MixerControl)
     */
    @Override
    public void updateControle(MixerControl mixerControl) {
        // @todo à écrire

    }

    private String runCmd(String cmd) throws IOException {
        JSch jsch = new JSch();
        com.jcraft.jsch.Session session;
        jsch.setConfig("StrictHostKeyChecking", "no");

        try {
            session = jsch.getSession("cyrille", "192.168.0.14", 22);

            session.setPassword("");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            try {
                channel.setCommand(cmd);
                channel.run();

                Log.d("SSH", "Commande exécutée");

                String result = inputStreamToString(channel.getInputStream(), "UTF-8");
                Log.d("SSH", "Résultat de la commande ssh: " + result);
                return result;
            } finally {
                if (channel != null) Log.e("SSH", inputStreamToString(channel.getErrStream(), "UTF-8"));

                session.disconnect();

            }
        } catch (JSchException e) {
            Log.e("SSH", e.getMessage(), e);
            throw new IOException(e.getMessage());

        }
    }

    private String runCmd2(String cmd) throws IOException {

        PasswordResponseProvider passwordResponseProvider = new PasswordResponseProvider(new PasswordFinder() {

            @Override
            public boolean shouldRetry(Resource<?> arg0) {

                return false;
            }

            @Override
            public char[] reqPassword(Resource<?> arg0) {
                Log.d("SSH", "Saisie du mot de passe");
                return "yac'h!mat56".toCharArray();
            }
        });
        AuthMethod authMethod = new AuthKeyboardInteractive(passwordResponseProvider);

        sshClient = new SSHClient(new AndroidConfig());

        sshClient.addHostKeyVerifier(new HostKeyVerifier() {
            @Override
            public boolean verify(String arg0, int arg1, PublicKey arg2) {
                Log.d("SSH", "Vérification de la clé: " + arg2);
                return true;
            }
        });
        Log.d("SSH", "Tentative de connexion ssh");
        sshClient.connect(InetAddress.getByName("192.168.0.14"));
        Log.d("SSH", "Authentification ssh");
        sshClient.auth("cyrille", authMethod);
        Log.d("SSH", "Authentification ssh: " + sshClient.isAuthenticated());

        try {

            Log.d("SSH", "Ouverture de la session ssh");
            Session session = sshClient.startSession();
            Session.Command cmdSsh = null;
            try {
                cmdSsh = session.exec(cmd);
                Log.d("SSH", "Commande exécutée");
                cmdSsh.join(2, TimeUnit.SECONDS);
                // BufferedWriter term = new BufferedWriter(new
                // OutputStreamWriter(cmdSsh.getOutputStream()));
                // term.write(cmd);
                // term.newLine();
                // term.flush();
                cmdSsh.sendEOF();

                return inputStreamToString(cmdSsh.getInputStream(), "UTF-8");

            } finally {
                if (cmdSsh != null) Log.e("SSH", inputStreamToString(cmdSsh.getErrorStream(), "UTF-8"));

                session.close();

            }
        } finally {
            sshClient.disconnect();
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
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected List<MixerControl> doInBackground(Object... params) {
        try {
            return getControles();
        } catch (IOException e) {
            Log.e("ControlActivity", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
