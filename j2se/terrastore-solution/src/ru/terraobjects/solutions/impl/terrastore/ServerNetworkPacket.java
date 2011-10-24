package ru.terraobjects.solutions.impl.terrastore;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author korostelev
 */
public class ServerNetworkPacket implements Serializable
{

    private static final int MAX_PACKET_SIZE = 255;
    ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
    int opCode = 0;

    public ServerNetworkPacket(int opcode)
    {
        this.opCode = opcode;
        //buffer.putInt(opcode);
    }

    public void setOpCode(int opCode)
    {
        this.opCode = opCode;
    }

    public int getOpCode()
    {
        return opCode;
    }

    public byte[] getPacket()
    {
        ByteBuffer outBuf = ByteBuffer.allocate(MAX_PACKET_SIZE);
        outBuf.putInt(opCode);
        outBuf.putInt(buffer.array().length + 8);
        outBuf.put(buffer);
        return outBuf.array();
    }

    public void putInt(int val)
    {
        buffer.putInt(val);
    }

    public void putFloat(float val)
    {
        buffer.putFloat(val);
    }

    public void putLong(long val)
    {
        buffer.putLong(val);
    }

    public void putString(String val)
    {
        try
        {
            buffer.put(TypeConverter.toByta(val));
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(ServerNetworkPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void putBuf(byte[] buf)
    {
        buffer.put(buf);
    }
}
