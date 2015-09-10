package org.talend.daikon.exception.error;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

public enum CommonErrorCodes implements ErrorCode {
                                                   UNEXPECTED_EXCEPTION(HttpServletResponse.SC_INTERNAL_SERVER_ERROR), // 500
                                                   UNABLE_TO_PARSE_JSON(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                                                   UNABLE_TO_WRITE_JSON(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                                                   UNABLE_TO_SERIALIZE_TO_JSON(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                                                   UNABLE_TO_READ_CONTENT(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
                                                   UNABLE_TO_PARSE_REQUEST(HttpServletResponse.SC_BAD_REQUEST); // 400,
                                                                                                                // e.g
                                                                                                                // IllegalArgumentException

    /** The http status to use. */
    private int httpStatus;

    /** Expected entries to be in the context. */
    private List<String> expectedContextEntries;

    /**
     * default constructor.
     * 
     * @param httpStatus the http status to use.
     */
    CommonErrorCodes(int httpStatus) {
        this.httpStatus = httpStatus;
        this.expectedContextEntries = Collections.emptyList();
    }

    /**
     * default constructor.
     *
     * @param httpStatus the http status to use.
     */
    CommonErrorCodes(int httpStatus, String... contextEntries) {
        this.httpStatus = httpStatus;
        this.expectedContextEntries = Arrays.asList(contextEntries);
    }

    /**
     * @return the product. Default value is Talend.
     */
    @Override
    public String getProduct() {
        return "Talend"; //$NON-NLS-1$
    }

    /**
     * @return the group. Default Value is ALL
     */
    @Override
    public String getGroup() {
        return "ALL"; //$NON-NLS-1$
    }

    /**
     * @return the http status.
     */
    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    /**
     * @return the expected context entries.
     */
    @Override
    public Collection<String> getExpectedContextEntries() {
        return expectedContextEntries;
    }

    @Override
    public String getCode() {
        return this.toString();
    }
}
