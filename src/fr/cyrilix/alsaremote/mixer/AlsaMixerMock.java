package fr.cyrilix.alsaremote.mixer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

/**
 * Mock implementation of {@link AlsaMixer}
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AlsaMixerMock implements AlsaMixer {

    /**
     * Constructor
     */
    public AlsaMixerMock() {}

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#getControles()
     */
    @Override
    public List<MixerControl> getControles() {
        return Arrays.asList(new MixerControl("Master", "50", "100"), new MixerControl("Front", "75", "100"),
                new MixerControl("Rear", "25", "100"));
    }

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#updateControle(java.lang.String,
     *      int)
     */
    @Override
    public void updateControle(String mixerName, int value) throws IOException {
        Log.d("Mock", "Mise à jour du controle: " + mixerName);
    }
}
