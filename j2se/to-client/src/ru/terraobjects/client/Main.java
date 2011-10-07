package ru.terraobjects.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
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
            String address = "192.168.1.3"; // это IP-адрес компьютера, где исполняется наша серверная программа.
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
            if ("scan".equals(args[0]))
            {
                String scanDir = args[1];
                System.out.println("Scannig dir: " + scanDir);
                final Collection<File> all = new ArrayList<File>();
                addFilesRecursively(new File(scanDir), all);
                for (File f : all)
                {
                    try
                    {
                        if (!f.isDirectory())
                        {
                            System.out.println("Scanning file: " + f.getAbsolutePath());
                            String hash = MD5Hasher.getMD5Checksum(f.getAbsolutePath());
                            out.writeInt(1);
                            out.writeUTF(f.getAbsolutePath());
                            out.writeUTF(hash);
                            out.flush();
                        }
                    } catch (Exception ex)
                    {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
		out.writeInt(0);
            } else
            {
                if ("read".equals(args[0]))
                {
                    Thread.sleep(500);
                    out.writeInt(2);
                    out.flush();
                } else if ("clean".equals(args[0]))
                {
                    out.writeInt(100);
                    out.flush();
                }
            }
            
        } catch (InterruptedException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void addFilesRecursively(File file, Collection<File> all)
    {
        final File[] children = file.listFiles();
        if (children != null)
        {
            for (File child : children)
            {
                all.add(child);
                addFilesRecursively(child, all);
            }
        }
    }
}
