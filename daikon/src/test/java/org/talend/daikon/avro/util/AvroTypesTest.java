package org.talend.daikon.avro.util;

import org.apache.avro.Schema;
import org.junit.Test;
import org.talend.daikon.avro.SchemaConstants;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertTrue;

/**
 * Created by bchen on 16-3-30.
 */
public class AvroTypesTest {

    @Test
    public void _boolean() throws Exception {
        Schema type = AvroTypes._boolean();
        assertThat(type.getType(), is(Schema.Type.BOOLEAN));
    }

    @Test
    public void _byte() throws Exception {
        Schema type = AvroTypes._byte();
        assertThat(type.getType(), is(Schema.Type.INT));
        assertThat(type.getProp(SchemaConstants.JAVA_CLASS_FLAG), is(Byte.class.getCanonicalName()));
    }

    @Test
    public void _bytes() throws Exception {
        Schema type = AvroTypes._bytes();
        assertThat(type.getType(), is(Schema.Type.BYTES));
    }

    @Test
    public void _character() throws Exception {
        Schema type = AvroTypes._character();
        assertThat(type.getType(), is(Schema.Type.STRING));
        assertThat(type.getProp(SchemaConstants.JAVA_CLASS_FLAG), is(Character.class.getCanonicalName()));
    }

    @Test
    public void _date() throws Exception {
        Schema type = AvroTypes._date();
        assertThat(type.getType(), is(Schema.Type.LONG));
        assertThat(type.getProp(SchemaConstants.JAVA_CLASS_FLAG), is(Date.class.getCanonicalName()));
    }

    @Test
    public void _double() throws Exception {
        Schema type = AvroTypes._double();
        assertThat(type.getType(), is(Schema.Type.DOUBLE));
    }

    @Test
    public void _float() throws Exception {
        Schema type = AvroTypes._float();
        assertThat(type.getType(), is(Schema.Type.FLOAT));
    }

    @Test
    public void _int() throws Exception {
        Schema type = AvroTypes._int();
        assertThat(type.getType(), is(Schema.Type.INT));
    }

    @Test
    public void _long() throws Exception {
        Schema type = AvroTypes._long();
        assertThat(type.getType(), is(Schema.Type.LONG));
    }

    @Test
    public void _short() throws Exception {
        Schema type = AvroTypes._short();
        assertThat(type.getType(), is(Schema.Type.INT));
        assertThat(type.getProp(SchemaConstants.JAVA_CLASS_FLAG), is(Short.class.getCanonicalName()));
    }

    @Test
    public void _string() throws Exception {
        Schema type = AvroTypes._string();
        assertThat(type.getType(), is(Schema.Type.STRING));
    }

    @Test
    public void _decimal() throws Exception {
        Schema type = AvroTypes._decimal();
        assertThat(type.getType(), is(Schema.Type.STRING));
        assertThat(type.getProp(SchemaConstants.JAVA_CLASS_FLAG), is(BigDecimal.class.getCanonicalName()));
    }

    @Test
    public void isSameType() throws Exception {
        assertTrue(AvroTypes.isSameType(AvroTypes._string(), AvroTypes._string()));
        assertTrue(!AvroTypes.isSameType(AvroTypes._string(), AvroTypes._character()));
    }
}