package net.sharksystem.api.dao_impl;

import net.sharkfw.system.L;
import net.sharksystem.api.models.Contact;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by j4rvis on 4/7/17.
 */

public class ThreadedSharkNetImplTest {

    @Test
    public void test() throws InterruptedException {
        L.setLogLevel(L.LOGLEVEL_ALL);
        ExecutorService executor = Executors.newSingleThreadExecutor();
//        mExecutor.execute(this);
        ThreadedSharkNetApiImpl api = new ThreadedSharkNetApiImpl(null, executor);

        Contact contact = new Contact("Alice", "mail@alice.com");
        Assert.assertEquals(0, api.numberOfContacts());
        api.addContact(contact);
        Assert.assertEquals(1, api.numberOfContacts());
    }

}
