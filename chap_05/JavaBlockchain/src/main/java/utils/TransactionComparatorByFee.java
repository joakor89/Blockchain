/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.utils;

import com.blockchainvision.basicblockchain.models.Transaction;

import java.util.Comparator;

public class TransactionComparatorByFee implements Comparator<Transaction>
{
	@Override public int compare( Transaction o1, Transaction o2 )
	{
		int result = 0;

		if ( o2.getTransactionFeeBasePrice( ) - o1.getTransactionFeeBasePrice( ) < 0.0 )
		{
			result = -1;
		}
		else if ( o2.getTransactionFeeBasePrice( ) - o1.getTransactionFeeBasePrice( ) > 0.0 )
		{
			result = 1;
		}

		return result;
	}
}
