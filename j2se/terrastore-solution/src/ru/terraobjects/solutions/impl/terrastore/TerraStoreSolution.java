package ru.terraobjects.solutions.impl.terrastore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
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
            //Integer opCode = in.readInt();

            while (in.available() == 0)
            {
            }
            byte[] inBuf = new byte[in.available()];
            in.read(inBuf);
            ClientNetworkPacket clientPacket = new ClientNetworkPacket(inBuf);
            ServerNetworkPacket serverPacket = null;
            switch (clientPacket.getOpcode())
            {
                case ClientOpcodes.LOGIN:
                {
                    String password = clientPacket.getString();//= new String(buf);
                    checkPassword(password);
                    if (!isLoggedIn())
                    {
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.LOGIN_FAILED);
                        serverPacket.putInt(ServerOpcodes.BYE);
                        return true;
                    } else
                    {
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.LOGIN_OK);
                        serverPacket.putInt(level);
                        clientState = ClientState.LOGGED_IN;
                        changeClientState(ClientState.LOGGED_IN);
                    }
                }
                break;

                case ClientOpcodes.GET_AVAIL_OBJECTS:
                {
                    if (clientState != ClientState.LOGGED_IN)
                    {
                        serverPacket = makeBye();
                        return true;
                    } else
                    {
                        //отсылаем список доступных темплейтов
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.AVAIL_OBJECTS);
                        serverPacket.putInt(17);
                        for (int i = 3; i <= 20; i++)
                        {
                            serverPacket.putInt(i);
                        }
                        serverPacket.putInt(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.READY:
                {
                    serverPacket = new ServerNetworkPacket(ServerOpcodes.OK);
                    clientState = ClientState.READY;
                    changeClientState(ClientState.READY);
                }
                break;

                case ClientOpcodes.GET_OBJECTS:
                {
                    if (clientState != ClientState.READY)
                    {
                        serverPacket = makeBye();
                        return true;
                    } else
                    {
                        System.out.println("GetObjects received");
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);

                        serverPacket = new ServerNetworkPacket(ServerOpcodes.OBJECTS);
                        clientPacket.getInt();//unknown 4 bytes 
                        Integer templateId = clientPacket.getInt();//in.readInt();
                        System.out.println("TemplateId : " + String.valueOf(templateId));
                        List<TOObject> objects = objectsManager.getAllObjsByTemplId(templateId);
                        serverPacket.putInt(templateId);
                        if (objects.size() >= 10)
                        {
                            serverPacket.putInt(10);
                        } else
                        {
                            serverPacket.putInt(objects.size());
                        }

                        System.out.println("Count : " + String.valueOf(objects.size()));
                        int countSent = 0;
                        int count = objects.size();
                        for (TOObject obj : objects)
                        {
                            if (countSent <= 10)
                            {
                                setObject(obj, serverPacket);
                                countSent++;
                            } else
                            {
                                count = count - 10;
                                countSent = 0;
                                out.write(serverPacket.getPacket(), 0, serverPacket.getPacket().length);
                                out.flush();
                                serverPacket = new ServerNetworkPacket(ServerOpcodes.OBJECTS);
                                serverPacket.putInt(templateId);
                                if (count >= 10)
                                {
                                    serverPacket.putInt(10);
                                } else
                                {
                                    serverPacket.putInt(count);
                                }
                            }
                        }
                    }
                }
                break;

                case ClientOpcodes.GET_OBJECT:
                {
                    if (clientState != ClientState.READY)
                    {
                        serverPacket = makeBye();
                        return true;
                    } else
                    {
                        clientState = ClientState.WORKING;
                        changeClientState(ClientState.WORKING);
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.OBJECT);
                        Integer objId = clientPacket.getInt();//in.readInt();
                        TOObject retObj = objectsManager.getObject(objId);
                        if (retObj != null)
                        {
                            setObject(retObj, serverPacket);
                        } else
                        {
                            serverPacket = new ServerNetworkPacket(ServerOpcodes.ERR);
                        }
                    }
                }
                break;

                case ClientOpcodes.NEW_OBJ:
                {
                    if (clientState != ClientState.READY)
                    {
                        serverPacket = makeBye();
                        return true;
                    } else
                    {
                        Integer templateId = clientPacket.getInt();//in.readInt();
                        Integer parentId = clientPacket.getInt();//in.readInt();
                        TOObject newObj = objectsManager.createNewObject(templateId);
                        newObj.setParentId(parentId);
                        Integer newObjId = newObj.getId();
                        propsManager.createDefaultPropsForObject(templateId, newObjId);
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.SET_PROP_VAL:
                {
                    if (clientState != ClientState.READY)
                    {
                        serverPacket = makeBye();
                        return true;
                    } else
                    {
                        Integer objId = clientPacket.getInt();//in.readInt();
                        Integer propId = clientPacket.getInt();//in.readInt();
                        Integer propType = clientPacket.getInt();//in.readInt();
                        //setPropVal(objId, propId, propType);
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.SET_PROPS_VAL:
                {
                    if (clientState != ClientState.READY)
                    {
                        serverPacket = makeBye();
                        return true;
                    } else
                    {
                        Integer count = clientPacket.getInt();//in.readInt();
                        for (int i = 0; i < count; i++)
                        {
                            Integer objId = clientPacket.getInt();//in.readInt();
                            Integer propId = clientPacket.getInt();//in.readInt();
                            Integer propType = clientPacket.getInt();//in.readInt();
                            //setPropVal(objId, propId, propType);
                        }
                        serverPacket = new ServerNetworkPacket(ServerOpcodes.OK);
                    }
                }
                break;

                case ClientOpcodes.QUIT:
                {
                    serverPacket = makeBye();
                    return true;
                }
                default:
                {
                    serverPacket = makeBye();
                    serverPacket.putInt(ServerOpcodes.BYE);
                    return true;
                }
            }
            out.write(serverPacket.getPacket(), 0, serverPacket.getPacket().length);
            out.flush();
            System.out.println("Size of written: " + serverPacket.getPacket().length);
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

    private void setPropVal(ClientNetworkPacket clientPacket, Integer objId, Integer propId, Integer propType) throws IOException
    {
        Object val = null;
        switch (propType)
        {
            case TOPropertyType.TYPE_STR:
            {
//                int len = in.readInt() + 4;
//                byte[] buf = new byte[len];
//                in.read(buf);
//                val = new String(buf).substring(3);
                val = clientPacket.getString();
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                val = (Integer) clientPacket.getInt();//
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
//                byte[] buf = new byte[4];
//                in.read(buf);
//                ByteBuffer bb = ByteBuffer.wrap(buf);
//                val = bb.getFloat();
                val = clientPacket.getFloat();
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
//                int len = in.readInt() + 4;
//                byte[] buf = new byte[len];
//                in.read(buf);
//                val = new String(buf).substring(3);
                val = clientPacket.getString();
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
//                byte[] buf = new byte[8];
//                in.read(buf);
//                ByteBuffer bb = ByteBuffer.wrap(buf);
//                val = new Date(bb.getLong());
                val = clientPacket.getLong();
            }
            break;
        }
        propsManager.setObjectPropertyValue(objId, propId, val, propType);
    }

    private void setObject(TOObject obj, ServerNetworkPacket packet) throws UnsupportedEncodingException, IOException
    {
        packet.putInt(obj.getId());
        if (obj.getParentId() != null)
        {
            packet.putInt(obj.getParentId());
        } else
        {
            packet.putInt(0);
        }
        setObjectProps(obj.getId(), packet);
    }

    private void setObjectProps(Integer objId, ServerNetworkPacket packet) throws UnsupportedEncodingException, IOException
    {
        List<TOObjectProperty> objProps = propsManager.getObjPropsForObjId(objId);
        packet.putInt(objProps.size());
        for (TOObjectProperty objProp : objProps)
        {
            packet.putInt(objProp.getId());
            Integer propType = propsManager.getProperty(objProp.getPropertyId()).getTypeId();
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