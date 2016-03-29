package sos.net.sosftp2;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import sos.net.SOSSSLSocketFactory;

/** @author ghassan beydoun */
public class SOSFTPS extends SOSFTP {

    protected static final String DEFAULT_SSL_TLS_PROTOCOL = "TLS";
    private String securityProtocol = DEFAULT_SSL_TLS_PROTOCOL;
    private int ftpPort = 990;
    private String proxyHost;
    private int proxyPort;

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public SOSFTPS(String ftpHost, int ftpPort, String proxyHost, int proxyPort) throws SocketException, IOException, UnknownHostException {
        this.setProxyHost(proxyHost);
        this.setProxyPort(proxyPort);
        this.connect(ftpHost, ftpPort);
    }

    public SOSFTPS(String ftpHost, int ftpPort) throws SocketException, IOException, UnknownHostException {
        this.initProxy();
        this.connect(ftpHost, ftpPort);
    }

    public SOSFTPS(String ftpHost) throws Exception {
        if (!isConnected()) {
            this.connect(ftpHost, this.getPort());
        }
    }

    public void connect(String ftpHost) throws SocketException, IOException {
        if (!isConnected()) {
            this.connect(ftpHost, this.getPort());
        }
    }

    public void connect(String ftpHost, int ftpPort) throws SocketException, IOException {
        initProxy();
        if (!isConnected()) {
            this.setSocketFactory(new SOSSSLSocketFactory(getProxyHost(), getProxyPort(), getSecurityProtocol()));
            try {
                super.connect(ftpHost, ftpPort);
            } catch (NullPointerException e) {
                throw new SocketException("Connect failed! Probably HTTP proxy in use or the entered ftps port is invalid: " + e.toString());
            } catch (Exception e) {
                throw new SocketException("Connect failed!, reason: " + e.toString());
            }
            this.sendCommand("PBSZ 0");
            this.sendCommand("PROT P");
            this.enterLocalPassiveMode();
        }
    }

    public int getPort() {
        return ftpPort;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String httpProxyHost) {
        this.proxyHost = httpProxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int httpProxyPort) {
        this.proxyPort = httpProxyPort;
    }

    private void initProxy() {
        if (System.getProperty("proxyHost") != null && !System.getProperty("proxyHost").isEmpty()) {
            this.setProxyHost(System.getProperty("proxyHost"));
        }
        if (System.getProperty("proxyPort") != null && !System.getProperty("proxyPort").isEmpty()) {
            try {
                this.setProxyPort(Integer.parseInt(System.getProperty("proxyPort")));
            } catch (Exception ex) {
                throw new NumberFormatException("Non-numeric value is set [proxyPort]: " + System.getProperty("proxyPort"));
            }
        }
        if (System.getProperty("http.proxyHost") != null && !System.getProperty("http.proxyHost").isEmpty()) {
            this.setProxyHost(System.getProperty("http.proxyHost"));
        }
        if (System.getProperty("http.proxyPort") != null && !System.getProperty("http.proxyPort").isEmpty()) {
            try {
                this.setProxyPort(Integer.parseInt(System.getProperty("http.proxyPort")));
            } catch (Exception ex) {
                throw new NumberFormatException("Non-numeric value is set [http.proxyPort]: " + System.getProperty("http.proxyPort"));
            }
        }
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public static void main(String[] args) throws Exception {
        SOSFTPS sosftp = null;
        try {
            sosftp = new SOSFTPS("WILMA", 21);
            sosftp.login("test", "12345");
            long s1 = System.currentTimeMillis();
            Vector va = sosftp.nList("/home/test/temp");
            long e1 = System.currentTimeMillis();
            long r1 = e1 - s1;
        } catch (Exception e) {
            System.err.println(e.toString());
        } finally {
            sosftp.disconnect();
        }
    }

}