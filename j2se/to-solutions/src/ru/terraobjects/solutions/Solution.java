package ru.terraobjects.solutions;

import java.net.Socket;
import java.sql.Connection;


/**
 *
 * @author terranz
 */
public interface Solution
{
    public Integer getPort();
    public String getName();
    public void go();
    public void stop();
    public void setConnection(Connection c);
}
