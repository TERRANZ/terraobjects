#ifndef OPCODES_H
#define OPCODES_H
#include <QtGlobal>

class COpCodes
{
public:
    const static qint32 OK = 0;
    const static qint32 QUIT = 999;
    const static qint32 LOGIN = 1;
    const static qint32 GET_AVAIL_OBJECTS = 2;
    const static qint32 READY = 3;
    const static qint32 GET_OBJECTS = 10;
    const static qint32 GET_OBJECTS_BY_PARENT = 11;
    const static qint32 NEW_OBJ = 20;
    const static qint32 DEL_OBJ = 21;
    const static qint32 SET_PROP_VAL = 22;
    const static qint32 SET_PROPS_VAL = 23;
    const static qint32 FIND_OBJ_BY_PROP = 30;
    const static qint32 TRADE_GET_ALL = 40;
    const static qint32 TRADE_ADD = 41;
    const static qint32 TRADE_ADD_PROD = 42;
    const static qint32 TRADE_DEL_PROD = 43;
    const static qint32 TRADE_DEL = 44;
    const static qint32 TRADE_REVERT = 45;
    const static qint32 WB_GET_ALL = 50;
    const static qint32 WB_ADD = 51;
    const static qint32 WB_ADD_PROD = 52;
    const static qint32 WB_DEL_PROD = 53;
    const static qint32 WB_DEL = 54;
};

#endif // OPCODES_H
