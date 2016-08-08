package org.talend.daikon.avro;

/**
 * Constants and enums used to augment avro schema for EDI representations.
 * <p/>
 * Used when Avro schema represents an EDI document.
 *
 */
public final class SchemaEDIConstants {

    /**
     * This utility class is best used as a static import.
     */
    private SchemaEDIConstants() {
    }

    /**
     * EDI element type. @link(EDIElementType) for possible values.
     * <p/>
     * Avro schema type level.
     * <p/>
     * Optional. When omitted, defaults to ELEMENT.
     */
    public static final String EDI_ELEM_TYPE_PROPERTY = "talend.edi.element.type";

    /**
     * In EDI, a reference to the corresponding specifications.
     * <p/>
     * Avro schema type level property for "type": "record" and "type": "enum".
     * <p/>
     * Optional.
     */
    public static final String EDI_ELEM_REF_PROPERTY = "talend.edi.element.ref";

    /**
     * An EDI type for an element. These match classical EDI types.
     */
    public enum EDIElementType {
        TRANSACTION,
        SEGMENT,
        COMPOSITE,
        LOOP,
        ELEMENT
    };

}
