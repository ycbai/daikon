package org.talend.daikon.talend6;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
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

    private void checkEnforcerWithComponentRecordData(Talend6IncomingSchemaEnforcer enforcer) {
        // The enforcer must be ready to receive values.
        assertThat(enforcer.needsInitDynamicColumns(), is(false));

        // Put values into the enforcer and get them as an IndexedRecord.
        enforcer.put(0, 1);
        enforcer.put(1, "User");
        enforcer.put(2, 100);
        enforcer.put(3, true);
        enforcer.put(4, "Main Street");
        enforcer.put(5, "This is a record with six columns.");
        IndexedRecord adapted = enforcer.createIndexedRecord();

        // Ensure that the result is the same as the expected component record.
        assertThat(adapted, is(componentRecord));

        // Ensure that we create a new instance when we give it another value.
        enforcer.put("id", 2);
        enforcer.put("name", "User2");
        enforcer.put("age", 200);
        enforcer.put("valid", false);
        enforcer.put("address", "2 Main Street");
        enforcer.put("comment", "2 This is a record with six columns.");
        IndexedRecord adapted2 = enforcer.createIndexedRecord();

        // It should have the same schema, but not be the same instance.
        assertThat(adapted2.getSchema(), sameInstance(adapted.getSchema()));
        assertThat(adapted2, not(sameInstance(adapted)));
        assertThat(adapted2.get(0), is((Object) 2));
        assertThat(adapted2.get(1), is((Object) "User2"));
        assertThat(adapted2.get(2), is((Object) 200));
        assertThat(adapted2.get(3), is((Object) false));
        assertThat(adapted2.get(4), is((Object) "2 Main Street"));
        assertThat(adapted2.get(5), is((Object) "2 This is a record with six columns."));
    }

    @Test
    public void testNonDynamic() {
        // The design time schema should be the same as the runtime schema.
        Schema talend6Schema = componentRecord.getSchema();
        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // The enforcer is immediately usable
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.getRuntimeSchema(), is(talend6Schema));
        assertThat(enforcer.needsInitDynamicColumns(), is(false));
    }

    @Test
    public void testDynamicColumn_DynamicColumnAtStart() {
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("valid").type().booleanType().noDefault() //
                .name("address").type().stringType().noDefault() //
                .name("comment").type().stringType().noDefault() //
                .endRecord();
        talend6Schema = AvroUtils.setIncludeAllFields(talend6Schema, true);
        talend6Schema = AvroUtils.setProperty(talend6Schema, Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "0");

        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // The enforcer isn't usable yet.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        assertThat(enforcer.getRuntimeSchema(), nullValue());

        enforcer.initDynamicColumn("id", null, "id_Integer", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("name", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("age", null, "id_Integer", null, 0, 0, 0, null, null, false, false, null, null);
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        enforcer.initDynamicColumnsFinished();
        assertThat(enforcer.needsInitDynamicColumns(), is(false));

        // Check the run-time schema was created.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.getRuntimeSchema(), not(nullValue()));

        // Put values into the enforcer and get them as an IndexedRecord.
        checkEnforcerWithComponentRecordData(enforcer);
    }

    @Test
    public void testDynamicColumn_DynamicColumnAtMiddle() {
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("id").type().intType().noDefault() //
                .name("address").type().stringType().noDefault() //
                .name("comment").type().stringType().noDefault() //
                .endRecord();
        talend6Schema = AvroUtils.setIncludeAllFields(talend6Schema, true);
        talend6Schema = AvroUtils.setProperty(talend6Schema, Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "1");

        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // The enforcer isn't usable yet.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        assertThat(enforcer.getRuntimeSchema(), nullValue());

        enforcer.initDynamicColumn("name", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("age", null, "id_Integer", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("valid", null, "id_Boolean", null, 0, 0, 0, null, null, false, false, null, null);
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        enforcer.initDynamicColumnsFinished();
        assertThat(enforcer.needsInitDynamicColumns(), is(false));

        // Check the run-time schema was created.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.getRuntimeSchema(), not(nullValue()));

        // Put values into the enforcer and get them as an IndexedRecord.
        checkEnforcerWithComponentRecordData(enforcer);
    }

    @Test
    public void testDynamicColumn_DynamicColumnAtEnd() {
        // The expected schema after enforcement.
        Schema talend6Schema = SchemaBuilder.builder().record("Record").fields() //
                .name("id").type().intType().noDefault() //
                .name("name").type().stringType().noDefault() //
                .name("age").type().intType().noDefault() //
                .endRecord();
        talend6Schema = AvroUtils.setIncludeAllFields(talend6Schema, true);
        talend6Schema = AvroUtils.setProperty(talend6Schema, Talend6SchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "3");

        Talend6IncomingSchemaEnforcer enforcer = new Talend6IncomingSchemaEnforcer(talend6Schema);

        // The enforcer isn't usable yet.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        assertThat(enforcer.getRuntimeSchema(), nullValue());

        enforcer.initDynamicColumn("valid", null, "id_Boolean", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("address", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        enforcer.initDynamicColumn("comment", null, "id_String", null, 0, 0, 0, null, null, false, false, null, null);
        assertThat(enforcer.needsInitDynamicColumns(), is(true));
        enforcer.initDynamicColumnsFinished();
        assertThat(enforcer.needsInitDynamicColumns(), is(false));

        // Check the run-time schema was created.
        assertThat(enforcer.getDesignSchema(), is(talend6Schema));
        assertThat(enforcer.getRuntimeSchema(), not(nullValue()));

        // Put values into the enforcer and get them as an IndexedRecord.
        checkEnforcerWithComponentRecordData(enforcer);
    }
}
