#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    worker = new WorkerObject(this);

    connect(worker,SIGNAL(logSignal(QString)),this,SLOT(onLogSignal(QString)));
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    worker->startLogin("mysuperpass");
}

void  MainWindow::onLogSignal(QString msg)
{
    ui->textEdit->append(msg);
    qDebug(msg.toUtf8());
}

void MainWindow::on_pushButton_2_clicked()
{
    worker->getObjects();
}
