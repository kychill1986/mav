package com.yang.disruptor.complex;

import com.lmax.disruptor.EventHandler;
import com.yang.disruptor.simple.TradeTransaction;

public class TradeTransactionJMSNotifyHandler implements EventHandler<TradeTransaction> {

	@Override
	public void onEvent(TradeTransaction event, long sequence, boolean endOfBatch) throws Exception {
		// do send jms message
	}

}
