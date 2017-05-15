package net.sharksystem.api.models;

import net.sharkfw.knowledgeBase.PeerSemanticTag;

/**
 * Created by j4rvis on 5/15/17.
 */

public class Settings {

    public final static String MAIL_ADDRESS_LABEL = "MAIL_ADDRESS";
    public final static String MAIL_PASSWORD_LABEL = "MAIL_PASSWORD";
    public final static String MAIL_USERNAME_LABEL = "MAIL_USERNAME";
    public final static String MAIL_POP_SERVER_LABEL = "MAIL_POP_SERVER";
    public final static String MAIL_SMTP_SERVER_LABEL = "MAIL_SMTP_SERVER";
    public final static String ACCOUNT_TAG = "ACCOUNT_TAG";

    private String mMailAddress;
    private String mMailPassword;
    private String mMailUsername;
    private String mMailPopServer;
    private String mMailSmtpServer;
    private PeerSemanticTag mAccountTag;

    public String getMailAddress() {
        return mMailAddress;
    }

    public void setMailAddress(String mailAddress) {
        mMailAddress = mailAddress;
    }

    public String getMailPassword() {
        return mMailPassword;
    }

    public void setMailPassword(String mailPassword) {
        mMailPassword = mailPassword;
    }

    public String getMailUsername() {
        return mMailUsername;
    }

    public void setMailUsername(String mailUsername) {
        mMailUsername = mailUsername;
    }

    public String getMailPopServer() {
        return mMailPopServer;
    }

    public void setMailPopServer(String mailPopServer) {
        mMailPopServer = mailPopServer;
    }

    public String getMailSmtpServer() {
        return mMailSmtpServer;
    }

    public void setMailSmtpServer(String mailSmtpServer) {
        mMailSmtpServer = mailSmtpServer;
    }

    public PeerSemanticTag getAccountTag() {
        return mAccountTag;
    }

    public void setAccountTag(PeerSemanticTag accountTag) {
        mAccountTag = accountTag;
    }
}
