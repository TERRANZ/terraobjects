package ru.terraobjects.solutions.hashscan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;
import ru.terraobject.entity.TOObject;
import ru.terraobject.entity.TOObjectProperty;
import ru.terraobject.entity.TOProperty;
import ru.terraobject.entity.TOPropertyType;
import ru.terraobject.entity.dao.TOObjectsManager;
import ru.terraobject.entity.dao.TOPropertiesManager;

/**
 *
 * @author terranz
 */
public class HSServerThread implements Runnable
{

    private final int OPCODE_END = 0;
    private final int OPCODE_HASH = 1;
    private final int OPCODE_GET_OBJ = 2;
    private Socket serverSocket;
    private Connection conn;

    public HSServerThread(Socket s, Connection conn)
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
	    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sout));
	    TOObjectsManager.getInstance().setConnection(conn);
	    TOPropertiesManager.getInstance().setConnection(conn);
	    boolean exit = false;
	    while (!exit)
	    {
		int opcode = 0;
		opcode = in.readInt();
		switch (opcode)
		{
		    case OPCODE_HASH:
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
			Integer newobjId = TOObjectsManager.getInstance().createNewObject(2).getObjectId();
			TOObjectsManager.getInstance().setPropertyValue(newobjId, 1, String.valueOf(filename));
			TOObjectsManager.getInstance().setPropertyValue(newobjId, 3, String.valueOf(hash));
		    }
		    break;
		    case OPCODE_END:
			exit = true;
			break;
		    case OPCODE_GET_OBJ:
		    {
			Integer objCount = TOObjectsManager.getInstance().getObjectsCountByTemplateId(2);
			out.writeInt(objCount);
			System.out.println("Sent count: "+String.valueOf(objCount));
			if (objCount != 0)
			{
			    List<TOObject> objects = TOObjectsManager.getInstance().getAllObjectsByTemplateId(2);
			    for (TOObject obj : objects)
			    {
				String name = TOPropertiesManager.getInstance().getObjectProperty(obj.getObjectId(), 1).getStringVal();
				String hash = TOPropertiesManager.getInstance().getObjectProperty(obj.getObjectId(), 3).getStringVal();
				out.writeUTF(name);
				out.writeUTF(hash);
				out.flush();
			    }
			}
		    }
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
