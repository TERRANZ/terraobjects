package ru.terraobjects.solutions.hashscan;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import ru.terraobject.entity.dao.TOObjectsManager;

/**
 *
 * @author terranz
 */
public class ServerThread implements Runnable
{

    private final int OPCODE_END = 0;
    private final int OPCODE_HASH = 1;
    private Socket serverSocket;
    private Connection conn;

    public ServerThread(Socket s, Connection conn)
    {
	this.serverSocket = s;
	this.conn = conn;
    }

    public void run()
    {
	try
	{
	    InputStream sin = serverSocket.getInputStream();
	    OutputStream sout = serverSocket.getOutputStream();
	    DataInputStream in = new DataInputStream(new BufferedInputStream(sin));
	    PrintWriter out = new PrintWriter(sout, true);
	    TOObjectsManager.getInstance().setConnection(conn);
	    while (true)
	    {
		int opcode = 0;

		opcode = in.readInt();

		if (opcode == OPCODE_HASH)
		{
		    int size = in.readInt();
		    char[] filename = new char[size];
		    for (int i = 1; i <= size; i++)
		    {
			filename[i - 1] = in.readChar();
		    }
		    size = in.readInt();
		    char[] hash = new char[size];
		    for (int i = 1; i <= size; i++)
		    {
			hash[i - 1] = in.readChar();
		    }

		    //= lookupEntityBeanRemote().createNewObjectByTemplate(2);

		    Integer newobjId = TOObjectsManager.getInstance().createNewObject(2).getObjectId();
		    TOObjectsManager.getInstance().setPropertyValue(newobjId, 1, String.valueOf(filename));
		    TOObjectsManager.getInstance().setPropertyValue(newobjId, 3, String.valueOf(hash));
//		    System.out.println("hashcode received:");
//		    System.out.println(String.valueOf(filename) + " = " + String.valueOf(hash));
		} else if (opcode == OPCODE_END)
		{
		    break;
		}
	    }
	    out.close();
	    in.close();
	    serverSocket.close();

	} catch (EOFException e)
	{
	    //System.out.println("init error: " + e.getStackTrace().toString());
	    e.printStackTrace();
	} // вывод исключений
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
