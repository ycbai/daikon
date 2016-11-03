package org.talend.daikon.di.benchmark;

import java.util.concurrent.TimeUnit;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.di.DiOutgoingSchemaEnforcer;
import org.talend.daikon.di.DiSchemaConstants;

/**
 * Benchmarks for {@link DiOutgoingSchemaEnforcer} class
 */
public class DiOutgoingSchemaEnforcerBenchmark {
    
    /**
     * Parent class for different Enforcer statesS
     */
    @State(Scope.Thread)
    public static abstract class EnforcerState {
        
        /**
         * Instance of {@link DiOutgoingSchemaEnforcer} for benchmarks
         */
        DiOutgoingSchemaEnforcer enforcer;
        
        public abstract void initializeEnforcer();
    }
    
    /**
     * {@link State} class for Enforcer without dynamic columns in Schema
     * and by index field matching mode
     */
    public static class WithoutDynamicByIndexEnforcerState extends EnforcerState {

        /**
         * Initializes Enforcer with Schema, which doesn't contain dynamic columns.
         * This method is called only once for benchmark method. See {@link Level#Trial}
         */
        @Setup(Level.Trial)
        @Override
        public void initializeEnforcer() {
            Schema nonDynamicDesignSchema = SchemaBuilder.builder().record("designNotDynamic").fields()
                    .name("col0").type().stringType().noDefault()
                    .name("col1").type().stringType().noDefault()
                    .name("col2").type().stringType().noDefault()
                    .name("col3").type().stringType().noDefault()
                    .name("col4").type().stringType().noDefault()
                    .name("col5").type().stringType().noDefault()
                    .name("col6").type().stringType().noDefault()
                    .name("col7").type().stringType().noDefault()
                    .name("col8").type().stringType().noDefault()
                    .name("col9").type().stringType().noDefault()
                    .endRecord();
            
            IndexedRecord nonDynamicRecord = new GenericData.Record(nonDynamicDesignSchema);
            nonDynamicRecord.put(0, "value0");
            nonDynamicRecord.put(1, "value1");
            nonDynamicRecord.put(2, "value2");
            nonDynamicRecord.put(3, "value3");
            nonDynamicRecord.put(4, "value4");
            nonDynamicRecord.put(5, "value5");
            nonDynamicRecord.put(6, "value6");
            nonDynamicRecord.put(7, "value7");
            nonDynamicRecord.put(8, "value8");
            nonDynamicRecord.put(9, "value9");
            
            enforcer = new DiOutgoingSchemaEnforcer(nonDynamicDesignSchema, true);
            enforcer.setWrapped(nonDynamicRecord);
        }
    }
    
    /**
     * {@link State} class for Enforcer without dynamic columns in Schema
     * and by name field matching mode
     */
    public static class WithoutDynamicByNameEnforcerState extends EnforcerState {

        /**
         * Initializes Enforcer with Schema, which doesn't contain dynamic columns.
         * This method is called only once for benchmark method. See {@link Level#Trial}
         */
        @Setup(Level.Trial)
        @Override
        public void initializeEnforcer() {
            Schema nonDynamicDesignSchema = SchemaBuilder.builder().record("designNotDynamic").fields()
                    .name("col0").type().stringType().noDefault()
                    .name("col1").type().stringType().noDefault()
                    .name("col2").type().stringType().noDefault()
                    .name("col3").type().stringType().noDefault()
                    .name("col4").type().stringType().noDefault()
                    .name("col5").type().stringType().noDefault()
                    .name("col6").type().stringType().noDefault()
                    .name("col7").type().stringType().noDefault()
                    .name("col8").type().stringType().noDefault()
                    .name("col9").type().stringType().noDefault()
                    .endRecord();
            
            IndexedRecord nonDynamicRecord = new GenericData.Record(nonDynamicDesignSchema);
            nonDynamicRecord.put(0, "value0");
            nonDynamicRecord.put(1, "value1");
            nonDynamicRecord.put(2, "value2");
            nonDynamicRecord.put(3, "value3");
            nonDynamicRecord.put(4, "value4");
            nonDynamicRecord.put(5, "value5");
            nonDynamicRecord.put(6, "value6");
            nonDynamicRecord.put(7, "value7");
            nonDynamicRecord.put(8, "value8");
            nonDynamicRecord.put(9, "value9");
            
            enforcer = new DiOutgoingSchemaEnforcer(nonDynamicDesignSchema, false);
            enforcer.setWrapped(nonDynamicRecord);
        }
    }
    
    /**
     * {@link State} class for Enforcer with dynamic columns in Schema and ByIndex mode
     */
    public static class DynamicByIndexEnforcerState extends EnforcerState {

