package ch.joelhaeberli.testobjectfactory.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class FieldUtil {

    private CollectionMapUtil collectionMapUtil;

    public FieldUtil(CollectionMapUtil collectionMapUtil) {
        this.collectionMapUtil = collectionMapUtil;
    }

    /**
     * Returns fields of clazz, but removes field declared final.
     *
     * @param clazz
     * @param <T>
     * @return List of fields
     */
    public <T> List<Field> getDeclaredFields(Class<T> clazz) {
        List<Field> fields = new LinkedList<>(Arrays.asList(clazz.getDeclaredFields()));
        List<Field> fieldsToRemove = new ArrayList<>();
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers())) {
                fieldsToRemove.add(f);
            }
        }
        fields.removeAll(fieldsToRemove);
        if (collectionMapUtil.isCollection(clazz) || collectionMapUtil.isMap(clazz)) {
            fields.add(abuseField(clazz));
        }
        return fields;
    }

    /**
     * The intention of this method is to prevent from cycles caused by a list of a specific type which could cause a cycle
     *
     * @param clazz
     * @param <T>
     * @return modified field
     */
    private <T> Field abuseField(Class<T> clazz) {

        Field f = null;

        if (collectionMapUtil.isCollection(clazz)) {
            f = changeCollectionField(clazz);
        }
        if (collectionMapUtil.isMap(clazz)) {
            f = changeMapField(clazz);
        }

        return f;
    }

    //helper field
    private Collection<?> collectionField;

    private <T> Field changeCollectionField(Class<T> clazz) {
        Field f = null;

        try {
            f = FieldUtil.class.getDeclaredField("collectionField");
            setTypeAndClassOfField(f, clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return f;
    }

    //helper field
    private Map<?, ?> mapField;

    private <T> Field changeMapField(Class<T> clazz) {
        Field f = null;

        try {
            f = FieldUtil.class.getDeclaredField("mapField");
            setTypeAndClassOfField(f, clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return f;
    }

    public <T> List<Field> getFieldsWithSameTypeAsParent(Class<T> clazz, List<Field> fields) {

        List<Field> toRemove = new ArrayList<>();

        for (Field f : fields) {
            if (typesAreEqual(clazz, f)) {
                toRemove.add(f);
            }
        }

        return toRemove;
    }

    public HashMap<Field, Class<?>> getTypesOfFields(List<Field> fields) {
        HashMap<Field, Class<?>> fieldsAndClasses = new HashMap<>();
        for (Field f : fields) {
            fieldsAndClasses.put(f, f.getType());
        }
        return fieldsAndClasses;
    }

    public Field setTypeAndClassOfField(Field f, Class typeToSet) {

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

    private <A> boolean typesAreEqual(Class<A> parent, Field field) {

        if (parent == field.getType()) {
            return true;
        }

        if (collectionMapUtil.isCollection(field)) {
            if (parent == collectionMapUtil.getCollectionType(field)) {
                return true;
            }
        }

        if (collectionMapUtil.isMap(field)) {
            if (parent == collectionMapUtil.getKeyTypeOfMap(field) || parent == collectionMapUtil.getValueTypeOfMap(field)) {
                return true;
            }
        }
        return false;
    }
}
