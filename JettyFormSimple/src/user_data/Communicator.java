/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user_data;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 *
 * @author cpu11165-local
 */
public class Communicator {
    
    static TTransport transport;
    static TProtocol protocol;
    static CommunicatedDataCenter.Client client;
    

    public static void init(String host, int port){
        transport= new TSocket(host, port);
        protocol = new TBinaryProtocol(transport); 
        client = new CommunicatedDataCenter.Client(protocol);     
    }
    
    public static void open() throws TException{
        if(!transport.isOpen()){
            transport.open();
        }   
    }
    
    public static CommunicatedDataCenter.Client getClient(){
        return client;
    }
    
    public static void close(){
        transport.close();
    }
}
