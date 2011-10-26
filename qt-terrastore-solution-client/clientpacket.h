#ifndef CLIENTPACKET_H
#define CLIENTPACKET_H
#include <QtGlobal>
#include <QByteArray>
#include <QDataStream>

class ClientPacket
{
public:
    ClientPacket();
    void setOpCode(quint32 opCode) {this->opCode = opCode;}
    int getSize() {return buff.size();}
    void putInt(quint32 val);
    void putFloat(qreal val);
    void putLong(qlonglong val);
    void putString(QString val);
    QByteArray getData();

private:
    quint32 opCode;
    QByteArray buff;
};

#endif // CLIENTPACKET_H
