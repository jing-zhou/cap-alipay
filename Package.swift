// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapAlipayPlugin",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "CapAlipayPlugin",
            targets: ["AlipayPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", branch: "main")
    ],
    targets: [
        .target(
            name: "AlipayPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/AlipayPlugin"),
        .testTarget(
            name: "AlipayPluginTests",
            dependencies: ["AlipayPlugin"],
            path: "ios/Tests/AlipayPluginTests")
    ]
)