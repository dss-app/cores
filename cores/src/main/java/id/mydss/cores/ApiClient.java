package id.mydss.cores;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    /* public static Retrofit getClient() {
           HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
           interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
           OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

           retrofit = new Retrofit.Builder()
                   .baseUrl("https://dev-api.dismart.id")
                   .addConverterFactory(GsonConverterFactory.create())
                   .client(client)
                   .build();
           return retrofit;
       }
   */
    public static Retrofit getRetrofitInstance(final String sKey, Context context) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        //  httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                // .header("Authorization", "Bearer " + sKey)
                // .header("Content-Type", "application/json; charset=utf-8");
                //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                //interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                Log.wtf("RESPON_TIME", "" + (response.receivedResponseAtMillis() - response.sentRequestAtMillis()) + " ms");

                /* ADD INTERCEPTOR */
/*
                // if(response.code() >= 400 && response.code() < 500){
                //SEND BROADCAST TO SERVICE
                Intent checkBroadCastState = new Intent();
                checkBroadCastState.setAction("id.dismart.app.HEADER_RESPONSE");
                checkBroadCastState.putExtra("code", response.code());
                checkBroadCastState.putExtra("url", "" + original.url());
                checkBroadCastState.putExtra("rsp_tm", "" + (response.receivedResponseAtMillis() - response.sentRequestAtMillis()) + " ms");


                context.sendBroadcast(checkBroadCastState);
*/
                // }
                return response;
            }
        });


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //  OkHttpClient client = httpClient.addInterceptor(interceptor).build();
        OkHttpClient client = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
                httpClient.sslSocketFactory(sslSocketFactory, trustManager);
                httpClient.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        // HostnameVerifier hv = OkHostnameVerifier.INSTANCE;
                        // Log.e("HTTPS_OKHTTP", "" + hv.verify("sample.com", session));
                        return true;
                    }
                });

                client = httpClient
                        .addNetworkInterceptor(interceptor)
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .build();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
                httpClient.sslSocketFactory(sslSocketFactory, trustManager);
                httpClient.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        // HostnameVerifier hv = OkHostnameVerifier.INSTANCE;
                        // Log.e("HTTPS_OKHTTP", "" + hv.verify("sample.com", session));
                        return true;
                    }
                });

                client = httpClient
                        .addNetworkInterceptor(interceptor)
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .build();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            // client = httpClient.sslSocketFactory(getSSLConfig(context).getSocketFactory()).build();

        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {

            X509TrustManager trustManager;
            SSLSocketFactory sslSocketFactory;
            try {
                trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                sslSocketFactory = sslContext.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
/*
            try {

                client = httpClient.sslSocketFactory(getSSLConfig(context).getSocketFactory()).build();
            } catch (CertificateException e) {
            } catch (IOException e) {
            } catch (KeyStoreException e) {
            } catch (NoSuchAlgorithmException e) {
            } catch (KeyManagementException e) {
            } catch (java.security.cert.CertificateException e) {
                e.printStackTrace();
            }*/
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://dev-api.dismart.id")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;

    }

    public static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }

    //Clear Retrofit
    public static Retrofit clearRetrofitInstance() {
        retrofit = null;
        return retrofit;
    }


    /**
     * Returns an input stream containing one or more certificate PEM files. This implementation just
     * embeds the PEM files in Java strings; most applications will instead read this from a resource
     * file that gets bundled with the application.
     */
    private static InputStream trustedCertificatesInputStream() {
        // PEM files for root certificates of Comodo and Entrust. These two CAs are sufficient to view
        // https://publicobject.com (Comodo) and https://squareup.com (Entrust). But they aren't
        // sufficient to connect to most HTTPS sites including https://godaddy.com and https://visa.com.
        // Typically developers will need to get a PEM file from their organization's TLS administrator.

        String entrustRootCertificateAuthority = "" +
                "-----BEGIN CERTIFICATE-----\n" +
                "MIIFsTCCBJmgAwIBAgIQAyOnDWIibB9Hsi3TaiQpmzANBgkqhkiG9w0BAQsFADBe\n" +
                "MQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3\n" +
                "d3cuZGlnaWNlcnQuY29tMR0wGwYDVQQDExRSYXBpZFNTTCBSU0EgQ0EgMjAxODAe\n" +
                "Fw0yMDAxMDQwMDAwMDBaFw0yMTAxMDMxMjAwMDBaMBcxFTATBgNVBAMMDCouZGlz\n" +
                "bWFydC5pZDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBALIm6jny4ocn\n" +
                "bIQZhypwNAb95wqXqKtAceBnm+db/42N4Nvr+yy/SoFUJsO6GlrTCW3JdgBfuDER\n" +
                "NZ6iRO0+TOBLvP8nm3b8Gw0Mj95sDr9JRwAB7fvAyoJAerIf4shrXtVX2ZhvAu7p\n" +
                "dexBYe508JudLgX2wL6cGHocKzbE7eznU8fdz5SwKPS9yngp5Me+sE+HipnKnH+b\n" +
                "KJD/B5/GlRiuFql45ELhDntD/tzKGaGI/weaxqusIUZVMzaRXXEWK3nwxsvPnZIq\n" +
                "bjmBsNOjjj25GrwnK+Hpm4MGbqE3iCszdBsYNh43+ASVvi7dwxyISWSAuHEBFWIE\n" +
                "VOhd2na9oN0CAwEAAaOCArAwggKsMB8GA1UdIwQYMBaAFFPKF1n8a8ADIS8aruSq\n" +
                "qByCVtp1MB0GA1UdDgQWBBQCy90LBUUawYhU6Z2Pmb3vaCKNZTAjBgNVHREEHDAa\n" +
                "ggwqLmRpc21hcnQuaWSCCmRpc21hcnQuaWQwDgYDVR0PAQH/BAQDAgWgMB0GA1Ud\n" +
                "JQQWMBQGCCsGAQUFBwMBBggrBgEFBQcDAjA+BgNVHR8ENzA1MDOgMaAvhi1odHRw\n" +
                "Oi8vY2RwLnJhcGlkc3NsLmNvbS9SYXBpZFNTTFJTQUNBMjAxOC5jcmwwTAYDVR0g\n" +
                "BEUwQzA3BglghkgBhv1sAQIwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGln\n" +
                "aWNlcnQuY29tL0NQUzAIBgZngQwBAgEwdQYIKwYBBQUHAQEEaTBnMCYGCCsGAQUF\n" +
                "BzABhhpodHRwOi8vc3RhdHVzLnJhcGlkc3NsLmNvbTA9BggrBgEFBQcwAoYxaHR0\n" +
                "cDovL2NhY2VydHMucmFwaWRzc2wuY29tL1JhcGlkU1NMUlNBQ0EyMDE4LmNydDAJ\n" +
                "BgNVHRMEAjAAMIIBBAYKKwYBBAHWeQIEAgSB9QSB8gDwAHcApLkJkLQYWBSHuxOi\n" +
                "zGdwCjw1mAT5G9+443fNDsgN3BAAAAFvcN5VagAABAMASDBGAiEAsOV+8B/1UNpv\n" +
                "IJk5OdY0KUskLF/Xr3NzklcsfiehBUMCIQD1zaqXNV8HQbkJoyrQ9XdERjWJGnq8\n" +
                "Id0WeVbnO3qmdQB1AESUZS6w7s6vxEAH2Kj+KMDa5oK+2MsxtT/TM5a1toGoAAAB\n" +
                "b3DeVSIAAAQDAEYwRAIgRM5UbpBNKDUkma9qnSjRq2jNXY9lq2zvS4QPkPTz6SIC\n" +
                "ICiFao3KLPaP4SSZfaz+CSJ0RCY9lA4hSZEmYJCgARWNMA0GCSqGSIb3DQEBCwUA\n" +
                "A4IBAQAH4kOGB39lHgAinhCNQXsRBMwUhaXB/CL+BKqceuo4kVZVKOOee9imKdIs\n" +
                "hBTWuI9gohnAUi+B+bNGCkUcmC7Sym5yTsaU74mwBl1juysxJesDVVYMBeEHsk5N\n" +
                "LvaiBWoI6NOjsFaJoEDcOVxZBF3UCwDhEcGrP2nNpSvaaB+XSrs2bbHb0b+07TzT\n" +
                "HpoiCa26aSh41K+3gsmcPZVKcXeE3GOGBlMZ43rn8TOddr6a9hNooYsnyz+1Fh0M\n" +
                "462PoIOtu4EZ0AwIlQAevN4aafgcEbm06kVmQ+y+bahMgEjrLBXQ21M3B6/hKvW3\n" +
                "yLckNY5q52z+Gc3tQSX4/YVL7XX5\n" +
                "-----END CERTIFICATE-----";


        return new Buffer().writeUtf8(entrustRootCertificateAuthority).inputStream();
    }

    /**
     * Returns a trust manager that trusts {@code certificates} and none other. HTTPS services whose
     * certificates have not been signed by these certificates will fail with a {@code
     * SSLHandshakeException}.
     *
     * <p>This can be used to replace the host platform's built-in trusted certificates with a custom
     * set. This is useful in development where certificate authority-trusted certificates aren't
     * available. Or in production, to avoid reliance on third-party certificate authorities.
     *
     * <p>See also {@link CertificatePinner}, which can limit trusted certificates while still using
     * the host platform's built-in trust store.
     *
     * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
     *
     * <p>Relying on your own trusted certificates limits your server team's ability to update their
     * TLS certificates. By installing a specific set of trusted certificates, you take on additional
     * operational complexity and limit your ability to migrate between certificate authorities. Do
     * not use custom trusted certificates in production without the blessing of your server's TLS
     * administrator.
     */
    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        // Put the certificates a key store.
        char[] password = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        // Use it to build an X509 trust manager.
        KeyManagerFactory keyManagerFactory =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException(
                    "Unexpected default trust managers:" + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream in = null; // By convention, 'null' creates an empty key store.
            keyStore.load(in, password);
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }


    @Deprecated
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException, java.security.cert.CertificateException {


        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca;
        // I'm using Java7. If you used Java6 close it manually with finally.
        try (InputStream cert = context.getResources().openRawResource(R.raw.dismart_id)) {
            ca = cf.generateCertificate(cert);
        }

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }
}
