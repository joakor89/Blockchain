/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.logic;

import com.blockchainvision.basicblockchain.models.Block;
import com.blockchainvision.basicblockchain.models.Chain;
import com.blockchainvision.basicblockchain.models.Transaction;
import com.blockchainvision.basicblockchain.utils.SHA3Helper;
import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Blockchain
{
	private static Logger logger = Logger.getLogger( Blockchain.class );

	public final static int MAX_BLOCK_SIZE_IN_BYTES = 1120;

	public final static int NETWORK_ID = 1;

	private BigInteger difficulty;

	private Chain chain;

	private Map<String, Block> blockCache;

	private Map<String, Transaction> transactionCache;

	public Blockchain( )
	{
		this.chain = new Chain( NETWORK_ID );
		this.blockCache = new ConcurrentHashMap<>( );
		this.transactionCache = new ConcurrentHashMap<>( );
		this.difficulty = new BigInteger(
			"-57896000000000000000000000000000000000000000000000000000000000000000000000000" );
	}

	public void addBlock( Block block )
	{
		chain.add( block );
		blockCache.put( SHA3Helper.digestToHex( block.getBlockHash( ) ), block );

		for ( Transaction transaction : block.getTransactions( ) )
		{
			transactionCache.put( transaction.getTxIdAsString( ), transaction );
		}
	}

	public boolean fulfillsDifficulty( byte[] digest )
	{
		BigInteger temp = new BigInteger( digest );

		return temp.compareTo( difficulty ) <= 0;
	}

	public BigInteger getDifficulty( )
	{
		return difficulty;
	}

	public void setDifficulty( BigInteger difficulty )
	{
		this.difficulty = difficulty;
	}

	public byte[] getPreviousHash( )
	{
		return chain.getLast( ).getBlockHash( );
	}

	public int size( )
	{
		return chain.size( );
	}

	public Transaction getTransactionByHash( String hex )
	{
		return transactionCache.get( hex );
	}

	public Block getBlockByHash( String hex )
	{
		return blockCache.get( hex );
	}

	public Block getBlockByHash( byte[] hash )
	{
		return blockCache.get( SHA3Helper.digestToHex( hash ) );
	}

	public Block getLatestBlock( )
	{
		return chain.getLast( );
	}

	public List<Block> getLatestBlocks( int size, int offset )
	{
		List<Block> blocks = new ArrayList<>( );

		Block block = this.getLatestBlock( );

		for ( int i = 0; i < ( size + offset ); i++ )
		{
			if ( block != null )
			{
				if ( i >= offset )
				{
					blocks.add( block );
				}

				String previousHash = SHA3Helper.digestToHex( block.getBlockHeader( ).getPreviousHash( ) );
				block = this.getBlockByHash( previousHash );
			}
		}

		return blocks;
	}

	public Block getChildOfBlock( Block block )
	{
		return chain.get( chain.getChain( ).indexOf( block ) + 1 );
	}
}
