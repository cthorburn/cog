package com.trabajo.vcl;

import java.util.Collections;
import java.util.List;

import com.trabajo.utils.Strings;

public class ResourceMagician<T extends CLMKey<T>> implements Magician<T> {

    private static final String ID = PathMagic.MAGIC + "/s6c4d8f6";
    private static final int MAGIC_PATH_POSITION = 3;

    private ClassLoaderManager<T, ?> clm;

    public ResourceMagician(final ClassLoaderManager<T, ?> clm) {
        this.clm = clm;
    }

    public final String id() {
        return ID;
    }

    public final String makeMagical(final String path) {
        final VCLThreadContext<T> pti = clm.getThreadContext();
        final T dv = pti.key();

        return makeMagical(dv, path);
    }

    public final String makeMagical(final T dv, final String path) {
        final StringBuilder sb = new StringBuilder();
        sb.append(id());
        sb.append('/');
        sb.append(dv.toString());
        if (!path.startsWith("/")) {
            sb.append('/');
        }
        sb.append(path);
        return sb.toString();
    }

    @Override
    public final String extractCLMKeyFactoryString(final String s) {
        return Strings.beforeFirst(s.substring(id().length() + 1), '/');
    }

    public final String muggle(final String s) {
        return Strings.itemAfterNthSlash(MAGIC_PATH_POSITION, s);
    }

    @Override
    public final boolean isMyKindOfMagic(final String s) {
        return s.startsWith(id());
    }

    @Override
    public final String makeMagical(final String[] classLoaderNames,
            final String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final List<String> getClassLoaderNames(final String path) {
        return Collections.emptyList();
    }
}
