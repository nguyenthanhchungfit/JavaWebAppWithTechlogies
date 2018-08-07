/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data_center;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import user_data.CommunicatedDataCenter;

/**
 *
 * @author cpu11165-local
 */
public class DataService {
    
    private static final int port = 8001;
    
    public static ServiceHandlers handlers;
    
    public static CommunicatedDataCenter.Processor processor;
    
    public static void main(String[] args){
        try{
            handlers = new ServiceHandlers();
            processor = new CommunicatedDataCenter.Processor(handlers);
            
            Runnable simple = new Runnable(){
                public void run(){
                    simple(processor);
                }
            };               
            new Thread(simple).start();        
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public static void simple(CommunicatedDataCenter.Processor processor){
        try{
            TServerTransport serverTransport = new TServerSocket(port);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
            System.out.println("Starting the server ....");
            server.serve();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
