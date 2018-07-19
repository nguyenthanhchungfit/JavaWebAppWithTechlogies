/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checklog4japi2x;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
/**
 *
 * @author cpu11165-local
 */
public class Bar {
    static final Logger logger = LogManager.getLogger(Bar.class.getName());
    public boolean doIt(){
        logger.traceEntry();
        logger.error("Did it again!");
        return logger.traceExit(false);
    }
    
}
