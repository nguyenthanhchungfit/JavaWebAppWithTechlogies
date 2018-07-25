/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_data;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import thrift_services.AlbumServices;
import thrift_services.AlbumServicesImpl;
import thrift_services.LyricServices;
import thrift_services.LyricServicesImpl;
import thrift_services.SingerServices;
import thrift_services.SingerServicesImpl;
import thrift_services.SongServices;
import thrift_services.SongServicesImpl;
import thrift_services.UserServices;
import thrift_services.UserServicesImpl;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class DataServices {
    
    private static final int port = 8001;
    
    public static void main(String[] args) throws TTransportException{
        System.out.println("Data Server is Running");
        
        TMultiplexedProcessor processors = new TMultiplexedProcessor();
        
        processors.registerProcessor("SongServices", new SongServices.Processor(
                new SongServicesImpl()));
        
        processors.registerProcessor("SingerServices", new SingerServices.Processor(
                new SingerServicesImpl()));
        
        processors.registerProcessor("AlbumServices", new AlbumServices.Processor(
                new AlbumServicesImpl()));
        
        processors.registerProcessor("LyricServices", new LyricServices.Processor(
                new LyricServicesImpl()));
        
        TServerTransport serverTransport = new TServerSocket(port);
        TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processors));
        
        server.serve();
    }
    
    
}
