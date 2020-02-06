declare module "@capacitor/core" {
  interface PluginRegistry {
    Alipay: AlipayPlugin;
  }
}

export interface AlipayPlugin {
  pay(options: { bill: string }): Promise<{}>;
}
