package com.zhihu.demo.vo;

/**
 * test RabbitMQ model
 */
public class Book implements java.io.Serializable{

    private static final long serialVersionUID = -2164058270260403154L;

    private String id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
