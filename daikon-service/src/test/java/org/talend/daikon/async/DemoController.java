package org.talend.daikon.async;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.concurrent.Callable;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.exception.error.CommonErrorCodes;
import org.talend.daikon.async.progress.Progress;
import org.talend.daikon.async.progress.ProgressNotification;

@RestController
public class DemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @RequestMapping(path = "/demo/sync", method = GET, produces = APPLICATION_JSON_VALUE)
    public DemoBean sync() throws InterruptedException {
        LOGGER.info("-> DemoController.sync");
        try {
            LOGGER.info("Running...");
            Thread.sleep(5000);
            LOGGER.info("Done.");
            return new DemoBean("DemoController.sync");
        } finally {
            LOGGER.info("<- DemoController.sync");
        }
    }

    @RequestMapping(path = "/demo/async", method = GET, produces = APPLICATION_JSON_VALUE)
    public Callable<DemoBean> async() {
        LOGGER.info("-> DemoController.async");
        try {
            return () -> {
                try {
                    LOGGER.info("Running...");
                    Thread.sleep(5000);
                    LOGGER.info("Done.");
                    return new DemoBean("DemoController.async");
                } catch (InterruptedException e) {
                    TalendRuntimeException.unexpectedException(e);
                    return null;
                }
            };
        } finally {
            LOGGER.info("<- DemoController.async");
        }
    }

    @RequestMapping(path = "/demo/long", method = GET)
    @AsyncOperation
    public DemoBean longRun() throws InterruptedException {
        LOGGER.info("-> DemoController.longRun");
        try {
            LOGGER.info("Running...");
            Thread.sleep(10000);
            LOGGER.info("Done.");
            return new DemoBean("DemoController.longRun");
        } finally {
            LOGGER.info("<- DemoController.longRun");
        }
    }

    @RequestMapping(path = "/demo/long-fail", method = GET)
    @AsyncOperation
    public DemoBean longRunFailing() throws InterruptedException {
        LOGGER.info("-> DemoController.longRunFailing");
        try {
            LOGGER.info("Running...");
            Thread.sleep(10000);
            LOGGER.info("Done.");
            throw new TalendRuntimeException(CommonErrorCodes.UNABLE_TO_WRITE_JSON);
        } finally {
            LOGGER.info("<- DemoController.longRunFailing");
        }
    }

    @RequestMapping(path = "/demo/long-notification", method = GET)
    @AsyncOperation
    public DemoBean longRunWithNotification() throws InterruptedException {
        LOGGER.info("-> DemoController.longRunningWithNotification");
        try {
            LOGGER.info("Running...");
            ProgressNotification.get().push(new DemoProgress(0));
            Thread.sleep(5000);
            ProgressNotification.get().push(new DemoProgress(1));
            Thread.sleep(5000);
            ProgressNotification.get().push(new DemoProgress(2));
            Thread.sleep(5000);
            ProgressNotification.get().push(new DemoProgress(3));
            Thread.sleep(5000);
            ProgressNotification.get().push(new DemoProgress(4));
            LOGGER.info("Done.");
            return new DemoBean("DemoController.longRunningWithNotification");
        } finally {
            LOGGER.info("<- DemoController.longRunningWithNotification");
        }
    }

    public static class DemoProgress implements Progress {

        @JsonProperty("counter")
        private final int counter;

        public DemoProgress(int counter) {
            this.counter = counter;
        }

    }

}
