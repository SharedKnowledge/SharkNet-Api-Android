package net.sharksystem.api.dao_impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
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

public class ContactDao implements DataAccessObject<Contact, PeerSemanticTag> {

    private final static String CONTACT_IMAGE = "CONTACT_IMAGE";
    private final static String CONTACT_NAME = "CONTACT_NAME";
    private final static String CONTACT_EMAIL = "CONTACT_EMAIL";

    private SharkKB kb;

    public ContactDao(SharkKB sharkKB) {
        this.kb = sharkKB;
    }

    @Override
    public List<Contact> getAll() {
        List<Contact> contactList = new ArrayList<>();
        try {
            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getAllInformationSpaces();
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                ASIPSpace asipSpace = next.getASIPSpace();
                Contact contact = new Contact(asipSpace.getSender());

                contact.setName(SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_NAME));
                contact.setEmail(SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_EMAIL));

                ASIPInformation information = SharkNetUtils.getInfoByName(this.kb, asipSpace, CONTACT_IMAGE);
                if (information != null) {
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
            ASIPSpace asipSpace = this.kb.createASIPSpace((SemanticTag) null, null, null, id, null, null, null, ASIPSpace.DIRECTION_NOTHING);

            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(asipSpace);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                if (SharkCSAlgebra.identical(asipSpace, next.getASIPSpace())) {
                    Contact contact = new Contact(asipSpace.getSender());
                    contact.setName(SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_NAME));
                    contact.setEmail(SharkNetUtils.getInfoAsString(this.kb, asipSpace, CONTACT_EMAIL));

                    ASIPInformation information = SharkNetUtils.getInfoByName(this.kb, asipSpace, CONTACT_IMAGE);
                    if (information != null) {
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
            ASIPInterest interest = InMemoSharkKB.createInMemoASIPInterest(null, null, object.getTag(), null, null, null, null, ASIPSpace.DIRECTION_NOTHING);
            Iterator<ASIPInformationSpace> allInformationSpaces = this.kb.getInformationSpaces(interest);
            while (allInformationSpaces.hasNext()) {
                ASIPInformationSpace next = allInformationSpaces.next();
                if (SharkCSAlgebra.identical(interest, next.getASIPSpace())) {
                    // We probably need to set also the name and the email as info because it can cause
                    // problems at updating the contact and so changing the PST as well
                    SharkNetUtils.setInfoWithName(this.kb, interest, CONTACT_NAME, object.getName());
                    SharkNetUtils.setInfoWithName(this.kb, interest, CONTACT_EMAIL, object.getEmail());

                    Bitmap image = object.getImage();
                    if (image != null) {
                        // setImage
                        // Create an inputStream out of the image
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                        byte[] byteArray = bos.toByteArray();
                        ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                        SharkNetUtils.setInfoWithName(this.kb, interest, CONTACT_IMAGE, bs);
                    }
                }
            }
            // Okay the contact should be added
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Contact object) {

    }

    @Override
    public void add(Contact object) {
        try {
            ASIPSpace asipSpace = this.kb.createASIPSpace((SemanticTag) null, null, null, object.getTag(), null, null, null, ASIPSpace.DIRECTION_NOTHING);

            // We probably need to set also the name and the email as info because it can cause
            // problems at updating the contact and so changing the PST as well
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, CONTACT_NAME, object.getName());
            SharkNetUtils.setInfoWithName(this.kb, asipSpace, CONTACT_EMAIL, object.getEmail());

            Bitmap image = object.getImage();
            if (image != null) {
                // setImage
                // Create an inputStream out of the image
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] byteArray = bos.toByteArray();
                ByteArrayInputStream bs = new ByteArrayInputStream(byteArray);
                SharkNetUtils.setInfoWithName(this.kb, asipSpace, CONTACT_IMAGE, bs);
            }
            // Okay the contact should be added
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
    }
}
