package ru.terraobjects.solutions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.Connection;

/**
 *
 * @author terranz
 */
public interface Solution
{
    public boolean parseInput();
    public void setParams(Connection c, DataInputStream in, DataOutputStream out);
}
