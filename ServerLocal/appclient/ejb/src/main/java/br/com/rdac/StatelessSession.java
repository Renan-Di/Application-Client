package br.com.rdac;

import javax.ejb.Remote;

@Remote
public interface StatelessSession {
    void invokeWithClientContext();
    String getGreeting();
}