package ru.terraobjects.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author terranz
 */
public class ReaderThread implements Runnable
{

    private DataInputStream in;

    public ReaderThread(DataInputStream br)
    {
	this.in = br;
    }

    public void run()
    {
	try
	{
	    int count = in.readInt();
	    System.out.println("Readed count: " + String.valueOf(count));
	    int i = 0;
	    while (true)
	    {
		System.out.println(String.valueOf(i++));
		String name = in.readUTF();
		String hash = in.readUTF();
		System.out.println(name + " : " + hash);
	    }
	} catch (IOException ex)
	{
	    Logger.getLogger(ReaderThread.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
