package ru.terraobjects.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Date: 02.06.14
 * Time: 12:45
 */
@XmlRootElement
public class Pair<K, V> {
    public K key;
    public V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
