/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.logic;

import com.blockchainvision.basicblockchain.threads.Miner;
import org.apache.log4j.Logger;

public class DependencyManager
{
	private static Logger logger = Logger.getLogger( DependencyManager.class );

	private static PendingTransactions pendingTransactions;

	public static PendingTransactions getPendingTransactions( )
	{
		if ( pendingTransactions == null )
		{
			pendingTransactions = new PendingTransactions( );
			logger.info( pendingTransactions + " created PendingTransactions" );
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

	private static Miner miner;

	public static Miner getMiner( )
	{
		if ( miner == null )
		{
			miner = new Miner( );
		}

		return miner;
	}
}
