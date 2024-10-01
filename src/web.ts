import { WebPlugin } from '@capacitor/core';
import type { AlipayPlugin } from './definitions';

export class AlipayWeb extends WebPlugin implements AlipayPlugin {
  async pay(options: { bill: string }): Promise<{}> {
    return options;
  }
}

