package test;

import org.junit.Test;

import java.lang.reflect.Method;

/**
 * Created by wei.ma on 2016/12/6.
 */
public class TestReflection {
    @Test
    public void testReflectMethod() {
        try {
            Class cls = Class.forName("test.A");
            Method methlist[] = cls.getDeclaredMethods();
            for (int i = 0; i < methlist.length; i++) {
                Method m = methlist[i];
                System.out.println("name = " + m.getName());
                System.out.println("decl class = " + m.getDeclaringClass());
                Class pvec[] = m.getParameterTypes();
                for (int j = 0; j < pvec.length; j++)
                    System.out.println("param #" + j + " " + pvec[j]);
                Class evec[] = m.getExceptionTypes();
                for (int j = 0; j < evec.length; j++)
                    System.out.println("exc #" + j + " " + evec[j]);
                System.out.println("return type = " + m.getReturnType());
                System.out.println("-----");
            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }


    @Test
    public void testInvokeMethod(){
        try {
//            Class cls = Class.forName("test.A");
//            Class partypes[] = new Class[2];
//            partypes[0] = Integer.TYPE;
//            partypes[1] = Integer.TYPE;
//            Method meth = cls.getMethod("add", partypes);
////            Method2 methobj = new Method2();
//            Object arglist[] = new Object[2];
//            arglist[0] = new Integer(37);
//            arglist[1] = new Integer(47);
////            Object retobj = meth.invoke(methobj, arglist);
//            Integer retval = (Integer) retobj;
//            System.out.println(retval.intValue());
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
}
