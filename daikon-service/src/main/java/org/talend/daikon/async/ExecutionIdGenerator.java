package org.talend.daikon.async;

@FunctionalInterface
public interface ExecutionIdGenerator {

    String generate(String value);
}
