package SelectiveRepeat;

import SelectiveRepeat.Packet;

import java.net.InetSocketAddress;

public class Config {
    private static String routerAddress = "localhost";
    private static int routerPort = 3000;

    public static String getRouterAddress() { return routerAddress; }
    public static int getRouterPort() { return routerPort; }

    public static Packet firstHandshakePacket(InetSocketAddress peerAddress) {
        return new Packet(1, 0, true, peerAddress, "Hi S");
    }

    public static boolean isFirstHandshakePacket(Packet p) {
        if (p == null) return false;
        return p.isAck() &&
                p.getSeqNum() == 1 &&
                p.getPacketNum() == 0 &&
                p.getData().equals("Hi S");
    }
}
