package com.trabajo.vcl;

public final class PathMagic<T extends CLMKey<T>> {

    public static final String MAGIC = "/rsc4q5gh";

    private final ResourceMagician<T> resourceMagician;
    private final RepositoryMagician<T> repositoryMagician;

    public PathMagic(ClassLoaderManager<T, ?> clm) {
        resourceMagician = new ResourceMagician<T>(clm);
        repositoryMagician = new RepositoryMagician<T>();
    }

    public Magician<T> magicForPath(String path) {
    	
    		if (resourceMagician.isMyKindOfMagic(path)) {
            return resourceMagician;
        } else if (repositoryMagician.isMyKindOfMagic(path)) {
            return repositoryMagician;
        }
        return null;
    }

		public Magician<T> getResourceMagician() {
			return resourceMagician;
		}

}
