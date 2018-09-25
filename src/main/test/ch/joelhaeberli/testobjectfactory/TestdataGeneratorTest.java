package ch.joelhaeberli.testobjectfactory;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestdataGeneratorTest {

    private int STR_LENGTH = 23;
    private boolean DEFAULT_BOOL = true;

    private TestdataGenerator testdataGenerator;

    @Before
    public void before() {
        this.testdataGenerator = new TestdataGenerator(STR_LENGTH, DEFAULT_BOOL);
    }

    @Test
    public void testStringIsProvided() {
        Assertions.assertThat(testdataGenerator.isProvided(String.class)).isTrue();
    }

    @Test
    public void testStringLength() throws Exception {
        String testString = testdataGenerator.generateValue(String.class);
        Assertions.assertThat(testString.length()).isEqualTo(STR_LENGTH);
    }

    @Test
    public void testDefaultBoolean() throws Exception {
        boolean testBool = testdataGenerator.generateValue(Boolean.class);
        Assertions.assertThat(testBool).isEqualTo(DEFAULT_BOOL);
    }

    @Test
    public void testByteArrayPrimitive() throws Exception {
        byte[] b = testdataGenerator.generateValue(byte[].class);
        Assertions.assertThat(b.length).isEqualTo(TestdataGenerator.BYTE_ARRAY_VALUES.length);
    }

    @Test
    public void testByteArray() throws Exception {
        Byte[] b = testdataGenerator.generateValue(Byte[].class);
        Assertions.assertThat(b.length).isEqualTo(TestdataGenerator.BYTE_ARRAY_VALUES.length);
    }

    @After
    public void after() {
        testdataGenerator = null;
    }
}
