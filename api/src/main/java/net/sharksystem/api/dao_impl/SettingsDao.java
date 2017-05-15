package net.sharksystem.api.dao_impl;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.serialization.ASIPMessageSerializerHelper;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharksystem.api.models.Settings;
import net.sharksystem.api.utils.SharkNetUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by j4rvis on 5/15/17.
 */

public class SettingsDao {

    private final static SemanticTag TYPE = InMemoSharkKB.createInMemoSemanticTag("SETTINGS", "si:settings");
    private ASIPSpace mAsipSpace;

    private SharkKB mSharkKB;
    private Settings mCachedSettings = null;
    private boolean mHasChanged = false;


    public SettingsDao(SharkKB sharkKB) {
        mSharkKB = sharkKB;
        try {
            mAsipSpace = mSharkKB.createASIPSpace(null, TYPE, null, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    public void setSettings(Settings settings){
        try {
            SharkNetUtils.setInfoWithName(mSharkKB, mAsipSpace, Settings.MAIL_ADDRESS_LABEL, settings.getMailAddress());
            SharkNetUtils.setInfoWithName(mSharkKB, mAsipSpace, Settings.MAIL_USERNAME_LABEL, settings.getMailUsername());
            SharkNetUtils.setInfoWithName(mSharkKB, mAsipSpace, Settings.MAIL_PASSWORD_LABEL, settings.getMailPassword());
            SharkNetUtils.setInfoWithName(mSharkKB, mAsipSpace, Settings.MAIL_POP_SERVER_LABEL, settings.getMailPopServer());
            SharkNetUtils.setInfoWithName(mSharkKB, mAsipSpace, Settings.MAIL_SMTP_SERVER_LABEL, settings.getMailSmtpServer());
            String serializedTag = ASIPMessageSerializerHelper.serializeTag(settings.getAccountTag()).toString();
            SharkNetUtils.setInfoWithName(mSharkKB, mAsipSpace, Settings.ACCOUNT_TAG, serializedTag);
            mHasChanged = true;
        } catch (SharkKBException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Settings getSettings(){
        if(mHasChanged){
            Settings settings = new Settings();
            try {
                settings.setMailAddress(SharkNetUtils.getInfoAsString(mSharkKB, mAsipSpace, Settings.MAIL_ADDRESS_LABEL));
                settings.setMailUsername(SharkNetUtils.getInfoAsString(mSharkKB, mAsipSpace, Settings.MAIL_USERNAME_LABEL));
                settings.setMailPassword(SharkNetUtils.getInfoAsString(mSharkKB, mAsipSpace, Settings.MAIL_PASSWORD_LABEL));
                settings.setMailPopServer(SharkNetUtils.getInfoAsString(mSharkKB, mAsipSpace, Settings.MAIL_POP_SERVER_LABEL));
                settings.setMailSmtpServer(SharkNetUtils.getInfoAsString(mSharkKB, mAsipSpace, Settings.MAIL_SMTP_SERVER_LABEL));
                String infoAsString = SharkNetUtils.getInfoAsString(mSharkKB, mAsipSpace, Settings.ACCOUNT_TAG);
                settings.setAccountTag(ASIPMessageSerializerHelper.deserializePeerTag(infoAsString));
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            mCachedSettings = settings;
            mHasChanged = false;
        }
        return mCachedSettings;
    }
}
