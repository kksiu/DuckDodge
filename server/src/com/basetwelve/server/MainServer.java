package com.basetwelve.server;

import com.esotericsoftware.kryonet.Server;

/**
 * Created by Kenneth on 8/18/14.
 */
public class MainServer {

    public static void main(String[] args) {
        System.out.println("WERF");
        Server server = new Server();
        server.start();

        try {
            server.bind(54555, 54777);
        } catch(Exception e) {
            System.out.println(e.toString());
        }
        
    }
}
