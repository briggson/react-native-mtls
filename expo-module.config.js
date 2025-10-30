module.exports = {
  platforms: ['ios', 'android'],
  ios: {
    sources: ['ios/**/*.{h,m,mm,swift}'],
    modules: ['MutualTLSModule'],
    podspecFile: 'react-native-mtls.podspec',
  },
  android: {
    sources: ['android/src/main/java/com/reactlibrary/**/*.{java,kt}'],
    modules: ['MutualTLSModule'],
  },
};
