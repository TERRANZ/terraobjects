package ru.terraobjects.solutions.impl.terrastore.consts;

/**
 *
 * @author korostelev
 */
public interface ClientOpcodes
{

    public static final int OK = 0;
    public static final int QUIT = 999;
    public static final int LOGIN = 1;
    public static final int GET_AVAIL_OBJECTS = 2;
    //отсылаем клиенту список темлейтов, которые он будет запрашивать
    //этот список захардкожен в клиенте, по ним он будет разблокировывать меню
    public static final int READY = 3;
    //клиент принял список и распарсил, теперь он будет посылать
    //GET_OBJECTS для получения списка объектов для работы
    //
    public static final int GET_OBJECTS = 10;
    //клиент просит послать ему список объектов данного темплейта
    //следует проверить его доступ и потом уже слать ему объекты
    //сначала отсылаем ему кол-во объектов данного типа, потом сами объекты
    public static final int GET_OBJECTS_BY_PARENT = 11;
    //клиент просит послать ему список объектов данного парента
    //нужно для отсылки всех товаров на складе, или в накладной
    public static final int GET_OBJECT = 12;
    public static final int NEW_OBJ = 20;
    public static final int DEL_OBJ = 21;
    public static final int SET_PROP_VAL = 22;
    //int objid, int propid, obj val
    public static final int SET_PROPS_VAL = 23;
    //int objid, (int propid, obj val)
    public static final int FIND_OBJ_BY_PROP = 30;
    //
    public static final int TRADE_GET_ALL = 40;
    public static final int TRADE_ADD = 41;
    public static final int TRADE_ADD_PROD = 42;
    public static final int TRADE_DEL_PROD = 43;
    public static final int TRADE_DEL = 44;
    public static final int TRADE_REVERT = 45;
    //
    public static final int WB_GET_ALL = 50;
    public static final int WB_ADD = 51;
    public static final int WB_ADD_PROD = 52;
    public static final int WB_DEL_PROD = 53;
    public static final int WB_DEL = 54;
}
