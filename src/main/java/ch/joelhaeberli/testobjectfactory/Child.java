package ch.joelhaeberli.testobjectfactory;

import java.lang.reflect.Field;
import java.util.List;

public class Child {

    private GenerationWrapper generationWrapper;

    private Class<?> type;

    private boolean isRoot = false;
    private int level;

    private boolean loadChildren;

    private Field field;
    private Object object;
    private Object parentObject;

    public Child(GenerationWrapper generationWrapper, Class<?> type, int level, Field field, Object parentObject) throws Exception {
        this.generationWrapper = generationWrapper;
        this.type = type;
        this.level = level;
        this.field = field;
        this.parentObject = parentObject;

        this.loadChildren = (this.level < this.generationWrapper.getLevels());

        compose();
    }

    private Child() {}
    public static Child getRoot(GenerationWrapper generationWrapper) {

        Child root = new Child();

        root.isRoot = true;
        root.level = 1;
        root.generationWrapper = generationWrapper;
        root.loadChildren = (root.level < root.generationWrapper.getLevels());

        root.parentObject = null;
        root.field = null;

        root.type = root.generationWrapper.getBaseClass();

        return root;
    }

    public void compose() throws Exception {

        generateChildsValue();
        if (loadChildren) {
            instantiateChildsChildren();
        }
        injectChildIntoParent();
    }

    private void generateChildsValue() throws Exception {

        Object cached = this.generationWrapper.getCached(this.type);
        if (cached == null) {
            if (this.generationWrapper.getTdg().isProvided(this.type)) {
                this.loadChildren = false;
                this.object = this.generationWrapper.getTdg().generateValue(this.type);
            } else if(this.generationWrapper.getCollectionMapUtil().isCollection(this.type)) {
                this.loadChildren = false;
                this.object = this.generationWrapper.getCollectionMapUtil().getCollection(this.field);
            } else if(this.generationWrapper.getCollectionMapUtil().isMap(this.type)) {
                this.loadChildren = false;
                this.object = this.generationWrapper.getCollectionMapUtil().getMap(this.field);
            } else {
                Object o = this.generationWrapper.getTog().getTestObjectOnlyInstance(this.type);
                this.generationWrapper.addToChache(o);
                this.object = o;
            }
        } else {
            this.object = cached;
        }
    }

    private void instantiateChildsChildren() {
        List<Field> childrensFields = generationWrapper.getFieldUtil().getDeclaredFields(type);
        int nextLevel = this.level + 1;
        childrensFields.forEach(f -> {
            try {
                new Child(this.generationWrapper, f.getType(), nextLevel, f, this.object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void injectChildIntoParent() throws IllegalAccessException {
        if (this.isRoot) {
            return;
        }
        this.field.setAccessible(true);
        this.field.set(this.parentObject, this.object);
    }

    public Object getObject() {
        return object;
    }
}
