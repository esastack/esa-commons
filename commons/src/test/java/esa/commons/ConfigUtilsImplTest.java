package esa.commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigUtilsImplTest {

    @Test
    void testConstructor() {
        assertNotNull(new ConfigUtilsImpl(k -> k));
        assertThrows(NullPointerException.class, () -> new ConfigUtilsImpl(null));
    }

    @Test
    void testGetStr() {
        assertEquals("foo", new ConfigUtilsImpl(k -> k).getStr("foo"));
        assertThrows(IllegalArgumentException.class, () -> new ConfigUtilsImpl(k -> k).getStr(null));
        assertThrows(IllegalArgumentException.class, () -> new ConfigUtilsImpl(k -> k).getStr(""));
    }

}
