package net.sharksystem.api.dao_impl;

import android.graphics.Bitmap;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.serialization.ASIPMessageSerializerHelper;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class MessageDao implements DataAccessObject<Message, ASIPSpace> {

    private final static SemanticTag TYPE = InMemoSharkKB.createInMemoSemanticTag("MESSAGE", "si:message");
    private final static String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    private final static String MESSAGE_IMAGE_CONTENT = "MESSAGE_IMAGE_CONTENT";
    private final static String MESSAGE_SENDER = "MESSAGE_SENDER";
    private final static String MESSAGE_DATE = "MESSAGE_DATE";
    private final static String MESSAGE_VERIFIED = "MESSAGE_VERIFIED";
    private final static String MESSAGE_SIGNED = "MESSAGE_SIGNED";
    private final static String MESSAGE_ENCRYPTED = "MESSAGE_ENCRYPTED";

    private SharkKB kb;

    public MessageDao(SharkKB kb) {
        this.kb = kb;
    }

    @Override
    public void add(Message object) {
        try {
            TimeSemanticTag timeSemanticTag = InMemoSharkKB.createInMemoTimeSemanticTag(object.getDate().getTime(), 0);
            ASIPSpace asipSpace = this.kb.createASIPSpace(null, TYPE, null, object.getSender().getTag(), null, timeSemanticTag, null, ASIPSpace.DIRECTION_INOUT);

            SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_CONTENT, object.getContent());
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_DATE, object.getDate().getTime());
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_VERIFIED, object.isVerified());
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_SIGNED, object.isSigned());
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_ENCRYPTED, object.isEncrypted());

            Bitmap image = object.getImageContent();
            if (image != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_IMAGE_CONTENT, bs);
            }

            String senderString = ASIPMessageSerializerHelper.serializeTag(object.getSender().getTag()).toString();
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, MESSAGE_SENDER, senderString);

        } catch (SharkKBException | IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getAll() {
        return null;
    }

    @Override
    public Message get(ASIPSpace id) {
        return null;
    }

    @Override
    public void update(Message object) {

    }

    @Override
    public void remove(Message object) {

    }

    @Override
    public int size() {
        return 0;
    }

    private ASIPSpace generateInterest(PeerSemanticTag sender, Date date) throws SharkKBException {
        STSet stSet = InMemoSharkKB.createInMemoSTSet();
        stSet.merge(TYPE);
        TimeSTSet timeSTSet = InMemoSharkKB.createInMemoTimeSTSet();
        timeSTSet.createTimeSemanticTag(date.getTime(), 0);
        return InMemoSharkKB.createInMemoASIPInterest(null, stSet, sender, null, null, timeSTSet, null, ASIPSpace.DIRECTION_INOUT);
    }
}
