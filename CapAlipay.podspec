
  Pod::Spec.new do |s|
    s.name = 'CapAlipay'
    s.version = '0.0.1'
    s.summary = 'capacitor plugin for alipay'
    s.license = 'MIT'
    s.homepage = 'https://github.com/jing-zhou/cap-alipay.git'
    s.author = 'jing-zhou'
    s.source = { :git => 'https://github.com/jing-zhou/cap-alipay.git', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end