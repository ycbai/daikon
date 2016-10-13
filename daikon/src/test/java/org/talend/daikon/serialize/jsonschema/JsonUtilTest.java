package org.talend.daikon.serialize.jsonschema;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Map;

import org.junit.Test;
import org.talend.daikon.properties.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtilTest {

    @Test
    public void test() throws URISyntaxException, IOException, ClassNotFoundException, NoSuchMethodException,
            InstantiationException, IllegalAccessException, InvocationTargetException {
        String jsonStr = readJson("FullExampleProperties.json").trim();

        Properties properties = JsonUtil.fromJson(jsonStr);
        properties.init();

        String jsonResult = JsonUtil.toJson(properties, true);
        assertEquals(jsonStr, jsonResult);
    }

    private static String readJson(String path) throws URISyntaxException, IOException {
        java.net.URL url = JsonUtilTest.class.getResource(path);
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        return new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
    }
}
