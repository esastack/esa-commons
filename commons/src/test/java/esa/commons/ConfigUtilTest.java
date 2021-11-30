package esa.commons;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigUtilTest {

    @Test
    void testGetStr() {
        assertEquals("foo",
                ConfigUtil.create()
                        .readFromMap(Collections.singletonMap("k", "foo")).build()
                        .getStr("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "foo")).build()
                .getStr("absent"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", null)).build()
                .getStr("k"));

        assertEquals("",
                ConfigUtil.create()
                        .readFromMap(Collections.singletonMap("k", "")).build()
                        .getStr("k"));


        assertEquals("foo",
                ConfigUtil.create()
                        .readFromMap(Collections.singletonMap("k", "foo")).build()
                        .getStr("k", "bar"));
        assertEquals("bar",
                ConfigUtil.create()
                        .readFromMap(Collections.singletonMap("k", "foo")).build()
                        .getStr("absent", "bar"));
    }

    @Test
    void testGetBool() {
        assertTrue(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "true")).build()
                .getBool("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getBool("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "true")).build()
                .getBool("absent"));

        assertTrue(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "true")).build()
                .getBool("k", false));
        assertFalse(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "true")).build()
                .getBool("absent", false));

    }

    @Test
    void testGetInt() {
        assertEquals(1, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getInt("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getInt("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getInt("absent"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getInt("k"));

        assertEquals(1, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getInt("k", 2));
        assertEquals(2, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getInt("absent", 2));
        assertEquals(2, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getInt("k", 2));
    }

    @Test
    void testGetLong() {
        assertEquals(1L, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getLong("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getLong("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getLong("absent"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getLong("k"));

        assertEquals(1L, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getLong("k", 2L));
        assertEquals(2L, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getLong("absent", 2L));
        assertEquals(2L, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getLong("k", 2L));
    }

    @Test
    void testGetDouble() {
        assertEquals(1.1D, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1.1")).build()
                .getDouble("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getDouble("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getDouble("absent"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getDouble("k"));

        assertEquals(1.1D, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1.1")).build()
                .getDouble("k", 2.2D));
        assertEquals(2.2D, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getDouble("absent", 2.2D));
        assertEquals(2.2D, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getDouble("k", 2.2D));
    }

    @Test
    void testGetDuration() {
        assertEquals(Duration.ofMillis(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000")).build()
                .getDuration("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getDouble("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getDuration("absent"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getDuration("k"));

        assertEquals(Duration.ofMillis(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000ms")).build()
                .getDuration("k"));

        assertEquals(Duration.ofSeconds(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000s")).build()
                .getDuration("k"));
        assertEquals(Duration.ofMinutes(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000m")).build()
                .getDuration("k"));
        assertEquals(Duration.ofHours(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000h")).build()
                .getDuration("k"));
        assertEquals(Duration.ofDays(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000d")).build()
                .getDuration("k"));


        assertEquals(Duration.ofMillis(1000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1000ms")).build()
                .getDuration("k", Duration.ofMillis(2000L)));
        assertEquals(Duration.ofMillis(2000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getDuration("absent", Duration.ofMillis(2000L)));
        assertEquals(Duration.ofMillis(2000L), ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getDuration("k", Duration.ofMillis(2000L)));
    }

    @Test
    void testGetList() {
        assertArrayEquals(new String[]{"1", "2", "3"}, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1 , 2,3")).build()
                .getList("k").toArray(new String[0]));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getList("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1")).build()
                .getList("absent"));

        assertArrayEquals(new String[]{"1", "2", "3"}, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1 , 2,3")).build()
                .getList("k", Arrays.asList("4", "5", "6")).toArray(new String[0]));
        assertArrayEquals(new String[]{"4", "5", "6"}, ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getList("absent", Arrays.asList("4", "5", "6")).toArray(new String[0]));
    }

    @Test
    void testGetMap() {
        assertEquals("{1=1, 2=2, 3=3}", ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1 =1 , 2= 2,3 = 3 ")).build()
                .getMap("k").toString());
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "")).build()
                .getList("k"));
        assertNull(ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1 =1 , 2= 2,3 = 3 ")).build()
                .getMap("absent"));

        final LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("4", "4");
        map.put("5", "5");
        assertEquals("{1=1, 2=2, 3=3}", ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "1 =1 , 2= 2,3 = 3 ")).build()
                .getMap("k", map).toString());
        assertEquals("{4=4, 5=5}", ConfigUtil.create()
                .readFromMap(Collections.singletonMap("k", "invalid")).build()
                .getMap("absent", map).toString());
    }

    @Test
    void testReadFromSystemProperty() {
        final String k = "io.esastack.configutil.k";
        System.setProperty(k, "foo");
        try {
            assertEquals("foo",
                    ConfigUtil.create()
                            .readFromSystemProperty().build()
                            .getStr(k));
            assertNull(ConfigUtil.create()
                    .readFromSystemProperty().build()
                    .getStr("absent"));
        } finally {
            System.clearProperty(k);
        }
    }

    @Test
    void testReadFromEnv() {
        final Map<String, String> envs = System.getenv();
        final ConfigUtil util = ConfigUtil.create().readFromEnv().build();
        envs.forEach((k, v) -> assertEquals(v, util.getStr(k)));

        assertNull(util.getStr("io.esastack.configutil.$absent"));
    }

    @Test
    void testDefaultReadFromSystemPropertiesAndThenReadFromEnv() {
        final String k = "io.esastack.configutil.k";
        System.setProperty(k, "foo");
        try {
            assertEquals("foo", ConfigUtil.get().getStr(k));
            assertNull(ConfigUtil.get().getStr("io.esastack.configutil.$absent"));
            final Map<String, String> envs = System.getenv();
            envs.forEach((k1, v) -> assertEquals(v, ConfigUtil.get().getStr(k1)));

        } finally {
            System.clearProperty(k);
        }
    }
}
