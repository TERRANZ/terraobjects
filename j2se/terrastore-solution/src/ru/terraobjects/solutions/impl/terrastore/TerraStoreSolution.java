package ru.terraobjects.solutions.impl.terrastore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
            ServerNetworkPacket packet = null;
            switch (opCode)
            {
                case ClientOpcodes.LOGIN:
                {

                    int len = in.readInt() + 4;
                    byte[] buf = new byte[in.available()];
                    in.read(buf);

                    String password = new String(buf);
                    checkPassword(password);
                    if (!isLoggedIn())
                    {
                        packet = new ServerNetworkPacket(ServerOpcodes.LOGIN_FAILED);
                        packet.putInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        packet = new ServerNetworkPacket(ServerOpcodes.LOGIN_OK);
                        packet.putInt(level);
                        clientState = ClientState.LOGGED_IN;
                        changeClientState(ClientState.LOGGED_IN);
                    }
                }
                break;

                case ClientOpcodes.GET_AVAIL_OBJECTS:
                {
                    if (clientState != ClientState.LOGGED_IN)
                    {
                        packet = makeBye();
                        return true;
                    } else
                    {
                        //отсылаем список доступных темплейтов
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);
                        packet = new ServerNetworkPacket(ServerOpcodes.AVAIL_OBJECTS);
                        packet.putInt(17);
                        for (int i = 3; i <= 20; i++)
                        {
                            packet.putInt(i);
                        }
                        packet.putInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.READY:
                {
                    packet = new ServerNetworkPacket(ServerOpcodes.OK);
                    clientState = ClientState.READY;
                    changeClientState(ClientState.READY);
                }
                break;

                case ClientOpcodes.GET_OBJECTS:
                {
                    if (clientState != ClientState.READY)
                    {
                        packet = makeBye();
                        return true;
                    } else
                    {
                        System.out.println("GetObjects received");
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);

                        packet = new ServerNetworkPacket(ServerOpcodes.OBJECTS);
                        Integer templateId = in.readInt();
                        System.out.println("TemplateId : " + String.valueOf(templateId));
                        List<TOObject> objects = objectsManager.getAllObjsByTemplId(templateId);
                        packet.putInt(templateId);
                        packet.putInt(objects.size());
                        for (TOObject obj : objects)
                        {
                            setObject(obj, packet);
                        }
                        packet.putInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.GET_OBJECT:
                {
                    if (clientState != ClientState.READY)
                    {
                        packet = makeBye();
                        return true;
                    } else
                    {
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);
                        packet = new ServerNetworkPacket(ServerOpcodes.OBJECT);
                        Integer objId = in.readInt();
                        TOObject retObj = objectsManager.getObject(objId);
                        if (retObj != null)
                        {
                            setObject(retObj, packet);
                        } else
                        {
                            packet = new ServerNetworkPacket(ServerOpcodes.ERR);
                        }
                    }
                }
                break;

                case ClientOpcodes.NEW_OBJ:
                {
                    if (clientState != ClientState.READY)
                    {
                        packet = makeBye();
                        return true;
                    } else
                    {
                        Integer templateId = in.readInt();
                        Integer parentId = in.readInt();
                        TOObject newObj = objectsManager.createNewObject(templateId);
                        newObj.setParentId(parentId);
                        Integer newObjId = newObj.getId();
                        propsManager.createDefaultPropsForObject(templateId, newObjId);
                        packet = new ServerNetworkPacket(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.SET_PROP_VAL:
                {
                    if (clientState != ClientState.READY)
                    {
                        packet = makeBye();
                        return true;
                    } else
                    {
                        Integer objId = in.readInt();
                        Integer propId = in.readInt();
                        Integer propType = in.readInt();
                        //setPropVal(objId, propId, propType);
                        packet = new ServerNetworkPacket(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.SET_PROPS_VAL:
                {
                    if (clientState != ClientState.READY)
                    {
                        packet = makeBye();
                        return true;
                    } else
                    {
                        Integer count = in.readInt();
                        for (int i = 0; i < count; i++)
                        {
                            Integer objId = in.readInt();
                            Integer propId = in.readInt();
                            Integer propType = in.readInt();
                            //setPropVal(objId, propId, propType);
                        }
                        packet = new ServerNetworkPacket(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.QUIT:
                {
                    packet = makeBye();
                    return true;
                }
                default:
                {
                    packet = makeBye();
                    packet.putInt(ServerOpcodes.BYE);
                    return true;
                }
            }
            out.write(packet.getPacket());
        } catch (IOException ex)
        {
            //Logger.getLogger(TerraStoreSolution.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("TerraStoreSolution: client disconnected");
            throw new RuntimeException(ex);
        }
        return false;
    }

    @Override
    public void setParams(Connection c, BufferedInputStream in, BufferedOutputStream out)
    {
        this.in = new DataInputStream(in);
        this.out = new DataOutputStream(out);
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
                int len = in.readInt() + 4;
                byte[] buf = new byte[len];
                in.read(buf);
                val = new String(buf).substring(3);
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                val = (Integer) in.readInt();
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
                byte[] buf = new byte[4];
                in.read(buf);
                ByteBuffer bb = ByteBuffer.wrap(buf);
                val = bb.getFloat();
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
                int len = in.readInt() + 4;
                byte[] buf = new byte[len];
                in.read(buf);
                val = new String(buf).substring(3);
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
                byte[] buf = new byte[8];
                in.read(buf);
                ByteBuffer bb = ByteBuffer.wrap(buf);
                val = new Date(bb.getLong());
            }
            break;
        }
        propsManager.setPropertyValue(objId, propId, val, propType);
    }

    private void setObject(TOObject obj, ServerNetworkPacket packet) throws UnsupportedEncodingException, IOException
    {
        packet.putInt(obj.getId());
        packet.putInt(obj.getParentId());
        setObjectProps(obj.getId(), packet);
    }

    private void setObjectProps(Integer objId, ServerNetworkPacket packet) throws UnsupportedEncodingException, IOException
    {
        List<TOObjectProperty> objProps = propsManager.getObjPropsForObjId(objId);
        packet.putInt(objProps.size());
        for (TOObjectProperty objProp : objProps)
        {
            packet.putInt(objProp.getId());
            Integer propType = propsManager.getPropertyType(objProp.getPropertyId()).getPropTypeId();
            packet.putInt(propType);
            byte[] outbytes = new byte[]
            {
            };
            switch (propType)
            {

                case TOPropertyType.TYPE_STR:
                {
                    outbytes = ((String) propsManager.getPropertyValue(objId, objProp.getPropertyId())).getBytes("UTF-8");
                }
                break;
                case TOPropertyType.TYPE_INT:
                {
                    outbytes = TypeConverter.toByta(((Integer) propsManager.getPropertyValue(objId, objProp.getPropertyId())).intValue());
                }
                break;
                case TOPropertyType.TYPE_FLOAT:
                {
                    outbytes = TypeConverter.toByta(((Float) propsManager.getPropertyValue(objId, objProp.getPropertyId())).floatValue());
                }
                break;
                case TOPropertyType.TYPE_TEXT:
                {
                    outbytes = ((String) propsManager.getPropertyValue(objId, objProp.getPropertyId())).getBytes("UTF-8");
                }
                break;
                case TOPropertyType.TYPE_DATE:
                {
                    outbytes = TypeConverter.toByta(((Date) propsManager.getPropertyValue(objId, objProp.getPropertyId())).getTime());
                }
                break;
            }
            packet.putInt(outbytes.length);
            packet.putBuf(outbytes);
        }
    }

    private ServerNetworkPacket makeBye()
    {
        return new ServerNetworkPacket(ServerOpcodes.BYE);
    }
}