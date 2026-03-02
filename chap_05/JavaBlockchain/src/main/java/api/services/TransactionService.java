/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.api.services;

import com.blockchainvision.basicblockchain.logic.DependencyManager;
import com.blockchainvision.basicblockchain.models.Block;
import com.blockchainvision.basicblockchain.models.Transaction;
import com.blockchainvision.basicblockchain.utils.SHA3Helper;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Path("transactions")
public class TransactionService
{
	private static Logger logger = Logger.getLogger( TransactionService.class );

	@Context
	UriInfo uriInfo;

	@POST
	@Consumes( MediaType.APPLICATION_JSON )
	public Response sendTransaction( Transaction transaction )
	{
		DependencyManager.getPendingTransactions( ).addPendingTransaction( transaction );
		DependencyManager.getMiner( ).cancelBlock( );

		logger.info( transaction );

		try
		{
			return Response.created(
				new URI( uriInfo.getRequestUriBuilder( ).path( transaction.getTxIdAsString( ) ).toString( ) ) )
						   .build( );
		}
		catch ( URISyntaxException e )
		{
			throw new WebApplicationException( "Uri for transaction could not be generated" );
		}
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path("{hash}")
	public Response getTransactionByHash( @PathParam( "hash" ) String hex )
	{
		Transaction trx = DependencyManager.getBlockchain( ).getTransactionByHash( hex );

		Response response = null;

		if ( trx == null )
		{
			response = Response.status( 404 ).build( );
		}
		else
		{
			response = Response.ok( trx )
							   .header( "Link",
								   uriInfo.getBaseUriBuilder( )
										  .path( "blocks" )
										  .path( SHA3Helper.digestToHex( trx.getBlockId( ) ) ).build( ) )
							   .build( );
		}

		return response;
	}

	@GET
	@Produces( MediaType.APPLICATION_JSON)
	public Response getRecentTransactions( @QueryParam( "size" ) @DefaultValue( "10" ) int size,
		@QueryParam( "offset" ) @DefaultValue( "0" ) int offset )
	{
		List<Transaction> transactions = new ArrayList<>( );

		for ( Block latestBlock : DependencyManager.getBlockchain( ).getLatestBlocks( size, offset ) )
		{
			for ( Transaction transaction : latestBlock.getTransactions( ) )
			{
				transactions.add( transaction );
			}
		}

		return Response.ok( transactions ).build( );
	}
}
