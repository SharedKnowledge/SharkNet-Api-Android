package net.sharksystem.api.dao_impl;

import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApi {

    private final ChatDao chatDao;
    private ContactDao contactDao;
    private SharkKB root = new InMemoSharkKB();

    private static SharkNetApi mInstance = new SharkNetApi();

    private SharkNetApi() {
        try {
            contactDao = new ContactDao(createKBFromRoot());
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        chatDao = new ChatDao(root);
    }

    public static SharkNetApi getInstance(){
        return mInstance;
    }

    public ContactDao getContactDao(){
        return contactDao;
    }

    public ChatDao getChatDao() {
        return chatDao;
    }

    private SharkKB createKBFromRoot() throws SharkKBException {
        return new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), root.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet());
    }
}
