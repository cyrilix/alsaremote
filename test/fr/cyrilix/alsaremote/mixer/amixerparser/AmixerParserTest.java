package fr.cyrilix.alsaremote.mixer.amixerparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.cyrilix.alsaremote.mixer.AmixerParser;

/**
 * Test de la classe {@link AmixerParser}
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AmixerParserTest {
    // private final static private final static Log LOGGER =
    // LogFactory.getLog(AmixerParserTest.class);

    private String amixerContent;

    /**
     * Constructeur par défaut
     */
    public AmixerParserTest() {}

    @Before
    public void setUp() throws IOException {
        BufferedReader contentReader = new BufferedReader(new InputStreamReader(new FileInputStream("amixer.txt"),
                "UTF-8"));

        StringBuilder content = new StringBuilder();
        String line = null;
        while ((line = contentReader.readLine()) != null)
            content.append(line).append('\n');

        amixerContent = content.toString();
    }

    /**
     * Test de la méthode {@link AmixerParser#parse(String)}
     */
    @Test
    public void testParse() {
        try {
            System.out.println("------ testParse -------");

            AmixerParser instance = new AmixerParser();
            instance.parse(amixerContent);

            System.out.print(instance.parse(amixerContent));

        } catch (Exception e) {
            System.err.println("Erreur imprévue: " + e.getMessage());
            e.printStackTrace();
            Assert.fail("Erreur imprévue: " + e.getMessage());
        }
    }
}
