package fr.cyrilix.alsaremote.mixer;

/**
 * Control of alsa mixer
 * 
 * @author Cyrille Nofficial
 * 
 */
public class MixerControl {

    private String name;

    private String value = "0";

    private String maxValue = "100";

    /**
     * Constructor
     */
    public MixerControl() {}

    /**
     * Constructor
     * 
     * @param name control name
     * @param value current value
     * @param maxValue value
     */
    public MixerControl(String name, String value, String maxValue) {
        super();
        this.name = name;
        this.value = value;
        this.maxValue = maxValue;
    }

    /**
     * Return maximum value for the control
     * 
     * @return maximum value for the control
     */
    public String getMaxValue() {
        return this.maxValue;
    }

    /**
     * Define maximum value for the control
     * 
     * @param maxValue maximum value for the control
     */
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Return name of the control
     * 
     * @return name of the control
     */
    public String getName() {
        return this.name;
    }

    /**
     * Define name of the control
     * 
     * @param name name of the control
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return current value
     * 
     * @return current value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Define current value
     * 
     * @param value current value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MixerControl [name=" + this.name + ", value=" + this.value + "]";
    }

}
