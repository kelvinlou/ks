package com.infi.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class App
{
    private final static Logger LOG = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        LOG.info("#####start#####");
        SpringApplication.run(App.class, args);
        LOG.info("#####end#####");
    }
}
