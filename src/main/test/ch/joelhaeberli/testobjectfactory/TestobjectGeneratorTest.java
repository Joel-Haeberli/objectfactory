package ch.joelhaeberli.testobjectfactory;

import ch.joelhaeberli.testobjectfactory.TestdataGenerator;
import ch.joelhaeberli.testobjectfactory.TestobjectGenerator;
import ch.joelhaeberli.testobjectfactory.test.dummies.DummyClassA;
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
    public void testEagerObjectGeneration() throws Exception {
        DummyClassA dummyClassA = this.testobjectGenerator.getTestObjectEager(DummyClassA.class, 3);
        Assertions.assertThat(dummyClassA).isNotNull();
    }

    @Test
    public void testEagerObjectGeneration2() throws Exception {
        Benutzer dummyClassA = this.testobjectGenerator.getTestObjectEager(Benutzer.class, 10);
        Assertions.assertThat(dummyClassA).isNotNull();
    }

    @After
    public void after() {
        this.testobjectGenerator = null;
    }
}
