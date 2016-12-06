package chapt2;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

/*用于处理 servlet 的 HTTP 请求*/
public class ServletProcessor1 {

    public void process(Request request, Response response) {
        /* URI 是以下形式的：/servlet/servletName*/
        String uri = request.getUri();
        /* servletName 是 servlet 类的名字,因为,加载 servlet 类，我们需要从 URI 中知道 servlet 的名称*/
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        URLClassLoader loader = null;

        try {
            //创建一个URLCLassLoader,需要创建一个类加载器并告诉这个类加载器要加载的类的位置
            //urls 指向了加载类时候查找的位置
            //任何以/结尾的 URL 都假设是一个目录
            URL[] urls = new URL[1];
            URLStreamHandler streamHandler = null;
            File classPath = new File(Constants.WEB_ROOT);
            // the forming of repository is taken from the createClassLoader method in
            // org.apache.catalina.startup. ClassLoaderFactory
            /*在一个 servlet 容器里边，一个类加载器可以找到 servlet 的地方被称为资源库(repository）*/
            /*classPath.getCanonicalPath()返回的是规范化的绝对路径*/
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            // the code for forming the URL is taken from the addRepository method in
            // org.apache.catalina.loader.StandardClassLoader class.
            urls[0] = new URL(null, repository, streamHandler);
            loader = new URLClassLoader(urls);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
        Class myClass = null;
        try {
            /*这里加载的是：E:\mojiworkspace\howtomwork\webroot\路劲下的class文件*/
            myClass = loader.loadClass(servletName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }

        Servlet servlet = null;

        try {
            servlet = (Servlet) myClass.newInstance();
            servlet.service((ServletRequest) request, (ServletResponse) response);
        } catch (Exception e) {
            System.out.println(e.toString());
        } catch (Throwable e) {
            System.out.println(e.toString());
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        File classPath = new File(Constants.WEB_ROOT);
        File classPath = new File(".\\Main ");
        String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
        System.out.println(repository);
        URL[] urls = new URL[1];
        URLStreamHandler streamHandler = null;
        urls[0] = new URL(null, repository, streamHandler);
        System.out.println(urls[0]);
//        classPath =
        System.out.println("返回的是定义时的路径,取决于定义时用的是相对路径还是绝对路径:" + classPath.getPath());
        System.out.println("返回的是定义时的路径对应的相对路径，但不会处理“.”和“..”的情况:" + classPath.getAbsolutePath());
        System.out.println("返回的是规范化的绝对路径，相当于将getAbsolutePath()中的“.”和“..”解析成对应的正确的路径:" + classPath.getCanonicalPath());
    }
}