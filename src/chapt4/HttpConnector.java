//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package chapt4;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Stack;
import java.util.Vector;
import org.apache.catalina.Connector;
import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Logger;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Service;
import org.apache.catalina.connector.http.HttpProcessor;
import org.apache.catalina.connector.http.HttpRequestImpl;
import org.apache.catalina.connector.http.HttpResponseImpl;
import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

public final class HttpConnector implements Connector, Lifecycle, Runnable {
    private Service service = null;
    private int acceptCount = 10;
    private String address = null;
    private int bufferSize = 2048;
    protected Container container = null;
    private Vector created = new Vector();
    private int curProcessors = 0;
    private int debug = 0;
    private boolean enableLookups = false;
    private ServerSocketFactory factory = null;
    private static final String info = "org.apache.catalina.connector.http.HttpConnector/1.0";
    protected LifecycleSupport lifecycle = new LifecycleSupport(this);
    protected int minProcessors = 5;
    private int maxProcessors = 20;
    private int connectionTimeout = '\uea60';
    private int port = 8080;
    private Stack processors = new Stack();
    private String proxyName = null;
    private int proxyPort = 0;
    private int redirectPort = 443;
    private String scheme = "http";
    private boolean secure = false;
    private ServerSocket serverSocket = null;
    private StringManager sm = StringManager.getManager("org.apache.catalina.connector.http");
    private boolean initialized = false;
    private boolean started = false;
    private boolean stopped = false;
    private Thread thread = null;
    private String threadName = null;
    private Object threadSync = new Object();
    private boolean allowChunking = true;
    private boolean tcpNoDelay = true;

    public HttpConnector() {
    }

