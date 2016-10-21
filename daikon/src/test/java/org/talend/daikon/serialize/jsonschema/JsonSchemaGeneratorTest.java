package org.talend.daikon.serialize.jsonschema;

import org.junit.Test;

import static org.junit.Assert.*;

public class JsonSchemaGeneratorTest {

    @Test
    public void genSchema() throws Exception {
        String jsonStr = JsonUtilTest.readJson("FullExampleJsonSchema.json");
        FullExampleProperties properties = new FullExampleProperties("properties");
        properties.init();
        JsonSchemaGenerator generator = new JsonSchemaGenerator();
        assertEquals(jsonStr, generator.genSchema(properties).toString());
    }

}