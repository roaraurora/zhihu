package com.zhihu.demo.event;

        import org.springframework.context.ApplicationEvent;

public class MyApplicationEvent extends ApplicationEvent {

    private String username;

    public MyApplicationEvent(Object source) {
        super(source);
    }

    public MyApplicationEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
