import ExpoModulesCore

public class MutualTLSModule: Module {
  public func definition() -> ModuleDefinition {
    Name("MutualTLS")

    AsyncFunction("configure") { (options: [String: Any], promise: Promise) in
      do {
        if let keychainServiceForP12 = options["keychainServiceForP12"] as? String {
          MutualTLSConfig.global.setKeychainServiceForP12(keychainServiceForP12)
          MutualTLSDebug.log("configured setting", withData: ["keychainServiceForP12": keychainServiceForP12])
        }

        if let keychainServiceForPassword = options["keychainServiceForPassword"] as? String {
          MutualTLSConfig.global.setKeychainServiceForPassword(keychainServiceForPassword)
          MutualTLSDebug.log("configured setting", withData: ["keychainServiceForPassword": keychainServiceForPassword])
        }

        if let insecureDisableVerifyServerInRootDomain = options["insecureDisableVerifyServerInRootDomain"] as? String {
          MutualTLSConfig.global.setInsecureDisableVerifyServerInRootDomain(insecureDisableVerifyServerInRootDomain)
          MutualTLSDebug.log("configured setting", withData: ["insecureDisableVerifyServerInRootDomain": insecureDisableVerifyServerInRootDomain])
        }

        promise.resolve(nil)
      } catch {
        promise.reject("CONFIGURE_ERROR", error.localizedDescription)
      }
    }

    // Note: Event handling for debug/error callbacks would be implemented
    // differently in Expo modules, typically through event emitters
  }
}
