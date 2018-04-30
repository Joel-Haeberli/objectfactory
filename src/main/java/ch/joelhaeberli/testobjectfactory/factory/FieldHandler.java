package ch.joelhaeberli.testobjectfactory.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static ch.joelhaeberli.testobjectfactory.factory.CollectionHandler.*;

public class FieldHandler {

    public static <T> List<Field> getDeclaredFields(Class<T> clazz) {
        List<Field> fields = new LinkedList<>(Arrays.asList(clazz.getDeclaredFields()));
        List<Field> fieldsToRemove = new ArrayList<>();
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers())) {
                fieldsToRemove.add(f);
            }
        }
        fields.removeAll(fieldsToRemove);
        if (isCollection(clazz) || isMap(clazz)) {
            fields.add(abuseField(clazz));
        }
        return fields;
    }

    private static <T> Field abuseField(Class<T> clazz) {

        Field f = null;

        if (isCollection(clazz)) {
            f = changeCollectionField(clazz);
        }
        if (isMap(clazz)) {
            f = changeMapField(clazz);
        }

        return f;
    }

    private Collection<?> collectionField;
    private static <T> Field changeCollectionField(Class<T> clazz) {
        Field f = null;

        try {
            f = FieldHandler.class.getDeclaredField("collectionField");
            setTypeAndClassOfField(f, clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return f;
    }

    private Map<?,?> mapField;
    private static <T> Field changeMapField(Class<T> clazz) {
        Field f = null;

        try {
            f = FieldHandler.class.getDeclaredField("mapField");
            setTypeAndClassOfField(f, clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return f;
    }

    public static <T> List<Field> getFieldsWithSameTypeAsParent(Class<T> clazz, List<Field> fields) {

        List<Field> toRemove = new ArrayList<>();

        for (Field f : fields) {
            if (typesAreEqual(clazz, f)) {
                toRemove.add(f);
            }
        }

        return toRemove;
    }

    public static <T> Field getFieldsWithSameTypeAsParentOfParent(Class<T> clazz, Field f, List<Field> declaredFields) {

        declaredFields = changeCollectionAndMapFieldsToFieldsOfCollectionOrMapType(declaredFields);
        if (getFieldsWithSameTypeAsParent(clazz, declaredFields).size() != 0) {
            return f;
        }
        return null;
    }

    public static  <T> Field getFieldsWithSameTypeAsParentOfParent(Class<T> clazz, Field field) {

        List<Field> fields = new ArrayList<>();
        fields.add(field);
        fields = changeCollectionAndMapFieldsToFieldsOfCollectionOrMapType(fields);

        return fields.get(0);
    }

    public static HashMap<Field, Class<?>> getTypesOfFields(List<Field> fields) {
        HashMap<Field, Class<?>> fieldsAndClasses = new HashMap<>();
        for (Field f : fields) {
            fieldsAndClasses.put(f,f.getType());
        }
        return fieldsAndClasses;
    }

    public static List<Field> changeCollectionAndMapFieldsToFieldsOfCollectionOrMapType(List<Field> declaredFields) {

        HashMap<Field, Class<?>> fieldsAndClasses = getTypesOfFields(declaredFields);

        for (Field c: fieldsAndClasses.keySet()) { //Class<?> clazz : fieldsAndClasses.values()
            if (isCollection(c)) {
                declaredFields.remove(c); //Remove the field from type collection
                declaredFields.add(setTypeAndClassOfField(c, getCollectionType(c))); //Field with Collection-Type
            }

            if (isMap(c)) {
                declaredFields.remove(c); //Remove the field from type map
                declaredFields.add(setTypeAndClassOfField(c, getKeyTypeOfMap(c))); //Field with Map-Key-Type
                declaredFields.add(setTypeAndClassOfField(c, getValueTypeOfMap(c))); //Field with Map-Value-Type
            }
        }
        return declaredFields;
    }

    public static Field setTypeAndClassOfField(Field f, Class typeToSet) {

        try {

            Field classField = f.getClass().getDeclaredField("clazz");
            Field typeField = f.getClass().getDeclaredField("type");

            classField.setAccessible(true);
            typeField.setAccessible(true);

            classField.set(f, typeToSet);
            typeField.set(f, typeToSet);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    private static <A> boolean typesAreEqual(Class<A> parent, Field field) {

        if (parent == field.getClass()) {
            return true;
        }

        if (isCollection(field)) {
            if (parent == getCollectionType(field)) {
                return true;
            }
        }

        if (isMap(field)) {
            if (parent == getKeyTypeOfMap(field) || parent == getValueTypeOfMap(field)) {
                return true;
            }
        }
        return false;
    }
}
