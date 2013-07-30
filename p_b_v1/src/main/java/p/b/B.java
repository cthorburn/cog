package p.b;

import p.Common;

public class B extends Common {
    
    public B() {
        super();
        
        System.out.println("good grief a B version 1!");
    }
    
    public Object getRef() {
        return new p.c.C();
    }
}
