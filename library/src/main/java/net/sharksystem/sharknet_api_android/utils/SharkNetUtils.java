package net.sharksystem.sharknet_api_android.utils;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;

import java.util.Iterator;

/**
 * Created by j4rvis on 09.08.16.
 */
public class SharkNetUtils {

    public static ASIPInformation getInfoByName(SharkKB kb, ASIPSpace space, String name) throws SharkKBException {
        Iterator<ASIPInformation> information = kb.getInformation(space);
        while(information.hasNext()){
            ASIPInformation next = information.next();
            if(next.getName().equals(name)){
                return next;
            }
        }
        return null;
    }

    public static boolean getInfoAsBoolean(SharkKB kb, ASIPSpace space, String name) throws SharkKBException {
        ASIPInformation information = getInfoByName(kb, space, name);
        if(information!=null){
            if (information.getContentAsString().equals("TRUE")){
                return true;
            } else if(information.getContentAsString().equals("FALSE")){
                return false;
            }
        }
        return false;
    }

    public static void setInfoAsBooleanString(SharkKB kb, ASIPSpace space, String name, boolean contentToSet) throws SharkKBException {
        String booleanString;
        if(contentToSet){
            booleanString = "TRUE";
        } else {
            booleanString = "FALSE";
        }
        ASIPInformation information = kb.addInformation(booleanString, space);
        information.setName(name);
    }
}
