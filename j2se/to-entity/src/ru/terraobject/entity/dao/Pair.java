package ru.terraobject.entity.dao;

import java.io.Serializable;

/**
 *
 * @author terranz
 */
public class Pair<F, S> implements Serializable
{
    public F arg1;
    public S arg2;

    public Pair(F arg1, S arg2)
    {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }
}
  