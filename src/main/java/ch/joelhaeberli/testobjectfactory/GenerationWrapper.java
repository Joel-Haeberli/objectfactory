package ch.joelhaeberli.testobjectfactory;

import ch.joelhaeberli.testobjectfactory.utils.CollectionMapUtil;
import ch.joelhaeberli.testobjectfactory.utils.FieldUtil;

import java.util.Map;

public class GenerationWrapper<T> {

    private Class<T> baseClass;
    private TestobjectGenerator tog;
    private Map<Class<?>, Object> cache;
    private int levels;
    private Child root;

    private CollectionMapUtil collectionMapUtil;
    private FieldUtil fieldUtil;
    private TestdataGenerator tdg;

    public GenerationWrapper(Class<T> baseClass, TestobjectGenerator tog, TestdataGenerator tdg, Map<Class<?>, Object> cache, int levels) {
        this.baseClass = baseClass;
        this.tog = tog;
        this.cache = cache;
        this.levels = levels;

        this.collectionMapUtil = new CollectionMapUtil();
        this.fieldUtil = new FieldUtil(this.collectionMapUtil);
        this.tdg = tdg;

        this.root = Child.getRoot(this);
    }

    public T composeObject() throws Exception {
        this.root.compose();
        return baseClass.isInstance(this.root.getObject()) ? baseClass.cast(this.root.getObject()) : null;
    }

    /**
     * Searches the cache and returns object if present.
     *
     * @param o Object to lookup
     * @return
     */
    public Object getCached(Class<?> o) {
        if (cache.containsKey(o)) {
            return cache.get(o);
        }
        return null;
    }

    public void addToChache(Object o) {
        if (cache.containsKey(o.getClass())) { return; }
        cache.put(o.getClass(), o);
    }

    public TestobjectGenerator getTog() {
        return tog;
    }

    public int getLevels() {
        return levels;
    }

    public CollectionMapUtil getCollectionMapUtil() {
        return collectionMapUtil;
    }

    public FieldUtil getFieldUtil() {
        return fieldUtil;
    }

    public Class<T> getBaseClass() {
        return baseClass;
    }

    public TestdataGenerator getTdg() {
        return tdg;
    }
}
