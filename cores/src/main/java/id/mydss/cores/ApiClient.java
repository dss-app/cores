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
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.CertificateException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
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
/*
        OkHttpClient client = null;
        try {
            client = httpClient.sslSocketFactory(getSSLConfig(context).getSocketFactory()).build();
        } catch (CertificateException e) {
        } catch (IOException e) {
        } catch (KeyStoreException e) {
        } catch (NoSuchAlgorithmException e) {
        } catch (KeyManagementException e) {
        }*/

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //  OkHttpClient client = httpClient.addInterceptor(interceptor).build();
        OkHttpClient client = null;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
             client = httpClient.addNetworkInterceptor(interceptor).build();
        }else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP){
            try {
                client = httpClient.sslSocketFactory(getSSLConfig(context).getSocketFactory()).build();
            } catch (CertificateException e) {
            } catch (IOException e) {
            } catch (KeyStoreException e) {
            } catch (NoSuchAlgorithmException e) {
            } catch (KeyManagementException e) {
            } catch (java.security.cert.CertificateException e) {
                e.printStackTrace();
            }
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
