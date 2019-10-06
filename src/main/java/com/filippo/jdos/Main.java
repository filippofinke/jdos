package com.filippo.jdos;

import java.io.IOException;


/**
 * @author Filippo Finke
 */
public class Main {
    
    public static void main(String[] args) throws IOException {
        Jdos jdos = new Jdos("IP_ADDRESS", 80, 250, 20);
        jdos.start();
    }
    
}
