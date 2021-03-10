package esa.commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ESADateUtilsTest {

    @Test
    void testConvert() {
        final Date now = new Date();
        assertEquals(now.getTime() / 1000L * 1000L,
                ESADateUtils.toDate(DateUtils.toString(now, ESADateUtils.yyyyMMddHHmmss),
                        ESADateUtils.yyyyMMddHHmmss).getTime() / 1000L * 1000L);
        assertThrows(RuntimeException.class, () -> ESADateUtils.toDate("abc", ESADateUtils.yyyyMMddHHmmss));
    }

}
