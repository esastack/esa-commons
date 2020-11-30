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

import esa.commons.annotation.Beta;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Beta
public class JvmUtils {
    public static JvmInfo getJVMInfo() {
        final JvmInfo jvmInfo = new JvmInfo();

        jvmInfo.setUptime(ManagementFactory.getRuntimeMXBean().getUptime());
        final List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();
        final List<Map> memoryList = new ArrayList<>(memoryPoolMXBeans.size());
        for (MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans) {
            Map<String, Object> bean = new HashMap<>(16);
            bean.put("name", memoryPoolMXBean.getName());
            bean.put("used", MathUtils.round(memoryPoolMXBean.getUsage().getUsed() / (1024 * 1024D)));
            bean.put("max", MathUtils.round(memoryPoolMXBean.getUsage().getMax() / (1024 * 1024D)));
            bean.put("committed", MathUtils.round(memoryPoolMXBean.getUsage().getCommitted() / (1024 * 1024D)));
            bean.put("rate", MathUtils.round(
                    memoryPoolMXBean.getUsage().getUsed() * 100D / memoryPoolMXBean.getUsage().getCommitted()));

            memoryList.add(bean);
        }
        jvmInfo.setMemoryList(memoryList);

        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        List<Map<String, Object>> threadList;
        try {
            final long[] threadIds = threadMXBean.getAllThreadIds();
            threadList = new ArrayList<>(threadIds.length);
            for (long threadId : threadIds) {
                final Map<String, Object> bean = new HashMap<>(16);
                bean.put("threadId", threadId);
                bean.put("cpuTime",
                        MathUtils.round(threadMXBean.getThreadCpuTime(threadId) / (1000D * 1000 * 1000)));
                threadList.add(bean);
            }

            threadList.sort((o1, o2) -> {
                Double cpuTimeA = (Double) o1.get("cpuTime");
                Double cpuTimeB = (Double) o2.get("cpuTime");
                return cpuTimeB.compareTo(cpuTimeA);
            });
            threadList = threadList.subList(0, Math.min(100, threadList.size()));

            threadMXBean.setThreadContentionMonitoringEnabled(true);

            for (Map<String, Object> bean : threadList) {
                long threadId = (long) bean.get("threadId");
                final ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId, 10);

                bean.put("threadName", threadInfo.getThreadName());
                bean.put("state", threadInfo.getThreadState().toString());

                StackTraceElement[] stackTrace = threadInfo.getStackTrace();
                StringBuilder sb = new StringBuilder();
                for (StackTraceElement row : stackTrace) {
                    sb.append(row).append("\n");
                }
                bean.put("stackTrace", sb.toString());
            }
        } finally {
            threadMXBean.setThreadContentionMonitoringEnabled(false);
        }
        jvmInfo.setThreadList(threadList);

        jvmInfo.setThreadCount(threadMXBean.getThreadCount());
        jvmInfo.setDaemonThreadCount(threadMXBean.getDaemonThreadCount());

        ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
        jvmInfo.setLoadedClassCount(classLoadingMXBean.getLoadedClassCount());

        return jvmInfo;
    }

    public static final class JvmInfo implements java.io.Serializable {
        private static final long serialVersionUID = -4843698110206999775L;
        private long uptime;
        private List<Map> memoryList;
        private Integer threadCount;
        private Integer daemonThreadCount;
        private List<Map<String, Object>> threadList;
        private Integer loadedClassCount;

        public Integer getThreadCount() {
            return threadCount;
        }

        public void setThreadCount(Integer threadCount) {
            this.threadCount = threadCount;
        }

        public Integer getDaemonThreadCount() {
            return daemonThreadCount;
        }

        public void setDaemonThreadCount(Integer daemonThreadCount) {
            this.daemonThreadCount = daemonThreadCount;
        }

        public Integer getLoadedClassCount() {
            return loadedClassCount;
        }

        public void setLoadedClassCount(Integer loadedClassCount) {
            this.loadedClassCount = loadedClassCount;
        }

        public List<Map<String, Object>> getThreadList() {
            return threadList;
        }

        public void setThreadList(List<Map<String, Object>> threadList) {
            this.threadList = threadList;
        }

        public List<Map> getMemoryList() {
            return memoryList;
        }

        public void setMemoryList(List<Map> memoryList) {
            this.memoryList = memoryList;
        }

        public long getUptime() {
            return uptime;
        }

        public void setUptime(long uptime) {
            this.uptime = uptime;
        }

        @Override
        public String toString() {
            return "JvmInfo{" +
                    "uptime=" + uptime +
                    ", memoryList=" + memoryList +
                    ", threadCount=" + threadCount +
                    ", daemonThreadCount=" + daemonThreadCount +
                    ", threadList=" + threadList +
                    ", loadedClassCount=" + loadedClassCount +
                    '}';
        }
    }
}
