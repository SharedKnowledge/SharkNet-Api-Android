package net.sharksystem.sharknet_api_android;

import android.util.Log;

import net.sharksystem.sharknet_api_android.dummy_impl.ImplSharkNet;
import net.sharksystem.sharknet_api_android.interfaces.Profile;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by j4rvis on 11.08.16.
 */
public class DummyTest extends BaseTest {
    @Test
    public void hasProfilesTest(){

        ImplSharkNet implSharkNet = new ImplSharkNet();
        implSharkNet.fillWithDummyData();
        List<Profile> profiles = implSharkNet.getProfiles();
        System.out.println(String.valueOf(profiles.size()));
        Assert.assertTrue(profiles.size()!=0);
    }
}
