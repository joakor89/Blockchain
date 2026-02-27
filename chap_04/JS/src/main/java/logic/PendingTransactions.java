/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.logic;

import com.blockchainvision.basicblockchain.models.Block;
import com.blockchainvision.basicblockchain.models.Transaction;
import com.blockchainvision.basicblockchain.utils.SizeHelper;
import com.blockchainvision.basicblockchain.utils.TransactionComparatorByFee;

import java.util.*;

public class PendingTransactions
{
	private PriorityQueue<Transaction> pendingTransactions;

	public PendingTransactions( )
	{
		Comparator<Transaction> comparator = new TransactionComparatorByFee( );
		pendingTransactions = new PriorityQueue<>( 11, comparator );
	}

	public PendingTransactions( Comparator<Transaction> comparator )
	{
		pendingTransactions = new PriorityQueue<>( 11, comparator );
	}

	public void addPendingTransaction( Transaction transaction )
	{
		pendingTransactions.add( transaction );
	}

	public List<Transaction> getTransactionsForNextBlock( )
	{
		List<Transaction> nextTransactions = new ArrayList<>( );

		int transactionCapacity = SizeHelper.calculateTransactionCapacity( );

		PriorityQueue<Transaction> temp = new PriorityQueue<>( pendingTransactions );
		while ( transactionCapacity > 0 && !temp.isEmpty( ) )
		{
			nextTransactions.add( temp.poll( ) );
			transactionCapacity--;
		}

		return nextTransactions;
	}

	public void clearPendingTransactions( Block block )
	{
		clearPendingTransactions( block.getTransactions( ) );
	}

	public void clearPendingTransactions( List<Transaction> transactions )
	{
		for ( Transaction transaction : transactions )
		{
			pendingTransactions.remove( transaction );
		}
	}

	public void clearPendingTransaction( Transaction transaction )
	{
		pendingTransactions.remove( transaction );
	}

	public boolean pendingTransactionsAvailable( )
	{
		return !pendingTransactions.isEmpty( );
	}
}
