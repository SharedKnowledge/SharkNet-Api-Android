package net.sharksystem.api.shark.protocols.wifidirect;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.engine.ASIPSerializer;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.system.L;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j4rvis on 28.07.16.
 */
public class WifiDirectUtil {

    public final static String TOPIC_RECORD = "TO";
    public final static String TYPE_RECORD = "TY";
    public final static String SENDER_RECORD = "SE";
    public final static String APPROVERS_RECORD = "AP";
    public final static String RECEIVER_RECORD = "RE";
    public final static String LOCATION_RECORD = "LO";
    public final static String TIME_RECORD = "TI";
    public final static String DIRECTION_RECORD = "DI";

    public static HashMap<String, String> interest2RecordMap(ASIPInterest interest){

        HashMap<String, String> map = new HashMap<>();

        WifiDirectUtil.set2Map(map, WifiDirectUtil.TOPIC_RECORD, interest.getTopics());
        WifiDirectUtil.set2Map(map, WifiDirectUtil.TYPE_RECORD, interest.getTypes());

        if(interest.getSender() != null){
            try {
                String senderString = ASIPSerializer.serializeTag(interest.getSender()).toString();
                map.put(WifiDirectUtil.SENDER_RECORD, senderString);
            } catch (JSONException | SharkKBException e) {
                e.printStackTrace();
            }
        }

        WifiDirectUtil.set2Map(map, WifiDirectUtil.APPROVERS_RECORD, interest.getApprovers());
        WifiDirectUtil.set2Map(map, WifiDirectUtil.RECEIVER_RECORD, interest.getReceivers());
        WifiDirectUtil.set2Map(map, WifiDirectUtil.TIME_RECORD, interest.getTimes());
        WifiDirectUtil.set2Map(map, WifiDirectUtil.LOCATION_RECORD, interest.getLocations());

        if(interest.getDirection() >= 0){
            int direction = interest.getDirection();
            map.put(DIRECTION_RECORD, String.valueOf(direction));
        }

        return map;
    }

    private static void set2Map(HashMap<String, String> map, String key, STSet set){
        if(set != null && !set.isEmpty()){
            try {
                String string = ASIPSerializer.serializeSTSet(set).toString();
                map.put(key, string);
            } catch (SharkKBException | JSONException e) {
                e.printStackTrace();
                L.d(e.getMessage(), WifiDirectUtil.class);
            }
        }
    }

    public static ASIPInterest recordMap2Interest(Map<String, String> map) throws SharkKBException {

        ASIPInterest interest = InMemoSharkKB.createInMemoASIPInterest();

        interest.setTopics(InMemoSharkKB.createInMemoSTSet());
        interest.setTypes(InMemoSharkKB.createInMemoSTSet());
        interest.setApprovers(InMemoSharkKB.createInMemoPeerSTSet());
        interest.setReceivers(InMemoSharkKB.createInMemoPeerSTSet());
        interest.setLocations(InMemoSharkKB.createInMemoSpatialSTSet());
        interest.setTimes(InMemoSharkKB.createInMemoTimeSTSet());

        if(map.containsKey(TOPIC_RECORD)){
            String record = map.get(TOPIC_RECORD);
            interest.getTopics().merge(ASIPSerializer.deserializeSTSet(record));
        }
        if(map.containsKey(TYPE_RECORD)){
            String record = map.get(TYPE_RECORD);
            interest.getTypes().merge(ASIPSerializer.deserializeSTSet(record));
        }
        if(map.containsKey(SENDER_RECORD)){
            String record = map.get(SENDER_RECORD);
            interest.setSender(ASIPSerializer.deserializePeerTag(record));
        }
        if(map.containsKey(APPROVERS_RECORD)){
            String record = map.get(APPROVERS_RECORD);
            interest.getApprovers().merge(ASIPSerializer.deserializePeerSTSet(null, record));
        }
        if(map.containsKey(RECEIVER_RECORD)){
            String record = map.get(RECEIVER_RECORD);
            interest.getReceivers().merge(ASIPSerializer.deserializePeerSTSet(null, record));
        }
        if(map.containsKey(LOCATION_RECORD)){
            String record = map.get(LOCATION_RECORD);
            interest.getLocations().merge(ASIPSerializer.deserializeSpatialSTSet(null, record));
        }
        if(map.containsKey(TIME_RECORD)){
            String record = map.get(TIME_RECORD);
            interest.getTimes().merge(ASIPSerializer.deserializeTimeSTSet(null, record));
        }
        if(map.containsKey(DIRECTION_RECORD)){
            int record = Integer.getInteger(map.get(DIRECTION_RECORD));
            interest.setDirection(record);
        }
        return interest;
    }
}
