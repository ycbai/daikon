package org.talend.daikon.serialize.jsonschema;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.talend.daikon.properties.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Util for round trip between ComponentProperties and JSONSchema/UISchema/JSONData
 */
public class JsonUtil {

    static final String TAG_JSON_SCHEMA = "jsonSchema";

    static final String TAG_JSON_UI = "uiSchema";

    static final String TAG_JSON_DATA = "properties";

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final JsonSchemaGenerator jsonSchemaGenerator = new JsonSchemaGenerator();

    private static final UiSchemaGenerator uiSchemaGenerator = new UiSchemaGenerator();

    private static final JsonDataGenerator jsonDataGenerator = new JsonDataGenerator();

    private static final JsonResolver resolver = new JsonResolver();

    public static Properties fromJson(String jsonStr) {
        try {
            JsonNode jsonNode = mapper.readTree(jsonStr);
            return fromJson(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Properties fromJson(InputStream jsonIS) {
        try {
            JsonNode jsonNode = mapper.readTree(jsonIS);
            return fromJson(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties fromJson(JsonNode jsonNode) throws NoSuchMethodException, IOException, InstantiationException,
            IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        JsonNode jsonSchema = jsonNode.get(TAG_JSON_SCHEMA);
        JsonNode jsonData = jsonNode.get(TAG_JSON_DATA);
        if (jsonSchema == null || jsonData == null) {
            throw new RuntimeException(TAG_JSON_SCHEMA + " or " + TAG_JSON_DATA + " should not be null");
        }
        Properties root = resolver.resolveJson((ObjectNode) jsonSchema, (ObjectNode) jsonData);
        return root;
    }

    public static String toJson(Properties cp, boolean hasWidget) {
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.set(TAG_JSON_SCHEMA, jsonSchemaGenerator.genSchema(cp));
        objectNode.set(TAG_JSON_DATA, jsonDataGenerator.genData(cp));
        if (hasWidget) {
            objectNode.set(TAG_JSON_UI, uiSchemaGenerator.genWidget(cp));
        }
        return objectNode.toString();
    }
}
