/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.logic;

public class DependencyManager
{
	private static PendingTransactions pendingTransactions;

	public static PendingTransactions getPendingTransactions( )
	{
		if ( pendingTransactions == null )
		{
			pendingTransactions = new PendingTransactions( );
		}

		return pendingTransactions;
	}

	public static void injectPendingTransactions( PendingTransactions pendingTransactions )
	{
		DependencyManager.pendingTransactions = pendingTransactions;
	}

	private static Blockchain blockchain;

	public static Blockchain getBlockchain( )
	{
		if ( blockchain == null )
		{
			blockchain = new Blockchain( );
		}

		return blockchain;
	}

	public static void injectBlockchain( Blockchain blockchain )
	{
		DependencyManager.blockchain = blockchain;
	}
}
