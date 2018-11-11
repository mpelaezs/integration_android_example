package pe.gob.reneic.pki.idaas.sdk.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
public enum Acr {

    ONE_FACTOR("one_factor"),
    TWO_FACTOR("two_factor"),
    PKI_DNIE("pki_dnie"),
    PKI_TOKEN("pki_token"),
    ONLY_PASSWORD("only_password"),
    ONLY_QUESTIONS("only_questions");

    private String value;
    private static final Map<String, Acr> lookup = new HashMap<>();

    static {
        for (Acr d : Acr.values()) {
            lookup.put(d.getValue(), d);
        }
    }

    Acr(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Acr get(String value) {
        return lookup.get(value);
    }

}
