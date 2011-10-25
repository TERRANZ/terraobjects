#ifndef OBJECT_H
#define OBJECT_H
#include <QtGlobal>
#include "property.h"
#include <QList>

class Object
{
public:
    Object(quint32 id,quint32 parentId);
    quint32 getId(){return id;}
    quint32 getParentid() {return parentId;}
    void addProp(Property *prop) {props.append(prop);}
    void delProp(Property *prop) {props.removeOne(prop);}
    void delProp(int ind) {props.removeAt(ind);}

private:
    quint32 id;
    quint32 parentId;
    QList<Property*> props;
};

#endif // OBJECT_H
