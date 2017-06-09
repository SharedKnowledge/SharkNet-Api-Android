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
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.persistent.sql.SqlSharkKB;
import net.sharkfw.system.L;
import net.sharksystem.api.dao_interfaces.ContactDao;
import net.sharksystem.api.dao_interfaces.DataAccessObject;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.utils.SharkNetUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by j4rvis on 3/22/17.
 */

public class ContactDaoImpl implements ContactDao {

    private final static SemanticTag TYPE = InMemoSharkKB.createInMemoSemanticTag("CONTACT", "si:contact");
    private final static String CONTACT_IMAGE = "CONTACT_IMAGE";
    private final static String CONTACT_NAME = "CONTACT_NAME";
    private final static String CONTACT_EMAIL = "CONTACT_EMAIL";

    private SharkKB kb;

    public ContactDaoImpl(SharkKB sharkKB) {
        this.kb = sharkKB;
    }

    @Override
    public void add(Contact object) {

        try {
            ASIPSpace asipSpace = this.kb.createASIPSpace(null, TYPE, null, object.getTagAsInMemoTag(), null, null, null, ASIPSpace.DIRECTION_INOUT);

            // We probably need to set also the name and the email as info because it can cause
            // problems at updating the contact and so changing the PST as well
//            L.d(L.asipSpace2String(asipSpace), this);
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, CONTACT_NAME, object.getName());
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, CONTACT_EMAIL, object.getEmail());

            if (object.getImage() != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                object.getImage().compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                L.d("Contact image size(add): " + bs.available() + "Bytes.", this);
                SharkNetUtils.setInfoWithName(this.kb, asipSpace, CONTACT_IMAGE, bs);
            }
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Contact> getAll() {
        List<Contact> contactList = new ArrayList<>();
        try {
            ASIPInterest asipInterest = generateInterest(null);
            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(asipInterest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                ASIPSpace asipSpace = next.getASIPSpace();
                Contact contact = new Contact(asipSpace.getSender());

                String name = SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_NAME);
                if(name!=null) contact.setName(name);
                String mail = SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_EMAIL);
                if(mail!=null) contact.setEmail(mail);

                ASIPInformation information = SharkNetUtils.getInfoByName(this.kb, asipSpace, CONTACT_IMAGE);
                if (information != null) {
                    L.d("Contact image size(getAll): " + information.getContentLength() + "Bytes.", this);
                    contact.setImage(BitmapFactory.decodeStream(new ByteArrayInputStream(information.getContentAsByte())));
                }
                contactList.add(contact);
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        return contactList;
    }

    @Override
    public Contact get(PeerSemanticTag id) {
        try {
            ASIPInterest asipSpace = generateInterest(id);

            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(asipSpace);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                if (SharkCSAlgebra.identical(asipSpace, next.getASIPSpace())) {
                    Contact contact = new Contact(asipSpace.getSender());
                    contact.setName(SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_NAME));
                    contact.setEmail(SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_EMAIL));

                    ASIPInformation information = SharkNetUtils.getInfoByName(this.kb, asipSpace, CONTACT_IMAGE);
                    if (information != null) {
                        L.d("Contact image size(get): " + information.getContentLength() + "Bytes.", this);
                        contact.setImage(BitmapFactory.decodeStream(new ByteArrayInputStream(information.getContentAsByte())));
                    }
                    return contact;
                }
            }
        } catch (SharkKBException e) {
            // TODO Throw exception instead of null?
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Contact object) {
        try {
            ASIPInterest interest = generateInterest(object.getTagAsInMemoTag());
            if (object.getName()!=null) {
                SharkNetUtils.setInfoWithName(this.kb, interest, CONTACT_NAME, object.getName());
            }
            if (object.getEmail()!=null){
                SharkNetUtils.setInfoWithName(this.kb, interest, CONTACT_EMAIL, object.getEmail());
            }

            if (object.getImage() != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                object.getImage().compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                L.d("Contact image size(update): " + bs.available() + "Bytes.", this);
                SharkNetUtils.setInfoWithName(this.kb, interest, CONTACT_IMAGE, bs);
            }
//            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(interest);
//            while (allInformationSpaces.hasNext()) {
//                ASIPInformationSpace next = allInformationSpaces.next();
//                if (SharkCSAlgebra.identical(interest, next.getASIPSpace())) {
//                    // We have found our space
//                    // Check for information already set
//                    Iterator<ASIPInformation> informations = next.informations();
//                    while (informations.hasNext()) {
//
//                        ASIPInformation information = informations.next();
//                        switch (information.getName()) {
//                            case CONTACT_NAME:
//                                if (object.getName() != null) {
//                                    information.setContent(object.getName());
//                                }
//                                break;
//                            case CONTACT_EMAIL:
//                                if (object.getEmail() != null) {
//                                    information.setContent(object.getEmail());
//                                }
//                                break;
//                            case CONTACT_IMAGE:
//                                if (object.getImage() != null) {
//                                    // setImage
//                                    // Create an inputStream out of the image
//                                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                                    object.getImage().compress(Bitmap.CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
//                                    byte[] byteArray = bos.toByteArray();
//                                    ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
//                                    L.d("Contact image size(update): " + bs.available() + "Bytes.", this);
//                                    information.setContent(bs, bs.available());
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }
//            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Contact object) {
        try {

            ASIPInterest interest = generateInterest(object.getTag());
            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(interest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                if (SharkCSAlgebra.identical(interest, next.getASIPSpace())) {
                    this.kb.removeInformation(next.getASIPSpace());
                    this.kb.removeInformationSpace(next.getASIPSpace());
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int size() {
        try {
            ASIPInterest asipInterest = generateInterest(null);
            Iterator<ASIPInformationSpace> spaces = this.kb.getInformationSpaces(asipInterest);
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

    private ASIPInterest generateInterest(PeerSemanticTag tag) throws SharkKBException {
        STSet inMemoSTSet = InMemoSharkKB.createInMemoSTSet();
        inMemoSTSet.merge(TYPE);
        return InMemoSharkKB.createInMemoASIPInterest(null, inMemoSTSet, tag, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
    }
}
