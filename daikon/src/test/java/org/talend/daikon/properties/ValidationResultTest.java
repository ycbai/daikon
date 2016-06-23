// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.properties.ValidationResult.Result;

public class ValidationResultTest {

    @Test
    public void testValidationResult() {
        assertEquals(Result.OK, new ValidationResult().getStatus());
    }

    @Test
    public void testValidationResultTalendRuntimeException() {
        TalendRuntimeException runtimeException = new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION);
        ValidationResult result = new ValidationResult(runtimeException);
        assertEquals(Result.ERROR, result.getStatus());
        assertEquals("UNEXPECTED_EXCEPTION", result.getMessage());
    }

    @Test
    public void testSetGetStatus() {
        assertEquals(Result.OK, new ValidationResult().setStatus(Result.OK).getStatus());
        assertEquals(Result.WARNING, new ValidationResult().setStatus(Result.WARNING).getStatus());
        assertEquals(Result.ERROR, new ValidationResult().setStatus(Result.ERROR).getStatus());
    }

    @Test
    public void testSetGetMessage() {
        assertEquals("foo", new ValidationResult().setMessage("foo").getMessage());
    }

    @Test
    public void testToString() {
        assertEquals("ERROR UNEXPECTED_EXCEPTION",
                new ValidationResult(new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION)).toString());
    }

}
