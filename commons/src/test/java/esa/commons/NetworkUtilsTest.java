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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NetworkUtilsTest {

    @Test
    void testGetLocalAddress() {
        final InetAddress address = NetworkUtils.getLocalAddress();
        assertNotNull(address);
        assertSame(address, NetworkUtils.getLocalAddress());
        assertNotNull(NetworkUtils.getAnyHostValue());
    }

    @Test
    void testSelectRandomPort() {
        assertDoesNotThrow(NetworkUtils::selectRandomPort);
    }

    @Test
    void testCheckPortStatus() {
        assertDoesNotThrow(() -> NetworkUtils.checkPortStatus(NetworkUtils.getAnyHostValue(),
                NetworkUtils.selectRandomPort()));
    }

    @Test
    void testIsReachable() throws IOException {
        final InetAddress address = mock(InetAddress.class);
        when(address.isReachable(anyInt())).thenReturn(true);
        assertTrue(NetworkUtils.isReachable(address));
        verify(address).isReachable(anyInt());

        assertThrows(IllegalArgumentException.class, () -> NetworkUtils.isReachable(address, 0));
        assertFalse(NetworkUtils.isReachable((InetAddress) null));

        reset(address);
        when(address.isReachable(anyInt())).thenThrow(new IOException());
        assertFalse(NetworkUtils.isReachable(address));
        assertDoesNotThrow(NetworkUtils::getLocalIP);
    }

    @Test
    void testIsV6Address() {
        assertTrue(NetworkUtils.isV6Address(mock(Inet6Address.class)));
        assertFalse(NetworkUtils.isV6Address(mock(Inet4Address.class)));
    }

    @Test
    void testIsValidV6Address() throws IOException {
        final InetAddress address = mock(Inet6Address.class);
        when(address.isReachable(anyInt())).thenReturn(true);

        if (NetworkUtils.isPreferIpv6()) {
            assertTrue(NetworkUtils.isValidV6Address(address));
            reset(address);
            when(address.isReachable(anyInt())).thenReturn(false);
            assertFalse(NetworkUtils.isValidV6Address(address));
        } else {
            assertFalse(NetworkUtils.isValidV6Address(address));
        }

        assertFalse(NetworkUtils.isValidV6Address(mock(Inet4Address.class)));
    }

    @Test
    void testNormalizeV6Address() {
        assertThrows(NullPointerException.class, () -> NetworkUtils.normalizeV6Address(null));
    }

    @Test
    void testParseAddress() {
        assertEquals("", NetworkUtils.parseAddress(null));
        final SocketAddress address = mock(SocketAddress.class);
        when(address.toString()).thenReturn("/127.0.0.1:2020");
        assertEquals("127.0.0.1:2020", NetworkUtils.parseAddress(address));

        reset(address);
        when(address.toString()).thenReturn("/127.0.0.1:2020");
        assertEquals("127.0.0.1:2020", NetworkUtils.parseAddress(address));
    }

    @Test
    void testGetPort() {
        assertEquals(-1, NetworkUtils.getPort(mock(SocketAddress.class)));

        final InetSocketAddress address = mock(InetSocketAddress.class);
        when(address.getPort()).thenReturn(2020);
        assertEquals(2020, NetworkUtils.getPort(address));
    }

}
