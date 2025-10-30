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

export interface MutualTLSModule {
  /**
   * Configure mutual TLS authentication
   */
  configure(options: MutualTLSOptions): Promise<void>;

  /**
   * Set debug callback function
   */
  onDebug(callback: (message: string, data?: any) => void): void;

  /**
   * Set error callback function
   */
  onError(callback: (message: string, data?: any) => void): void;
}

declare const MutualTLS: MutualTLSModule;
export default MutualTLS;
