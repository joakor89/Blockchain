/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain.api.services;

import com.blockchainvision.basicblockchain.logic.DependencyManager;
import com.blockchainvision.basicblockchain.models.Block;
import com.blockchainvision.basicblockchain.models.Transaction;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class DispatcherService
{
	@GET
	@Produces( MediaType.APPLICATION_JSON )
	@Path( "{hash}" )
	public Response searchForHash( @PathParam( "hash" ) String hex )
	{
		Transaction transaction = null;
		Block block = DependencyManager.getBlockchain( ).getBlockByHash( hex );

		Response response = null;
		if ( block == null )
		{
			transaction = DependencyManager.getBlockchain( ).getTransactionByHash( hex );

			if ( transaction == null )
			{
				response = Response.status( 404 ).build( );
			}
			else
			{
				response = Response.ok( transaction ).build( );
			}
		}
		else
		{
			response = Response.ok( block ).build( );
		}

		return response;
	}
}
