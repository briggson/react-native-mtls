package com.reactlibrary;

import expo.modules.core.ModuleRegistry;
import expo.modules.core.ExpoModule;
import expo.modules.core.Promise;
import expo.modules.core.ExpoMethod;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.network.NetworkingModule;

import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;

import java.util.Arrays;
import java.util.List;

public class MutualTLSModule extends ExpoModule {

    public MutualTLSModule() {
        super();
    }

    @Override
    public String getName() {
        return "MutualTLS";
    }

    @ExpoMethod
    public void configure(ReadableMap readableMap, Promise promise) {
        try {
            String certificateFileP12 = readableMap.getString("certificateFileP12");
            String certificatePassword = readableMap.getString("certificatePassword");
            CustomClientFactory factory = new CustomClientFactory(certificateFileP12, certificatePassword);

            // Get the NetworkingModule from the module registry
            ModuleRegistry registry = getModuleRegistry();
            NetworkingModule networkingModule = (NetworkingModule) registry.getModule(NetworkingModule.class);

            if (networkingModule != null) {
                networkingModule.setCustomClientBuilder(builder -> {
                    OkHttpClient customClient = factory.createNewNetworkModuleClient();
                    builder.sslSocketFactory(customClient.sslSocketFactory(), (X509TrustManager) customClient.x509TrustManager());
                    builder.cookieJar(customClient.cookieJar());
                    builder.addInterceptor(customClient.interceptors().get(0));
                    builder.connectionSpecs(createConnectionSpecs());
                });
            }

            promise.resolve(null);
        } catch (Exception e) {
            promise.reject("CONFIGURE_ERROR", e.getMessage(), e);
        }
    }

    private List<ConnectionSpec> createConnectionSpecs() {
        // Enable TLS v1.3 and v1.2
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2)
            .build();
        return Arrays.asList(spec, ConnectionSpec.COMPATIBLE_TLS);
    }
}
