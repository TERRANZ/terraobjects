package ru.terraobjects.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.solutions.Solution;

/**
 *
 * @author korostelev
 */
public class ServerThread implements Runnable
{

    private Socket serverSocket = null;
    private Connection sqlConnection = null;
    private Solution solution = null;

    public ServerThread(Socket s, Solution solution, int connNum)
    {
	System.out.println("Server thread started, num: " + String.valueOf(connNum));
	try
	{
	    this.serverSocket = s;
	    String driverName = "com.mysql.jdbc.Driver";

	    Class.forName(driverName);

	    // Create a connection to the database
	    String serverName = "127.0.0.1";
	    String mydatabase = "terraobjects";
	    String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
	    String username = "scan";
	    String password = "scan";

	    Connection connection = DriverManager.getConnection(url, username, password);
	    System.out.println("Connected to DB " + connection.getCatalog());

	    this.sqlConnection = connection;
	    this.solution = (Solution) Class.forName(solution.getClass().getName()).newInstance();
	} catch (SQLException ex)
	{
	    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	} catch (InstantiationException ex)
	{
	    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex)
	{
	    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	} catch (ClassNotFoundException ex)
	{
	    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public void run()
    {
	InputStream sin = null;
	OutputStream sout = null;
	try
	{
	    sin = serverSocket.getInputStream();
	    sout = serverSocket.getOutputStream();
	    DataInputStream in = new DataInputStream(sin);
	    DataOutputStream out = new DataOutputStream(sout);
	    boolean exit = false;
	    solution.setParams(sqlConnection, in, out);
	    while (!exit)
	    {
		try
		{
		    exit = solution.parseInput();
		} catch (Exception e)
		{
		    System.out.println("Client disconnected");
		    serverSocket.close();
		    return;
		}
	    }
	} catch (IOException ex)
	{
	    Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	    return;
	} finally
	{
	    try
	    {
		sin.close();
		sout.close();
		serverSocket.close();
		sqlConnection.close();
	    } catch (SQLException ex)
	    {
		Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	    } catch (IOException ex)
	    {
		Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }
}
