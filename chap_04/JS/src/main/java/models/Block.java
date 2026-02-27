/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.models;

import com.blockchainvision.basicblockchain.utils.SHA3Helper;
import com.blockchainvision.basicblockchain.utils.SizeHelper;
import org.bouncycastle.util.Arrays;

import java.util.ArrayList;
import java.util.List;

public class Block
{
	private int magicNumber = 0xD9B4BEF9;

	private int blockSize;

	private int transactionCount;

	private List<Transaction> transactions;

	private BlockHeader blockHeader;

	public Block( )
	{
	}

	public Block( byte[] previousHash )
	{
		this.transactionCount = 0;
		this.transactions = new ArrayList<>( );
		this.blockSize = SizeHelper.calculateBlockSize( this );

		this.blockHeader = new BlockHeader( System.currentTimeMillis( ), previousHash, getTransactionHash( ) );
	}

	public Block( List<Transaction> transactions, byte[] previousHash )
	{
		this.transactions = transactions;
		this.transactionCount = transactions.size( );
		this.blockSize = SizeHelper.calculateBlockSize( this );
		this.blockHeader = new BlockHeader( System.currentTimeMillis( ), previousHash, getTransactionHash( ) );
	}

	private byte[] getTransactionHash( )
	{
		byte[] transactionsInBytes = new byte[ 0 ];

		for ( Transaction transaction : transactions )
		{
			transactionsInBytes = Arrays.concatenate( transactionsInBytes, transaction.getTxId( ) );
		}

		return SHA3Helper.hash256( transactionsInBytes );
	}

	public void addTransaction( Transaction transaction )
	{
		this.transactions.add( transaction );
		this.transactionCount++;

		this.blockHeader.setTransactionListHash( getTransactionHash( ) );
		this.blockSize = SizeHelper.calculateBlockSize( this );
	}

	public byte[] getBlockHash( )
	{
		return blockHeader.asHash( );
	}

	public int getNonce( )
	{
		return this.blockHeader.getNonce( );
	}

	public void setNonce( int nonce )
	{
		this.blockHeader.setNonce( nonce );
	}

	public void incrementNonce( ) throws ArithmeticException
	{
		this.blockHeader.incrementNonce( );
	}

	public int getMagicNumber( )
	{
		return magicNumber;
	}

	public void setMagicNumber( int magicNumber )
	{
		this.magicNumber = magicNumber;
	}

	public int getBlockSize( )
	{
		return blockSize;
	}

	public void setBlockSize( int blockSize )
	{
		this.blockSize = blockSize;
	}

	public int getTransactionCount( )
	{
		return transactionCount;
	}

	public void setTransactionCount( int transactionCount )
	{
		this.transactionCount = transactionCount;
	}

	public List<Transaction> getTransactions( )
	{
		return transactions;
	}

	public void setTransactions( List<Transaction> transactions )
	{
		this.transactions = transactions;
	}

	public BlockHeader getBlockHeader( )
	{
		return blockHeader;
	}

	public void setBlockHeader( BlockHeader blockHeader )
	{
		this.blockHeader = blockHeader;
	}
}
