package net.sharksystem.api.dao_impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSTSet;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.dao_interfaces.ContactDao;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Message;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by j4rvis on 3/22/17.
 */

public class MessageDao implements DataAccessObject<Message, SemanticTag> {

    public final static SemanticTag MESSAGE_TYPE = InMemoSharkKB.createInMemoSemanticTag("MESSAGE", "si:message");
    public final static String MESSAGE_CONTENT = "MESSAGE_CONTENT";
    public final static String MESSAGE_IMAGE_CONTENT = "MESSAGE_IMAGE_CONTENT";
    //    private final static String MESSAGE_SENDER = "MESSAGE_SENDER";
    public final static String MESSAGE_DATE = "MESSAGE_DATE";
    public final static String MESSAGE_VERIFIED = "MESSAGE_VERIFIED";
    public final static String MESSAGE_SIGNED = "MESSAGE_SIGNED";
    public final static String MESSAGE_ENCRYPTED = "MESSAGE_ENCRYPTED";
    public final static String MESSAGE_TOPIC = "MESSAGE_TOPIC";
    public final static String MESSAGE_TYPE_A = "MESSAGE_TYPE";
    public final static String MESSAGE_PEER = "MESSAGE_PEER";
    public final static String MESSAGE_TIME = "MESSAGE_TIME";
    public final static String MESSAGE_LOCATION = "MESSAGE_LOCATION";
    private ContactDao mContactDao = null;

    private SharkKB mSharkKb;

    public MessageDao(SharkKB kb, ContactDao contactDao) {
        mSharkKb = kb;
        mContactDao = contactDao;
    }

    public MessageDao(SharkKB kb) {
        mSharkKb = kb;
    }

