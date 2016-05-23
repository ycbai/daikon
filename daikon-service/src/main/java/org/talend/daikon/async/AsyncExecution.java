package org.talend.daikon.async;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.async.json.TalendRuntimeExceptionSerializer;
import org.talend.daikon.async.progress.Progress;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

// TODO Switch to immutable
@JsonRootName("execution")
public class AsyncExecution<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncExecution.class);

    @JsonProperty("id")
    private final String id = UUID.randomUUID().toString();

    @JsonProperty("time")
    private final Time time = new Time();

    @JsonProperty("status")
    private Status status = Status.NEW;

    @JsonProperty("result")
    @JsonInclude(value = NON_NULL, content = NON_NULL)
    private ListenableFuture<T> result;

    @JsonProperty("error")
    @JsonSerialize(using = TalendRuntimeExceptionSerializer.class)
    @JsonInclude(value = NON_NULL, content = NON_NULL)
    private TalendRuntimeException error;

    private Progress progress;

    void cancel(boolean b) {
        result.cancel(b);
    }

    public String getId() {
        return id;
    }

    @JsonProperty("progress")
    @JsonInclude(value = NON_NULL, content = NON_NULL)
    public Progress getProgress() {
        switch (status) {
        case NEW:
        case RUNNING:
        case CANCELLED:
        case FAILED:
        default:
            return progress;
        case DONE:
            return null;
        }
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        LOGGER.debug("Execution {} set to {}", id, status);
        this.status = status;
        switch (status) {
        case NEW:
            time.creationDate = System.currentTimeMillis();
            break;
        case RUNNING:
            time.startDate = System.currentTimeMillis();
            break;
        case CANCELLED:
        case FAILED:
        case DONE:
            time.endDate = System.currentTimeMillis();
            break;
        }
    }

    public T getResult() {
        switch (status) {
        case NEW:
        case RUNNING:
        case CANCELLED:
        case FAILED:
        default:
            return null;
        case DONE:
            try {
                return result.get();
            } catch (Exception e) {
                TalendRuntimeException.unexpectedException(e);
                return null;
            }
        }
    }

    public void setResult(ListenableFuture<T> result) {
        this.result = result;
    }

    public void setError(Throwable error) {
        if (error instanceof TalendRuntimeException) {
            this.error = (TalendRuntimeException) error;
        } else {
            this.error = new TalendRuntimeException(CommonErrorCodes.UNEXPECTED_EXCEPTION, error);
        }
    }

    public enum Status {
                        NEW,
                        RUNNING,
                        CANCELLED,
                        FAILED,
                        DONE
    }

    public static class Time {

        @JsonProperty("creation")
        private long creationDate = System.currentTimeMillis();

        @JsonProperty("start")
        @JsonInclude(value = NON_DEFAULT, content = NON_DEFAULT)
        private long startDate;

        @JsonProperty("end")
        @JsonInclude(value = NON_DEFAULT, content = NON_DEFAULT)
        private long endDate;
    }
}
