package pe.gob.reniec.idaas.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Miguel Pazo (http://miguelpazo.com)
 */
public class UrlQueryString {

    private static UrlQueryString __instance = null;
    private final String DEFAULT_ENCODING = "UTF-8";

    private UrlQueryString() {
    }

    public static UrlQueryString getInstance() {
        if (__instance == null) {
            __instance = new UrlQueryString();
        }

        return __instance;
    }

    public String buildQuery(final LinkedHashMap<String, Object> map) {
        try {
            final Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            final StringBuilder sb = new StringBuilder(map.size() * 8);

            while (it.hasNext()) {
                final Map.Entry<String, Object> entry = it.next();
                final String key = entry.getKey();

                if (key != null) {
                    sb.append(URLEncoder.encode(key, DEFAULT_ENCODING));
                    sb.append('=');
                    final Object value = entry.getValue();
                    final String valueAsString = value != null ? URLEncoder.encode(value.toString(), DEFAULT_ENCODING) : "";
                    sb.append(valueAsString);

                    if (it.hasNext()) {
                        sb.append('&');
                    }
                } else {
                    // Do what you want...for example:
                    assert false : String.format("Null key in query map: %s", map.entrySet());
                }
            }
            return sb.toString();
        } catch (final UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