    public Service getService() {
        return this.service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getAcceptCount() {
        return this.acceptCount;
    }

    public void setAcceptCount(int count) {
        this.acceptCount = count;
    }

    public boolean isChunkingAllowed() {
        return this.allowChunking;
    }

    public void setAllowChunking(boolean allowChunking) {
        this.allowChunking = allowChunking;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAvailable() {
        return this.started;
    }

    public int getBufferSize() {
        return this.bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public Container getContainer() {
        return this.container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public int getCurProcessors() {
        return this.curProcessors;
    }

    public int getDebug() {
        return this.debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public boolean getEnableLookups() {
        return this.enableLookups;
    }

    public void setEnableLookups(boolean enableLookups) {
        this.enableLookups = enableLookups;
    }

    public ServerSocketFactory getFactory() {
        if(this.factory == null) {
            synchronized(this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }

        return this.factory;
    }

    public void setFactory(ServerSocketFactory factory) {
        this.factory = factory;
    }

    public String getInfo() {
        return "org.apache.catalina.connector.http.HttpConnector/1.0";
    }

    public int getMinProcessors() {
        return this.minProcessors;
    }

    public void setMinProcessors(int minProcessors) {
        this.minProcessors = minProcessors;
    }

    public int getMaxProcessors() {
        return this.maxProcessors;
    }

    public void setMaxProcessors(int maxProcessors) {
        this.maxProcessors = maxProcessors;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProxyName() {
        return this.proxyName;
    }

    public void setProxyName(String proxyName) {
        this.proxyName = proxyName;
    }

    public int getProxyPort() {
        return this.proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public int getRedirectPort() {
        return this.redirectPort;
    }

    public void setRedirectPort(int redirectPort) {
        this.redirectPort = redirectPort;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean getSecure() {
        return this.secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean getTcpNoDelay() {
        return this.tcpNoDelay;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public Request createRequest() {
        HttpRequestImpl request = new HttpRequestImpl();
        request.setConnector(this);
        return request;
    }

    public Response createResponse() {
        HttpResponseImpl response = new HttpResponseImpl();
        response.setConnector(this);
        return response;
    }

    void recycle(HttpProcessor processor) {
        this.processors.push(processor);
    }

    private HttpProcessor createProcessor() {
        Stack var1 = this.processors;
        synchronized(this.processors) {
            return this.processors.size() > 0?(HttpProcessor)this.processors.pop():(this.maxProcessors > 0 && this.curProcessors < this.maxProcessors?this.newProcessor():(this.maxProcessors < 0?this.newProcessor():null));
        }
    }

    private void log(String message) {
        Logger logger = this.container.getLogger();
        String localName = this.threadName;
        if(localName == null) {
            localName = "HttpConnector";
        }

        if(logger != null) {
            logger.log(localName + " " + message);
        } else {
            System.out.println(localName + " " + message);
        }

    }

    private void log(String message, Throwable throwable) {
        Logger logger = this.container.getLogger();
        String localName = this.threadName;
        if(localName == null) {
            localName = "HttpConnector";
        }

        if(logger != null) {
            logger.log(localName + " " + message, throwable);
        } else {
            System.out.println(localName + " " + message);
            throwable.printStackTrace(System.out);
        }

    }

    private HttpProcessor newProcessor() {
        HttpProcessor processor = new HttpProcessor(this, this.curProcessors++);
        if(processor instanceof Lifecycle) {
            try {
                processor.start();
            } catch (LifecycleException var3) {
                this.log("newProcessor", var3);
                return null;
            }
        }

        this.created.addElement(processor);
        return processor;
    }

    private ServerSocket open() throws IOException {
        ServerSocketFactory factory = this.getFactory();
        if(this.address == null) {
            this.log(this.sm.getString("httpConnector.allAddresses"));

            try {
                return factory.createSocket(this.port, this.acceptCount);
            } catch (BindException var5) {
                throw new BindException(var5.getMessage() + ":" + this.port);
            }
        } else {
            try {
                InetAddress e = InetAddress.getByName(this.address);
                this.log(this.sm.getString("httpConnector.anAddress", this.address));

                try {
                    return factory.createSocket(this.port, this.acceptCount, e);
                } catch (BindException var6) {
                    throw new BindException(var6.getMessage() + ":" + this.address + ":" + this.port);
                }
            } catch (Exception var7) {
                this.log(this.sm.getString("httpConnector.noAddress", this.address));

                try {
                    return factory.createSocket(this.port, this.acceptCount);
                } catch (BindException var4) {
                    throw new BindException(var4.getMessage() + ":" + this.port);
                }
            }
        }
    }

    public void run() {
        while(true) {
            Object socket;
            if(!this.stopped) {
                label72: {
                    socket = null;

                    Socket socket1;
                    try {
                        socket1 = this.serverSocket.accept();
                        if(this.connectionTimeout > 0) {
                            socket1.setSoTimeout(this.connectionTimeout);
                        }

                        socket1.setTcpNoDelay(this.tcpNoDelay);
                    } catch (AccessControlException var11) {
                        this.log("socket accept security exception", var11);
                        continue;
                    } catch (IOException var12) {
                        IOException e = var12;

                        try {
                            Object ex = this.threadSync;
                            synchronized(this.threadSync) {
                                if(this.started && !this.stopped) {
                                    this.log("accept: ", e);
                                }

                                if(!this.stopped) {
                                    this.serverSocket.close();
                                    this.serverSocket = this.open();
                                }
                                continue;
                            }
                        } catch (IOException var10) {
                            this.log("socket reopen: ", var10);
                            break label72;
                        }
                    }

                    HttpProcessor processor = this.createProcessor();
                    if(processor == null) {
                        try {
                            this.log(this.sm.getString("httpConnector.noProcessor"));
                            socket1.close();
                        } catch (IOException var9) {
                            ;
                        }
                        continue;
                    }

                    processor.assign(socket1);
                    continue;
                }
            }

            socket = this.threadSync;
            synchronized(this.threadSync) {
                this.threadSync.notifyAll();
                return;
            }
        }
    }

    private void threadStart() {
        this.log(this.sm.getString("httpConnector.starting"));
        this.thread = new Thread(this, this.threadName);
        this.thread.setDaemon(true);
        this.thread.start();
    }

    private void threadStop() {
        this.log(this.sm.getString("httpConnector.stopping"));
        this.stopped = true;

        try {
            this.threadSync.wait(5000L);
        } catch (InterruptedException var2) {
            ;
        }

        this.thread = null;
    }

    public void addLifecycleListener(LifecycleListener listener) {
        this.lifecycle.addLifecycleListener(listener);
    }

    public void removeLifecycleListener(LifecycleListener listener) {
        this.lifecycle.removeLifecycleListener(listener);
    }

    public void initialize() throws LifecycleException {
        if(this.initialized) {
            throw new LifecycleException(this.sm.getString("httpConnector.alreadyInitialized"));
        } else {
            this.initialized = true;

            try {
                this.serverSocket = this.open();
            } catch (IOException var2) {
                throw new LifecycleException(this.threadName + ".open", var2);
            }
        }
    }

    public void start() throws LifecycleException {
        if(this.started) {
            throw new LifecycleException(this.sm.getString("httpConnector.alreadyStarted"));
        } else {
            this.threadName = "HttpConnector[" + this.port + "]";
            this.lifecycle.fireLifecycleEvent("start", (Object)null);
            this.started = true;
            this.threadStart();

            while(this.curProcessors < this.minProcessors && (this.maxProcessors <= 0 || this.curProcessors < this.maxProcessors)) {
                HttpProcessor processor = this.newProcessor();
                this.recycle(processor);
            }

        }
    }

    public void stop() throws LifecycleException {
        if(!this.started) {
            throw new LifecycleException(this.sm.getString("httpConnector.notStarted"));
        } else {
            this.lifecycle.fireLifecycleEvent("stop", (Object)null);
            this.started = false;

            for(int i = this.created.size() - 1; i >= 0; --i) {
                HttpProcessor processor = (HttpProcessor)this.created.elementAt(i);
                if(processor instanceof Lifecycle) {
                    try {
                        processor.stop();
                    } catch (LifecycleException var6) {
                        this.log("HttpConnector.stop", var6);
                    }
                }
            }

            Object var8 = this.threadSync;
            synchronized(this.threadSync) {
                if(this.serverSocket != null) {
                    try {
                        this.serverSocket.close();
                    } catch (IOException var5) {
                        ;
                    }
                }

                this.threadStop();
            }

            this.serverSocket = null;
        }
    }
}
