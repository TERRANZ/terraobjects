package ru.terraobjects.solutions.impl.terrastore;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

//    private static final int MAX_PACKET_SIZE = 255;
//    ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(stream);
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
        ByteBuffer outBuf = ByteBuffer.allocate(dos.size() + 10);
        outBuf.putInt(opCode);
        outBuf.putInt(dos.size() + 8);
        outBuf.put(stream.toByteArray());
        return outBuf.array();
    }

    public void putInt(int val) throws IOException
    {
        // buffer.putInt(val);
        dos.write(TypeConverter.toByta(val));
    }

    public void putFloat(float val) throws IOException
    {
        dos.write(TypeConverter.toByta(val));
    }

    public void putLong(long val) throws IOException
    {
        dos.write(TypeConverter.toByta(val));
    }

    public void putString(String val) throws IOException
    {
        try
        {
            dos.write(TypeConverter.toByta(val));
        } catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(ServerNetworkPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void putBuf(byte[] buf) throws IOException
    {
        dos.write(buf);
    }
}
