package org.talend.daikon.avro;

/**
 * Constants to be used to augment the Avro schema.
 */
public class SchemaConstants {

    /**
     * If Schema.Type can't represent this java type, then use JAVA_CLASS_FLAG as the property key and the real class name as the value
     */
    public static final String JAVA_CLASS_FLAG = "java-class";

    /**
     * If a schema is used as an input specification, and the record includes this property (with ANY value), then the
     * actual schema return should be expanded to include all possible fields that the input component can find.
     */
    public static final String INCLUDE_ALL_FIELDS = "include-all-fields"; //$NON-NLS-1$

    public final static String TALEND_COLUMN_DB_TYPE = "talend.column.sourceType"; //$NON-NLS-1$

    public final static String TALEND_COLUMN_PATTERN = "talend.column.pattern"; //$NON-NLS-1$

    public final static String TALEND_COLUMN_DB_COLUMN_NAME = "talend.column.dbColumnName"; //$NON-NLS-1$

    /**
     * String representation of an int.
     */
    public final static String TALEND_COLUMN_DB_LENGTH = "talend.column.length"; //$NON-NLS-1$

    /**
     * String representation of an int.
     */
    public final static String TALEND_COLUMN_PRECISION = "talend.column.precision"; //$NON-NLS-1$

    public final static String TALEND_COLUMN_SCALE = "talend.column.scale"; //$NON-NLS-1$

    public final static String TALEND_COLUMN_DEFAULT = "talend.column.default"; //$NON-NLS-1$
}
