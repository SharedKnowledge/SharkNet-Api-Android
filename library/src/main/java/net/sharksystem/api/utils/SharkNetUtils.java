package net.sharksystem.api.utils;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.TimeSemanticTag;
import net.sharksystem.api.interfaces.ContainsContent;
import net.sharksystem.api.interfaces.Timeable;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by j4rvis on 09.08.16.
 */
public class SharkNetUtils {

    public static ASIPSpace createCurrentTimeSpace(SharkKB kb, SemanticTag type) throws SharkKBException {
        TimeSemanticTag timeSemanticTag =
                kb.getTimeSTSet().createTimeSemanticTag(System.currentTimeMillis(), 0);
        return kb.createASIPSpace(null, type, null, null, null, timeSemanticTag, null, ASIPSpace.DIRECTION_OUT);
    }

    public static void setInfoWithName(SharkKB kb, ASIPSpace space, String name, String content) throws SharkKBException {
        ASIPInformation information = kb.addInformation(content, space);
        if(information!=null){
            information.setName(name);
        } else {
            throw new SharkKBException("Information could not be added");
        }
    }

    public static void setInfoWithName(SharkKB kb, ASIPSpace space, String name, InputStream content) throws SharkKBException, IOException {
        ASIPInformation information = kb.addInformation(content, content.available(), space);
        if(information!=null){
            information.setName(name);
        } else {
            throw new SharkKBException("Information could not be added");
        }
    }

    public static void setInfoWithName(SharkKB kb, ASIPSpace space, String name, int content) throws SharkKBException{
        String s = String.valueOf(content);
        SharkNetUtils.setInfoWithName(kb, space, name, s);
    }


    public static void setInfoWithName(SharkKB kb, ASIPSpace space, String name, long content) throws SharkKBException{
        String s = String.valueOf(content);
        SharkNetUtils.setInfoWithName(kb, space, name, s);
    }

    public static void setInfoWithName(SharkKB kb, ASIPSpace space, String name, boolean content) throws SharkKBException{
        String booleanString;
        if(content){
            booleanString = "TRUE";
        } else {
            booleanString = "FALSE";
        }
        SharkNetUtils.setInfoWithName(kb, space, name, booleanString);
    }

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

    public static String getInfoAsString(SharkKB kb, ASIPSpace space, String name) throws SharkKBException {
        ASIPInformation information = getInfoByName(kb, space, name);
        if(information!=null){
            return information.getContentAsString();
        }
        return null;
    }

    public static int getInfoAsInteger(SharkKB kb, ASIPSpace space, String name) throws SharkKBException {
        ASIPInformation information = getInfoByName(kb, space, name);
        if(information!=null){
            return Integer.getInteger(information.getContentAsString());
        }
        return 0;
    }


    public static Long getInfoAsLong(SharkKB kb, ASIPSpace space, String name) throws SharkKBException {
        ASIPInformation information = getInfoByName(kb, space, name);
        if(information!=null){
            return Long.getLong(information.getContentAsString());
        }
        return 0L;
    }

//    public static void setInfoWithName(SharkKB kb, ASIPSpace space, String name, boolean contentToSet) throws SharkKBException {
//        String booleanString;
//        if(contentToSet){
//            booleanString = "TRUE";
//        } else {
//            booleanString = "FALSE";
//        }
//        ASIPInformation information = kb.addInformation(booleanString, space);
//        information.setName(name);
//    }

    public static List<? extends ContainsContent> search(String searchTerm, List<? extends  ContainsContent> searchList) throws SharkKBException {
        List<ContainsContent> swapList = new LinkedList<>();
        swapList.addAll(searchList);
        searchTerm = searchTerm.toLowerCase();
        for(int t = swapList.size() -1; t >= 0; t--){
            ContainsContent c = swapList.get(t);
            if (!c.getContent().getMessage().toLowerCase().contains(searchTerm)){
                swapList.remove(c);
            }
        }
        return swapList;
    }

    /**
     * Sorts the Lists of Timeable Objects which are Objects with the Method getTimestamp (Comment, Feed, Message)
     *
     * @param sortList
     * @return
     */
    public static List<? extends Timeable> sortList(List<? extends Timeable> sortList, boolean ascending) throws SharkKBException {
        List<Timeable> m_list = new LinkedList<>();
        boolean change = false;
        // fix so it doesn't crash
        if (sortList == null) {
            sortList = new ArrayList<>();
        }
        m_list.addAll(sortList);
        Timeable temp, temp2;
        for (int i = 1; i < m_list.size(); i++) {
            for (int j = 0; j < m_list.size() - 1; j++) {
                change = false;
                if(m_list.get(j).getTimestamp() != null && m_list.get(j+1).getTimestamp()!=null){
                    if (m_list.get(j).getTimestamp().before(m_list.get(j + 1).getTimestamp()) && ascending) {
                        change = true;
                    }
                    if(m_list.get(j).getTimestamp().after(m_list.get(j + 1).getTimestamp()) && !ascending){
                        change = true;
                    }

                    if(change){
                        temp = m_list.get(j);
                        temp2 = m_list.get(j + 1);
                        m_list.remove(j + 1);
                        m_list.remove(j);
                        m_list.add(j, temp2);
                        m_list.add(j + 1, temp);
                    }
                }
            }
        }
        return m_list;
    }

    /**
     * Returns a List with object within the given timerange
     * @param cutList
     * @param start
     * @param stop
     * @return
     */

    public static List<? extends Timeable> cutList(List<? extends Timeable> cutList, Timestamp start, Timestamp stop) throws SharkKBException {

        List<Timeable> swapList = new LinkedList<>();
        if (start.before(stop)) {
            swapList.addAll(cutList);
            boolean reachStart = false;
            boolean reachStop = false;
            int iteratorstart = 0;
            int iteratorstop = 0;
            int iterator = 0;
            while ((!reachStart && !reachStop) && iterator < swapList.size()) {
                if (!reachStart && reachStop) {
                    if (swapList.get(iterator).getTimestamp().before(start) || swapList.get(iterator).getTimestamp().equals(start)) {
                        reachStart = true;
                    } else iteratorstart = iterator;
                }
                if (!reachStop) {
                    if (swapList.get(iterator).getTimestamp().before(stop)) {
                        reachStop = true;
                    } else iteratorstop = iterator;
                }
                iterator++;
            }
            for(int i = swapList.size()-1; i >= 0; i--){
                if(i<iteratorstop && reachStop) swapList.remove(i);
                if(i>=iteratorstart && reachStart) swapList.remove(i);
            }
        }
        return swapList;
    }

    /**
     * Returns the List with the Objects within the given Intervall
     * @param cutList
     * @param startIndex
     * @param stopIndex
     * @return
     */
    public static List<? extends Timeable> cutList(List<? extends Timeable> cutList, int startIndex, int stopIndex) {
        List<Timeable> swapList = new LinkedList<>();
        if (startIndex <= stopIndex && startIndex >= 0 && stopIndex >= 0) {
            if (stopIndex >= cutList.size()) {
                stopIndex = cutList.size();
            }
            if(startIndex > cutList.size()-1){
                return swapList;
            }
            swapList.addAll(cutList);
            int iterator = stopIndex;

            while (iterator < swapList.size()) {
                swapList.remove(iterator);
            }

            iterator = 0;
            while (iterator < startIndex) {
                swapList.remove(iterator);
                iterator++;
            }
        }
        return swapList;

    }

}
