package ru.terraobjects.solutions.impl.terrastore;

import java.io.Serializable;

/**
 *
 * @author korostelev
 */
public class NetworkPacket implements Serializable
{

    byte[] buf;

    public NetworkPacket(int val)
    {
        buf = new byte[Integer.SIZE];
        buf = intToByteArray(val);
    }

    public byte[] getBuf()
    {
        return buf;
    }

    public static byte[] intToByteArray(int value)
    {
        return new byte[]
                {
                    (byte) (value >>> 24),
                    (byte) (value >>> 16),
                    (byte) (value >>> 8),
                    (byte) value
                };
    }

    public static int byteArrayToInt(byte[] b)
    {
        return (b[0] << 24)
                + ((b[1] & 0xFF) << 16)
                + ((b[2] & 0xFF) << 8)
                + (b[3] & 0xFF);
    }
}
