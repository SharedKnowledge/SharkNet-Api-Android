
//    public void startShark() throws SharkKBException, SharkProtocolNotSupportedException, IOException {
//
////        Profile profile = getMyProfile();
////        mSharkEngine.setEngineOwnerPeer(profile.getPST());
//
////        Interest interests = profile.getInterests();
//        ASIPSpace asipSpace = null;
////        if (interests != null) {
////            asipSpace = interests.asASIPSpace();
////        }
////        mSharkEngine.setSpace(asipSpace);
//
//        // Sync
//        mSharkEngine.getSyncManager().allowInvitation(true);
//        mSharkEngine.getSyncManager().addInviteListener(this);
//
//        // Discovery
//        mSharkEngine.addNearbyPeerListener(this);
////        mSharkEngine.startBluetooth();
////        mSharkEngine.startDiscovery();
//
//    }
//
//
//    public void addRadarListener(RadarListener listener) {
//        if (!mRadarListeners.contains(listener)) {
//            mRadarListeners.add(listener);
//        }
//    }
//
//
//    public void removeRadarListener(RadarListener listener) {
//        if (!mRadarListeners.contains(listener)) {
//            mRadarListeners.remove(listener);
//        }
//    }
//
//
//    @Override
//    public void onNearbyPeerFound(ArrayList<NearbyPeer> peers) {
////        mContacts = new ArrayList<>();
////
////        for (NearbyPeer peer : peers) {
////            try {
////                // TODO do we already have the peer but without the address?
////                // TODO Update if already known peer
////                Contact contact = newContact(peer.getSender());
////                contact.setLastWifiContact(new Timestamp(peer.getLastSeen()));
////                mContacts.add(contact);
////                // TODO further information in this space?
////            } catch (SharkKBException e) {
////                e.printStackTrace();
////            }
////        }
////        for (RadarListener listener : mRadarListeners) {
//////            listener.onNewRadarContact(mContacts);
////        }
//    }
//
//    @Override
//    public void onInvitation(SyncComponent component) {
////        PeerSTSet members = component.getMembers();
////        ArrayList<Contact> contacts = new ArrayList<>();
////        Enumeration<PeerSemanticTag> peerSemanticTagEnumeration = members.peerTags();
////        while (peerSemanticTagEnumeration.hasMoreElements()) {
////            PeerSemanticTag peerSemanticTag = peerSemanticTagEnumeration.nextElement();
////            try {
////                Contact byTag = getContactByTag(peerSemanticTag);
////                contacts.add(byTag);
////            } catch (SharkKBException e) {
////                e.printStackTrace();
////            }
//
//        }
//
////        L.d("onInvitation triggered!", this);
////        try {
////            ChatImpl chat = new ChatImpl(this, component, contacts, getMyProfile());
////            mChatComponents.add(component);
//////            for (EventListener listener : mEventListeners){
//////                listener.onNewChat(chat);
//////            }
////        } catch (SharkKBException | JSONException e) {
////            e.printStackTrace();
////        }
//
//    }
//}
