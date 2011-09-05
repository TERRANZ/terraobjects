package ru.terra.solution.hashscan;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import ru.terra.ejb.entity.EntityBeanRemote;

/**
 *
 * @author terranz
 */
public class ServerThread implements Runnable
{

    EntityBeanRemote entityBean = lookupEntityBeanRemote();
    private final int OPCODE_END = 0;
    private final int OPCODE_HASH = 1;
    private Socket serverSocket;

    public ServerThread(int num, Socket s)
    {
	this.serverSocket = s;
    }

    @Override
    public void run()
    {
	try
	{
	    InputStream sin = serverSocket.getInputStream();
	    OutputStream sout = serverSocket.getOutputStream();

	    // Конвертируем потоки в другой тип, чтобы легче обрабатывать текстовые сообщения.
	    DataInputStream in = new DataInputStream(new BufferedInputStream(sin));
	    PrintWriter out = new PrintWriter(sout, true);

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
			filename[i-1] = in.readChar();
		    }
		    size = in.readInt();
		    char[] hash = new char[size];
		    for (int i = 1; i <= size; i++)
		    {
			hash[i-1] = in.readChar();
		    }

		    Integer newobjId = lookupEntityBeanRemote().createNewObjectByTemplate(2);
		    lookupEntityBeanRemote().setPropertyValue(newobjId, 1, String.valueOf(filename));
		    lookupEntityBeanRemote().setPropertyValue(newobjId, 3, String.valueOf(hash));
		    System.out.println("hashcode received:");
		    System.out.println(String.valueOf(filename) + " = " + String.valueOf(hash));
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

    private EntityBeanRemote lookupEntityBeanRemote()
    {
	try
	{
	    Context c = new InitialContext();
	    return (EntityBeanRemote) c.lookup("java:global/TerraObjects/TOEntities/EntityBean!ru.terra.ejb.entity.EntityBeanRemote");
	} catch (NamingException ne)
	{
	    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
	    throw new RuntimeException(ne);
	}
    }
}
