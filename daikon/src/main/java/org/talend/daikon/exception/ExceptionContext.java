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
     * creates a context from a builder
     * @param builder
     */
    private ExceptionContext(ExceptionContextBuilder builder){
        context = builder.context;
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
     * Creates an ExceptionContext with a builder
     *
     * <pre><code>
     *     ExceptionContext.withBuilder().put("key1", value1).put("key2".value2).build();
     * </code></pre>
     *
     * @return the builder
     */
    public static ExceptionContextBuilder withBuilder(){
        return new ExceptionContextBuilder();
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

    /**
     * Exception Context Builder. Used with {@link ExceptionContext#withBuilder()}
     */
    public static class ExceptionContextBuilder {

        private Map<String, Object> context = new HashMap<>();

        /**
         * Adds a new key / value pair in the context
         * @param key
         * @param value
         * @return the builder
         */
        public ExceptionContextBuilder put(String key, Object value){
            context.put(key, value);
            return this;
        }

        /**
         * @return the ExceptionContext resulting from the different calls to this builder
         */
        public ExceptionContext build(){
            return new ExceptionContext(this);
        }
    }

}
