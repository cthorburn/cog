package com.trabajo.vcl;

import java.util.List;

public interface Magician<T extends CLMKey<T>> {
    String id();

    String makeMagical(T dv, String s);

    String makeMagical(String[] classLoaderNames, String s);

    String makeMagical(String s);

    String extractCLMKeyFactoryString(String s);

    /**
     * @param s
     * @return true if the param is encoded using this magicians shenanigans
     */
    boolean isMyKindOfMagic(String s);

    /**
     * Extract the everyday reference from the magical version
     *
     * @param s
     * @return
     */
    String muggle(String magical);

    List<String> getClassLoaderNames(String path);
}
