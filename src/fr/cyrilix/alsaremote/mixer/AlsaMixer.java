package fr.cyrilix.alsaremote.mixer;

import java.io.IOException;
import java.util.List;

/**
 * @author Cyrille Nofficial
 * 
 */
public interface AlsaMixer {

    List<MixerControl> getControles() throws IOException;

    void updateControle(MixerControl mixerControl) throws IOException;

}