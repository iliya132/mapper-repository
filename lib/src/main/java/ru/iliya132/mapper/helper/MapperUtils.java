package ru.iliya132.mapper.helper;

import java.util.*;

public class MapperUtils {
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypes = Map.of(
            Boolean.class, boolean.class,
            Byte.class, byte.class,
            Character.class, char.class,
            Double.class, double.class,
            Float.class, float.class,
            Integer.class, int.class,
            Long.class, long.class,
            Short.class, short.class,
            Void.class, void.class
    );

    private static final Set<Class<?>> primitivesTypes = Set.of(
            boolean.class,
            byte.class,
            char.class,
            double.class,
            float.class,
            int.class,
            long.class,
            short.class,
            void.class
    );

    public static boolean isPrimitive(Class<?> cls){
        return primitivesTypes.contains(cls);
    }

    public static boolean isPrimitiveWrapper(Class<?> cls){
        return primitiveWrapperTypes.containsKey(cls);
    }

    public static Class<?> getWrapperForPrimitive(Class<?> cls){
        return primitiveWrapperTypes.get(cls);
    }
}
