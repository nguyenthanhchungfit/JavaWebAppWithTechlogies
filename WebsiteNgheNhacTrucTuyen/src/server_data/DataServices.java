/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import org.apache.thrift.transport.TTransportException;
import servers.DataServer;


/**
 *
 * @author Nguyen Thanh Chung
 */
public class DataServices {

    private static final int port = 8001;

    public static void main(String[] args) throws TTransportException {
        DataServer dataServer = new DataServer();
        if (!dataServer.setupAndStart()) {
            System.err.println("Could not start thrift servers! Exit now.");
            System.exit(1);
        } else {
            System.out.println("@@@ Data Server started!!!");
        }
    }

}
