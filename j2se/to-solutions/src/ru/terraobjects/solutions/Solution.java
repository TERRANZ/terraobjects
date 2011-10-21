package ru.terraobjects.solutions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.sql.Connection;

/**
 *
 * @author terranz
 */
public interface Solution
{
    public boolean parseInput();
    public void setParams(Connection c, BufferedInputStream in, BufferedOutputStream out);
}
