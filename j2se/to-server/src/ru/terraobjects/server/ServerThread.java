package ru.terraobjects.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.solutions.Solution;

/**
 *
 * @author korostelev
 */
public class ServerThread implements Runnable
{

    private Socket serverSocket = null;
    private Connection sqlConnection = null;
    private Solution solution = null;

    public ServerThread(Socket s, Connection conn, Solution solution)
    {
        this.serverSocket = s;
        this.sqlConnection = conn;
        this.solution = solution;
    }

    public void run()
    {
        InputStream sin = null;
        OutputStream sout = null;
        try
        {
            sin = serverSocket.getInputStream();
            sout = serverSocket.getOutputStream();
            DataInputStream in = new DataInputStream(new BufferedInputStream(sin));
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(sout));
            boolean exit = false;
            solution.setParams(sqlConnection, in, out);
            while (!exit)
            {
                exit = solution.parseInput();
            }
        } catch (IOException ex)
        {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                sin.close();
                sout.close();
                serverSocket.close();
            } catch (IOException ex)
            {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}