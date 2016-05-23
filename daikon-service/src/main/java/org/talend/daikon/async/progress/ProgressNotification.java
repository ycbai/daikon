package org.talend.daikon.async.progress;

import java.util.HashMap;
import java.util.Map;

import org.talend.daikon.async.AsyncExecution;

public class ProgressNotification {

    public static final Progress INITIAL_PROGRESS = new Progress() {
    };
    private static final Map<Thread, ProgressEntry> currentProgress = new HashMap<>();

    public static ProgressNotification get() {
        return new ProgressNotification();
    }

    public static <T> void link(AsyncExecution<T> asyncExecution, Thread thread) {
        final ProgressEntry entry = new ProgressEntry(asyncExecution, INITIAL_PROGRESS);
        currentProgress.put(thread, entry);
    }

    public static void unlink(Thread thread) {
        currentProgress.remove(thread);
    }

    public void push(Progress progress) {
        currentProgress.get(Thread.currentThread()).execution.setProgress(progress);
    }

    private static class ProgressEntry {

        Progress progress;

        AsyncExecution execution;

        public <T> ProgressEntry(AsyncExecution<T> asyncExecution, Progress progress) {
            this.execution = asyncExecution;
            this.progress = progress;
        }
    }
}
