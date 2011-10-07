package ru.terraobjects.solutions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import ru.terraobjects.entity.dao.TOObjectsManager;
import ru.terraobjects.entity.dao.TOPropertiesManager;

/**
 *
 * @author korostelev
 */
public class DefaultSolution implements Solution
{

    private Connection conn;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    public Integer getPort()
    {
        return 31337;
    }

    public String getName()
    {
        return "Default";
    }

    public boolean parseInput()
    {
        try
        {
            int opcode = 0;
            opcode = in.readInt();
            switch (opcode)
            {
                case Opcodes.C_OPCODE_END:
                {
                    return true;
                }
                case Opcodes.C_OPCODE_CREATE_OBJ:
                {
                    //read 1 int - object template
                    //return 1 int - id of new object
                }
                break;
                case Opcodes.C_OPCODE_SET_PROP:
                {
                    //read 2 int - objId,propId
                    //read 2 utf - propType,value
                }
                break;
                case Opcodes.C_OPCODE_GET_OBJ:
                {
                    //read 1 int - objId
                    //return full object fields
                    //int - fields count
                    //utf - fields
                }
                break;
                case Opcodes.C_OPCODE_GET_OBJ_PROPS:
                {
                    //read 1 int - objId
                    //get template of object and return count of props
                    //int - propId
                    //utf - propType
                }
                break;
                case Opcodes.C_OPCODE_GET_PROP:
                {
                    //read 2 int - objInd,propId
                    //return 2 utf - propType,value                    
                }
                break;
                case Opcodes.C_OPCODE_CLEAN_FULL:
                {
                }
                break;
                case Opcodes.C_OPCODE_REM_OBJ:
                {
                }
                break;
                case Opcodes.C_OPCODE_REM_OBJ_BY_TEMPL:
                {
                }
                break;
                default:
                {
                    System.out.println("Default solution: Unhandled opcode: " + String.valueOf(opcode));
                }
                break;
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public void setParams(Connection c, DataInputStream in, DataOutputStream out)
    {
        this.conn = c;
        this.in = in;
        this.out = out;
        TOObjectsManager.getInstance().setConnection(conn);
        TOPropertiesManager.getInstance().setConnection(conn);
    }
}
