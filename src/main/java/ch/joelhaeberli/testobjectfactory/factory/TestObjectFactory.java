package ch.joelhaeberli.testobjectfactory.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static ch.joelhaeberli.testobjectfactory.factory.CollectionHandler.*;
import static ch.joelhaeberli.testobjectfactory.factory.FieldHandler.getDeclaredFields;
import static ch.joelhaeberli.testobjectfactory.factory.FieldHandler.getFieldsWithSameTypeAsParent;
import static ch.joelhaeberli.testobjectfactory.factory.FieldHandler.getFieldsWithSameTypeAsParentOfParent;

/**
 * READ CLASS DESCRIPTION OF DATAGENERATOR.JAVA
 */
public class TestObjectFactory {

    private static TestObjectFactory INSTANCE;

    private DataGenerator dataGenerator;
    private List<Field> doNotGenerateValuesForTheseFields;

    public static TestObjectFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TestObjectFactory();
        }
        return INSTANCE;
    }

    public static TestObjectFactory getInstance(Boolean useShortString, Boolean defaultBooleanValue) {
        if (INSTANCE == null ||
                INSTANCE.dataGenerator.isUseShortString() != useShortString ||
                INSTANCE.dataGenerator.isDefaultBooleanValue() != defaultBooleanValue) {
            INSTANCE = new TestObjectFactory(useShortString, defaultBooleanValue);
        }
        return INSTANCE;
    }

    private TestObjectFactory() {
        dataGenerator = new DataGenerator();
        doNotGenerateValuesForTheseFields = new ArrayList<>();
    }

    private TestObjectFactory(Boolean useShortString, Boolean defaultBooleanValue) {
        dataGenerator = new DataGenerator(useShortString, defaultBooleanValue);
    }

    //Generates Test-Object with Random-Data or with defines data from the DataGenerator
    public <T> T getTestObject(Class<T> clazz) {
        try {

            T object = instantiateObject(clazz);

            List<Field> fields = getDeclaredFields(clazz);
            fields = eliminateCycles(clazz, fields);
            HashMap<Field, Object> fieldsAndValues = getValuesForTypes(fields);

            fillFields(object, fieldsAndValues);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    private <T> T instantiateObject(Class<T> clazz) {

        try {
            Class ref = Class.forName(clazz.getName());
            return (T) ref.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not instantiate an object of type <" + clazz.getTypeName() + ">");
        }
    }

    /**
     * Eliminates bidirectional cycles
     *
     * @param clazz the parent class
     * @param fields the fields of the parent
     * @return
     */
    private <T> List<Field> eliminateCycles(Class<T> clazz, List<Field> fields) {

        List<Field> secondLevelCyclesOfClazz = new ArrayList<>();
        List<Field> directCyclesOfClazz = getFieldsWithSameTypeAsParent(clazz, fields);

        fields.forEach(field -> {
            Field subF;
            if (isCollection(field) || isMap(field)) {
                subF = getFieldsWithSameTypeAsParentOfParent(clazz, field);

            } else {
                subF = getFieldsWithSameTypeAsParentOfParent(clazz, field, getDeclaredFields(field.getType()));
            }
            if (subF != null) {
                secondLevelCyclesOfClazz.add(subF);
            }
        });

        fields.removeAll(directCyclesOfClazz);
        fields.removeAll(secondLevelCyclesOfClazz);

        return fields;
    }

    private HashMap<Field, Object> getValuesForTypes(List<Field> fields) throws Exception {

        HashMap<Field, Object> fieldsAndValues = new HashMap<>();

        for (Field f : fields) {
            if (isGeneratedFromDataGenerator(f.getType())) {
                if (f.getType() == String.class && f.getName().toLowerCase().contains("up")) {
                    fieldsAndValues.put(f, (String)dataGenerator.generateValue(f.getType()).toString().toUpperCase());
                } else {
                    fieldsAndValues.put(f, dataGenerator.generateValue(f.getType()));
                }
            } else if (isCollection(f)) {
                fieldsAndValues.put(f, getCollection(f));
            } else if (isMap(f)) {
                fieldsAndValues.put(f, getMap(f));
            } else {
                Object o = getTestObject(f.getType());
                fieldsAndValues.put(f, o);
            }
        }

        return fieldsAndValues;
    }

    private <T> boolean isGeneratedFromDataGenerator(Class<T> clazz) {
        return dataGenerator.isProvided(clazz);
    }

    private void fillFields(Object o, HashMap<Field, Object> fieldsAndValues) throws Exception {
        for (Field f : fieldsAndValues.keySet()) {
            if (Arrays.asList(f.getModifiers()).contains("final")) {
                continue;
            }
            f.setAccessible(true);
            try {
                f.set(o, fieldsAndValues.get(f));
            } catch (Exception e) {
                throw new IllegalAccessException("Could not set field <" + f + "> with value <" + fieldsAndValues.get(f) + ">");
            }
        }
    }

    /**
     * @param subclassObject
     * @param fieldsAndValues fields and values for subclass and parent
     * @throws Exception
     */
    private void fillFieldsOfParent(Object subclassObject, HashMap<Field, Object> fieldsAndValues) throws Exception {
        Class parent = subclassObject.getClass().getSuperclass();
        List<Field> fieldsOfParent = getDeclaredFields(parent);
        List<Field> fieldsToRemove = new ArrayList<>();
        for (Field f : fieldsAndValues.keySet()) {
            if (fieldsOfParent.contains(f)) {
                f.setAccessible(true);
                try {
                    f.set(subclassObject, fieldsAndValues.get(f));
                    fieldsToRemove.add(f);
                } catch (Exception e) {
                    throw new IllegalAccessException("Could not set parent-field <" + f + "> with value <" + fieldsAndValues.get(f) + ">");
                }
            }
        }
        HashMap<Field, Object> subclassFieldsAndValues = fieldsAndValues;
        for (Field f : fieldsToRemove) {
            subclassFieldsAndValues.remove(f);
        }
        fillFields(subclassObject, subclassFieldsAndValues);
    }
}
