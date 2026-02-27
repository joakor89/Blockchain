/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.threads;

import com.blockchainvision.basicblockchain.models.Block;

public interface MinerListener
{
	public void notifyNewBlock( Block block );
}
