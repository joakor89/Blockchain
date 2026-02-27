/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.models;

public class GenesisBlock extends Block
{
	public static byte[] ZERO_HASH_IN = new byte[32];

	public GenesisBlock( )
	{
		super( ZERO_HASH_IN );
	}
}
