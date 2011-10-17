package ru.terraobjects.solutions.impl.terrastore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.Connection;
import ru.terraobjects.solutions.Solution;
import ru.terraobjects.solutions.annotation.ASolution;

/**
 *
 * @author korostelev
 */
@ASolution(name = "Terra Store Solution", port = "2011")
public class TerraStoreSolution implements Solution
{
    private Connection conn;
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public boolean parseInput()
    {
        return false;
    }

    @Override
    public void setParams(Connection c, DataInputStream in, DataOutputStream out)
    {
        this.in = in;
        this.out = out;
        this.conn = c;
    }
}
