package ru.terraobjects.solutions.impl.hashscan;

import ru.terraobjects.entity.annotations.*;

/**
 *
 * @author terranz
 */
@TemplateId(id = "2")
public class HashObject
{

    private final static String PROP_NAME = "1";
    private final static String PROP_HASH = "3";
    private String fileName;
    private String hash;

    @PropGetter(id = PROP_NAME)
    public String getFileName()
    {
        return fileName;
    }

    @PropSetter(id = PROP_NAME)
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @PropGetter(id = PROP_HASH)
    public String getHash()
    {
        return hash;
    }

    @PropSetter(id = PROP_HASH)
    public void setHash(String hash)
    {
        this.hash = hash;
    }
}
