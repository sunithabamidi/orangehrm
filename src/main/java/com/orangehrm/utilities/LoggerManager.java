package com.orangehrm.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerManager {

    //return logger instance of provided class
    public static Logger getLogger(Class<?> cls){
       return LogManager.getLogger(cls);
    }
}
