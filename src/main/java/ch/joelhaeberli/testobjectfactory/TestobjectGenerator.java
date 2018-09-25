package ch.joelhaeberli.testobjectfactory;

import ch.joelhaeberli.testobjectfactory.utils.CollectionMapUtil;
import ch.joelhaeberli.testobjectfactory.utils.FieldUtil;

import java.lang.reflect.Field;
import java.util.HashMap;

public class TestobjectGenerator {

    private TestdataGenerator testdataGenerator;
    private CollectionMapUtil collectionMapUtil;
    private FieldUtil fieldUtil;

    /**
     * TestobjectGenerator generates java-objects with dummy values. It's mainly used for testing reasons
     *
     * @param testdataGenerator A dummy data generator
     */
    public TestobjectGenerator(TestdataGenerator testdataGenerator) {
        this.testdataGenerator = testdataGenerator;
        this.collectionMapUtil = new CollectionMapUtil();
        this.fieldUtil = new FieldUtil(this.collectionMapUtil);
    }

    public <T> T getTestObjectEager(Class<T> clazz, int levels) {
         GenerationWrapper generationWrapper = new GenerationWrapper(clazz, this, this.testdataGenerator, new HashMap<Class, Object>(), levels);
        try {
            return (T) generationWrapper.composeObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    public <T, S> T getTestObjectEagerWithParent(Class<T> clazz, int levels) {
        GenerationWrapper generationWrapper = new GenerationWrapper(clazz, this, this.testdataGenerator, new HashMap<Class, Object>(), levels);
        return (T) fillParent(generationWrapper, levels);
    }

    /**
     * Creates an object filled with data from the given class
     *
     * @param clazz type of object
     * @param <T>   generic
     * @return filled object of clazz
     */
    public <T> T getTestObject(Class<T> clazz) {
        try {

            int levels = 2;
            GenerationWrapper generationWrapper = new GenerationWrapper(clazz, this, this.testdataGenerator, new HashMap<Class, Object>(), levels);

            return (T) generationWrapper.composeObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Returns an empty instance from the given class. It's used in the TestobjectGenerator to easily prevent cycles
     * @param clazz type of object
     * @param <T> generic
     * @return empty object of clazz
     */
    public <T> T getTestObjectOnlyInstance(Class<T> clazz) {
        try {
            return instantiateObject(clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * simply instantiates an object of clazz
     * @param clazz
     * @param <T> generic
     * @return object of T
     */
    private <T> T instantiateObject(Class<T> clazz) {

        ClassLoader loader = ClassLoader.getSystemClassLoader(); //clazz.getClassLoader();

        try {
            Class ref = loader.loadClass(clazz.getName()); //Class.forName(clazz.getName());
            return clazz.cast(ref.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not instantiate an object of type <" + clazz.getTypeName() + ">");
        }
    }

    /**
     * Fills the parent of the current object.
     *
     * @param generationWrapper holding information about the current object
     * @param levels how deep the parent should be generated
     * @return returns instance from type T
     */
    private <T> T fillParent(GenerationWrapper<T> generationWrapper, int levels) {
        //GenerationWrapper for parent-object
        GenerationWrapper parentWrapper = new GenerationWrapper(generationWrapper.getBaseClass().getSuperclass(), this, this.testdataGenerator, new HashMap<Class, Object>(), levels);
        T object;

        try {
            //compose base-object
            object = generationWrapper.composeObject();

            //get fields of parent
            Field[] parentFields = generationWrapper.getBaseClass().getSuperclass().getDeclaredFields();

            // set each field to the generated value in parent-object
            for (int i = 0; i <= parentFields.length -1; i++) {
                parentFields[i].setAccessible(true);
                Object value = TestobjectGeneratorFactory.createTestobjectGenerator().getTestObject(parentFields[i].getType());
                parentFields[i].set(object, value);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not instantiate an object as parent of type <" + generationWrapper.getBaseClass().getSuperclass() + ">");
        }

        //return base-object with its parent-properties set
        return object;
    }
}
