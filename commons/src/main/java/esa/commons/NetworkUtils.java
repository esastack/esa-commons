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

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public final class NetworkUtils {

    private static final String IP_PATTERN_REGEX = "\\d{1,3}(\\.\\d{1,3}){3,5}$";
    private static final Pattern IP_PATTERN = Pattern.compile(IP_PATTERN_REGEX);
    private static final String ANY_HOST_VALUE = "0.0.0.0";
    private static final String LOCALHOST_VALUE = "127.0.0.1";
    private static final boolean PREFER_IPV6 = Boolean.getBoolean("java.net.preferIPv6Addresses");
    private static final int DEFAULT_REACHABLE_CHECK_TIMEOUT = 3000;
    private static volatile InetAddress LOCAL_ADDRESS;

    /**
     * Check given address is reachable.
     *
     * @param ipAddress ipAddress
     * @return is reachable
     */
    public static boolean checkPortStatus(String ipAddress, int port) {
        boolean isOK = false;
        try (Socket mSocket = new Socket()) {
            SocketAddress socketAddress = new InetSocketAddress(ipAddress, port);
            mSocket.connect(socketAddress, DEFAULT_REACHABLE_CHECK_TIMEOUT);
            isOK = true;
        } catch (Exception ignored) {
        }
        return isOK;
    }

    public static String getLocalIP() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return null;
    }

    /**
     * Select a available local port.
     *
     * @return available port or else -1 means not found.
     */
    public static int selectRandomPort() {
        int port = -1;
        try (ServerSocket ss = new ServerSocket()) {
            ss.bind(null);
            port = ss.getLocalPort();
        } catch (IOException ignored) {
        }
        return port;
    }

    public static String getAnyHostValue() {
        return ANY_HOST_VALUE;
    }

    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS == null) {
            synchronized (NetworkUtils.class) {
                if (LOCAL_ADDRESS == null) {
                    LOCAL_ADDRESS = getLocalAddress0();
                }
            }
        }
        return LOCAL_ADDRESS;
    }

    public static boolean isReachable(String address) {
        return isReachable(address, DEFAULT_REACHABLE_CHECK_TIMEOUT);
    }

    public static boolean isReachable(String address, int timeout) {
        try {
            return isReachable(InetAddress.getByName(address), timeout);
        } catch (UnknownHostException ignored) {
        }
        return false;
    }

    public static boolean isReachable(InetAddress address) {
        return isReachable(address, DEFAULT_REACHABLE_CHECK_TIMEOUT);
    }

    public static boolean isReachable(InetAddress address, int timeout) {
        Checks.checkArg(timeout > 0, "timeout must be positive: " + timeout);
        if (address == null) {
            return false;
        }
        try {
            return address.isReachable(timeout);
        } catch (IOException e) {
            // ignore
        }
        return false;
    }

    /**
     * is prefer ipv6 address.
     *
     * @return result of Boolean.getBoolean("java.net.preferIPv6Addresses")
     */
    public static boolean isPreferIpv6() {
        return PREFER_IPV6;
    }

    public static boolean isV6Address(InetAddress address) {
        return address instanceof Inet6Address;
    }

    public static boolean isValidV6Address(InetAddress address) {
        return isV6Address(address) && isValidV6Address((Inet6Address) address);
    }

    public static boolean isValidV6Address(Inet6Address address) {
        return isPreferIpv6() && isReachable(address);
    }

    /**
     * convert
     * fe80:0:0:0:894:aeec:f37d:23e1%en0
     * to
     * fe80:0:0:0:894:aeec:f37d:23e1%5
     */
    public static InetAddress normalizeV6Address(Inet6Address address) {
        Checks.checkNotNull(address, "address");
        String addr = address.getHostAddress();
        int i;
        if (addr != null && (i = addr.lastIndexOf('%')) > 0) {
            try {
                return InetAddress.getByName(addr.substring(0, i) + '%' + address.getScopeId());
            } catch (UnknownHostException ignored) {
            }
        }
        return address;
    }

    public static String parseAddress(SocketAddress remote) {
        if (remote == null) {
            return StringUtils.empty();
        }
        final String address = remote.toString().trim();
        int index = address.indexOf('/');
        if (index < 0 || ++index >= address.length()) {
            return address;
        } else {
            return address.substring(index);
        }
    }

    public static int getPort(SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            InetSocketAddress inet = (InetSocketAddress) address;
            return inet.getPort();
        }
        return -1;
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress;
        try {
            localAddress = validAddress(InetAddress.getLocalHost());
            if (localAddress != null) {
                return localAddress;
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (null == interfaces) {
                return null;
            }
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = validAddress(addresses.nextElement());
                    if (address != null) {
                        return address;
                    }
                }
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        return null;
    }

    private static InetAddress validAddress(InetAddress address) {
        if (address instanceof Inet6Address) {
            Inet6Address v6Address = (Inet6Address) address;
            if (isValidV6Address(v6Address)) {
                return normalizeV6Address(v6Address);
            }
        }
        if (isValidV4AddressExceptedLocalAndAnyHost(address)) {
            return address;
        }
        return null;
    }

    private static boolean isValidV4AddressExceptedLocalAndAnyHost(InetAddress address) {
        String name = address.getHostAddress();
        return (name != null
                && IP_PATTERN.matcher(name).matches()
                && !ANY_HOST_VALUE.equals(name)
                && !LOCALHOST_VALUE.equals(name));
    }

    /**
     * check given address is reachable.
     *
     * @param ipAddress ipAddress
     * @return result
     * @deprecated use {@link #isReachable(InetAddress)}
     */
    @Deprecated
    public static boolean checkNetworkStatus(String ipAddress) {
        return isReachable(ipAddress);
    }

    private NetworkUtils() {
    }
}
