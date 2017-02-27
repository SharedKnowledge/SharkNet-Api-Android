package net.sharksystem.api.shark.ports;

/**
 * These are the methods to ask for Acceptation of the signing or including of the keys and/or
 * certificates.
 */
public interface NfcPkiPortEventListener {
    void onPublicKeyDecision(boolean accepted);
    void onCertificatesDecision(boolean accepted);
}
