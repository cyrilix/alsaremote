package fr.cyrilix.alsaremote.mixer.amixerparser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.cyrilix.alsaremote.mixer.AmixerParser;

/**
 * Test class {@link AmixerParser}
 * 
 * @author Cyrille Nofficial
 * 
 */
public class AmixerParserTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(AmixerParserTest.class);

    private String amixerContent;

    /**
     * Default constructor
     */
    public AmixerParserTest() {}

    /**
     * Prepare test data
     * 
     * @throws IOException
     */
    @Before
    public void setUp() throws IOException {
        BufferedReader contentReader = new BufferedReader(new InputStreamReader(
                new FileInputStream("./test/amixer.txt"), "UTF-8"));
        try {
            StringBuilder content = new StringBuilder();
            String line = null;
            while ((line = contentReader.readLine()) != null)
                content.append(line).append('\n');

            amixerContent = content.toString();
        } finally {
            contentReader.close();
        }
    }

    /**
     * Test method {@link AmixerParser#parse(String)}
     */
    @Test
    public void testParse() {
        try {
            LOGGER.info("------ testParse -------");

            AmixerParser instance = new AmixerParser();
            instance.parse(amixerContent);

            LOGGER.info(instance.parse(amixerContent).toString());

        } catch (Exception e) {
            LOGGER.error("Unexpected error: {}", e.getMessage(), e);
            Assert.fail("Unexpected error:: " + e.getMessage());
        }
    }
}
