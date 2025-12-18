/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.mongo.wrapper;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DefaultMongoClientFactory implements MongoClientFactory {

  public MongoClient getMongoClient(
      List<ServerAddress> serverAddressList, List<MongoCredential> credList,
      MongoClientOptions opts, boolean useReplicaSet ) {
    
    // Convert legacy options to modern MongoClientSettings
    MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
        .applyToClusterSettings( b -> b.hosts( serverAddressList ) )
        .applyToConnectionPoolSettings( b -> {
            b.maxSize( opts.getConnectionsPerHost() );
            b.maxWaitTime( opts.getMaxWaitTime(), TimeUnit.MILLISECONDS );
        })
        .applyToSocketSettings( b -> {
            b.connectTimeout( opts.getConnectTimeout(), TimeUnit.MILLISECONDS );
            b.readTimeout( opts.getSocketTimeout(), TimeUnit.MILLISECONDS );
        })
        .readPreference( opts.getReadPreference() )
        .writeConcern( opts.getWriteConcern() );

    if ( opts.isSslEnabled() ) {
        settingsBuilder.applyToSslSettings( b -> b.enabled( true ) );
    }

    if ( credList != null && !credList.isEmpty() ) {
        settingsBuilder.credential( credList.get( 0 ) );
    }

    return new MongoClient( settingsBuilder.build() );
  }

  @Override
  public MongoClient getConnectionStringMongoClient( String connectionString ) {
    ConnectionString cs = new ConnectionString( connectionString );
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString( cs )
            .build();
    return new MongoClient( settings );
  }
}