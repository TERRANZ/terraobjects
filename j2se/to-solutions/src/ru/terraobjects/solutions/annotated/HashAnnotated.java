package ru.terraobjects.solutions.annotated;

import ru.terraobjects.entity.annotations.Property;
import ru.terraobjects.entity.annotations.Template;

/**
 *
 * @author terranz
 */
@Template(id = "2")
public class HashAnnotated
{

    private String fileName;
    private String hash;

    public String getFileName()
    {
	return fileName;
    }

    @Property(id = "1")
    public void setFileName(String fileName)
    {
	this.fileName = fileName;
    }

    public String getHash()
    {
	return hash;
    }

    @Property(id = "3")
    public void setHash(String hash)
    {
	this.hash = hash;
    }
}
