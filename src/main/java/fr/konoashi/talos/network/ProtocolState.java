package fr.konoashi.talos.network;

import fr.konoashi.talos.util.Utils;

public enum ProtocolState {

    HANDSHAKING, STATUS, LOGIN, PLAY;

    public String getDisplayName() {
        return Utils.capitalize(this.name().toLowerCase());
    }

}
