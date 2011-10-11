package ru.terraobjects.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ru.terraobjects.solutions.Solution;

/**
 *
 * @author korostelev
 */
public class SolutionThread implements Runnable
{

    private Solution solution = null;

    public SolutionThread(Solution s)
    {
	this.solution = s;
    }

    public void run()
    {
	int count = 0;
	try
	{
	    Integer port = solution.getPort();
	    System.out.println("Starting solution " + solution.getName());
	    ServerSocket server = new ServerSocket(port);
	    boolean stop = false;
	    Socket socket = null;

	    while (!stop)
	    {
		socket = server.accept();
		ServerThread st = new ServerThread(socket, solution, count++);
		Thread t = new Thread(st);
		t.start();
	    }
	} catch (IOException ex)
	{
	    //Logger.getLogger(SolutionThread.class.getName()).log(Level.SEVERE, null, ex);
	    //return;
	    System.out.println("Solution thread: client disconnected: "+String.valueOf(count));
	    count--;
	}

    }
}
