import { WebPlugin } from '@capacitor/core';
import { AlipayPlugin } from './definitions';

export class AlipayWeb extends WebPlugin implements AlipayPlugin {
  constructor() {
    super({
      name: 'Alipay',
      platforms: ['web']
    });
  }

  async pay(options: { bill: string }): Promise<{}> {
    return options;
  }
}

const Alipay = new AlipayWeb();

export { Alipay };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(Alipay);
