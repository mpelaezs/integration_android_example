package pe.gob.reneic.pki.idaas.sdk.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
public enum Prompt {

    NONE("none"),
    LOGIN("login"),
    CONSENT("consent");

    private String value;
    private static final Map<String, Prompt> lookup = new HashMap<>();

    static {
        for (Prompt d : Prompt.values()) {
            lookup.put(d.getValue(), d);
        }
    }

    Prompt(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Prompt get(String value) {
        return lookup.get(value);
    }

}
