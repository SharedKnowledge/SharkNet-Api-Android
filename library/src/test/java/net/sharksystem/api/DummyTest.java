package net.sharksystem.api;

import net.sharksystem.api.impl.ContactImpl;
import net.sharksystem.api.interfaces.Contact;

import org.junit.Test;
import org.reflections.ReflectionUtils;
import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by j4rvis on 11.08.16.
 */
public class DummyTest{
    @Test
    public void hasProfilesTest(){
        Set<Method> getters = ReflectionUtils.getAllMethods(Contact.class,
                withModifier(Modifier.PUBLIC), withPrefix("get"));

        Iterator<Method> iterator = getters.iterator();
        while (iterator.hasNext()){
            Method next = iterator.next();
            System.out.println(next.getName());
        }
    }


}
