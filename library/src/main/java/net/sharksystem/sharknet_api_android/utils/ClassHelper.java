package net.sharksystem.sharknet_api_android.utils;

import net.sharksystem.sharknet_api_android.impl.ContactImpl;
import net.sharksystem.sharknet_api_android.interfaces.Contact;

import org.reflections.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withPrefix;

/**
 * Created by j4rvis on 15.08.16.
 */
public class ClassHelper {

    public static <T> boolean equals(Class<T> classToCheck, Object lhs, Object rhs) throws InvocationTargetException, IllegalAccessException {
        if(!classToCheck.isInstance(lhs)) return false;
        classToCheck.cast(lhs);
        if(!classToCheck.isInstance(rhs)) return false;
        classToCheck.cast(rhs);

//        if(!lhs.getClass().equals(classToCheck)) return false;
//        if(!rhs.getClass().equals(classToCheck)) return false;

        boolean equals = false;

        Set<Method> getters = ReflectionUtils.getAllMethods(classToCheck,
                withModifier(Modifier.PUBLIC), withPrefix("get"));

        for (Method next : getters) {
            System.out.println("Testing " + classToCheck.getName() + "." + next.getName());
            System.out.println(next.invoke(lhs));
            if (next.invoke(lhs) == null && next.invoke(rhs) == null) {
                equals = true;
            } else if (next.invoke(lhs) != null && next.invoke(rhs) != null) {
                equals = next.invoke(lhs).equals(next.invoke(rhs));
            } else equals = false;
        }

        return equals;
    }
}
