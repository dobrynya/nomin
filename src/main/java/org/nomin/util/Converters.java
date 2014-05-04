package org.nomin.util;

import org.nomin.core.NominException;
import org.slf4j.*;
import java.io.File;
import java.math.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;
import static java.lang.String.format;
import static java.util.Arrays.asList;

/**
 * Supplies scalar converters.
 * @author Dmitry Dobrynin
 *         Created at 26.04.2014 21:25.
 */
@SuppressWarnings("unchecked")
public class Converters {
    private static Logger logger = LoggerFactory.getLogger(Converters.class);
    private static final Map<Class<?>, Map<Class<?>, ScalarConverter<?, ?>>> converters = new HashMap<Class<?>, Map<Class<?>, ScalarConverter<?, ?>>>();

    public static final ScalarConverter<Object, String> obj2str = new ScalarConverter<Object, String>() {
        public String convert(Object source) {
            return source.toString();
        }
    };
    public static final ScalarConverter<Number, Byte> number2byte = new ScalarConverter<Number, Byte>() {
        public Byte convert(Number source) {
            return source.byteValue();
        }
    };
    public static final ScalarConverter<Number, Short> number2short = new ScalarConverter<Number, Short>() {
        public Short convert(Number source) {
            return source.shortValue();
        }
    };
    public static final ScalarConverter<Number, Integer> number2int = new ScalarConverter<Number, Integer>() {
        public Integer convert(Number source) {
            return source.intValue();
        }
    };
    public static final ScalarConverter<Number, Long> number2long = new ScalarConverter<Number, Long>() {
        public Long convert(Number source) {
            return source.longValue();
        }
    };
    public static final ScalarConverter<Number, Float> number2float = new ScalarConverter<Number, Float>() {
        public Float convert(Number source) {
            return source.floatValue();
        }
    };
    public static final ScalarConverter<Number, Double> number2dbl = new ScalarConverter<Number, Double>() {
        public Double convert(Number source) {
            return source.doubleValue();
        }
    };
    public static final ScalarConverter<Number, BigInteger> number2bigint = new ScalarConverter<Number, BigInteger>() {
        public BigInteger convert(Number source) {
            return BigInteger.valueOf(source.longValue());
        }
    };
    public static final ScalarConverter<Number, BigDecimal> number2bigdec = new ScalarConverter<Number, BigDecimal>() {
        public BigDecimal convert(Number source) {
            return BigDecimal.valueOf(source.doubleValue());
        }
    };
    public static final ScalarConverter<String, Class<?>> str2class = new ScalarConverter<String, Class<?>>() {
        public Class<?> convert(String source) {
            try { return Class.forName(source); } catch (ClassNotFoundException e) {
                throw new NominException("Could not find specified class!", e);
            }
        }
    };
    public static final ScalarConverter<String, URL> str2url = new ScalarConverter<String, URL>() {
        public URL convert(String source) {
            try { return new URL(source); } catch (MalformedURLException e) {
                throw new NominException(source + " is invalid URL!", e);
            }
        }
    };
    public static final ScalarConverter<String, URI> str2uri = new ScalarConverter<String, URI>() {
        public URI convert(String source) {
            try { return new URI(source); } catch (URISyntaxException e) {
                throw new NominException(source + " is invalid URI!", e);
            }
        }
    };
    public static final ScalarConverter<String, File> str2file = new ScalarConverter<String, File>() {
        public File convert(String source) {
            return new File(source);
        }
    };
    public static final ScalarConverter<String, Byte> str2byte = new ScalarConverter<String, Byte>() {
        public Byte convert(String source) {
            return Byte.valueOf(source);
        }
    };
    public static final ScalarConverter<String, Short> str2short = new ScalarConverter<String, Short>() {
        public Short convert(String source) {
            return Short.valueOf(source);
        }
    };
    public static final ScalarConverter<String, Integer> str2int = new ScalarConverter<String, Integer>() {
        public Integer convert(String source) {
            return Integer.valueOf(source);
        }
    };
    public static final ScalarConverter<String, Long> str2long = new ScalarConverter<String, Long>() {
        public Long convert(String source) {
            return Long.valueOf(source);
        }
    };
    public static final ScalarConverter<String, Float> str2float = new ScalarConverter<String, Float>() {
        public Float convert(String source) {
            return Float.valueOf(source);
        }
    };
    public static final ScalarConverter<String, Double> str2dbl = new ScalarConverter<String, Double>() {
        public Double convert(String source) {
            return Double.valueOf(source);
        }
    };
    public static final ScalarConverter<String, BigInteger> str2bigint = new ScalarConverter<String, BigInteger>() {
        public BigInteger convert(String source) {
            return new BigInteger(source);
        }
    };
    public static final ScalarConverter<String, BigDecimal> str2bigdec = new ScalarConverter<String, BigDecimal>() {
        public BigDecimal convert(String source) {
            return new BigDecimal(source);
        }
    };
    public static final ScalarConverter<Date, java.sql.Date> udate2sdate = new ScalarConverter<Date, java.sql.Date>() {
        public java.sql.Date convert(Date source) {
            return new java.sql.Date(source.getTime());
        }
    };
    public static final ScalarConverter<Date, Timestamp> udate2timestamp = new ScalarConverter<Date, Timestamp>() {
        public Timestamp convert(Date source) {
            return new Timestamp(source.getTime());
        }
    };
    public static final ScalarConverter<Date, Time> udate2time = new ScalarConverter<Date, Time>() {
        public Time convert(Date source) {
            return new Time(source.getTime());
        }
    };
    private static final ScalarConverter<String, Boolean> str2bool = new ScalarConverter<String, Boolean>() {
        public Boolean convert(String source) {
            return Boolean.valueOf(source);
        }
    };

