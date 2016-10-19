// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.properties;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.properties.property.EnumProperty;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.properties.property.PropertyValueEvaluator;
import org.talend.daikon.serialize.SerializerDeserializer;

public class EnumPropertyTest {

    public final class EnumPropForI18NTest extends EnumProperty {

        public EnumPropForI18NTest(Class zeEnumType, String name) {
            super(zeEnumType, name);
        }
    }

    public static class PropertiesWithOneEnum extends PropertiesImpl {

        public PropertiesWithOneEnum() {
            super("");
        }

        public EnumProperty<TestEnum> enumProperty = new EnumProperty<>(TestEnum.class, "enumProperty");
    };

    public enum TestEnum {
        FOO,
        BAR,
        FOOBAR
    }

    @Test
    public void testGetValue() {
        EnumProperty<TestEnum> enumProperty = PropertyFactory.newEnum("prop", TestEnum.class);
        assertThat((List<TestEnum>) enumProperty.getPossibleValues(), contains(TestEnum.FOO, TestEnum.BAR, TestEnum.FOOBAR));
        // test the Stored value as a String
        enumProperty.setStoredValue("FOO");
        assertEquals(TestEnum.FOO, enumProperty.getValue());
    }

    @Test
    public void testGetValueWithEvaluator() {
        EnumProperty<TestEnum> enumProperty = PropertyFactory.newEnum("prop", TestEnum.class);
        enumProperty.setValueEvaluator(new PropertyValueEvaluator() {

            @Override
            public <T> T evaluate(Property<T> property, Object storedValue) {
                return (T) TestEnum.valueOf((String) storedValue);
            }
        });
        enumProperty.setStoredValue("FOO");
        assertEquals(TestEnum.FOO, enumProperty.getValue());
    }

    @Test
    public void testI18nForEnums() {
        EnumProperty<TestEnum> enumProperty = new EnumPropForI18NTest(TestEnum.class, "enumProperty");
        assertEquals("fo o", enumProperty.getPossibleValuesDisplayName(TestEnum.FOO));
        assertEquals("ba r", enumProperty.getPossibleValuesDisplayName(TestEnum.BAR));
        assertEquals("fooba r", enumProperty.getPossibleValuesDisplayName(TestEnum.FOOBAR));
        try {
            enumProperty.getPossibleValuesDisplayName("I am your father");
            fail("exception should have been thrown.");
        } catch (TalendRuntimeException e) {
            assertEquals(CommonErrorCodes.UNEXPECTED_ARGUMENT, e.getCode());
        }
    }

    @Test
    public void testDeserialization() {
        // Properties prop = new PropertiesWithOneEnum();
        // System.out.println(prop.toSerialized());

        String oldEnumSerialized = "{\"@type\":\"org.talend.daikon.properties.EnumPropertyTest$PropertiesWithOneEnum\",\"enumProperty\":{\"flags\":null,\"taggedValues\":{\"@type\":\"java.util.HashMap\"},\"storedValue\":null,\"size\":-1,\"occurMinTimes\":0,\"occurMaxTimes\":0,\"precision\":0,\"pattern\":null,\"nullable\":false,\"possibleValues\":{\"@type\":\"java.util.Arrays$ArrayList\",\"@items\":[{\"@type\":\"org.talend.daikon.properties.EnumPropertyTest$TestEnum\",\"name\":\"FOO\",\"ordinal\":0},{\"@type\":\"org.talend.daikon.properties.EnumPropertyTest$TestEnum\",\"name\":\"BAR\",\"ordinal\":1}]},\"children\":{\"@type\":\"java.util.ArrayList\"},\"currentType\":\"org.talend.daikon.properties.EnumPropertyTest.TestEnum\",\"name\":\"enumProperty\",\"displayName\":null,\"title\":null},\"name\":\"\",\"validationResult\":null}\n"
                + "";
        Properties prop = SerializerDeserializer.fromSerializedPersistent(oldEnumSerialized, Properties.class).object;
        EnumProperty<?> enumProp = (EnumProperty<?>) prop.getProperty("enumProperty");
        assertEquals(3, enumProp.getPossibleValues().size());
    }

}
