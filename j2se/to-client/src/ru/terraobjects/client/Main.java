package ru.terraobjects.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author terranz
 */
public class Main
{

    public static void main(String[] args)
    {
	try
	{
	    int serverPort = 12345; // здесь обязательно нужно указать порт к которому привязывается сервер.
	    String address = "127.0.0.1"; // это IP-адрес компьютера, где исполняется наша серверная программа.
	    InetAddress ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
	    System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
	    Socket socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
	    System.out.println("Yes! I just got hold of the program.");
	    // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
	    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
	    DataInputStream in = new DataInputStream(socket.getInputStream());
	    // Создаем поток для чтения с клавиатуры.	   	    
	    ReaderThread rt = new ReaderThread(in);
	    Thread t = new Thread(rt);
	    t.start();
	    Thread.sleep(500);
	    out.writeInt(2);
	} catch (InterruptedException ex)
	{
	    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex)
	{
	    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
