package ru.terraobjects.solutions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.entity.TOObject;
import ru.terraobjects.entity.dao.TOObjectsManager;
import ru.terraobjects.entity.dao.TOPropertiesManager;

/**
 *
 * @author terranz
 */
public class HashScanSolution implements Solution
{

    private Connection conn;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public HashScanSolution()
    {
    }

    public Integer getPort()
    {
	return 12345;
    }

    public String getName()
    {
	return "Hash scan";
    }

    public void setParams(Connection c, DataInputStream in, DataOutputStream out)
    {
	this.conn = c;
	this.in = in;
	this.out = out;
	TOObjectsManager.getInstance().setConnection(conn);
	TOPropertiesManager.getInstance().setConnection(conn);
    }

    public boolean parseInput()
    {
	try
	{
	    int opcode = 0;
	    opcode = in.readInt();

	    switch (opcode)
	    {
		case Opcodes.C_OPCODE_INSERT_HASH:
		{
		    String filename = in.readUTF();
		    String hash = in.readUTF();
		    Integer newobjId = TOObjectsManager.getInstance().createNewObject(2).getObjectId();
		    TOObjectsManager.getInstance().setPropertyValue(newobjId, 1, filename);
		    TOObjectsManager.getInstance().setPropertyValue(newobjId, 3, hash);
		    System.out.println("Insert new hash: " + filename + " : " + hash);
		}
		break;
		case Opcodes.C_OPCODE_END:
		    return true;
		case Opcodes.C_OPCODE_GET_HASHES:
		{
		    Integer objCount = TOObjectsManager.getInstance().getObjectsCountByTemplateId(2);
		    out.writeInt(objCount);
		    System.out.println("Sent hashes count: " + String.valueOf(objCount));
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
		case Opcodes.C_OPCODE_CLEAN_FULL:
		{
		    TOObjectsManager.getInstance().removeAllObjects();
		}
		break;
		default:
		{
		    System.out.println("Hash solution: Unhandled opcode: " + String.valueOf(opcode));
		}
		break;
	    }


	} catch (IOException ex)
	{
	    System.out.println("hashscan solution: Client disconnected");
	    throw new RuntimeException(ex);
	}
	return false;
    }
}
