#ifndef SOPCODES_H
#define SOPCODES_H
#include <QtGlobal>

class SOpCodes
{
public:
    const static qint32 OK = 0;
    const static qint32 ERR = 1;
    const static qint32 BYE = 999;
    const static qint32 LOGIN_FAILED = 2;
    const static qint32 LOGIN_OK = 3;//+qint32 = user level
    const static qint32 AVAIL_OBJECTS = 4;
    const static qint32 OBJECT = 5;
    const static qint32 OBJECTS = 6;
};

#endif // SOPCODES_H
