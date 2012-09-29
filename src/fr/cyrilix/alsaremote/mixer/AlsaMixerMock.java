package fr.cyrilix.alsaremote.mixer;

import java.util.Arrays;
import java.util.List;

import android.util.Log;

/**
 * @author Cyrille Nofficial
 * 
 */
public class AlsaMixerMock {

    /**
     * Constructeur par défaut
     */
    public AlsaMixerMock() {}

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#getControles()
     */
    public List<MixerControl> getControles() {
        return Arrays.asList(new MixerControl("Master", "50"), new MixerControl("Front", "75"), new MixerControl(
                "Rear", "25"));
    }

    /**
     * @see fr.cyrilix.alsaremote.mixer.AlsaMixer#updateControle(fr.cyrilix.alsaremote.mixer.MixerControl)
     */
    public void updateControle(MixerControl mixerControl) {
        Log.d("Mock", "Mise à jour du controle: " + mixerControl);
    }
}
