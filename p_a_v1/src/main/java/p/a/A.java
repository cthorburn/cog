package p.a;

import p.Common;

public class A extends Common {
    
    static   p.b.B instance=new p.b.B();

    public A() {
        super();
        System.out.println("made A Version 1!!!");
        System.out.println("A - made A Version with a static 1!!!");
    }
    
    public Object getRef() {
        return instance;
    }
}
