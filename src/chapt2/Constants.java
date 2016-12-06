package chapt2;

import java.io.File;

public class Constants {
    /*这个容器可以提供的静态资源的位置*/
    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public static void main(String[] args) {
        System.out.println(WEB_ROOT);
    }
}