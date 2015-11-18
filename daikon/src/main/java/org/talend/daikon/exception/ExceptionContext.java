package org.talend.daikon.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Exception context holds the exception context, e.g. additional information about TDPException when they occur.
 */
public class ExceptionContext implements Serializable {

    private static final long serialVersionUID = -8905451634883948364L;

    /** The internal context. */
    private Map<String, Object> context;

    /**
     * private constructor to ensure the build method use.
     */
    private ExceptionContext() {
        context = new HashMap<>();
    }

    /**
     * Put the given key/value into this context.
     * 
     *
     * @param key the key entry.
     * @param value the value entry.
     * @return the context itself so that 'put' calls can be chained.
     */
    public ExceptionContext put(String key, Object value) {
        context.put(key, value);
        return this;
    }

    /**
     * @return a fresh new context.
     */
    public static ExceptionContext build() {
        return new ExceptionContext();
    }

    /**
     * @return wrapper for the Map.entrySet method so that one can iterate over this context entries.
     * @see Map#entrySet()
     */
    public Iterable<Map.Entry<String, Object>> entries() {
        return context.entrySet();
    }

    /**
     * @param key the key to check.
     * @return true if this context contains the given key.
     */
    public boolean contains(String key) {
        return context.containsKey(key);
    }

    public ExceptionContext from(Map<String, Object> context) {
        if (context != null) {
            this.context.putAll(context);
        }
        return this;
    }

    @Override
    public String toString() {
        return context != null ? context.toString() : super.toString();

    }
}
