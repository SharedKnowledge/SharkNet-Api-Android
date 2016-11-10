package net.sharksystem.api.interfaces;

import java.util.List;

/**
 * Created by j4rvis on 11/9/16.
 */

public interface RadarListener {
    void onNewRadarContact(List<Contact> contacts);
}
