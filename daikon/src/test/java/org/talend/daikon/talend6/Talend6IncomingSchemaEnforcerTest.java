package org.talend.daikon.talend6;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.talend.daikon.avro.util.AvroUtils;

/**
 * Unit tests for {Talend6SchemaOutputEnforcer}.
 */
@SuppressWarnings("nls")
public class Talend6IncomingSchemaEnforcerTest {

    /**
     * An actual record that a component would like to be emitted, which may or may not contain enriched schema
     * information.
     */
    private IndexedRecord componentRecord;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        Schema componentSchema = SchemaBuilder.builder().record("Record").fields() //
                .name("id").type().intType().noDefault() //
                .name("name").type().stringType().noDefault() //
                .name("age").type().intType().noDefault() //
                .name("valid").type().booleanType().noDefault() //
                .name("address").type().stringType().noDefault() //
                .name("comment").type().stringType().noDefault() //
                .endRecord();
        componentRecord = new GenericData.Record(componentSchema);
        componentRecord.put(0, 1);
        componentRecord.put(1, "User");
        componentRecord.put(2, 100);
        componentRecord.put(3, true);
        componentRecord.put(4, "Main Street");
        componentRecord.put(5, "This is a record with six columns.");
    }

    @Test
    public void testDynamicColumn_DynamicColumnAtStart() {
        // The design time schema after enforcement.
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("out1").type().bytesType().noDefault() //
                .name("out2").type().stringType().noDefault() //
                .name("out3").type().stringType().noDefault() //
                .endRecord();
        talend6Schema = AvroUtils.setIncludeAllFields(talend6Schema, true);
        talend6Schema = AvroUtils.setProperty(talend6Schema, Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "0");

        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // The enforcer isn't usable yet.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        assertThat(enforcer.getSchema(), nullValue());

        enforcer.initDynamicColumn("id", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("name", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("age", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("valid", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        enforcer.initDynamicColumnsFinished();
        assertThat(enforcer.needsInitDynamicColumns(), is(false));

        // Check the run-time schema
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.getSchema(), not(nullValue()));

        // TODO: other than string... other than non-nullable.

        Schema incomingDynamicRuntimeSchema = enforcer.getSchema();
        assertThat(incomingDynamicRuntimeSchema.getFields().size(), is(6));
        assertThat(incomingDynamicRuntimeSchema.getField("id").schema().getType(), is(Schema.Type.STRING));
        assertThat(incomingDynamicRuntimeSchema.getField("name").schema().getType(), is(Schema.Type.STRING));
        assertThat(incomingDynamicRuntimeSchema.getField("age").schema().getType(), is(Schema.Type.STRING));
        assertThat(incomingDynamicRuntimeSchema.getField("valid").schema().getType(), is(Schema.Type.STRING));

        // TODO: check copied properties
    }

    @Test
    public void testDynamicColumn_DynamicColumnAtMiddle() {
        // The expected schema after enforcement.
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("out1").type().intType().noDefault() //
                .name("out2").type().bytesType().noDefault() //
                .name("out3").type().stringType().noDefault() //
                .endRecord();
        talend6Schema = AvroUtils.setIncludeAllFields(talend6Schema, true);
        talend6Schema = AvroUtils.setProperty(talend6Schema, Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "1");

        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // TODO
    }

    @Test
    public void testDynamicColumn_DynamicColumnAtEnd() {
        // The expected schema after enforcement.
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("out1").type().intType().noDefault() //
                .name("out2").type().stringType().noDefault() //
                .name("out3").type().bytesType().noDefault() //
                .endRecord();
        talend6Schema = AvroUtils.setIncludeAllFields(talend6Schema, true);
        talend6Schema = AvroUtils.setProperty(talend6Schema, Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "3");

        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // TODO
    }

    @Test
    public void testDynamicColumn_getOutOfBounds() {
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("id").type().intType().noDefault() //
                .endRecord();
        Talend6OutgoingSchemaEnforcer enforcer = new Talend6OutgoingSchemaEnforcer(talend6Schema, false);
        enforcer.setWrapped(componentRecord);

        assertThat(enforcer.get(0), is((Object) 1));

        thrown.expect(ArrayIndexOutOfBoundsException.class);
        enforcer.get(1); // Only one field available.
    }
}
