package org.talend.daikon.avro.util;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.daikon.avro.AvroConverter;
import org.talend.daikon.avro.SchemaConstants;

import java.util.UUID;

public class ConvertUUID implements AvroConverter<UUID, String> {

    @Override
    public Schema getSchema() {
        return SchemaBuilder.builder().stringBuilder().prop(SchemaConstants.JAVA_CLASS_FLAG, getDatumClass().getCanonicalName())
                .endString();
    }

    @Override
    public Class<UUID> getDatumClass() {
        return UUID.class;
    }

    @Override
    public UUID convertToDatum(String value) {
        return value == null ? null : UUID.fromString(value);
    }

    @Override
    public String convertToAvro(UUID value) {
        return value == null ? null : value.toString();
    }
}