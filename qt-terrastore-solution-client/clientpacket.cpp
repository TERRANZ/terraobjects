#include "clientpacket.h"

ClientPacket::ClientPacket()
{
}

void ClientPacket::putInt(quint32 val)
{
    QDataStream out(&buff, QIODevice::WriteOnly);
    out << val;
}

void ClientPacket::putFloat(qreal val)
{
    QDataStream out(&buff, QIODevice::WriteOnly);
    out << val;
}

void ClientPacket::putLong(qlonglong val)
{
    QDataStream out(&buff, QIODevice::WriteOnly);
    out << val;
}

void ClientPacket::putString(QString val)
{
    QDataStream out(&buff, QIODevice::WriteOnly);
    out << (quint32) val.size();
    out << val.toUtf8().data();
}

QByteArray ClientPacket::getData()
{
    QByteArray outBuf;
    QDataStream out(&outBuf, QIODevice::WriteOnly);
    out << opCode;
    out << (quint32) buff.size() + 8;
    out << buff;
    //out.writeRawData(buff.constData(),sizeof(buff.constData()));
    return outBuf;
}
