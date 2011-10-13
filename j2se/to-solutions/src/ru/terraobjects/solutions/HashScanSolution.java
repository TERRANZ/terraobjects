package ru.terraobjects.solutions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import ru.terraobjects.entity.TOObject;
import ru.terraobjects.entity.dao.TOObjectsHelper;
import ru.terraobjects.entity.dao.TOObjectsManager;
//import ru.terraobjects.entity.dao.TOPropertiesManager;
import ru.terraobjects.solutions.annotated.HashObject;

/**
 *
 * @author terranz
 */
public class HashScanSolution implements Solution
{

    private Connection conn;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private TOObjectsManager objectsManager;
    //private TOPropertiesManager propsManager;
    private TOObjectsHelper objectsHelper;

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
        objectsManager = new TOObjectsManager(conn);
        //propsManager = new TOPropertiesManager(conn);
        objectsHelper = new TOObjectsHelper(conn);
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
//                    Integer newobjId = objectsManager.createNewObject(2).getObjectId();
//                    //System.out.println("newobjId "+String.valueOf(newobjId));
//                    objectsManager.createNewPropertyWithValue(newobjId, 1, filename, 1);
//                    objectsManager.createNewPropertyWithValue(newobjId, 3, hash, 1);
                    HashObject ho = new HashObject();
                    ho.setFileName(filename);
                    ho.setHash(hash);
                    objectsHelper.storeObject(ho);
                }
                break;
                case Opcodes.C_OPCODE_END:
                    return true;
                case Opcodes.C_OPCODE_GET_HASHES:
                {
                    Integer objCount = objectsManager.getObjectsCountByTemplateId(2);
                    out.writeInt(objCount);
                    System.out.println("Sent hashes count: " + String.valueOf(objCount));
                    if (objCount != 0)
                    {
                        List<TOObject> objects = objectsManager.getAllObjectsByTemplateId(2);
                        for (TOObject obj : objects)
                        {
                            HashObject ho = (HashObject) objectsHelper.loadObject(HashObject.class, obj.getObjectId());
                            out.writeUTF(ho.getFileName());
                            out.writeUTF(ho.getHash());
                            out.flush();
                        }
                    }
                }
                break;
                case Opcodes.C_OPCODE_CLEAN_FULL:
                {
                    objectsManager.removeAllObjects();
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
