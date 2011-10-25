#ifndef WORKERTHREAD_H
#define WORKERTHREAD_H

#include <QThread>

class WorkerThread : public QThread
{
    Q_OBJECT
public:
    explicit WorkerThread(QObject *parent = 0);
    WorkerThread()
    {
        moveToThread(this);
    }
    void run();
signals:
    void progress(int);
    void dataReady(QByteArray);

public slots:
    void doWork();
    void timeoutHandler();

};

#endif // WORKERTHREAD_H
