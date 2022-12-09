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

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricID;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.Tag;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

import hu.icellmobilsoft.sampler.sample.readiness.restservice.rest.UsageCache;

/**
 * 
 * Db health
 * 
 * @author czenczl
 *
 */
@Dependent
public class DatabaseHealth {

    @Inject
    private Config config;

    @Inject
    private UsageCache usageCache;

    // @Produces
    // @Readiness
    // public HealthCheck produceDataBasePoolCheck() {
    // return this::checkConnectionPool;
    // }

    @Inject
    @RegistryType(type = MetricRegistry.Type.VENDOR)
    private MetricRegistry vendorRegistry;

    @Inject
    @RegistryType(type = MetricRegistry.Type.APPLICATION)
    private MetricRegistry applicationRegistry;

    public HealthCheckResponse checkConnectionPool() {

        Integer usagePercentTreshold = config.getOptionalValue("DATASOURCE_USAGE_TRESHOLD_PERCENT", Integer.class).orElse(80);
        System.out.println("usagePercentTreshold: " + usagePercentTreshold);

        Integer maxPoolSize = config.getOptionalValue("POSTGRESQL_DS_MAX_POOL_SIZE", Integer.class).orElse(60);
        System.out.println("max pool size: " + maxPoolSize);

        MetricID inUseId = new MetricID("wildfly_datasources_pool_in_use_count", new Tag("data_source", "icellmobilsoftDS"));
        Gauge<?> inUseGauge = vendorRegistry.getGauge(inUseId);
        Double inUse = (Double) inUseGauge.getValue();
        System.out.println("inuse " + inUse);

        MetricID availableId = new MetricID("wildfly_datasources_pool_available_count", new Tag("data_source", "icellmobilsoftDS"));
        Gauge<?> availableGauge = vendorRegistry.getGauge(availableId);
        Double availableUse = (Double) availableGauge.getValue();
        System.out.println("available " + availableUse);

        double usagePercent = (inUse / maxPoolSize) * 100;
        System.out.println(usagePercent);

        HealthCheckResponseBuilder builder = HealthCheckResponse.builder().name("database_connectionPool");
        builder.withData("DB_CONNECTION_USAGE_PERCENT", usagePercent + "%");

        Metadata readinessDsPoolMeta = Metadata.builder().withName("READINESS_DS_POOL").withDescription("READINESS_DS_POOL")
                .withType(MetricType.GAUGE).build();
        Tag tag = new Tag("DS_USAGE_PERCENT_TRESHOLD", usagePercentTreshold + "");
        if (usagePercent > usagePercentTreshold) {
            // down mert db usage > 70%
            builder.down();
            usageCache.setWaitTime(1);
            applicationRegistry.gauge(readinessDsPoolMeta, () -> usageCache.getWaitTime(), tag);

        } else {
            builder.up();
            usageCache.setWaitTime(0);
            applicationRegistry.gauge(readinessDsPoolMeta, () -> usageCache.getWaitTime(), tag);
        }

        return builder.build();
    }

}