        /**
         * Initializes Enforcer with Schema, which contains dynamic columns.
         * Also ByIndex mode is used
         * This method is called only once for benchmark method. See {@link Level#Trial}
         */
        @Setup(Level.Trial)
        @Override
        public void initializeEnforcer() {
            Schema dynamicDesignSchema = SchemaBuilder.builder().record("designDynamic")
                    .prop(SchemaConstants.INCLUDE_ALL_FIELDS, "true")
                    .prop(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "2").fields()
                    .name("col0").type().stringType().noDefault()
                    .name("col1").type().stringType().noDefault()
                    .name("col3").type().stringType().noDefault()
                    .name("col4").type().stringType().noDefault()
                    .endRecord();
            
            Schema dynamicActualSchema = SchemaBuilder.builder().record("actualDynamic").fields()
                    .name("col0").type().stringType().noDefault()
                    .name("col1").type().stringType().noDefault()
                    .name("col2_0").type().stringType().noDefault()
                    .name("col2_1").type().stringType().noDefault()
                    .name("col2_2").type().stringType().noDefault()
                    .name("col3").type().stringType().noDefault()
                    .name("col4").type().stringType().noDefault()
                    .endRecord();
            
            IndexedRecord dynamicRecord = new GenericData.Record(dynamicActualSchema);
            dynamicRecord.put(0, "value0");
            dynamicRecord.put(1, "value1");
            dynamicRecord.put(2, "value2_0");
            dynamicRecord.put(3, "value2_1");
            dynamicRecord.put(4, "value2_2");
            dynamicRecord.put(5, "value3");
            dynamicRecord.put(6, "value4");
            
            enforcer = new DiOutgoingSchemaEnforcer(dynamicDesignSchema, true);
            enforcer.setWrapped(dynamicRecord);
        }
    }
    
    /**
     * {@link State} class for Enforcer with dynamic columns in Schema and ByName mode
     */
    public static class DynamicByNameEnforcerState extends EnforcerState {

        /**
         * Initializes Enforcer with Schema, which contains dynamic columns.
         * Also ByName mode is used
         * This method is called only once for benchmark method. See {@link Level#Trial}
         */
        @Setup(Level.Trial)
        @Override
        public void initializeEnforcer() {
            Schema dynamicDesignSchema = SchemaBuilder.builder().record("designDynamic")
                    .prop(SchemaConstants.INCLUDE_ALL_FIELDS, "true")
                    .prop(DiSchemaConstants.TALEND6_DYNAMIC_COLUMN_POSITION, "2").fields()
                    .name("col0").type().stringType().noDefault()
                    .name("col1").type().stringType().noDefault()
                    .name("col3").type().stringType().noDefault()
                    .name("col4").type().stringType().noDefault()
                    .endRecord();
            
            Schema dynamicActualSchema = SchemaBuilder.builder().record("actualDynamic").fields()
                    .name("col0").type().stringType().noDefault()
                    .name("col1").type().stringType().noDefault()
                    .name("col2_0").type().stringType().noDefault()
                    .name("col2_1").type().stringType().noDefault()
                    .name("col2_2").type().stringType().noDefault()
                    .name("col3").type().stringType().noDefault()
                    .name("col4").type().stringType().noDefault()
                    .endRecord();
            
            IndexedRecord dynamicRecord = new GenericData.Record(dynamicActualSchema);
            dynamicRecord.put(0, "value0");
            dynamicRecord.put(1, "value1");
            dynamicRecord.put(2, "value2_0");
            dynamicRecord.put(3, "value2_1");
            dynamicRecord.put(4, "value2_2");
            dynamicRecord.put(5, "value3");
            dynamicRecord.put(6, "value4");
            
            enforcer = new DiOutgoingSchemaEnforcer(dynamicDesignSchema, false);
            enforcer.setWrapped(dynamicRecord);
        }
    }

    /**
     * Design schema doesn't contain dynamic field, field matching is by index
     * Get non dynamic column value
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object benchmarkGetWithoutDynamicByIndex(WithoutDynamicByIndexEnforcerState state) {
        return state.enforcer.get(0);
    }
    
    /**
     * Design schema doesn't contain dynamic field, field matching is by name
     * Get non dynamic column value
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object benchmarkGetWithoutDynamicByName(WithoutDynamicByNameEnforcerState state) {
        return state.enforcer.get(0);
    }
  
    /**
     * Get non dynamic column value from Schema, which contains dynamic column
     * ByIndex mode is used
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object benchmarkGetNonDynamicByIndex(DynamicByIndexEnforcerState state) {
        return state.enforcer.get(0);
    }
    
    /**
     * Get non dynamic column value from Schema, which contains dynamic column
     * ByName mode is used
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object benchmarkGetNonDynamicByName(DynamicByNameEnforcerState state) {
        return state.enforcer.get(0);
    }
    
    /**
     * Get dynamic column value from Schema, which contains dynamic column
     * ByIndex mode is used
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object benchmarkGetDynamicByIndex(DynamicByIndexEnforcerState state) {
        return state.enforcer.get(2);
    }
    
    /**
     * Get dynamic column value from Schema, which contains dynamic column
     * ByName mode is used
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public Object benchmarkGetDynamicByName(DynamicByNameEnforcerState state) {
        return state.enforcer.get(2);
    }

    /**
     * Runs Benchmark
     */
    @Test
    public void runBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder().include(DiOutgoingSchemaEnforcerBenchmark.class.getSimpleName()).warmupIterations(5)
                .measurementIterations(5).forks(1).build();

        new Runner(opt).run();
    }
}
