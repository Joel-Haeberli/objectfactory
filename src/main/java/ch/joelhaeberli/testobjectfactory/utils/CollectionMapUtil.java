package ch.joelhaeberli.testobjectfactory.utils;

import ch.joelhaeberli.testobjectfactory.TestobjectGeneratorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class CollectionMapUtil {

    public <T> Collection<T> getCollection(Field f) {

        Collection<T> collection = null;

        Class<T> collectionValueType = getCollectionType(f);

        try {
            T object1 = TestobjectGeneratorFactory.createTestobjectGenerator().getTestObjectOnlyInstance(collectionValueType);
            T object2 = TestobjectGeneratorFactory.createTestobjectGenerator().getTestObjectOnlyInstance(collectionValueType);

            collection = getCollectionBody(f);

            collection.add(object1);
            collection.add(object2);
        } catch (Exception e) {
            throw new IllegalStateException("Could not generate object for collection of type <" + collectionValueType + ">");
        }
        return collection;
    }

    public <T> Class<T> getCollectionType(Field f) {

        ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    public <K, V> Map<K, V> getMap(Field f) {

        Map<K, V> map;

        ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
        Class<K> mapKeyType = (Class<K>) parameterizedType.getActualTypeArguments()[0];
        Class<V> mapValueType = (Class<V>) parameterizedType.getActualTypeArguments()[1];

        try {
            K key1 = TestobjectGeneratorFactory.createTestobjectGenerator().getTestObjectOnlyInstance(mapKeyType);
            K key2 = TestobjectGeneratorFactory.createTestobjectGenerator().getTestObjectOnlyInstance(mapKeyType);
            V value = TestobjectGeneratorFactory.createTestobjectGenerator().getTestObjectOnlyInstance(mapValueType);

            map = getMapBody(f, mapKeyType, mapValueType);

            map.put(key1, value);
            map.put(key2, value);
        } catch (Exception e) {
            throw new IllegalStateException("Could not generate object for map with key-type <" + mapKeyType + "> and value-type <" + mapValueType + ">");
        }
        return map;
    }

    public Class getKeyTypeOfMap(Field f) {

        ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
        return (Class) parameterizedType.getActualTypeArguments()[0];
    }

    public Class getValueTypeOfMap(Field f) {

        ParameterizedType parameterizedType = (ParameterizedType) f.getGenericType();
        return (Class) parameterizedType.getActualTypeArguments()[1];
    }

    public <K, V> Map<K, V> getMapBody(Field f, Class<K> mapKeyType, Class<V> mapValueType) {

        Type t = f.getType();

        if (t == Map.class) {
            if (t == Hashtable.class) {
                return new Hashtable<>();
            } else if (t == LinkedHashMap.class) {
                return new LinkedHashMap<>();
            } else if (t == HashMap.class) {
                return new HashMap<>();
            } else if (t == SortedMap.class) {
                return new TreeMap<>();
            }
        }
        return new HashMap<>();
    }

    // Collection-Framework as http://www.beingjavaguys.com/2013/03/java-collection-framework.html
    public <T> Collection<T> getCollectionBody(Field f) {

        if (f.getType() == Collection.class) {
            if (f.getType() == List.class) {
                if (f.getType() == ArrayList.class) {
                    return new ArrayList<>();
                } else if (f.getType() == LinkedList.class) {
                    return new LinkedList<>();
                } else if (f.getType() == Vector.class) {
                    return new Vector<>();
                } else if (f.getType() == Stack.class) {
                    return new Stack<>();
                }
                return new ArrayList<>();
            }
        } else if (f.getType() == Set.class) {
            if (f.getType() == HashSet.class) {
                return new HashSet<>();
            } else if (f.getType() == LinkedHashSet.class) {
                return new LinkedHashSet<>();
            } else if (f.getType() == SortedSet.class) {
                return new TreeSet<>();
            }
            return new HashSet<>();
        } else if (f.getType() == Queue.class) {
            if (f.getType() == PriorityQueue.class) {
                return new PriorityQueue<>();
            } else if (f.getType() == Deque.class) {
                return new ArrayDeque<>();
            }
        }
        return new ArrayList<>();
    }

    public boolean isCollection(Field f) {
        List<Class> superclasses = Arrays.asList(f.getType().getInterfaces());
        if (superclasses.contains(Collection.class) || f.getType() == Collection.class) {
            return true;
        }
        return false;
    }

    public boolean isCollection(Class c) {
        List<Class> superclasses = Arrays.asList(c.getInterfaces());
        if (superclasses.contains(Collection.class) || c == Collection.class) {
            return true;
        }
        return false;
    }

    public boolean isMap(Field f) {
        List<Class> superclasses = Arrays.asList(f.getType().getInterfaces());
        if (superclasses.contains(Map.class) || f.getType() == Map.class) {
            return true;
        }
        return false;
    }

    public boolean isMap(Class c) {
        List<Class> superclasses = Arrays.asList(c.getInterfaces());
        if (superclasses.contains(Map.class) || c == Map.class) {
            return true;
        }
        return false;
    }
}
