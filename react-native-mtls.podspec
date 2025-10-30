require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-mtls"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = <<-DESC
                  react-native-mtls
                   DESC
  s.homepage     = "https://github.com/briggson/react-native-mtls"
  s.license      = "MIT"
  s.authors      = { "Briggson Verdugo" => "briggsonverdugo@gmail.com" }
  s.platforms    = { :ios => "13.0" }
  s.source       = { :git => "https://github.com/briggson/react-native-mtls.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true
  s.swift_version = "5.0"

  s.dependency "ExpoModulesCore"
end
