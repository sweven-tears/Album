package com.sweven.company;

public class EchoServiceImpl implements EchoService {
    public String echo(String ping) {
        return (ping != null) ? (ping + " --> I am ok.") : " I am ok.";
    }
}


/* Location:              C:\Users\Administrator.SC-201911131242\Desktop\simplerpc.jar!\com\company\EchoServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */