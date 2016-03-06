package org.projw.blackserver.models;

public class BlackServer {
    private static final BlackServer INSTANCE = new BlackServer();

    static {
        INSTANCE.setStatus("OK");
    }

    private BlackServer(){}

    public static BlackServer getInstance() {
        return INSTANCE;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
