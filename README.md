# react-native-mtls

Mutual TLS authentication for HTTP requests in React Native and Expo.

The client certificate and associated password are stored securely in the native Keychain (iOS) or provided directly (Android).

Once the module is set up, it applies to all normal HTTP requests (e.g. through `fetch`, `XMLHttpRequest`, or any library that uses these) for HTTPS connections that ask for a client certificate. There is no overhead for connections that do not request a client certificate.

Supports both iOS and Android, and is compatible with Expo SDK 49+.

## Installation

### For Expo projects (SDK 49+)

```sh
npx expo install react-native-mtls
```

### For React Native CLI projects

```sh
npm install react-native-mtls
# or
yarn add react-native-mtls
```

For iOS, you'll also need a Keychain management solution:

```sh
npm install react-native-keychain
# or
yarn add react-native-keychain
```

Then run pod install:

```sh
npx pod-install
```

## Prerequisites

In order to use this module, you'll need a client certificate encoded as a `p12` file, encrypted with a password.

The [example project in this repository](./example) uses [this test certificate from `badssl.com`](https://badssl.com/download/), but you'll need to provide your own.

The certificate and password are expected to be loaded into the native Keychain at runtime, because it's considered bad practice to hard-code them or embed them as static resources in your app bundle. You'll need to expect the user to supply these, or download them at runtime from some secure source.

## Usage

Import the `MutualTLS` module:

```javascript
import MutualTLS from 'react-native-mtls';
```

### iOS Setup

For iOS, you'll need to store the certificate and password in the Keychain. Import the Keychain module:

```javascript
import Keychain from 'react-native-keychain';
```

Before making a request, you'll need to load the secrets into the Keychain.

Refer to [the documentation for the `Keychain` module](https://github.com/oblador/react-native-keychain) for more information about managing secrets in the keychain.

```javascript
const myP12DataBase64 = "YOUR P12 FILE ENCODED AS BASE64 GOES HERE";
const myPassword = "THE PASSWORD TO DECRYPT THE P12 FILE GOES HERE";

await Promise.all([
  Keychain.setGenericPassword('', myP12DataBase64, { service: "my-tls.client.p12" }),
  Keychain.setGenericPassword('', myPassword, { service: "my-tls.client.p12.password" }),
]);
```

Next configure the MutualTLS module to tell it where to find the secrets in the keychain:

```javascript
await MutualTLS.configure({
  keychainServiceForP12: 'my-tls.client.p12',
  keychainServiceForPassword: 'my-tls.client.p12.password',
});
```

If you do not call `MutualTLS.configure`, then the following defaults are used:
- `keychainServiceForP12`: `mutual-tls.client.p12`
- `keychainServiceForPassword`: `mutual-tls.client.p12.password`

### Android Setup

For Android, provide the certificate data directly:

```javascript
const myP12DataBase64 = "YOUR P12 FILE ENCODED AS BASE64 GOES HERE";
const myPassword = "THE PASSWORD TO DECRYPT THE P12 FILE GOES HERE";

await MutualTLS.configure({
  certificateFileP12: myP12DataBase64,
  certificatePassword: myPassword,
});
```

### Optional: Debug and Error Callbacks

You can set up debug and error callbacks for troubleshooting:

```javascript
MutualTLS.onDebug((message, data) => {
  console.debug('MutualTLS Debug:', message, data);
});

MutualTLS.onError((message, data) => {
  console.error('MutualTLS Error:', message, data);
});
```

### Test Environment Configuration

If you're using MutualTLS in a test environment with a proxied connection where the server name does not match the server name in the server certificate, you can set the `insecureDisableVerifyServerInRootDomain` option to the root domain for which you want to insecurely trust all subdomains. For example, setting it to `example.com` would let you insecurely trust servers at a domain like `bad.example.com`.

⚠️ **DO NOT USE THIS SETTING IN A PRODUCTION ENVIRONMENT**, as it defeats server authentication security features which form the other half of the "mutual" part of Mutual TLS.

```javascript
await MutualTLS.configure({
  keychainServiceForP12: 'my-tls.client.p12',
  keychainServiceForPassword: 'my-tls.client.p12.password',
  insecureDisableVerifyServerInRootDomain: 'example.com', // Only for testing!
});
```

### Test

Assuming you've done all that setup, then you're ready to make secure Mutual TLS requests with a server that is configured to trust the client certificate you provided.

As stated before, any normal react-native HTTP request (e.g. through `fetch`, `XMLHttpRequest`, or any library that uses these) for HTTPS connections that ask for a client certificate will work, with no special options needed at request time.

```javascript
const response = await fetch('https://my-secure.example.com/');
```

To see and run a fully working demonstration using `https://client.badssl.com/` as the test server, see [the example project in this repository](./example).

### Contributors

- [willerrodrigo](https://github.com/willerrodrigo)
- [undeaDD](https://github.com/undeaDD)
