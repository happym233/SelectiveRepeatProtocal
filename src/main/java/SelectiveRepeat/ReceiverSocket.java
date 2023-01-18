package SelectiveRepeat;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReceiverSocket {
    private static int windowSize = 8;
    private InetSocketAddress peerAddress;
    private DatagramSocket datagramSocket;
    private SingleSocketThread singleSocketThread;

    private Lock readLock;

    private String request;

    private int seqNum;

    public ReceiverSocket(int port) throws SocketException {
        datagramSocket = new DatagramSocket(port);
        datagramSocket.setSoTimeout(100);
        singleSocketThread = new SingleSocketThread(this, windowSize);
        readLock = new ReentrantLock(true);
        seqNum = 0;
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


    public void resendThirdHandshake() throws IOException {
        Packet thirdHandshakePacket = new Packet(2, 0, true, peerAddress, request);
        // System.out.println(thirdHandshakePacket);
        udpSend(thirdHandshakePacket);
    }
    public void connect(InetSocketAddress peerAddress, String request) throws IOException {
        readLock.lock();
        this.peerAddress = peerAddress;
        Packet firstHandshakePacket = Config.firstHandshakePacket(peerAddress);
        udpSend(firstHandshakePacket);
        try {
            Packet response = udpReceive();
            // System.out.println(response);
            if (!response.isAck()) throw new SocketException("Connecting to server rejected");
            else if (response.getSeqNum() != 1 || response.getPacketNum() != 0 || !response.getData().equals("Hi")) {
                connect(peerAddress, request);
            } else {
                this.seqNum = response.getSeqNum();
                this.request = request;
                Packet thirdHandshakePacket = new Packet(2, response.getPacketNum(), response.isAck(), peerAddress, request);
                // System.out.println(thirdHandshakePacket);
                udpSend(thirdHandshakePacket);
            }
            singleSocketThread.run();
        } catch (SocketTimeoutException e) {
            connect(peerAddress, request);
        } finally {
            readLock.unlock();
        }
    }

    public String read() {
        try {
            readLock.lock();
            return singleSocketThread.read();
        } finally {
            readLock.unlock();
        }
    }

    public class SingleSocketThread extends Thread {
        private ReceiverSocket socket;
        private StringBuilder response;
        private String[] window;
        private int startIdx;

        private boolean start;

        public SingleSocketThread(ReceiverSocket socket, int windowSize) throws SocketException {
            this.socket = socket;
            this.window = new String[windowSize];
            this.response = new StringBuilder();
            this.startIdx = 0;
            this.start = false;
        }

        public String read() {
            return response.toString();
        }


        @Override
        public void run() {
            int k = 0;
            boolean end = false;
            while (true) {
                int i = 0;
                try {
                    Packet p = socket.udpReceive();
                    // System.out.println(p);
                    if (p.getSeqNum() < 3) {
                        k++;
                        if (k > 2) {
                            socket.resendThirdHandshake();
                            k = 0;
                        }
                    } else {
                        if (p.getSeqNum() == 4) end = true;
                        int packetNum = p.getPacketNum();
                        if (packetNum < startIdx || packetNum - startIdx >= windowSize || window[packetNum - startIdx] != null) {
                        } else {
                            window[packetNum - startIdx] = p.getData();
                            for (i = 0; i < windowSize; i++) {
                                if (window[i] != null) {
                                    response.append(window[i]);
                                    window[i] = null;
                                }
                                else break;
                            }

                            for (int j = 0; j < windowSize; j++) {
                                if (j < windowSize - i) {
                                    window[j] = window[j + i];
                                } else {
                                    window[j] = null;
                                }
                            }
                            startIdx += i;
                        }
                        socket.udpSend(new Packet(p.getSeqNum(), packetNum, true, socket.peerAddress, ""));

                        if (end) {
                            boolean allEmpty = true;
                            for (int n = 0; n < windowSize; n++) {
                                if (window[n] != null) allEmpty = false;
                            }
                            if (allEmpty) break;
                        }
                    }
                } catch (SocketTimeoutException e) {

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void main(String[] args) {
        try {
            ReceiverSocket socket = new ReceiverSocket(41830);
            InetSocketAddress peerAddress = new InetSocketAddress("localhost",8007);
            socket.connect(peerAddress, "aaaaaaaaaaaaaaaaaaaaaaaaa");
            System.out.println(socket.read());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
