package org.talend.daikon.async.json;

import java.io.IOException;
import java.io.StringWriter;

import org.talend.daikon.exception.TalendRuntimeException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TalendRuntimeExceptionSerializer extends JsonSerializer<TalendRuntimeException> {

    @Override
    public void serialize(TalendRuntimeException exception, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        final StringWriter writer = new StringWriter();
        exception.writeTo(writer);
        jsonGenerator.writeRawValue(writer.toString());
    }
}
