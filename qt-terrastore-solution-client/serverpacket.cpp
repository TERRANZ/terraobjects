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
    QByteArray ret = buff.mid(0,size);
    this->buff = this->buff.mid(size);
    return ret;
}

qlonglong ServerPacket::readLong()
{
    QDataStream stream(&buff,QIODevice::ReadOnly);
    qlonglong res;
    stream >> res;
    this->buff = this->buff.mid(sizeof(qlonglong));
    return res;
}
qreal ServerPacket::readFloat()
{
    QDataStream stream(&buff,QIODevice::ReadOnly);
    qreal res;
    stream >> res;
    this->buff = this->buff.mid(sizeof(qreal));
    return res;
}

