#ifndef SERVERPACKET_H
#define SERVERPACKET_H
#include <QByteArray>
#include <QDataStream>

class ServerPacket
{
public:
    ServerPacket(QByteArray arr);
    quint32 getOpCode() {return opCode;}
    quint32 getSize() {return size;}
    qint32 readInt();

private:
    quint32 size, opCode;
    QByteArray buff;

};

#endif // SERVERPACKET_H
