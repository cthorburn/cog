package com.trabajo.vcl;

import java.util.ArrayList;
import java.util.List;

public final class RepositoryMagician<T extends CLMKey<T>> implements Magician<T> {

    private static final String ID = PathMagic.MAGIC + "/a3cxxnfy";
    public static final String CLASS_LOADER_NAME_DELIM_REGEX = "\\+";
    public static final String CLASS_LOADER_NAME_DELIM = "+";
    private static final int MUGGLE_POSITION = 4;
    private static final int THREE = 3; //TODO  WTF?

    public RepositoryMagician() {

    }

    public String id() {
        return ID;
    }

    public String makeMagical(T dv, String path) {
        return ID + "/" + path;
    }

    public String makeMagical(String path) {
        return ID + "/" + path;
    }

    @Override
    public String extractCLMKeyFactoryString(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String muggle(String s) {
        return s.split("/")[MUGGLE_POSITION];
    }

    @Override
    public boolean isMyKindOfMagic(String s) {
        return s.startsWith(id());
    }

    @Override
    public String makeMagical(String[] classLoaderNames, String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(id());
        sb.append('/');
        for (String cln : classLoaderNames) {
            sb.append(cln);
            sb.append(CLASS_LOADER_NAME_DELIM);
        }
        sb.setLength(sb.length() - 1);
        if (!s.startsWith("/")) {
            sb.append('/');
        }
        sb.append(s);

        return sb.toString();
    }

    @Override
    public List<String> getClassLoaderNames(String path) {
        final List<String> result = new ArrayList<String>();
        final String[] cls = path.split("/")[THREE].split(CLASS_LOADER_NAME_DELIM_REGEX);

        for (String s : cls) {
            result.add(s);
        }

        return result;
    }
}
