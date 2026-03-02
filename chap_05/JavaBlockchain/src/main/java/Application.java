/*
 * Copyright (c) Tobias Fertig, 2018.
 */

package com.blockchainvision.basicblockchain;

import com.blockchainvision.basicblockchain.api.services.BlockService;
import com.blockchainvision.basicblockchain.api.services.DispatcherService;
import com.blockchainvision.basicblockchain.api.services.TransactionService;
import com.blockchainvision.basicblockchain.logic.DependencyManager;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.ext.jaxrs.GensonJaxRSFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath( "api" )
public class Application extends ResourceConfig
{
	public Application( )
	{
		packages(true, "de.etherbasics.basicblockchain.api.services" );
		registerClasses( getServiceClasses( ) );
		register( new GensonJaxRSFeature( ).use(
			new GensonBuilder( ).setSkipNull( true )
								.useIndentation( true )
								.useDateAsTimestamp( false )
								.useDateFormat( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" ) )
								.create( ) ) );

		Thread thread = new Thread( DependencyManager.getMiner( ) );
		thread.start( );
	}

	protected Set<Class<?>> getServiceClasses( )
	{
		final Set<Class<?>> returnValue = new HashSet<>( );

		returnValue.add( BlockService.class );
		returnValue.add( TransactionService.class );
		returnValue.add( DispatcherService.class );

		return returnValue;
	}
}
