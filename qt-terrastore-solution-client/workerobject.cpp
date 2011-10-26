#include "workerobject.h"

WorkerObject::WorkerObject(QObject *parent) :
    QObject(parent)
{
    QString ipAddress;
    ipAddress = QHostAddress(QHostAddress::LocalHost).toString();
    tcpSocket = new QTcpSocket(this);
    connect(tcpSocket, SIGNAL(readyRead()), this, SLOT(readFromSocket()));
    connect(tcpSocket, SIGNAL(error(QAbstractSocket::SocketError)),
            this, SLOT(displayError(QAbstractSocket::SocketError)));
    tcpSocket->connectToHost(ipAddress,2011);
    clientState = CS_STARTED;
    innerState = IS_IDLE;
    parsedObjects = 0;
}

void WorkerObject::startLogin(QString pass)
{
    emit logSignal("Start logging in");
    clientState = CS_LOGIN;

    ClientPacket *cp = new ClientPacket();
    cp->setOpCode(COpCodes::LOGIN);
    cp->putString(pass);
    sendToClient(cp->getData());
}

void WorkerObject::readFromSocket()
{
    emit logSignal("Read from socket");
    emit logSignal("Available is: " + QString::number(tcpSocket->bytesAvailable()));
    ServerPacket *sp = new ServerPacket(tcpSocket->read(tcpSocket->bytesAvailable()));
    emit logSignal("OpCode is: " + QString::number(sp->getOpCode()));
    emit logSignal("Size is: " + QString::number(sp->getSize()));

    quint32 opCode = sp->getOpCode();
    switch (opCode)
    {
    case SOpCodes::OK:
    {
        emit logSignal("Server said: OK");
        emit logSignal("ParsedObjects: "+QString::number(parsedObjects));
    }break;

    case SOpCodes::LOGIN_OK:
    {
        emit logSignal("Logged in");
        quint32 level = sp->readInt();
        emit logSignal("Level is: " + QString::number(level));
        //writeInt(COpCodes::GET_AVAIL_OBJECTS);
        ClientPacket *cp = new ClientPacket();
        cp->setOpCode(COpCodes::GET_AVAIL_OBJECTS);
        sendToClient(cp->getData());
    }break;

    case SOpCodes::AVAIL_OBJECTS:
    {
        emit logSignal("Receiving avail templates...");
        quint32 templatesCount = sp->readInt();
        emit logSignal("Templates count: " + QString::number(templatesCount));
        for (int i = 0;i<=templatesCount;i++)
        {
            quint32 templateId = sp->readInt();
            emit logSignal("Template received: " + QString::number(templateId));
            availTemplates.append(templateId);
        }
        ClientPacket *cp = new ClientPacket();
        cp->setOpCode(COpCodes::READY);
        sendToClient(cp->getData());
    }break;

    case SOpCodes::LOGIN_FAILED:
    {
        emit logSignal("Login failed!");
    }break;

    case SOpCodes::OBJECTS:
    {
        emit logSignal("Receiving objects...");
        quint32 templateId = sp->readInt();
        emit logSignal("Template received: " + QString::number(templateId));
        quint32 count = sp->readInt();
        emit logSignal("Objects count: " + QString::number(count));
        if (count != 0){
            for (int i = 0;i<count;i++)
            {
                parseObject(sp);
            }
        }else
        {
            emit logSignal("Objects count is zero");
        }
        emit logSignal("ParsedObjects: "+QString::number(parsedObjects));
        ClientPacket *cp = new ClientPacket();
        cp->setOpCode(COpCodes::READY);
        sendToClient(cp->getData());
    }break;

    }
}

void WorkerObject::parseObject(ServerPacket *sp)
{
    quint32 objId = sp->readInt();
    emit logSignal("parseObject objId: "+QString::number(objId));
    quint32 parentId = sp->readInt();
    emit logSignal("parseObject parentId: "+QString::number(parentId));
    Object *newObj = new Object(objId,parentId);
    quint32 count = sp->readInt();
    emit logSignal("parseObject props count: "+QString::number(count));
    parsedObjects++;
    for (int i = 0; i<count;i++)
    {
        emit logSignal("Parsing iteration: "+QString::number(i));
        quint32 propId = sp->readInt();
        emit logSignal("parseObject propId: "+QString::number(propId));
        quint32 propType = sp->readInt();
        emit logSignal("parseObject propType: "+QString::number(propType));
        Property *newProp = new Property(propId,propType);
        quint32 size = sp->readInt();
        emit logSignal("parseObject propsize: "+QString::number(size));
        QByteArray val = sp->read(size);
        newProp->setVal(val);
        newObj->addProp(newProp);
        QString strVal = QString::fromUtf8(newProp->getVal().data());
        emit logSignal("parseObject prop string value: "+strVal.toUtf8());
    }
    objects.append(newObj);
}

Property* WorkerObject::parseObjectProps()
{
    return NULL;
}

void WorkerObject::getObjects()
{
    parsedObjects = 0;
    emit logSignal("Getting objects");
    clientState = CS_PARSING_OBJECT;
    //    foreach (quint32 templ,availTemplates){
    quint32 templ = 2;
    emit logSignal("Getting objects for templateId: "+QString::number(templ));
    ClientPacket *cp = new ClientPacket();
    cp->setOpCode(COpCodes::GET_OBJECTS);
    cp->putInt(templ);
    sendToClient(cp->getData());
    //    }
}

void WorkerObject::sendToClient(QByteArray arr)
{
    //    QDataStream out(&arr,QIODevice::ReadOnly);
    emit logSignal("Sending to client: "+QString::number(arr.size()));
    tcpSocket->write(arr);
}

void WorkerObject::displayError(QAbstractSocket::SocketError socketError)
{
    switch (socketError) {
    case QAbstractSocket::RemoteHostClosedError:
        break;
    case QAbstractSocket::HostNotFoundError:
        emit logSignal(
                    tr("The host was not found. Please check the "
                       "host name and port settings."));
        break;
    case QAbstractSocket::ConnectionRefusedError:
        emit logSignal(
                    tr("The connection was refused by the peer. "
                       "Make sure the fortune server is running, "
                       "and check that the host name and port "
                       "settings are correct."));
        break;
    default:
        emit logSignal(
                    tr("The following error occurred: %1.")
                    .arg(tcpSocket->errorString()));
    }
}
