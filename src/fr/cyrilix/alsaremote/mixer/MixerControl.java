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

    /**
     * Constructeur par défaut
     */
    public MixerControl() {}

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

    public MixerControl(String name, String value) {
        super();
        this.name = name;
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
