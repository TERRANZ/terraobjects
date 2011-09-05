/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terra.solution.ejb;

import javax.ejb.Remote;

/**
 *
 * @author terranz
 */
@Remote
public interface HashscanBeanRemote {

    void start();
    
}
