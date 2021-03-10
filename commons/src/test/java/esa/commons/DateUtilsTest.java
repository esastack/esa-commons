package esa.commons;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateUtilsTest {

    @Test
    void testConvert() {
        final Date now = new Date();
        assertEquals(now.getTime() / 1000L * 1000L,
                DateUtils.toDate(DateUtils.toString(now, DateUtils.yyyyMMddHHmmss),
                        DateUtils.yyyyMMddHHmmss).getTime() / 1000L * 1000L);
        assertThrows(RuntimeException.class, () -> DateUtils.toDate("abc", DateUtils.yyyyMMddHHmmss));
    }

}
