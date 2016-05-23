package org.talend.daikon.async;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.talend.daikon.async.progress.ProgressNotification;

public class ManagedTaskExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManagedTaskExecutor.class);

    private final AsyncListenableTaskExecutor delegate;

    private Map<String, AsyncExecution> managedFutures = Collections.synchronizedMap(new HashMap<>());

    private ExecutionIdGenerator executionIdGenerator = (s) -> s;

    private ManagedTaskExecutor(AsyncListenableTaskExecutor delegate) {
        this.delegate = delegate;
    }

    public static ManagedTaskExecutor get(AsyncListenableTaskExecutor delegate) {
        return new ManagedTaskExecutor(delegate);
    }

    public <T> AsyncExecution queue(final Callable<T> callable) {
        final AsyncExecution<T> asyncExecution = new AsyncExecution<>();
        final Callable<T> wrapper = () -> {
            asyncExecution.setStatus(AsyncExecution.Status.RUNNING);
            ProgressNotification.link(asyncExecution, Thread.currentThread());
            return callable.call();
        };
        ListenableFuture<T> future = delegate.submitListenable(wrapper);
        asyncExecution.setResult(future);
        future.addCallback(new ListenableFutureCallback<T>() {

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.error("Execution {} finished with error.", asyncExecution.getId());
                asyncExecution.setError(throwable);
                asyncExecution.setStatus(AsyncExecution.Status.FAILED);
            }

            @Override
            public void onSuccess(T t) {
                LOGGER.debug("Execution {} finished with success.", asyncExecution.getId());
                asyncExecution.setStatus(AsyncExecution.Status.DONE);
            }
        });
        managedFutures.put(asyncExecution.getId(), asyncExecution);
        LOGGER.debug("Execution {} queued for execution.", asyncExecution.getId());
        return asyncExecution;
    }

    public <T> AsyncExecution<T> find(final String id) {
        LOGGER.debug("Request for execution #{}", id);
        return managedFutures.get(id);
    }

    public <T> AsyncExecution<T> cancel(final String id) {
        LOGGER.debug("Cancel execution #{}", id);
        final AsyncExecution asyncExecution = managedFutures.get(id);
        if (asyncExecution != null) {
            asyncExecution.cancel(true);
            asyncExecution.setStatus(AsyncExecution.Status.CANCELLED);
        }
        return asyncExecution;
    }

    public ExecutionIdGenerator getExecutionIdGenerator() {
        return executionIdGenerator;
    }
}
