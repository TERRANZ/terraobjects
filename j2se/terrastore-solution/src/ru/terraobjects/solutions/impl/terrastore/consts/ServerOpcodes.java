package ru.terraobjects.solutions.impl.terrastore.consts;

/**
 *
 * @author korostelev
 */
public interface ServerOpcodes
{

    public static final Integer OK = 0;
    public static final Integer ERR = 1;
    public static final Integer BYE = 999;
    public static final Integer LOGIN_FAILED = 2;
    public static final Integer LOGIN_OK = 3;//+int = user level
    public static final Integer AVAIL_OBJECTS = 4;
    public static final Integer OBJECTS_COUNT = 5;
    public static final Integer OBJECTS = 6;
    //xml
}
