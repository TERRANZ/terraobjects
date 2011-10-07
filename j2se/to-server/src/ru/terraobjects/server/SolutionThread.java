package ru.terraobjects.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.solutions.Solution;

/**
 *
 * @author korostelev
 */
public class SolutionThread implements Runnable
{

    private Solution solution = null;
    private Connection conn = null;

    public SolutionThread(Solution s, Connection c)
    {
        this.solution = s;
        this.conn = c;
    }

    public void run()
    {
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
                ServerThread st = new ServerThread(socket, conn, solution);
                Thread t = new Thread(st);
                t.start();
            }
        } catch (IOException ex)
        {
            Logger.getLogger(SolutionThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
