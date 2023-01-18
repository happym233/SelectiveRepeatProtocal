package SelectiveRepeat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Packet {

    private boolean ack;
    private Integer seqNum;
    private Integer packetNum;
    private byte[] address;
    private int port;
    private String data;
    private static Charset charset = StandardCharsets.UTF_8;

    public Packet(int seqNum, int packetNum, boolean ack, InetSocketAddress address, String data) {
        this.seqNum = seqNum;
        this.packetNum = packetNum;
        this.ack = ack;
        this.address = address.getAddress().getAddress();
        this.port = address.getPort();
        this.data = data;
    }

    public Packet (byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);
        byte[] buff = new byte[1];
        bb.get(buff);
        ack = byteToBoolean(buff[0]);
        buff = new byte[2];
        bb.get(buff);
        seqNum = twoBytesToInt(buff);
        bb.get(buff);
        packetNum = twoBytesToInt(buff);
        address = new byte[4];
        bb.get(address);
        bb.get(buff);
        port = twoBytesToPort(buff);
        buff = new byte[bytes.length - 11];
        bb.get(buff);
        data = new String(buff, charset).replaceAll("\u0000.*", "");
    }

    private boolean byteToBoolean(byte b) {
        Integer a = Integer.valueOf(b) & 0xff;
        return a == 255;
    }

    private int twoBytesToPort(byte[] bytes) {
        return bytes[1] & 0xff | (bytes[0] & 0xff) << 8;
    }

    private int twoBytesToInt(byte[] bytes) {
        Integer a = Integer.valueOf(bytes[0]) & 0xff;
        Integer b = Integer.valueOf(bytes[1]) & 0xff;
        if (a == b) return a;
        else return -1;
    }

    private byte booleanToByte(boolean ack) {
        Integer ackNum = 0, a = (ack == true) ? 1:0;
        for (int i = 0; i < 8; i++) {
            ackNum = ackNum << 1;
            if (i % 2 == 0)
                ackNum += a;
            else
                ackNum += 1;
        }
        return ackNum.byteValue();
    }

    public byte[] generate() {
        byte[] header = new byte[5];
        header[0] = booleanToByte(ack);
        header[1] = seqNum.byteValue();
        header[2] = seqNum.byteValue();
        header[3] = packetNum.byteValue();
        header[4] = packetNum.byteValue();
        byte[] peerAddress = address;
        byte[] peerPort = new byte[2];
        peerPort[1] = (byte) (port & 0xFF);
        peerPort[0] = (byte) ((port >> 8) & 0xFF);
        byte[] byteData = data.getBytes(charset);
        ByteBuffer byteBuffer =  ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
        byteBuffer.put(header);
        byteBuffer.put(peerAddress);
        byteBuffer.put(peerPort);
        byteBuffer.put(byteData);
        return byteBuffer.array();
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public Integer getPacketNum() {
        return packetNum;
    }

    public void setPacketNum(Integer packetNum) {
        this.packetNum = packetNum;
    }

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public InetSocketAddress getInetAddress() throws UnknownHostException {
        return new InetSocketAddress(InetAddress.getByAddress(address), port);
    }

    @Override
    public String toString() {
        return "Packet{" +
                "ack=" + ack +
                ", clientNum=" + seqNum +
                ", packetNum=" + packetNum +
                ", address=" + Arrays.toString(address) +
                ", port=" + port +
                ", data='" + data + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Packet p = new Packet(4, 3, true, new InetSocketAddress("localhost", 80), "abcde");
        byte[] bytes = p.generate();
        Packet p2 = new Packet(bytes);
        System.out.println(p2);
    }
}
