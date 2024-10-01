export interface AlipayPlugin {
  pay(options: { bill: string }): Promise<{}>;
}
