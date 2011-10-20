package ru.terraobjects.solutions.impl.terrastore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.entity.TOObject;
import ru.terraobjects.entity.TOObjectProperty;
import ru.terraobjects.entity.TOPropertyType;
import ru.terraobjects.entity.dao.TOObjectsManager;
import ru.terraobjects.entity.dao.TOPropertiesManager;
import ru.terraobjects.solutions.Solution;
import ru.terraobjects.solutions.annotation.ASolution;
import ru.terraobjects.solutions.impl.terrastore.consts.ClientOpcodes;
import ru.terraobjects.solutions.impl.terrastore.consts.ServerOpcodes;

/**
 *
 * @author korostelev
 */
@ASolution(name = "Terra Store Solution", port = "2011")
public class TerraStoreSolution implements Solution
{

    private String LOG_TAG = "TerraStoreSolution:";
    private Connection conn;
    private DataInputStream in;
    private DataOutputStream out;
    private TOObjectsManager objectsManager;
    private TOPropertiesManager propsManager;
    private Integer level = -1;

    private enum ClientState
    {

        NOT_LOGGED_IN,
        LOGGED_IN,
        WORKING,
        READY,
        RECV_OBJECTS;
    };
    private ClientState clientState = ClientState.NOT_LOGGED_IN;

    @Override
    public boolean parseInput()
    {
        try
        {
            Integer opCode = in.readInt();
            switch (opCode)
            {

                case ClientOpcodes.LOGIN:
                {
                    int len = in.readInt();
                    byte[] buf = new byte[len];
                    in.read(buf);
                    String password = new String(buf);
                    checkPassword(password);
                    if (!isLoggedIn())
                    {
                        out.writeInt(ServerOpcodes.LOGIN_FAILED);
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        out.writeInt(ServerOpcodes.LOGIN_OK);
                        out.writeInt(level);
                        clientState = ClientState.LOGGED_IN;
                        changeClientState(ClientState.LOGGED_IN);
                    }
                }
                break;

                case ClientOpcodes.GET_AVAIL_OBJECTS:
                {
                    if (clientState != ClientState.LOGGED_IN)
                    {
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        //отсылаем список доступных темлейтов
                        for (int i = 3; i <= 20; i++)
                        {
                            out.writeInt(i);
                        }
                        out.writeInt(ServerOpcodes.OK);
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);
                    }
                }
                break;

                case ClientOpcodes.READY:
                {
                    if (clientState != ClientState.WORKING)
                    {
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        out.writeInt(ServerOpcodes.OK);
                        clientState = ClientState.READY;
                        changeClientState(ClientState.READY);
                    }
                }
                break;

                case ClientOpcodes.GET_OBJECTS:
                {
                    if (clientState != ClientState.READY)
                    {
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);

                        Integer templateId = in.readInt();
                        List<TOObject> objects = objectsManager.getAllObjsByTemplId(templateId);
                        out.writeInt(objects.size());
                        for (TOObject obj : objects)
                        {
                            List<TOObjectProperty> objProps = propsManager.getObjPropsForObjId(obj.getObjectId());
                            out.writeInt(objProps.size());
                            for (TOObjectProperty objProp : objProps)
                            {
                                out.writeInt(objProp.getObjectPropertyId());
                                Integer propType = propsManager.getPropertyType(objProp.getPropertyId()).getPropTypeId();
                                out.writeInt(propType);
                                switch (propType)
                                {
                                    case TOPropertyType.TYPE_STR:
                                    {
                                        byte[] outbytes = ((String) propsManager.getPropertyValue(obj.getObjectId(), objProp.getPropertyId())).getBytes("UTF-8");
                                        out.writeInt(outbytes.length);
                                        out.write(outbytes);
                                    }
                                    break;
                                    case TOPropertyType.TYPE_INT:
                                    {
                                        out.writeInt(((Integer) propsManager.getPropertyValue(obj.getObjectId(), objProp.getPropertyId())).intValue());
                                    }
                                    break;
                                    case TOPropertyType.TYPE_FLOAT:
                                    {
                                        out.writeFloat(((Float) propsManager.getPropertyValue(obj.getObjectId(), objProp.getPropertyId())).floatValue());
                                    }
                                    break;
                                    case TOPropertyType.TYPE_TEXT:
                                    {
                                        byte[] outbytes = ((String) propsManager.getPropertyValue(obj.getObjectId(), objProp.getPropertyId())).getBytes("UTF-8");
                                        out.writeInt(outbytes.length);
                                        out.write(outbytes);
                                    }
                                    break;
                                    case TOPropertyType.TYPE_DATE:
                                    {
                                        out.writeLong(((Date) propsManager.getPropertyValue(obj.getObjectId(), objProp.getPropertyId())).getTime());
                                    }
                                    break;
                                }
                            }
                        }
                        out.writeInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.NEW_OBJ:
                {
                    if (clientState != ClientState.READY)
                    {
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        Integer templateId = in.readInt();
                        Integer parentId = in.readInt();
                        Integer newObjId = objectsManager.createNewObject(templateId).getObjectId();
                        propsManager.createDefaultPropsForObject(templateId, newObjId);
                        out.writeInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.SET_PROP_VAL:
                {
                    if (clientState != ClientState.READY)
                    {
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        Integer objId = in.readInt();
                        Integer propId = in.readInt();
                        Integer propType = in.readInt();
                        setPropVal(objId, propId, propType);
                        out.writeInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.SET_PROPS_VAL:
                {
                    if (clientState != ClientState.READY)
                    {
                        out.writeInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        Integer count = in.readInt();
                        for (int i = 0; i < count; i++)
                        {
                            Integer objId = in.readInt();
                            Integer propId = in.readInt();
                            Integer propType = in.readInt();
                            setPropVal(objId, propId, propType);
                        }
                        out.writeInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.QUIT:
                {
                    out.writeInt(ServerOpcodes.BYE);
                    return true;
                }
                default:
                {
                    out.writeInt(ServerOpcodes.ERR);
                    out.writeInt(ServerOpcodes.BYE);
                    return true;
                }
            }            
        } catch (IOException ex)
        {
            //Logger.getLogger(TerraStoreSolution.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("TerraStoreSolution: client disconnected");
        }
        return false;
    }

    @Override
    public void setParams(Connection c, DataInputStream in, DataOutputStream out)
    {
        this.in = in;
        this.out = out;
        this.conn = c;
        objectsManager = new TOObjectsManager(conn);
        propsManager = new TOPropertiesManager(conn);
    }

    private void checkPassword(String pass)
    {
        //TODO implement password checking
        level = 0;
    }

    private Boolean isLoggedIn()
    {
        return level != -1;
    }

    private void changeClientState(ClientState newState)
    {
        System.out.println(LOG_TAG + "client state " + clientState.toString() + " => " + newState.toString());
    }

    private void setPropVal(Integer objId, Integer propId, Integer propType) throws IOException
    {
        Object val = null;

        switch (propType)
        {
            case TOPropertyType.TYPE_STR:
            {
                int len = in.readInt();
                byte[] buf = new byte[len];
                in.read(buf);
                val = new String(buf);
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                val = (Integer) in.readInt();
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
                val = (Float) in.readFloat();
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
                int len = in.readInt();
                byte[] buf = new byte[len];
                in.read(buf);
                val = new String(buf);
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
                val = (Long) in.readLong();
            }
            break;
        }
        propsManager.setPropertyValue(objId, propId, val, propType);
    }
}
