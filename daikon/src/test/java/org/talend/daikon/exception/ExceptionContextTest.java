package org.talend.daikon.exception;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link ExceptionContext}
 */
public class ExceptionContextTest {

    @Test
    public void createExceptionContextWithBuilder(){
        ExceptionContext context = ExceptionContext.withBuilder().put("key1", "value1").put("key2", "value2")
                .put("key3", "value3").build();
        Assert.assertTrue(context.contains("key1"));
        Assert.assertTrue(context.contains("key2"));
        Assert.assertTrue(context.contains("key3"));
    }


}
