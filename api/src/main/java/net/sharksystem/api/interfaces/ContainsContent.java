package net.sharksystem.api.interfaces;

import net.sharkfw.knowledgeBase.SharkKBException;

/**
 * Created by timol on 06.06.2016.
 */
public interface  ContainsContent {

	Content getContent() throws SharkKBException;
}
