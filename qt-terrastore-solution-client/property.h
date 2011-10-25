#ifndef PROPERTY_H
#define PROPERTY_H
#include <QtGlobal>
#include <QByteArray>

class Property
{
public:
    Property(quint32 id,quint32 typeId);
    quint32 getId() {return id;}
    quint32 getTypeId() {return typeId;}
    void setId(quint32 id) {this->id = id;}
    void setType(quint32 typeId) {this->typeId = typeId;}
    void setVal(QByteArray val) {this->val = val;}
    QByteArray getVal() {return val;}

private:
    quint32 id;
    quint32 typeId;
    QByteArray val;
};

#endif // PROPERTY_H
