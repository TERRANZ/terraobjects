#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include "workerobject.h"
#include <QThread>

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_pushButton_clicked();
    void onLogSignal(QString msg);

    void on_pushButton_2_clicked();

private:
    Ui::MainWindow *ui;
    WorkerObject *worker;
};

#endif // MAINWINDOW_H
