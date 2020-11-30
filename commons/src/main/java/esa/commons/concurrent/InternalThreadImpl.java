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
package esa.commons.concurrent;

import esa.commons.annotation.Beta;
import esa.commons.annotation.Internal;

/**
 * Default implementation of {@link InternalThread} that extends the {@link Thread}.
 */
@Beta
@Internal
public class InternalThreadImpl extends Thread implements InternalThread {

    private Object meter;
    private Object tracer;

    public InternalThreadImpl() {
        super();
    }

    public InternalThreadImpl(Runnable target) {
        super(target);
    }

    public InternalThreadImpl(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public InternalThreadImpl(String name) {
        super(name);
    }

    public InternalThreadImpl(ThreadGroup group, String name) {
        super(group, name);
    }

    public InternalThreadImpl(Runnable target, String name) {
        super(target, name);
    }

    public InternalThreadImpl(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public InternalThreadImpl(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public Thread thread() {
        return this;
    }

    @Override
    public final Object meter() {
        return meter;
    }

    @Override
    public final void meter(Object meter) {
        this.meter = meter;
    }

    @Override
    public final Object tracer() {
        return tracer;
    }

    @Override
    public final void tracer(Object tracer) {
        this.tracer = tracer;
    }
}
