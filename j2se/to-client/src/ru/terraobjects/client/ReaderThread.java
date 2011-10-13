package ru.terraobjects.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author terranz
 */
public class ReaderThread implements Runnable
{

    private DataInputStream in;
    LinkedList<String> hashes = new LinkedList<String>();
    LinkedList<String> names = new LinkedList<String>();

    public ReaderThread(DataInputStream br)
    {
        this.in = br;
    }

    public void run()
    {
        try
        {
            int count = in.readInt();

            int i = 0;
            while (i < count)
            {
                i++;
                String name = in.readUTF();
                String hash = in.readUTF();
                addHash(name, hash);
            }
            System.out.println("Readed count: " + String.valueOf(count));

        } catch (IOException ex)
        {
            //Logger.getLogger(ReaderThread.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Server disconnected us");
        }
    }

    private void addHash(String name, String hash)
    {
        names.add(name);
        int i = hashes.indexOf(hash);
        if (i != -1)
        {
            System.out.println(name + " is a dublicate of " + names.get(i));
        }
        hashes.add(hash);
    }
}
