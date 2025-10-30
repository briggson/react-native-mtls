import { requireNativeModule, NativeModulesProxy } from 'expo-modules-core';

const MutualTLS = requireNativeModule('MutualTLS');

export interface MutualTLSOptions {
  /** iOS: Keychain service name for P12 certificate file */
  keychainServiceForP12?: string;
  /** iOS: Keychain service name for certificate password */
  keychainServiceForPassword?: string;
  /** iOS: Root domain to disable server verification for (INSECURE - dev only) */
  insecureDisableVerifyServerInRootDomain?: string;
  /** Android: Base64-encoded P12 certificate file */
  certificateFileP12?: string;
  /** Android: Password for P12 certificate */
  certificatePassword?: string;
}

interface MutualTLSModule {
  configure(options: MutualTLSOptions): Promise<void>;
}

const module = MutualTLS as MutualTLSModule;

// Store callback references to prevent garbage collection
let debugCallback: ((message: string, data?: any) => void) | null = null;
let errorCallback: ((message: string, data?: any) => void) | null = null;

/**
 * Configure mutual TLS authentication
 */
export function configure(options: MutualTLSOptions): Promise<void> {
  return module.configure(options);
}

/**
 * Set debug callback function
 */
export function onDebug(callback: (message: string, data?: any) => void): void {
  debugCallback = callback;
  // In Expo modules, events are handled differently
  // The native side will emit events that can be listened to
}

/**
 * Set error callback function
 */
export function onError(callback: (message: string, data?: any) => void): void {
  errorCallback = callback;
  // In Expo modules, events are handled differently
  // The native side will emit events that can be listened to
}

export default {
  configure,
  onDebug,
  onError,
};
