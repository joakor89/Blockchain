/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.threads;

import com.blockchainvision.basicblockchain.logic.Blockchain;
import com.blockchainvision.basicblockchain.logic.DependencyManager;
import com.blockchainvision.basicblockchain.logic.PendingTransactions;
import com.blockchainvision.basicblockchain.models.Block;
import com.blockchainvision.basicblockchain.models.Transaction;
import com.blockchainvision.basicblockchain.utils.SHA3Helper;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Miner implements Runnable
{
	private final static Logger logger = Logger.getLogger( Miner.class );

	private List<MinerListener> listeners = new ArrayList<>( );

	private boolean mining = true;

	private boolean cancelBlock = false;

	private Block block;

	@Override public void run( )
	{
		logger.info( "Miner started" );

		while ( isMining( ) )
		{
			block = getNewBlockForMining( );

			while ( !cancelBlock && doesNotFulfillDifficulty( block.getBlockHash( ) ) )
			{
				try
				{
					block.incrementNonce( );
				}
				catch ( ArithmeticException e )
				{
					restartMining( );
				}
			}

			if ( cancelBlock )
			{
				block = null;
				cancelBlock = false;
			}
			else
			{
				blockMined( block );
			}
		}
	}

	private Block getNewBlockForMining( )
	{
		PendingTransactions pendingTransactions = DependencyManager.getPendingTransactions( );
		Blockchain blockchain = DependencyManager.getBlockchain( );
		List<Transaction> transactions = pendingTransactions.getTransactionsForNextBlock( );

		return new Block( transactions, blockchain.getPreviousHash( ) );
	}

	private boolean doesNotFulfillDifficulty( byte[] digest )
	{
		Blockchain blockchain = DependencyManager.getBlockchain( );
		return !blockchain.fulfillsDifficulty( digest );
	}

	private void restartMining( )
	{
		PendingTransactions pendingTransactions = DependencyManager.getPendingTransactions( );
		List<Transaction> transactions = pendingTransactions.getTransactionsForNextBlock( );

		block.setTransactions( transactions );
	}

	private void blockMined( Block block )
	{
		logger.debug( "block mined" );

		if(block.getTransactions().size() > 0)
		{
			for ( Transaction transaction : block.getTransactions( ) )
			{
				transaction.setBlockId( block.getBlockHash( ) );
				logger.info(
					transaction.getTxIdAsString( ) + "; " + SHA3Helper.digestToHex( transaction.getBlockId( ) ) );
			}
		}

		DependencyManager.getBlockchain( ).addBlock( block );
		DependencyManager.getPendingTransactions().clearPendingTransactions( block );

		for ( MinerListener listener : listeners )
		{
			listener.notifyNewBlock( block );
		}
	}

	public boolean isMining( )
	{
		return mining;
	}

	public void setMining( boolean mining )
	{
		this.mining = mining;
	}

	public void stopMining( )
	{
		logger.info( "stopping mining" );

		this.mining = false;
	}

	public void setCancelBlock( boolean cancelBlock )
	{
		logger.info( "canceling block" );

		this.cancelBlock = cancelBlock;
	}

	public void cancelBlock( )
	{
		this.cancelBlock = true;
	}

	public void registerListener( MinerListener listener )
	{
		listeners.add( listener );
	}
}