    public static <S, T> void register(Class<S> source, Class<T> target, ScalarConverter<? super S, ? extends T> converter) {
        logger.debug("Registering converter from {} to {}", source, target);
        Map<Class<?>, ScalarConverter<?, ?>> registered = converters.get(source);
        if (registered == null) converters.put(source, registered = new HashMap<Class<?>, ScalarConverter<?, ?>>());
        registered.put(target, converter);
    }

    public static <S, T> void register(List<Class<S>> sources, List<Class<T>> targets, ScalarConverter<? super S, ? extends T> converter) {
        for (Class<S> s : sources) for (Class<T> t : targets) register(s, t, converter);
    }

    public static boolean isRegistered(Class<?> source, Class<?> target) {
        return findConverter(source, target) != null;
    }

    public static ScalarConverter<Object,  Object> findConverter(Class<?> source, Class<?> target) {
        Map<Class<?>, ScalarConverter<?, ?>> registered = converters.get(source);
        return (ScalarConverter<Object, Object>) (registered != null && registered.containsKey(target) ?
                        registered.get(target) : target == String.class ? obj2str : null);
    }

    public static Object convert(Object source, Class<?> target) {
        ScalarConverter converter = findConverter(source.getClass(), target);
        if (converter == null)
            throw new NominException(format("Scalar converter between %s and %s has not been registered!", source.getClass(), target));
        return converter.convert(source);
    }

