package ch.joelhaeberli.testobjectfactory;

public class TestobjectGeneratorFactory {

    public static TestobjectGenerator createTestobjectGenerator(int strLength, boolean defaultBoolean) {
        return new TestobjectGenerator(new TestdataGenerator(strLength, defaultBoolean));
    }

    public static TestobjectGenerator createTestobjectGenerator() {
        return createTestobjectGenerator(6, true);
    }
}
