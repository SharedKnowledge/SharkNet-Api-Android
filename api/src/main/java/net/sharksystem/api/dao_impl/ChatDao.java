package net.sharksystem.api.dao_impl;

import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.models.Chat;

import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class ChatDao implements DataAccessObject<Chat, SemanticTag> {

    private SharkKB rootKb;
    private List<SyncComponent> syncComponentList;

    public ChatDao(SharkKB rootKb) {
        this.rootKb = rootKb;
    }

    @Override
    public List<Chat> getAll() {
        return null;
    }

    @Override
    public Chat get(SemanticTag id) {
        return null;
    }

    @Override
    public void update(Chat object) {

    }

    @Override
    public void remove(Chat object) {

    }

    @Override
    public void add(Chat object) {

    }

    @Override
    public int size() {
        return 0;
    }
}
