package fr.cyrilix.alsaremote.mixer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser pour le résultat de la commande "amixer -i contents"
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AmixerParser {

    /**
     * Constructeur par défaut
     */
    public AmixerParser() {}

    public List<MixerControl> parse(String response) throws IOException {

        BufferedReader reader = new BufferedReader(new StringReader(response));

        String line = null;
        StringBuilder currentLine = null;
        List<String> entries = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {

            if (line.toLowerCase().startsWith("numid")) {
                if (currentLine != null) entries.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            currentLine.append(line);

        }
        entries.add(currentLine.toString());

        return convertEntriesToMixerControl(entries);
    }

    /**
     * @param entries
     * @return
     */
    private List<MixerControl> convertEntriesToMixerControl(List<String> entries) {
        Pattern pattern = Pattern
                .compile("numid=\\d+,iface=/*,name='(.+)' ?; ?type=(.+),access=rw------,values=(.*) : values=(.*)");

        List<MixerControl> mixerControls = new ArrayList<MixerControl>(entries.size());
        for (String entry : entries) {
            Matcher result = pattern.matcher(entry);
            String name = result.group(1);
            String type = result.group(2);
            String value = result.group(4);

            if ("INTEGER".equals(type)) {
                mixerControls.add(new MixerControl(name, value));
            }
        }
        return null;
    }
}
