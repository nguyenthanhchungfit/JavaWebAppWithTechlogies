/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servers;
import com.vng.zing.thriftserver.ThriftServers;
import org.apache.thrift.TMultiplexedProcessor;
import thrift_services.AlbumServices;
import thrift_services.AlbumServicesImpl;
import thrift_services.LyricServices;
import thrift_services.LyricServicesImpl;
import thrift_services.SingerServices;
import thrift_services.SingerServicesImpl;
import thrift_services.SongServices;
import thrift_services.SongServicesImpl;
/**
 *
 * @author cpu11165-local
 */
public class DataServer {
    public boolean setupAndStart(){
        
        ThriftServers servers = new ThriftServers("appmp3-data");
        
        TMultiplexedProcessor processors = new TMultiplexedProcessor();
        
        processors.registerProcessor("SongServices", new SongServices.Processor(
                new SongServicesImpl()));
        
        processors.registerProcessor("SingerServices", new SingerServices.Processor(
                new SingerServicesImpl()));
        
        processors.registerProcessor("AlbumServices", new AlbumServices.Processor(
                new AlbumServicesImpl()));
        
        processors.registerProcessor("LyricServices", new LyricServices.Processor(
                new LyricServicesImpl()));
        
        servers.setup(processors);
        
        return servers.start();
    }
}
