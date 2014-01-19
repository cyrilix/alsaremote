package fr.cyrilix.alsaremote.mixer;

import java.io.IOException;
import java.util.List;

/**
 * Interface to alsa controls
 * 
 * @author Cyrille Nofficial
 * 
 */
public interface AlsaMixer {

    /**
     * Return list of remote controls
     * 
     * @return list of remote controls
     * @throws IOException if connection to remote mixer failed
     */
    List<MixerControl> getControles() throws IOException;

    /**
     * Update value of sound control
     * 
     * @param mixerName name of the control
     * @param value value to set, between 0 and 100
     * 
     * @throws IOException if update failed
     */
    void updateControle(String mixerName, int value) throws IOException;

}