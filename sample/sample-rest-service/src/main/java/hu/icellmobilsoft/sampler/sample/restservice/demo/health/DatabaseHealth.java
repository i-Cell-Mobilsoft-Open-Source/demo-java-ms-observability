/*-
 * #%L
 * Sampler
 * %%
 * Copyright (C) 2022 i-Cell Mobilsoft Zrt.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package hu.icellmobilsoft.sampler.sample.restservice.demo.health;

import java.sql.Connection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import hu.icellmobilsoft.coffee.se.logging.Logger;

/**
 * 
 * Db health
 * 
 * @author czenczl
 *
 */
@Dependent
public class DatabaseHealth {

	/**
	 * producer for database rediness check
	 * 
	 * @return health check funcional interface
	 */
	@Produces
	@Readiness
	public HealthCheck produceDataBaseCheck() {
		return this::checkReadiness;
	}

	/**
	 * readiness
	 * 
	 * @return HealthCheckResponse
	 */
	public HealthCheckResponse checkReadiness() {

		HealthCheckResponseBuilder builder = HealthCheckResponse.builder().name("database");
		builder.withData("POSTGRES" + "URL", "jdbc:postgresql://postgres-local:5432/coredb");

		String dsName = "icellmobilsoftDS";
		DataSource datasource = getDataSource(dsName);
		try {
			connectWithTimeout(datasource, 10, TimeUnit.SECONDS);
			builder.up();
		} catch (Exception e) {
			Logger.getLogger(DatabaseHealth.class).error("Error occurred while establishing connection: " + e.getLocalizedMessage(), e);
			builder.down();
		}

		return builder.build();

	}

	private DataSource getDataSource(String dsName) {
		try {
			Context initContext = new InitialContext();
			return (DataSource) initContext.lookup("java:jboss/datasources/" + dsName);
		} catch (NamingException e) {
			Logger.getLogger(DatabaseHealth.class).error("Error occured while getting datasource: " + e.getLocalizedMessage(), e);
		}
		return null;
	}

	private Connection connectWithTimeout(DataSource datasource, long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		Future<Connection> future = executor.submit(() -> {
			try (Connection connection = datasource.getConnection()) {
				connection.isValid(1);
				return connection;
			}
		});
		return future.get(timeout, unit);
	}

}
