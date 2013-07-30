package com.colin.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class Serv1Bean
 */
@Stateless
@LocalBean
public class Serv1Bean {

    /**
     * Default constructor. 
     */
    public Serv1Bean() {
        // TODO Auto-generated constructor stub
    }

    
    public String hello() {
        return "hello";
    }
}