    static {
        register(asList(Byte.class, Byte.TYPE), asList(Short.class, Short.TYPE), number2short);
        register(asList(Byte.class, Byte.TYPE), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(Byte.class, Byte.TYPE), asList(Long.class, Long.TYPE), number2long);
        register(asList(Byte.class, Byte.TYPE), asList(Float.class, Float.TYPE), number2float);
        register(asList(Byte.class, Byte.TYPE), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(Byte.class, Byte.TYPE), asList(BigInteger.class), number2bigint);
        register(asList(Byte.class, Byte.TYPE), asList(BigDecimal.class), number2bigdec);

        register(asList(Short.class, Short.TYPE), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(Short.class, Short.TYPE), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(Short.class, Short.TYPE), asList(Long.class, Long.TYPE), number2long);
        register(asList(Short.class, Short.TYPE), asList(Float.class, Float.TYPE), number2float);
        register(asList(Short.class, Short.TYPE), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(Short.class, Short.TYPE), asList(BigInteger.class), number2bigint);
        register(asList(Short.class, Short.TYPE), asList(BigDecimal.class), number2bigdec);

        register(asList(Integer.class, Integer.TYPE), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(Integer.class, Integer.TYPE), asList(Short.class, Short.TYPE), number2short);
        register(asList(Integer.class, Integer.TYPE), asList(Long.class, Long.TYPE), number2long);
        register(asList(Integer.class, Integer.TYPE), asList(Float.class, Float.TYPE), number2float);
        register(asList(Integer.class, Integer.TYPE), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(Integer.class, Integer.TYPE), asList(BigInteger.class), number2bigint);
        register(asList(Integer.class, Integer.TYPE), asList(BigDecimal.class), number2bigdec);

        register(asList(Long.class, Long.TYPE), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(Long.class, Long.TYPE), asList(Short.class, Short.TYPE), number2short);
        register(asList(Long.class, Long.TYPE), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(Long.class, Long.TYPE), asList(Float.class, Float.TYPE), number2float);
        register(asList(Long.class, Long.TYPE), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(Long.class, Long.TYPE), asList(BigInteger.class), number2bigint);
        register(asList(Long.class, Long.TYPE), asList(BigDecimal.class), number2bigdec);

        register(asList(Float.class, Float.TYPE), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(Float.class, Float.TYPE), asList(Short.class, Short.TYPE), number2short);
        register(asList(Float.class, Float.TYPE), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(Float.class, Float.TYPE), asList(Long.class, Long.TYPE), number2long);
        register(asList(Float.class, Float.TYPE), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(Float.class, Float.TYPE), asList(BigInteger.class), number2bigint);
        register(asList(Float.class, Float.TYPE), asList(BigDecimal.class), number2bigdec);

        register(asList(Double.class, Double.TYPE), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(Double.class, Double.TYPE), asList(Short.class, Short.TYPE), number2short);
        register(asList(Double.class, Double.TYPE), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(Double.class, Double.TYPE), asList(Long.class, Long.TYPE), number2long);
        register(asList(Double.class, Double.TYPE), asList(Float.class, Float.TYPE), number2float);
        register(asList(Double.class, Double.TYPE), asList(BigInteger.class), number2bigint);
        register(asList(Double.class, Double.TYPE), asList(BigDecimal.class), number2bigdec);

        register(asList(BigInteger.class), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(BigInteger.class), asList(Short.class, Short.TYPE), number2short);
        register(asList(BigInteger.class), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(BigInteger.class), asList(Long.class, Long.TYPE), number2long);
        register(asList(BigInteger.class), asList(Float.class, Float.TYPE), number2float);
        register(asList(BigInteger.class), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(BigInteger.class), asList(BigDecimal.class), number2bigdec);

        register(asList(BigDecimal.class), asList(Byte.class, Byte.TYPE), number2byte);
        register(asList(BigDecimal.class), asList(Short.class, Short.TYPE), number2short);
        register(asList(BigDecimal.class), asList(Integer.class, Integer.TYPE), number2int);
        register(asList(BigDecimal.class), asList(Long.class, Long.TYPE), number2long);
        register(asList(BigDecimal.class), asList(Float.class, Float.TYPE), number2float);
        register(asList(BigDecimal.class), asList(Double.class, Double.TYPE), number2dbl);
        register(asList(BigDecimal.class), asList(BigInteger.class), number2bigint);

        register(asList(String.class), asList(Boolean.class, Boolean.TYPE), str2bool);
        register(asList(String.class), asList(Byte.class, Byte.TYPE), str2byte);
        register(asList(String.class), asList(Short.class, Short.TYPE), str2short);
        register(asList(String.class), asList(Integer.class, Integer.TYPE), str2int);
        register(asList(String.class), asList(Long.class, Long.TYPE), str2long);
        register(asList(String.class), asList(Float.class, Float.TYPE), str2float);
        register(asList(String.class), asList(Double.class, Double.TYPE), str2dbl);
        register(asList(String.class), asList(BigInteger.class), str2bigint);
        register(asList(String.class), asList(BigDecimal.class), str2bigdec);
        register(String.class, URI.class, str2uri);
        register(String.class, Class.class, str2class);
        register(String.class, File.class, str2file);
        register(String.class, URL.class, str2url);
        register(String.class, URI.class, str2uri);

        register(Date.class, java.sql.Date.class, udate2sdate);
        register(Date.class, Time.class, udate2time);
        register(Date.class, Timestamp.class, udate2timestamp);
    }
}
