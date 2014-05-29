package ru.terraobjects.tests;

import javax.persistence.Id;
import java.util.Date;

/**
 * Date: 29.05.14
 * Time: 18:03
 */
public class TestObject {

    @Id
    private Integer id;
    private String name;
    private Date creationDate;
    private Long sum;
    private Double size;

    public TestObject() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
