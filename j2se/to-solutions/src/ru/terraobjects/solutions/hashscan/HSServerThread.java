package ru.terraobjects.solutions.hashscan;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.List;
import ru.terraobject.entity.TOObject;
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
    private final int OPCODE_CLEAN_FULL = 100;
    private final int OPCODE_REMOVE_OBJ = 101;
    private final int OPCODE_REMOVE_OBJ_BY_TEMPLATE = 102;
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
                try
                {
                    opcode = in.readInt();
                } catch (Exception e)
                {
                    exit = true;
                }
                switch (opcode)
                {
                    case OPCODE_HASH:
                    {
                        String filename = in.readUTF();
                        String hash = in.readUTF();
                        Integer newobjId = TOObjectsManager.getInstance().createNewObject(2).getObjectId();
                        TOObjectsManager.getInstance().setPropertyValue(newobjId, 1, filename);
                        TOObjectsManager.getInstance().setPropertyValue(newobjId, 3, hash);
                        System.out.println("Created new object: " + filename + " hash: " + hash);
                    }
                    break;
                    case OPCODE_END:
                        exit = true;
                        break;
                    case OPCODE_GET_OBJ:
                    {
                        Integer objCount = TOObjectsManager.getInstance().getObjectsCountByTemplateId(2);
                        out.writeInt(objCount);
                        System.out.println("Sent count: " + String.valueOf(objCount));
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
                    case OPCODE_CLEAN_FULL:
                    {
                        TOObjectsManager.getInstance().removeAllObjects();
                    }
                    break;
                    case OPCODE_REMOVE_OBJ:
                    {
                        int oId = in.readInt();
                        TOObjectsManager.getInstance().removeObjectWithProps(oId);
                    }
                    break;
                    case OPCODE_REMOVE_OBJ_BY_TEMPLATE:
                    {
                        int templateId = in.readInt();
                        TOObjectsManager.getInstance().removeObjectsByTemplate(templateId);
                    }
                    break;
                    default:
                    {
                        System.out.println("Unknow opcode: " + String.valueOf(opcode));
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
