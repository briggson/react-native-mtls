package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.network.NetworkingModule;

import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

import java.util.Arrays;
import java.util.List;

public class MutualTLSModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public MutualTLSModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "MutualTLS";
    }

    @ReactMethod
    public void configure(ReadableMap readableMap) {
        String certificateFileP12 = readableMap.getString("certificateFileP12");
        String certificatePassword = readableMap.getString("certificatePassword");
        CustomClientFactory factory = new CustomClientFactory(certificateFileP12, certificatePassword);
        NetworkingModule.setCustomClientBuilder(builder -> {
          OkHttpClient customClient = factory.createNewNetworkModuleClient();
          builder.sslSocketFactory(customClient.sslSocketFactory(), (X509TrustManager) customClient.x509TrustManager());
          builder.cookieJar(customClient.cookieJar());
          builder.addInterceptor(customClient.interceptors().get(0));
          builder.connectionSpecs(createConnectionSpecs());
        });
    }

    private List<ConnectionSpec> createConnectionSpecs() {
        // Enable TLS v1.3 and v1.2
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2)
            .build();
        return Arrays.asList(spec, ConnectionSpec.COMPATIBLE_TLS);
    }
}
