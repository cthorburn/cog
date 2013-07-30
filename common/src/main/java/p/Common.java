package p;

import java.lang.reflect.InvocationTargetException;

public abstract class Common {

    public Common() {
        ClassLoader cl=getClass().getClassLoader();
        ClassLoader bcl=getRef().getClass().getClassLoader();
        try {
            System.out.println("class "+getClass().getName()+":"+cl.getClass().getMethod("getLogicalName").invoke(cl));
            System.out.println("reference "+getRef().getClass().getName()+":"+bcl.getClass().getMethod("getLogicalName").invoke(bcl));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public abstract Object getRef();
}
