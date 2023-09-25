package org.example.network;

import org.example.util.Utils;

public enum ProtocolState {

    HANDSHAKING, STATUS, LOGIN, PLAY;

    public String getDisplayName() {
        return Utils.capitalize(this.name().toLowerCase());
    }

}
