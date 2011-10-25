#include "serverpacket.h"

ServerPacket::ServerPacket(QByteArray arr)
{
    this->buff = arr.mid(8);
    QDataStream stream(&arr,QIODevice::ReadOnly);
    stream >> opCode;
    stream >> size;
}

quint32 ServerPacket::readInt()
{
    QDataStream stream(&buff,QIODevice::ReadOnly);
    quint32 res;
    stream >> res;
    this->buff = this->buff.mid(sizeof(quint32));
    return res;
}

QByteArray ServerPacket::read(quint32 size)
{
    return this->buff.mid(0,size);
}
