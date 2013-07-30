package p.a;

import p.Common;

public class A extends Common {
    
    public A() {
        super();
        System.out.println("made A Version 2!!!");
    }
    
    public Object getRef() {
        return new p.b.B();
    }
}
