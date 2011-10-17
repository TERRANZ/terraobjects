package ru.terraobjects.solutions;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import ru.terraobjects.entity.TOObject;
import ru.terraobjects.entity.TOObjectProperty;
import ru.terraobjects.entity.TOPropertyType;
import ru.terraobjects.entity.dao.TOObjectsManager;
import ru.terraobjects.entity.dao.TOPropertiesManager;
import ru.terraobjects.solutions.annotation.ASolution;

/**
 *
 * @author korostelev
 */
@ASolution(name = "default", port = "31337")
public class DefaultSolution implements Solution
{

    private Connection conn;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private TOObjectsManager objectsManager;
    private TOPropertiesManager propsManager;

    public boolean parseInput()
    {
        try
        {
            int opcode = in.readInt();
            int objId = in.readInt();
            switch (opcode)
            {
                case Opcodes.C_OPCODE_END:
                {
                    out.writeInt(Opcodes.S_OPCODE_OK);
                    return true;
                }
                case Opcodes.C_OPCODE_CREATE_OBJ:
                {
                    //read 2 int - object template,parentObjectId        
                    int objParentId = in.readInt();
                    TOObject newObj = objectsManager.createNewObject(objId);
                    newObj.setObjectParentId(objParentId);
                    //return 1 int - id of new object
                    out.writeInt(newObj.getObjectId());
                    out.writeInt(Opcodes.S_OPCODE_OK);
                }
                break;
                case Opcodes.C_OPCODE_SET_PROP:
                {
                    //read 2 int - objId,propId                    
                    int propId = in.readInt();
                    //read 1 utf - value
                    String value = in.readUTF();
                    propsManager.setPropertyValue(objId, propId, value);
                    out.writeInt(Opcodes.S_OPCODE_OK);
                }
                break;
                case Opcodes.C_OPCODE_GET_OBJ:
                {
                    //read 1 int - objId                                       
                    //return full object fields       
                    TOObject obj = objectsManager.getObject(objId);
                    if (obj != null)
                    {
                        out.writeInt(obj.getObjectParentId());
                        out.writeInt(obj.getObjectTemplateId());
                        out.writeLong(obj.getObjectCreatedAt().getTime());
                        out.writeLong(obj.getObjectUpdatedAt().getTime());
                        out.writeInt(Opcodes.S_OPCODE_OK);
                    } else
                    {
                        out.writeInt(Opcodes.S_OPCODE_ERR);
                    }
                }
                break;
                case Opcodes.C_OPCODE_GET_OBJ_PROPS:
                {
                    //read 1 int - objId
                    //get template of object and return count of props
                    List<TOObjectProperty> props = propsManager.getObjectPropertiesForObjectId(objId);
                    //int - fields count
                    out.writeInt(props.size());

                    for (TOObjectProperty prop : props)
                    {
                        //int - prop type
                        int propType = propsManager.getPropertyType(prop.getObjectPropertyId()).getPropTypeId();
                        out.writeInt(propType);
                        String outVal = "";
                        switch (propType)
                        {
                            case TOPropertyType.TYPE_INT:
                            {
                                outVal = String.valueOf(prop.getIntVal());
                            }
                            break;
                            case TOPropertyType.TYPE_FLOAT:
                            {
                                outVal = String.valueOf(prop.getFloatVal());
                            }
                            default:
                            {
                                outVal = prop.getStringVal();
                            }
                        }
                        //utf - field values
                        out.writeUTF(outVal);
                        out.writeInt(Opcodes.S_OPCODE_OK);
                    }
                }
                break;
                case Opcodes.C_OPCODE_GET_PROP:
                {
                    //read 2 int - objId,propId
                    int propId = in.readInt();
                    //return 1 int, 1 utf - propType,value        
                    TOObjectProperty prop = propsManager.getObjectProperty(objId, propId);
                    int propType = propsManager.getPropertyType(prop.getObjectPropertyId()).getPropTypeId();
                    out.writeInt(propType);
                    String outVal = "";
                    switch (propType)
                    {
                        case TOPropertyType.TYPE_INT:
                        {
                            outVal = String.valueOf(prop.getIntVal());
                        }
                        break;
                        case TOPropertyType.TYPE_FLOAT:
                        {
                            outVal = String.valueOf(prop.getFloatVal());
                        }
                        default:
                        {
                            outVal = prop.getStringVal();
                        }
                    }
                    //utf - field values
                    out.writeUTF(outVal);
                    out.writeInt(Opcodes.S_OPCODE_OK);

                }
                break;
                case Opcodes.C_OPCODE_CLEAN_FULL:
                {
                    objectsManager.removeAllObjects();
                    out.writeInt(Opcodes.S_OPCODE_OK);
                }
                break;
                case Opcodes.C_OPCODE_REM_OBJ:
                {
                    objectsManager.removeObjectWithProps(objId);
                    out.writeInt(Opcodes.S_OPCODE_OK);
                }
                break;
                case Opcodes.C_OPCODE_REM_OBJ_BY_TEMPL:
                {
                    objectsManager.removeObjectsByTemplate(objId);
                    out.writeInt(Opcodes.S_OPCODE_OK);
                }
                break;
                default:
                {
                    System.out.println("Default solution: Unhandled opcode: " + String.valueOf(opcode));
                    out.writeInt(Opcodes.S_OPCODE_ERR);
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
        objectsManager = new TOObjectsManager(conn);
        propsManager = new TOPropertiesManager(conn);
    }
}
