package ch.joelhaeberli.testobjectfactory;

import ch.joelhaeberli.testobjectfactory.dummies.DummyClassA;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestobjectGeneratorTest {

    TestobjectGenerator testobjectGenerator;

    @Before
    public void before() {
        this.testobjectGenerator = new TestobjectGenerator(new TestdataGenerator(6, true));
    }

    @Test
    public void testGetTestObject() {
        DummyClassA dummyClassA = this.testobjectGenerator.getTestObject(DummyClassA.class);
        Assertions.assertThat(dummyClassA).isNotNull();
    }

    @Test
    public void testEagerObjectGeneration() {
        DummyClassA dummyClassA = this.testobjectGenerator.getTestObjectEager(DummyClassA.class, 3);
        Assertions.assertThat(dummyClassA).isNotNull();
    }

    @After
    public void after() {
        this.testobjectGenerator = null;
    }
}
