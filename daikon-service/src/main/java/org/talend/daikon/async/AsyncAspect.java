// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// https://github.com/Talend/data-prep/blob/master/LICENSE
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package org.talend.daikon.async;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.async.http.HttpResponseContext;

@ConditionalOnBean(AsyncConfiguration.class)
@Component
@Aspect
public class AsyncAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncAspect.class);

    @Autowired
    private ManagedTaskExecutor executor;

    @Around(value = "execution(* *(..)) && @annotation(requestMapping) && @annotation(asyncOperation)", argNames = "pjp,requestMapping,asyncOperation")
    public Object exception(ProceedingJoinPoint pjp, RequestMapping requestMapping, AsyncOperation asyncOperation)
            throws Throwable {
        // Scheduling method execution
        LOGGER.debug("Scheduling for execution of {} ({})", pjp.getSignature().toLongString(),
                Arrays.toString(requestMapping.path()));
        final AsyncExecution future = executor.queue(() -> {
            try {
                return pjp.proceed(pjp.getArgs());
            } catch (TalendRuntimeException e) {
                throw e;
            } catch (Throwable throwable) {
                TalendRuntimeException.unexpectedException(throwable);
                return null;
            }
        });
        LOGGER.debug("Scheduling done.");
        // Redirecting (HTTP 202 + Location header).
        LOGGER.debug("Redirecting to execution queue...");
        HttpResponseContext.status(HttpStatus.ACCEPTED);
        HttpResponseContext.header("Location", "/" + AsyncController.QUEUE_PATH + "/" + future.getId());
        LOGGER.debug("Redirection done.");
        return null;
    }
}
