/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servers;
import com.vng.zing.thriftserver.ThriftServers;
import org.apache.thrift.TMultiplexedProcessor;
import thrift_services.UserServices;
import thrift_services.UserServicesImpl;
/**
 *
 * @author cpu11165-local
 */
public class UserServer {
    public boolean setupAndStart(){
        ThriftServers servers = new ThriftServers("appmp3-user");
        TMultiplexedProcessor processors = new TMultiplexedProcessor();
        
        processors.registerProcessor("UserServices", new UserServices.Processor<>(
                new UserServicesImpl()));
        
        servers.setup(processors);
        return servers.start();
    }
}
