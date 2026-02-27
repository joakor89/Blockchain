/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.models;

import java.util.ArrayList;
import java.util.List;

public class Chain
{
	private List<Block> chain = new ArrayList<>( );

	private int networkId;

	public Chain( int networkId )
	{
		this.networkId = networkId;
		chain.add( new GenesisBlock( ) );
	}

	public void add( Block block )
	{
		chain.add( block );
	}

	public Block get( int index )
	{
		return chain.get( index );
	}

	public Block getLast( )
	{
		return chain.get( chain.size( ) - 1 );
	}

	public int size( )
	{
		return chain.size( );
	}

	public List<Block> getChain( )
	{
		return chain;
	}

	public int getNetworkId( )
	{
		return networkId;
	}

	public void setNetworkId( int networkId )
	{
		this.networkId = networkId;
	}

	@Override public String toString( )
	{
		return "Chain{" +
			"chain=" + chain +
			", networkId=" + networkId +
			'}';
	}
}
