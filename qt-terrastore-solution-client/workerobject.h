#ifndef WORKEROBJECT_H
#define WORKEROBJECT_H

#include <QObject>
#include <QTcpSocket>
#include <QtNetwork>
#include <QMessageBox>
#include <QThread>
#include "COpCodes.h"
#include "SOpCodes.h"
#include "object.h"
#include "serverpacket.h"

class WorkerObject : public QObject
{
    Q_OBJECT
public:
    explicit WorkerObject(QObject *parent = 0);
    void startLogin(QString pass);
    void getObjects();

private:
    enum ClientState{
        CS_STARTED = 0,
        CS_LOGIN,
        CS_LOGGED_IN,
        CS_GETTING_AVAIL_TEMPLATES,
        CS_READY,
        CS_WORKING,
        CS_PARSING_OBJECT
    };
    enum InnerState {
        IS_IDLE = 0,
        IS_GETTING_OBJECTS
    };

    QTcpSocket *tcpSocket;
    ClientState clientState;
    InnerState innerState;

    void writeInt(quint32 val);
    void parseObjects();
    void parseObject(ServerPacket *sp);
    Property* parseObjectProps();
    QList<qint32> availTemplates;
    QList<Object*> objects;
    int parsedObjects;

signals:
    void logSignal(QString msg);

public slots:

private slots:
    void readFromSocket();
    void displayError(QAbstractSocket::SocketError);

};

#endif // WORKEROBJECT_H
