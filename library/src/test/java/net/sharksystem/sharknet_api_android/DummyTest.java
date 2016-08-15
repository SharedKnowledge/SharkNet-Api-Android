package net.sharksystem.sharknet_api_android;

import net.sharksystem.sharknet_api_android.impl.BaseTest;
import net.sharksystem.sharknet_api_android.impl.ContactImpl;
import net.sharksystem.sharknet_api_android.interfaces.Contact;

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
