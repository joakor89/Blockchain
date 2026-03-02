/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.api.converters;

import com.blockchainvision.basicblockchain.utils.SHA3Helper;
import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

public class HashConverter implements Converter<byte[]>
{
	@Override public void serialize( byte[] bytes, ObjectWriter objectWriter, Context context ) throws Exception
	{
		objectWriter.writeString( SHA3Helper.digestToHex( bytes ) );
	}

	@Override public byte[] deserialize( ObjectReader objectReader, Context context ) throws Exception
	{
		return SHA3Helper.hexToDigest( objectReader.valueAsString( ) );
	}

}
