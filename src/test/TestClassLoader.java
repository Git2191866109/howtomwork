package test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * Created by wei.ma on 2016/12/6.
 */
public class TestClassLoader {
    public static void main(String[] args) throws Exception {
        ClassLoaderSub classLoaderSub = new ClassLoaderSub();
        Class clazz = classLoaderSub.getClassByFile("C:\\Users\\wei.ma\\Desktop\\test.A.class");
        System.out.println("类的名字：" + clazz.getName());
        System.out.println("类的加载器：" + clazz.getClassLoader());
        classLoaderSub.loadClass(A.class.getName());
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod("doSome");
        method.invoke(obj, null);
    }
}

class ClassLoaderSub extends ClassLoader {
    public Class defineClassByName(String name, byte[] b, int off, int len) {
        //由于defineClass是protected，所以需要继承后来调用
        Class clazz = super.defineClass(name, b, off, len);
        return clazz;
    }

    public Class getClassByFile(String fileName) throws Exception {
        File classFile = new File(fileName);
        //一般的class文件通常都小于100k，如果现实情况超出这个范围可以放大长度
        byte bytes[] = new byte[102400];
        FileInputStream fis = null;
        Class clazz = null;
        try {
            fis = new FileInputStream(classFile);
            int j = 0;
            while (true) {
                int i = fis.read(bytes);
                if (i == -1)
                    break;
                j += i;
            }
            clazz = defineClassByName(null, bytes, 0, j);
        } finally {
            fis.close();
            return clazz;
        }
    }
}