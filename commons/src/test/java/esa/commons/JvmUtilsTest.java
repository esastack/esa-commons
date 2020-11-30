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
package esa.commons;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JvmUtilsTest {

    @Test
    void testGetJvmInfo() {
        final JvmUtils.JvmInfo info = JvmUtils.getJVMInfo();
        assertNotNull(info);
        assertTrue(info.getUptime() > 0L);

        // The Java virtual machine can have one or more memory pools.
        assertFalse(info.getMemoryList().isEmpty());
        final Map m = info.getMemoryList().get(0);
        assertTrue(m.containsKey("name"));
        assertTrue((double) m.get("used") >= 0.0D);
        assertTrue((double) m.get("max") > 0.0D);
        assertTrue((double) m.get("committed") >= 0.0D);
        assertTrue((double) m.get("rate") >= 0.0D);

        assertTrue(info.getThreadCount() > 0);
        assertTrue(info.getDaemonThreadCount() >= 0);
        assertTrue(info.getLoadedClassCount() > 0);

        assertFalse(info.getThreadList().isEmpty());

        assertTrue(info.getThreadList()
                .stream()
                .anyMatch(t -> {
                    if (Thread.currentThread().getId() == (long) t.get("threadId")) {
                        assertTrue((double) t.get("cpuTime") > 0);
                        assertEquals(Thread.currentThread().getName(), t.get("threadName"));
                        assertTrue(t.containsKey("state"));
                        assertTrue(t.containsKey("stackTrace"));
                        return true;
                    }
                    return false;
                }));
    }

}
