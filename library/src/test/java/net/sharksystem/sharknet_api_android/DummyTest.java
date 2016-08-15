package net.sharksystem.sharknet_api_android;

import net.sharksystem.sharknet_api_android.impl.BaseTest;
import net.sharksystem.sharknet_api_android.impl.ContactImpl;

import org.junit.Test;
import org.reflections.ReflectionUtils;
import org.reflections.ReflectionUtils.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withModifier;
import static org.reflections.ReflectionUtils.withPrefix;

/**
 * Created by j4rvis on 11.08.16.
 */
public class DummyTest extends BaseTest {
    @Test
    public void hasProfilesTest(){

//        ImplSharkNet implSharkNet = new ImplSharkNet();
//        implSharkNet.fillWithDummyData();
//        List<Profile> profiles = implSharkNet.getProfiles();
//        System.out.println(String.valueOf(profiles.size()));
//        Assert.assertTrue(profiles.size()!=0);

        Set<Method> getters = ReflectionUtils.getAllMethods(ContactImpl.class,
                withModifier(Modifier.PUBLIC), withPrefix("get"));

        Iterator<Method> iterator = getters.iterator();
        while (iterator.hasNext()){
            Method next = iterator.next();
            System.out.println(next.getName());
        }
    }


}
