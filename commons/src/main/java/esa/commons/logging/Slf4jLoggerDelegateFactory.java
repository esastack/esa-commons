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

import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

class Slf4jLoggerDelegateFactory implements LoggerDelegateFactory {

    static Slf4jLoggerDelegateFactory INSTANCE = new Slf4jLoggerDelegateFactory();

    static {
        if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
            throw new Error();
        }
    }

    @Override
    public Logger create(String name) {
        org.slf4j.Logger logger = LoggerFactory.getLogger(name);
        if (logger instanceof LocationAwareLogger) {
            return new LocationAwareSlf4Logger((LocationAwareLogger) logger);
        } else {
            return new Slf4cjLogger(logger);
        }
    }
}
