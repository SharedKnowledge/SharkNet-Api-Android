package net.sharksystem.api.dao_impl;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharksystem.api.dao_interfaces.ContactDao;
import net.sharksystem.api.models.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 5/9/17.
 */

public class CachedContactDaoImpl implements ContactDao {

    private final ContactDaoImpl mDao;
    private List<Contact> mContacts = new ArrayList<>();
    private boolean mContactsChanged = true;

    public CachedContactDaoImpl(SharkKB sharkKB) {
        mDao = new ContactDaoImpl(sharkKB);
    }

    private void refreshContacts() {
        if (mContacts.isEmpty() || mContactsChanged) {
            mContacts = mDao.getAll();
            mContactsChanged = false;
        }
    }

    @Override
    public void add(Contact object) {
        mDao.add(object);
        mContactsChanged = true;
    }

    @Override
    public List<Contact> getAll() {
        refreshContacts();
        return mContacts;
    }

    @Override
    public Contact get(PeerSemanticTag id) {
        refreshContacts();
        for (Contact contact : mContacts) {
            if (SharkCSAlgebra.identical(id, contact.getTag())) return contact;
        }
        return null;
    }

    @Override
    public void update(Contact object) {
        mDao.update(object);
        mContactsChanged = true;
    }

    @Override
    public void remove(Contact object) {
        mDao.remove(object);
        mContactsChanged = true;
    }

    @Override
    public int size() {
        refreshContacts();
        return mContacts.size();
    }
}