    @Override
    public void add(Message object) {
        try {
            SemanticTag topic = InMemoSharkKB.createInMemoSemanticTag(Message.MESSAGE_ID, object.getSender().getTag().getName() + object.getDate().getTime());
            ASIPSpace asipSpace;
            if (object.getTopic() != null) {
                asipSpace = mSharkKb.createASIPSpace(object.getTopic(), MESSAGE_TYPE, null, object.getSender().getTag(), null, object.getTime(), object.getLocation(), ASIPSpace.DIRECTION_INOUT);
            }
            else {
                asipSpace = mSharkKb.createASIPSpace(topic, MESSAGE_TYPE, null, object.getSender().getTag(), null, object.getTime(), object.getLocation(), ASIPSpace.DIRECTION_INOUT);
            }

            SharkNetUtils.setInfoWithName(mSharkKb, asipSpace, MESSAGE_CONTENT, object.getContent());
            SharkNetUtils.setInfoWithName(mSharkKb, asipSpace, MESSAGE_DATE, object.getDate().getTime());
            SharkNetUtils.setInfoWithName(mSharkKb, asipSpace, MESSAGE_VERIFIED, object.isVerified());
            SharkNetUtils.setInfoWithName(mSharkKb, asipSpace, MESSAGE_SIGNED, object.isSigned());
            SharkNetUtils.setInfoWithName(mSharkKb, asipSpace, MESSAGE_ENCRYPTED, object.isEncrypted());

            Bitmap image = object.getImageContent();
            if (image != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                SharkNetUtils.setInfoWithName(mSharkKb, asipSpace, MESSAGE_IMAGE_CONTENT, bs);
            }
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> getAll() {
        List<Message> messageList = new ArrayList<>();
        try {
            ASIPInterest asipInterest = (ASIPInterest) generateInterest(null, null);
            Iterator<ASIPInformationSpace> allInformationSpaces = mSharkKb.getInformationSpaces(asipInterest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                ASIPSpace asipSpace = next.getASIPSpace();
                Contact contact = null;
                if (mContactDao != null) {
                    contact = mContactDao.get(asipSpace.getSender());
                    if(contact==null){
                        contact = new Contact(asipSpace.getSender());
                        mContactDao.add(contact);
                    }
                }
                else  {
                    contact = new Contact(asipSpace.getSender());
                }

                Date date = new Date(SharkNetUtils.getInfoAsLong(mSharkKb, asipSpace, MESSAGE_DATE));
                STSet topics = asipSpace.getTopics();
                if(topics!=null){
                    Iterator<SemanticTag> tagIterator = topics.stTags();
                    if(tagIterator!=null){
                        try{
                            SemanticTag semanticTag = tagIterator.next();
                            if(semanticTag!=null){
                                Message message = new Message(semanticTag, date, contact);
                                message.setContent(SharkNetUtils.getInfoAsString(mSharkKb, asipSpace, MESSAGE_CONTENT));
                                message.setEncrypted(SharkNetUtils.getInfoAsBoolean(mSharkKb, asipSpace, MESSAGE_ENCRYPTED));
                                message.setSigned(SharkNetUtils.getInfoAsBoolean(mSharkKb, asipSpace, MESSAGE_SIGNED));
                                message.setVerified(SharkNetUtils.getInfoAsBoolean(mSharkKb, asipSpace, MESSAGE_VERIFIED));

                                ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKb, asipSpace, MESSAGE_IMAGE_CONTENT);
                                if (information != null) {
                                    message.setImageContent(BitmapFactory.decodeStream(new ByteArrayInputStream(information.getContentAsByte())));
                                }
                                messageList.add(message);
                            }
                        } catch (NoSuchElementException e){
                            return null;
                        }
                    }
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return messageList;
    }

    @Override
    public Message get(SemanticTag id) {
        try {
            ASIPSpace interest = generateInterest(id, null);
            Iterator<ASIPInformationSpace> informationSpaces = mSharkKb.getInformationSpaces(interest);
            while (informationSpaces.hasNext()) {
                ASIPInformationSpace informationSpace = informationSpaces.next();
                ASIPSpace asipSpace = informationSpace.getASIPSpace();

                Contact contact = mContactDao.get(asipSpace.getSender());
                if(contact==null){
                     contact = new Contact(asipSpace.getSender());
                    mContactDao.add(contact);
                }
                Date date = new Date(SharkNetUtils.getInfoAsLong(mSharkKb, asipSpace, MESSAGE_DATE));
                Message message = new Message(id, date, contact);
                message.setContent(SharkNetUtils.getInfoAsString(mSharkKb, asipSpace, MESSAGE_CONTENT));
                message.setEncrypted(SharkNetUtils.getInfoAsBoolean(mSharkKb, asipSpace, MESSAGE_ENCRYPTED));
                message.setSigned(SharkNetUtils.getInfoAsBoolean(mSharkKb, asipSpace, MESSAGE_SIGNED));
                message.setVerified(SharkNetUtils.getInfoAsBoolean(mSharkKb, asipSpace, MESSAGE_VERIFIED));

                ASIPInformation information = SharkNetUtils.getInfoByName(mSharkKb, asipSpace, MESSAGE_IMAGE_CONTENT);
                if (information != null) {
                    message.setImageContent(BitmapFactory.decodeStream(new ByteArrayInputStream(information.getContentAsByte())));
                }
                return message;
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void update(Message object) {
        try {
            ASIPSpace interest = generateInterest(object.getId(), object.getSender().getTag());
            Iterator<ASIPInformationSpace> allInformationSpaces = mSharkKb.getInformationSpaces(interest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                if (SharkCSAlgebra.identical(interest, next.getASIPSpace())) {
                    Iterator<ASIPInformation> informations = next.informations();
                    while (informations.hasNext()) {
                        ASIPInformation information = informations.next();
                        switch (information.getName()) {
                            case MESSAGE_CONTENT:
                                if (object.getContent() != null) {
                                    information.setContent(object.getContent());
                                }
                                break;
                            case MESSAGE_DATE:
                                if (object.getDate() != null) {
                                    information.setContent(String.valueOf(object.getDate().getTime()));
                                }
                                break;
                            case MESSAGE_ENCRYPTED:
                                information.setContent(object.isEncrypted() ? "TRUE" : "FALSE");
                                break;
                            case MESSAGE_VERIFIED:
                                information.setContent(object.isVerified() ? "TRUE" : "FALSE");
                                break;
                            case MESSAGE_SIGNED:
                                information.setContent(object.isSigned() ? "TRUE" : "FALSE");
                                break;
                            case MESSAGE_IMAGE_CONTENT:
                                Bitmap image = object.getImageContent();
                                if (image != null) {
                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                    image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                                    byte[] byteArray = bos.toByteArray();
                                    ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                                    information.setContent(bs, bs.available());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Message object) {
        try {
            ASIPSpace interest = generateInterest(object.getId(), object.getSender().getTag());
            Iterator<ASIPInformationSpace> allInformationSpaces = mSharkKb.getInformationSpaces(interest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                if (SharkCSAlgebra.identical(interest, next.getASIPSpace())) {
                    mSharkKb.removeInformation(next.getASIPSpace());
                    mSharkKb.removeInformationSpace(next.getASIPSpace());
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        try {
            ASIPSpace asipInterest = generateInterest(null, null);
            Iterator<ASIPInformationSpace> spaces = mSharkKb.getInformationSpaces(asipInterest);
            int number = 0;
            while (spaces.hasNext()) {
                spaces.next();
                number++;
            }
            return number;
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void update(List<Message> messages) {
        List<Message> all = getAll();
        for (Message message : messages) {
            if (!all.contains(message)) {
                add(message);
            }
        }
    }

    private ASIPSpace generateInterest(SemanticTag tag, PeerSemanticTag sender) throws SharkKBException {
        STSet typeSet = InMemoSharkKB.createInMemoSTSet();
        typeSet.merge(MESSAGE_TYPE);
        STSet topicSet = InMemoSharkKB.createInMemoSTSet();
        topicSet.merge(tag);
        TimeSTSet timeSet = InMemoSharkKB.createInMemoTimeSTSet();
        return InMemoSharkKB.createInMemoASIPInterest(topicSet, typeSet, sender, null, null, timeSet, null, ASIPSpace.DIRECTION_INOUT);
    }
}
