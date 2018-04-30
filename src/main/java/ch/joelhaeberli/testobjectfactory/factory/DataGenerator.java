package ch.joelhaeberli.testobjectfactory.factory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Generates test-data random-based
 * <p>
 * If you want to generate test-data for specific types create a method below that returns a value of your desired type.
 * <p>
 * !!!ATTENTION!!! : The method-name MUST start with "get".
 */
public class DataGenerator {

    private final int LONG_STRING_LENGTH = 30;
    private final int SHORT_STRING_LENGTH = 6;

    private boolean useShortString = false;
    private boolean defaultBooleanValue = true;

    public final String METHOD_SUFIX = "get";

    private final Random RANDOM = new Random();

    private final LocalDateTime LOCAL_DATE_TIME_VALUE = LocalDateTime.now();

    private final String ALPHANUMERICS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz1234567890";

    private List<Type> providedTypes;

    public DataGenerator() {
        this.providedTypes = loadProvidedTypes();
    }

    public DataGenerator(Boolean useShortString, Boolean defaultBooleanValue) {
        this.useShortString = useShortString;
        this.defaultBooleanValue = defaultBooleanValue;
        this.providedTypes = loadProvidedTypes();
    }

    public <T> T generateValue(Class<T> clazz) throws Exception {
        Method m = loadMethodWithReturnType(clazz);
        if (m != null) {
            return (T) m.invoke(this);
        } else {
            throw new NoSuchMethodException("loadMethodWithReturnType in DataGenerator could not find Method with return-type <" + clazz + ">");
        }
    }

    public <T> boolean isProvided(Class<T> clazz) {
        return (this.providedTypes.contains(clazz));
    }

    public boolean isUseShortString() {
        return useShortString;
    }

    public boolean isDefaultBooleanValue() {
        return defaultBooleanValue;
    }

    private Integer getInteger() {
        return RANDOM.nextInt();
    }

    private int getIntegerPrimitive() {
        return getInteger();
    }

    private Short getShort() {
        return (short) RANDOM.nextInt();
    }

    private Long getLong() {
        return RANDOM.nextLong();
    }

    private long getLongPrimitive() {
        return getLong();
    }

    private String getString() {
        if (useShortString) {
            return genString(SHORT_STRING_LENGTH);
        }
        return genString(LONG_STRING_LENGTH);
    }

    private Boolean getBoolean() {
        return new Boolean(defaultBooleanValue);
    }

    private BigDecimal getBigDecimal() {
        return new BigDecimal(getLong());
    }

    private LocalDate getLocalDate() {
        return LocalDate.now();
    }

    private boolean[] getBooleanArray() {
        boolean[] booleans = new boolean[1];
        booleans[0] = true;
        return booleans;
    }

    private LocalDateTime getLocalDateTime() {
        return LOCAL_DATE_TIME_VALUE;
    }

    private Date getSqlDate() {
        return new Date(System.currentTimeMillis());
    }

    private Timestamp getSqlTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    private java.util.Date getJavaUtilDate() {
        return new java.util.Date(System.currentTimeMillis());
    }

    private String genString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= length; i++) {
            sb.append(ALPHANUMERICS.charAt(RANDOM.nextInt(ALPHANUMERICS.length())));
        }
        return sb.toString();
    }

    private List<Type> loadProvidedTypes() {

        List<Type> types = new ArrayList<>();

        for (Method m : Arrays.asList(this.getClass().getDeclaredMethods())) {
            if (m.getName().contains(METHOD_SUFIX)) {
                types.add(m.getReturnType());
            }
        }

        return types;
    }

    private <T> Method loadMethodWithReturnType(Class<T> clazz) {
        for (Method m : Arrays.asList(this.getClass().getDeclaredMethods())) {
            if (m.getReturnType() == clazz) {
                return m;
            }
        }
        return null;
    }
}
