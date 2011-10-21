package ru.terraobjects.server;

/**
 *
 * @author terranz
 */
import ru.terraobjects.solutions.Solution;

public class Main
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
	// Название драйвера
	//Starting new thread for every solution
	for (Solution s : SolutionManager.getInstance().getSolutions())
	{
	    SolutionThread st = new SolutionThread(s);
	    Thread t = new Thread(st);
	    t.start();
	}
    }
}
