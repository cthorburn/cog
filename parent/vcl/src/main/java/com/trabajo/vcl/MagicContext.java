package com.trabajo.vcl;

public final class MagicContext<T extends CLMKey<T>> {
    private T dv;
    private String[] classLoaderNames;
    private Magician<T> magician;

    public MagicContext(T dv, Magician<T> magician) {
        super();
        this.dv = dv;
        this.magician = magician;
    }

    public MagicContext(String[] classLoaderNames, Magician<T> magician) {
        super();
        this.classLoaderNames = classLoaderNames;
        this.magician = magician;
    }

    public T getDv() {
        return dv;
    }

    public Magician<T> getMagic() {
        return magician;
    }

    public String makeMagical(String resource) {
        return dv == null ? magician.makeMagical(classLoaderNames, resource) : magician.makeMagical(dv, resource);
    }
}
