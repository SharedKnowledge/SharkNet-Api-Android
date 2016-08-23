package net.sharksystem.api.utils;

import org.reflections.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withPrefix;

/**
 * Created by j4rvis on 15.08.16.
 */
public class ClassHelper {

    public static boolean equals(Class classToCheck, Object lhs, Object rhs) throws InvocationTargetException, IllegalAccessException {
        if(!classToCheck.isInstance(lhs)) return false;
        classToCheck.cast(lhs);
        if(!classToCheck.isInstance(rhs)) return false;
        classToCheck.cast(rhs);

        boolean equals = false;

        Set<Method> getters = ReflectionUtils.getAllMethods(classToCheck,
                withModifier(Modifier.PUBLIC), withPrefix("get"));

        for (Method next : getters) {
            System.out.println("------------------------------------------------------------------");
            System.out.println(classToCheck.getName() + "." + next.getName());
            System.out.println(next.invoke(lhs));
            if (next.invoke(lhs) == null && next.invoke(rhs) == null) {
                equals = true;
            } else if (next.invoke(lhs) != null && next.invoke(rhs) != null) {
                equals = next.invoke(lhs).equals(next.invoke(rhs));
            } else equals = false;
        }

        return equals;
    }

    public static int hashCode(Class classToCheck, Object object) throws InvocationTargetException, IllegalAccessException {
        if (classToCheck.isInstance(object)) {
            classToCheck.cast(object);
        }

        Set<Method> getters = ReflectionUtils.getAllMethods(classToCheck,
                withModifier(Modifier.PUBLIC), withPrefix("get"));

        ArrayList list = new ArrayList();
        for (Method next : getters) {
            list.add(next.invoke(object));
        }

        return Arrays.hashCode(list.toArray());
    }
}
