#-------------------------------------------------
#
# Project created by QtCreator 2011-10-20T10:14:46
#
#-------------------------------------------------

QT       += core gui network socket

TARGET = terrastore
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    workerobject.cpp \
    workerthread.cpp \
    object.cpp \
    property.cpp \
    serverpacket.cpp \
    clientpacket.cpp

HEADERS  += mainwindow.h \
    COpCodes.h \
    SOpCodes.h \
    workerobject.h \
    workerthread.h \
    object.h \
    property.h \
    serverpacket.h \
    clientpacket.h

FORMS    += mainwindow.ui




















