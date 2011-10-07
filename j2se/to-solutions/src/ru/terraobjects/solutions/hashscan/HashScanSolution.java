package ru.terraobjects.solutions.hashscan;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.solutions.Solution;

/**
 *
 * @author terranz
 */
public class HashScanSolution implements Solution
{

    private Integer port = 12345;
    private Socket socket;
    private HSServerThread st;
    private Thread t;
    private Connection conn;
    private Boolean stop = false;

    public HashScanSolution()
    {
    }

    public Integer getPort()
    {
	return port;
    }

    public String getName()
    {
	return "Hash scan";
    }

    public void go()
    {
	try
	{
	    ServerSocket server = new ServerSocket(port);
	    while (!stop)
	    {
		socket = server.accept();
		st = new HSServerThread(socket, conn);
		t = new Thread(st);
		t.start();
	    }

	} catch (IOException ex)
	{
	    Logger.getLogger(HashScanSolution.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void stop()
    {
	stop = true;
    }

    public void setConnection(Connection c)
    {
	this.conn = c;
    }
}
