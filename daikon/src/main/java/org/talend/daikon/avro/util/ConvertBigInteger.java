package org.talend.daikon.avro.util;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.daikon.avro.AvroConverter;
import org.talend.daikon.avro.SchemaConstants;

import java.math.BigInteger;

public class ConvertBigInteger implements AvroConverter<BigInteger, String> {

    @Override
    public Schema getSchema() {
        //TODO is it ok to use string type rather than logical decimal type? logical type need two parameters, precision and scale
        return SchemaBuilder.builder().stringBuilder().prop(SchemaConstants.JAVA_CLASS_FLAG, getDatumClass().getCanonicalName()).endString();
    }

    @Override
    public Class<BigInteger> getDatumClass() {
        return BigInteger.class;
    }

    @Override
    public BigInteger convertToDatum(String value) {
        return value == null ? null : new BigInteger(value);
    }

    @Override
    public String convertToAvro(BigInteger value) {
        return value == null ? null : value.toString();
    }
}