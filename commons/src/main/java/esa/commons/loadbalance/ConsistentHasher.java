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
package esa.commons.loadbalance;

import esa.commons.Checks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHasher<N> {

    private final int virtualNodes;

    private final Map<N, Integer> counterMap = new HashMap<>(16);

    private final TreeMap<Integer, VirtualNode<N>> nodes = new TreeMap<>();

    private final KeyGenerator<N> keyGenerator;

    private static final KeyGenerator<String> STRING_GENERATOR = node -> node;

    public static ConsistentHasher<String> newStringHasher() {
        return newHasher(STRING_GENERATOR);
    }

    public static ConsistentHasher<String> newStringHasher(int virtualNodes) {
        return newHasher(virtualNodes, STRING_GENERATOR);
    }

    public static <NODE> ConsistentHasher<NODE> newHasher(KeyGenerator<NODE> keyGenerator) {
        return new ConsistentHasher<>(keyGenerator);
    }

    public static <NODE> ConsistentHasher<NODE> newHasher(int virtualNodes, KeyGenerator<NODE> keyGenerator) {
        return new ConsistentHasher<>(virtualNodes, keyGenerator);
    }

    private ConsistentHasher(KeyGenerator<N> keyGenerator) {
        this(16, keyGenerator);
    }

    private ConsistentHasher(int virtualNodes, KeyGenerator<N> keyGenerator) {
        Checks.checkArg(virtualNodes >= 0, "VirtualNodes must equals or over than 0");
        Checks.checkNotNull(keyGenerator, "keyGenerator");
        this.keyGenerator = keyGenerator;
        this.virtualNodes = virtualNodes;
    }

    public void addNodeIfAbsent(N node) {
        addNodeIfAbsent(node, virtualNodes);
    }

    public void addNodeIfAbsent(N node, int virtualNodes) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        if (virtualNodes < 0) {
            throw new IllegalArgumentException("VirtualNodes must equals or over than 0.");
        }

        Integer counter = counterMap.get(node);

        if (counter == null) {
            addNode(node, virtualNodes);
        }
    }

    public void addNode(N node) {
        addNode(node, virtualNodes);
    }

    public void addNodes(Collection<N> nodes) {
        for (N node : nodes) {
            addNode(node, virtualNodes);
        }
    }

    public void addNode(N node, int virtualNodes) {
        if (node == null) {
            throw new NullPointerException("node");
        }
        if (virtualNodes < 0) {
            throw new IllegalArgumentException("VirtualNodes must equals or over than 0.");
        }
        for (int i = 0; i < virtualNodes; i++) {
            VirtualNode<N> vNode = newNode(node);
            nodes.put(hash(node, vNode.index), vNode);
        }
    }

    public void remove(N node) {
        int max = counterMap.remove(node);
        for (int i = 0; i <= max; i++) {
            nodes.remove(hash(node, i));
        }
    }

    public N get(String target) {
        if (nodes.isEmpty()) {
            return null;
        }
        SortedMap<Integer, VirtualNode<N>> tailMap = nodes.tailMap(fnv132(target));
        int h = !tailMap.isEmpty() ? tailMap.firstKey() : nodes.firstKey();
        return nodes.get(h).realNode;
    }


    private VirtualNode<N> newNode(N node) {
        Integer counter = counterMap.get(node);
        if (counter == null) {
            counterMap.put(node, counter = 0);
        } else {
            counterMap.put(node, ++counter);
        }
        return new VirtualNode<>(node, counter);
    }

    private int hash(N node, int index) {
        return fnv132(keyGenerator.getKey(node) + index);
    }

    private static class VirtualNode<N> {

        private final N realNode;
        private final int index;

        private VirtualNode(N realNode, int index) {
            this.realNode = realNode;
            this.index = index;
        }
    }

    public interface KeyGenerator<N> {

        String getKey(N node);

    }

    private static int fnv132(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }
}
