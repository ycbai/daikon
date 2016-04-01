package org.talend.daikon.avro.util;

import org.apache.avro.Schema;
import org.talend.daikon.avro.AvroConverter;
import org.talend.daikon.avro.SchemaConstants;

public class AvroTypes {

    public static Schema _boolean() {
        return Schema.create(Schema.Type.BOOLEAN);
    }

    private static final AvroConverter BYTE_TYPE = new ConvertByte();

    public static Schema _byte() {
        return BYTE_TYPE.getSchema();
    }

    public static Schema _bytes() {
        return Schema.create(Schema.Type.BYTES);
    }

    private static final AvroConverter CHARACTER_TYPE = new ConvertCharacter();

    public static Schema _character() {
        return CHARACTER_TYPE.getSchema();
    }

    private static final AvroConverter DATE_TYPE = new ConvertDate();

    public static Schema _date() {
        return DATE_TYPE.getSchema();
    }

    public static Schema _double() {
        return Schema.create(Schema.Type.DOUBLE);
    }

    public static Schema _float() {
        return Schema.create(Schema.Type.FLOAT);
    }

    public static Schema _int() {
        return Schema.create(Schema.Type.INT);
    }

    public static Schema _long() {
        return Schema.create(Schema.Type.LONG);
    }

    private static final AvroConverter SHORT_TYPE = new ConvertShort();

    public static Schema _short() {
        return SHORT_TYPE.getSchema();
    }

    public static Schema _string() {
        return Schema.create(Schema.Type.STRING);
    }

    private static final AvroConverter DECIMAL_TYPE = new ConvertBigDecimal();

    public static Schema _decimal() {
        return DECIMAL_TYPE.getSchema();
    }

    public static boolean isSameType(Schema actual, Schema expect) {
        String expectJavaClass = expect.getProp(SchemaConstants.JAVA_CLASS_FLAG);
        String actualJavaClass = actual.getProp(SchemaConstants.JAVA_CLASS_FLAG);
        if(actual.getType() == expect.getType()) {
            if (actualJavaClass == null) {
                if(expectJavaClass == null) {
                    return true;
                }
            }else{
                if(actualJavaClass.equals(expectJavaClass)){
                    return true;
                }
            }
        }
        return false;
    }
}
