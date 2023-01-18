package SelectiveRepeat;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerSocket {

    /*
    private static int maxClientsNumber = 8;
    private static int windowSize = 8;
    private static int timeout = 10000;
    private Map<InetSocketAddress, SenderSocket> socketsPool;
    private Queue<Packet> queue;
    private DatagramSocket datagramSocket;


    private int port;

    private SenderSocket newAcceptedSocket;
    private Lock acceptLock;

    public ServerSocket(int port) throws SocketException {
        queue = new LinkedList<>();
        datagramSocket = new DatagramSocket(port);
        datagramSocket.setSoTimeout(timeout);
        socketsPool = new HashMap<>();
        acceptLock = new ReentrantLock(true);
    }


    private void udpSend(Packet packet) throws IOException {
        byte[] packetBytes = packet.generate();
        DatagramPacket dpacket = new DatagramPacket(packetBytes, packetBytes.length, InetAddress.getByName(Config.getRouterAddress()), Config.getRouterPort());
        datagramSocket.send(dpacket);
    }

    private Packet udpReceive() throws IOException {
        byte[] buffer = new byte[1024];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(datagramPacket);
        return new Packet(datagramPacket.getData());
    }

    private void processPacket(Packet packet) {
        try {
            if (Config.isFirstHandshakePacket(packet) && !socketsPool.containsKey(packet.getInetAddress())) {
                socketsPool.put(packet.getInetAddress(), new SenderSocket());
            }
        } catch (UnknownHostException e) {}
    }

    public Socket accept() {
        try {
            acceptLock.lock();
            return newAcceptedSocket;
        } finally {
            acceptLock.unlock();
        }
    }

    public class ListenerThread extends Thread {
        ServerSocket serverSocketUnfinished;

        public ListenerThread(ServerSocket serverSocketUnfinished) {
            this.serverSocketUnfinished = serverSocketUnfinished;
            serverSocketUnfinished.acceptLock.lock();
        }

        public void run() {
            while (true) {
                try {
                    Packet packet = udpReceive();
                    serverSocketUnfinished.processPacket(packet);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public class SenderThread extends Thread {
        ServerSocket serverSocketUnfinished;

        public SenderThread (ServerSocket serverSocketUnfinished) {
            this.serverSocketUnfinished = serverSocketUnfinished;
        }

        public void run() {
            while (true) {
                if (!serverSocketUnfinished.queue.isEmpty()) {
                    try {
                        udpSend(queue.remove());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }


    public class SenderSocket implements Socket{
        private ServerSocket serverSocketUnfinished;
        private InetSocketAddress peerAddress;
        private SingleSenderThread[] window;
        private int lastIdx;

        @Override
        public void send(String s) throws IOException {

        }

        @Override
        public String read() {
            return null;
        }

        public class SingleSenderThread {
        }
    }

     */

}
