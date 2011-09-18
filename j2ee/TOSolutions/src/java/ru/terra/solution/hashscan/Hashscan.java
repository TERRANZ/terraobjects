package ru.terra.solution.hashscan;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author terranz
 */
public class Hashscan
{

    private static Hashscan instance = new Hashscan();

    private Hashscan()
    {
	try
	{
	    // установить сокет на 127.0.0.1, порт 12345
	    ServerSocket server = new ServerSocket(12345, 0,
		    InetAddress.getByName("127.0.0.1"));

	    System.out.println("server is started");

	    // слушаем порт
	    while (true)
	    {
		Socket client = server.accept();
		ServerThread st = new ServerThread(0, client);
		Thread t = new Thread(st);
		t.start();
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static Hashscan getInstance()
    {
	return instance;
    }

    public void startListen()
    {
    }
}
