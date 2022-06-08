package com.mahama.common.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class SslUtil {

    public static SSLSocketFactory getSocketFactory(final InputStream caCrtFile) throws NoSuchAlgorithmException,
            IOException, KeyStoreException, CertificateException, KeyManagementException {
        Security.addProvider(new BouncyCastleProvider());

        //===========加载 ca 证书==================================
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        if (null != caCrtFile) {
            // 加载本地指定的 ca 证书
            PemReader reader = new PemReader(new InputStreamReader(caCrtFile));
            byte[] content = reader.readPemObject().getContent();
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream in = new ByteArrayInputStream(content);
            X509Certificate caCert = (X509Certificate) factory.generateCertificate(in);
            reader.close();

            // CA certificate is used to authenticate server
            KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
            caKs.load(null, null);
            caKs.setCertificateEntry("ca-certificate", caCert);
            // 把ca作为信任的 ca 列表,来验证服务器证书
            tmf.init(caKs);
        } else {
            //使用系统默认的安全证书
            tmf.init((KeyStore) null);
        }

        // ============finally, create SSL socket factory==============
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

}
