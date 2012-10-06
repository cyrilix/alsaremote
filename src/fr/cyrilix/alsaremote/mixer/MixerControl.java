package fr.cyrilix.alsaremote.mixer;

/**
 * Contrôle d'un mixer alsa
 * 
 * @author Cyrille Nofficial
 * 
 */
public class MixerControl {

    private String name;

    private String value = "0";

    private String maxValue = "100";

    /**
     * Constructeur par défaut
     */
    public MixerControl() {}

    /**
     * @param name
     * @param value
     */
    public MixerControl(String name, String value, String maxValue) {
        super();
        this.name = name;
        this.value = value;
        this.maxValue = maxValue;
    }

    /**
     * Renvoie la valeur maximale autorisée sur le contrôle
     * 
     * @return la valeur maximale autorisée sur le contrôle
     */
    public String getMaxValue() {
        return this.maxValue;
    }

    /**
     * Définit la valeur maximale autorisée sur le contrôle
     * 
     * @param maxValue la valeur maximale autorisée sur le contrôle
     */
    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Renvoie le name
     * 
     * @return le name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Définit le name
     * 
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Renvoie le value
     * 
     * @return le value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Définit le value
     * 
     * @param value value
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
