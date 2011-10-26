package ru.terraobjects.solutions.impl.terrastore;

import java.nio.ByteBuffer;

/**
 *
 * @author korostelev
 */
public class ClientNetworkPacket
{

    private ByteBuffer buffer;
    private int opCode = 0;
    private int packetSize = 0;

    public ClientNetworkPacket(byte[] buf)
    {
        buffer = ByteBuffer.wrap(buf);
        this.opCode = buffer.getInt();
        this.packetSize = buffer.getInt();
    }

    public int getPacketSize()
    {
        return packetSize;
    }

    public int getOpcode()
    {
        return opCode;
    }

    public int getInt()
    {
        return buffer.getInt();
    }

    public float getFloat()
    {
        return buffer.getFloat();
    }

    public long getLong()
    {
        return buffer.getLong();
    }

    public String getString(int size)
    {
        byte[] tmpBuf = new byte[size];
        buffer.get(tmpBuf);
        return TypeConverter.toString(tmpBuf);
    }

    public String getString()
    {
        byte[] tmpBuf = new byte[buffer.getInt()];
        buffer.get(tmpBuf);
        return TypeConverter.toString(tmpBuf);
    }
}
