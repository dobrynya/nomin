package org.nomin.util

import org.junit.*
import java.sql.Time
import java.sql.Timestamp
import static org.nomin.util.Converters.*

/**
 * Tests converters.
 * @author Dmitry Dobrynin 
 * Created at 03.05.2014 14:03.
 */
class ConvertersTest {
    @Test
    void convertersShouldConvertAnyObjectToString() {
        assert isRegistered(Object, String)
        assert "customObject" == convert(new Object() { String toString() { "customObject" }}, String)
    }

    @Test
    void convertersShouldConvertByteToAnyNumber() {
        def v = new Byte("1")
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Short.valueOf("1") == convert(v, Short)
        assert Short.valueOf("1") == convert(v, Short.TYPE)
        assert 1.0F == convert(v, Float)
        assert 1.0F == convert(v, Float.TYPE)
        assert 1.0D == convert(v, Double)
        assert 1.0D == convert(v, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(v, BigInteger)
        assert BigDecimal.valueOf(1.0D) == convert(v, BigDecimal)
    }

    @Test
    void convertersShouldConvertShortToAnyNumber() {
        def v = new Short("1")
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Byte.valueOf("1") == convert(v, Byte)
        assert Byte.valueOf("1") == convert(v, Byte.TYPE)
        assert 1.0F == convert(v, Float)
        assert 1.0F == convert(v, Float.TYPE)
        assert 1.0D == convert(v, Double)
        assert 1.0D == convert(v, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(v, BigInteger)
        assert BigDecimal.valueOf(1.0D) == convert(v, BigDecimal)
    }

    @Test
    void convertersShouldConvertIntegerToAnyNumber() {
        assert 1L == convert(1, Long)
        assert 1L == convert(1, Long.TYPE)
        assert Byte.valueOf("1") == convert(1, Byte)
        assert Byte.valueOf("1") == convert(1, Byte.TYPE)
        assert Short.valueOf("1") == convert(1, Short)
        assert Short.valueOf("1") == convert(1, Short.TYPE)
        assert 1.0F == convert(1, Float)
        assert 1.0F == convert(1, Float.TYPE)
        assert 1.0D == convert(1, Double)
        assert 1.0D == convert(1, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(1, BigInteger)
        assert BigDecimal.valueOf(1.0D) == convert(1, BigDecimal)
    }

    @Test
    void convertersShouldConvertLongToAnyNumber() {
        assert 1 == convert(1L, Integer)
        assert 1 == convert(1L, Integer.TYPE)
        assert Byte.valueOf("1") == convert(1L, Byte)
        assert Byte.valueOf("1") == convert(1L, Byte.TYPE)
        assert Short.valueOf("1") == convert(1L, Short)
        assert Short.valueOf("1") == convert(1L, Short.TYPE)
        assert 1.0F == convert(1L, Float)
        assert 1.0F == convert(1L, Float.TYPE)
        assert 1.0D == convert(1L, Double)
        assert 1.0D == convert(1L, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(1L, BigInteger)
        assert BigDecimal.valueOf(1.0D) == convert(1L, BigDecimal)
    }

    @Test
    void convertersShouldConvertFloatToAnyNumber() {
        def v = 1.0F
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Byte.valueOf("1") == convert(v, Byte)
        assert Byte.valueOf("1") == convert(v, Byte.TYPE)
        assert new Short("1") == convert(v, Short)
        assert new Short("1") == convert(v, Short.TYPE)
        assert 1.0D == convert(v, Double)
        assert 1.0D == convert(v, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(v, BigInteger)
        assert BigDecimal.valueOf(1.0D) == convert(v, BigDecimal)
    }

    @Test
    void convertersShouldConvertDoubleToAnyNumber() {
        def v = 1.0D
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Byte.valueOf("1") == convert(v, Byte)
        assert Byte.valueOf("1") == convert(v, Byte.TYPE)
        assert Short.valueOf("1") == convert(v, Short)
        assert Short.valueOf("1") == convert(v, Short.TYPE)
        assert 1.0F == convert(v, Float)
        assert 1.0F == convert(v, Float.TYPE)
        assert BigInteger.valueOf(1L) == convert(v, BigInteger)
        assert BigDecimal.valueOf(1.0D) == convert(v, BigDecimal)
    }


    @Test
    void convertersShouldConvertBigIntegerToAnyNumber() {
        def v = BigInteger.valueOf(1L)
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Byte.valueOf("1") == convert(v, Byte)
        assert Byte.valueOf("1") == convert(v, Byte.TYPE)
        assert Short.valueOf("1") == convert(v, Short)
        assert Short.valueOf("1") == convert(v, Short.TYPE)
        assert 1.0F == convert(v, Float)
        assert 1.0F == convert(v, Float.TYPE)
        assert 1.0D == convert(v, Double)
        assert 1.0D == convert(v, Double.TYPE)
        assert BigDecimal.valueOf(1.0D) == convert(v, BigDecimal)
    }

    @Test
    void convertersShouldConvertBigDecimalToAnyNumber() {
        def v = BigDecimal.valueOf(1L)
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Byte.valueOf("1") == convert(v, Byte)
        assert Byte.valueOf("1") == convert(v, Byte.TYPE)
        assert Short.valueOf("1") == convert(v, Short)
        assert Short.valueOf("1") == convert(v, Short.TYPE)
        assert 1.0F == convert(v, Float)
        assert 1.0F == convert(v, Float.TYPE)
        assert 1.0D == convert(v, Double)
        assert 1.0D == convert(v, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(v, BigInteger)
    }

    @Test
    void convertersShouldConvertStringToAnyNumber() {
        def v = "1"
        assert 1 == convert(v, Integer)
        assert 1 == convert(v, Integer.TYPE)
        assert 1L == convert(v, Long)
        assert 1L == convert(v, Long.TYPE)
        assert Byte.valueOf("1") == convert(v, Byte)
        assert Byte.valueOf("1") == convert(v, Byte.TYPE)
        assert Short.valueOf("1") == convert(v, Short)
        assert Short.valueOf("1") == convert(v, Short.TYPE)
        assert 1.0F == convert(v, Float)
        assert 1.0F == convert(v, Float.TYPE)
        assert 1.0D == convert(v, Double)
        assert 1.0D == convert(v, Double.TYPE)
        assert BigInteger.valueOf(1L) == convert(v, BigInteger)
        assert BigDecimal.valueOf(1L) == convert(v, BigDecimal)
    }

    @Test
    void convertersShouldConvertBetweenWrapperAndPrimitive() {
        assert (byte) 1 == convert(new Byte((byte) 1), Byte.TYPE)
        assert (short) 1 == convert((short) 1, Short.TYPE)
        assert 1 == convert(1, Integer.TYPE)
        assert 1L == convert(1L, Long.TYPE)
        assert 1F == convert(1F, Float.TYPE)
        assert 1D == convert(1D, Double.TYPE)
    }

    @Test
    void convertersShouldConvertString2Class_File_URL_bool() {
        assert convert("true", Boolean)
        assert convert("true", Boolean.TYPE)
        assert !convert("false", Boolean)
        assert this.class == convert("org.nomin.util.ConvertersTest", Class)
        assert new URL("https://github.com/dobrynya/nomin") == convert("https://github.com/dobrynya/nomin", URL)
        assert new File("pom.xml") == convert("pom.xml", File)
        assert new URI("https://github.com/dobrynya/nomin") == convert("https://github.com/dobrynya/nomin", URI)
    }

    @Test
    void convertersShouldConvertDatesAndTimes() {
        def v = System.currentTimeMillis()
        def sqlDate = convert(new Date(v), java.sql.Date)
        assert sqlDate instanceof java.sql.Date && v == sqlDate.getTime()
        sqlDate = convert(new Date(v), Time)
        assert sqlDate instanceof Time && v == sqlDate.getTime()
        sqlDate = convert(new Date(v), Timestamp)
        assert sqlDate instanceof Timestamp && v == sqlDate.getTime()
    }
}
