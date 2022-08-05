import { Plugin } from "@capacitor/core";
export interface AlipayPlugin extends Plugin {
  pay(options: { bill: string }): Promise<{}>;
}
