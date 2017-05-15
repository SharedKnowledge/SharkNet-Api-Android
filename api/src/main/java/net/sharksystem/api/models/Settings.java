package net.sharksystem.api.models;

/**
 * Created by j4rvis on 5/15/17.
 */

public class Settings {

    private String mMailAddress;
    private String mMailPassword;
    private String mMailUsername;
    private String mMailPopServer;
    private String mMailSmtpServer;

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
}
