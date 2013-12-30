package fr.cyrilix.alsaremote.mixer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser pour le résultat de la commande "amixer -i contents"
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AmixerParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AmixerParser.class);

    /**
     * Constructeur par défaut
     */
    public AmixerParser() {}

    public List<MixerControl> parse(String response) throws IOException {
        if (response == null) return Collections.emptyList();

        BufferedReader reader = new BufferedReader(new StringReader(response));

        String line = null;
        StringBuilder currentLine = null;
        List<String> entries = new ArrayList<String>();
        while ((line = reader.readLine()) != null) {

            if (line.toLowerCase(Locale.US).startsWith("numid")) {
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
                .compile("numid=\\d+,iface=.*,name='(.+)' *; ?type=(.+),access=.*,values=(\\d*).*max=(\\d+).*: values=(\\d*).*");

        List<MixerControl> mixerControls = new ArrayList<MixerControl>(entries.size());
        for (String entry : entries) {
            LOGGER.debug("AmixerParser", "Parsing de la ligne: {}", entry);
            Matcher result = pattern.matcher(entry);
            if (!result.find()) continue;

            String name = result.group(1);
            String type = result.group(2);
            int nbCanaux = Integer.parseInt(result.group(3));
            String maxValue = result.group(4);
            String value = result.group(5);

            if ("INTEGER".equals(type)) {
                if (nbCanaux > 1) {
                    value = value.split(",")[0];
                }
                mixerControls.add(new MixerControl(name, value, maxValue));
            }
        }
        LOGGER.debug("AmixerParser", "Mixers: {}", mixerControls);
        return mixerControls;
    }
}
