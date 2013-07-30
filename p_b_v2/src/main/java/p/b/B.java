package p.b;

import p.Common;

public class B extends Common {
    
    public B() {
        super();
        System.out.println("made B Version 2!!!");

    }
    
    public Object getRef() {
        return new p.c.C();
    }
}
