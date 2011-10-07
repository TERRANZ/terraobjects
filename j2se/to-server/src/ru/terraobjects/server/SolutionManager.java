package ru.terraobjects.server;

import java.util.ArrayList;
import java.util.List;
import ru.terraobjects.solutions.Solution;
import ru.terraobjects.solutions.DefaultSolution;
import ru.terraobjects.solutions.HashScanSolution;

/**
 *
 * @author korostelev
 */
public class SolutionManager
{
    
    private static SolutionManager instance = new SolutionManager();
    
    private SolutionManager()
    {
    }
    
    public static SolutionManager getInstance()
    {
        return instance;
    }
    
    public List<Solution> getSolutions()
    {
        ArrayList<Solution> solutions = new ArrayList<Solution>();
        solutions.add(new HashScanSolution());
        solutions.add(new DefaultSolution());
        return solutions;
    }
}
