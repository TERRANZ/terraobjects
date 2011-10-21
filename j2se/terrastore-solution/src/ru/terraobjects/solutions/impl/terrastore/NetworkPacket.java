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
        buf = TypeConverter.toByta(val);
    }

    public byte[] getBuf()
    {
        return buf;
    }
}
