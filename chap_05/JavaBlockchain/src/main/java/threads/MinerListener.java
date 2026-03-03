/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.threads;

import com.blockchainvision.basicblockchain.models.Block;

public interface MinerListener
{
	void notifyNewBlock( Block block );
}
