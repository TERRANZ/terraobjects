package ru.terraobjects.server;

/**
 *
 * @author terranz
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ru.terraobjects.solutions.hashscan.HashScanSolution;

public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
	Connection connection;
	try
	{
	    // Название драйвера
	    String driverName = "com.mysql.jdbc.Driver";

	    Class.forName(driverName);

	    // Create a connection to the database
	    String serverName = "localhost";
	    String mydatabase = "terraobjects";
	    String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
	    String username = "scan";
	    String password = "scan";

	    connection = DriverManager.getConnection(url, username, password);
	    System.out.println("is connect to DB" + connection.getCatalog());

	    HashScanSolution hss = new HashScanSolution();
	    hss.setConnection(connection);
	    hss.go();

//	    connection.close();
	} // end try
	catch (ClassNotFoundException e)
	{
	    e.printStackTrace();
	    // Could not find the database driver
	} catch (SQLException e)
	{
	    e.printStackTrace();
	    // Could not connect to the database
	}
    }
}
