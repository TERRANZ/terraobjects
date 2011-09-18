package ru.terra.solution.ejb;

import javax.ejb.Stateless;
import ru.terra.solution.hashscan.Hashscan;

/**
 *
 * @author terranz
 */
@Stateless
public class HashscanBean implements HashscanBeanRemote {

    @Override
    public void start()
    {
	Hashscan.getInstance().startListen();
    }  
}
