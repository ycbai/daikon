package org.talend.daikon.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@ConditionalOnBean(AsyncConfiguration.class)
@RestController
public class AsyncController {

    static final String QUEUE_PATH = "queue";

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncController.class);

    @Autowired
    private ManagedTaskExecutor executor;

    @RequestMapping(method = RequestMethod.GET, path = "/" + QUEUE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AsyncExecution get(@PathVariable("id") String id) {
        LOGGER.debug("Get execution {}", id);
        return executor.find(id);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/" + QUEUE_PATH + "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AsyncExecution cancel(@PathVariable("id") String id) {
        LOGGER.debug("Cancel execution {}", id);
        return executor.cancel(id);
    }

}
