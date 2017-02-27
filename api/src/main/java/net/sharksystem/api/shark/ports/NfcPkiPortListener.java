package net.sharksystem.api.shark.ports;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.security.SharkCertificate;

import java.util.List;

/**
 * Will trigger the corresponding methods to show that we:
 * - onMessageReceived -> to display that the devices can be removed fromm each other
 * - onExchangeFailed -> to display that we couldn't parse the message to ASIP or another reason
 * - onPublicKeyReceived -> to display the Key that we received and ask if it should be signed
 * - onCertificatesReceived -> if the key was signed we will display all the certificates we
 *          received as well and ask if we want to include them to our storage
 */
public interface NfcPkiPortListener {
    void onMessageReceived();
    void onExchangeFailed(String reason);
    void onPublicKeyReceived(PeerSemanticTag owner);
    void onCertificatesReceived(List<SharkCertificate> certificates);
}
