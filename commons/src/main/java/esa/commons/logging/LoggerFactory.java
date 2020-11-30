/*
 * Copyright 2020 OPPO ESA Stack Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package esa.commons.logging;

/**
 * Factory of {@link Logger} that will choose a available log framework ASAP or the {@link JdkLogger} as the
 * fallback.
 */
public class LoggerFactory {

    private static final LoggerDelegateFactory DELEGATE = init();

    private static LoggerDelegateFactory init() {
        LoggerDelegateFactory var;
        try {
            var = Slf4jLoggerDelegateFactory.INSTANCE;
            var.create(LoggerFactory.class.getName())
                    .debug("Using slf4j as the logging framework.");
        } catch (Throwable t) {
            var = JdkLoggerDelegateFactory.INSTANCE;
            var.create(LoggerFactory.class.getName())
                    .debug("Using slf4j as the logging framework.");
        }
        return var;
    }

    public static Logger getLogger(Class<?> clz) {
        return getLogger(clz.getName());
    }

    public static Logger getLogger(String name) {
        return DELEGATE.create(name);
    }

}
