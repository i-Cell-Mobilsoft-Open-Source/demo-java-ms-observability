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

import java.util.SortedMap;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.metrics.Gauge;
import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricFilter;
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
public class CpuHealth {

    @Inject
    private Config config;

    @Produces
    @Readiness
    public HealthCheck produceDataBasePoolCheck() {
        return this::checkConnectionPool;
    }

    @Inject
    @RegistryType(type = MetricRegistry.Type.BASE)
    private MetricRegistry vendorRegistry;

    public HealthCheckResponse checkConnectionPool() {

        Integer usagePercentTreshold = config.getOptionalValue("CPU_USAGE_TRESHOLD_PERCENT", Integer.class).orElse(100);
        System.out.println("usagePercentTreshold: " + usagePercentTreshold);

        // "base_cpu_processCpuLoad"
        MetricID availableProcessors = new MetricID("cpu.availableProcessors");
        
        SortedMap<MetricID, Gauge> gauges = vendorRegistry.getGauges();
        
        for(MetricID id :gauges.keySet()) {
            System.out.println(id.toString());
            
        }
        
        Gauge<?> availableProcessorsGauge = vendorRegistry.getGauge(availableProcessors);
        Integer availableCpus = (Integer) availableProcessorsGauge.getValue();
        System.out.println("availableCpus " + availableCpus);

        // "base_cpu_processCpuLoad"
        MetricID processCpuLoad = new MetricID("cpu.processCpuLoad");
        Gauge<?> processCpuLoadGauge = vendorRegistry.getGauge(processCpuLoad);
        Double processCpuLoadValue = (Double) processCpuLoadGauge.getValue();
        System.out.println("processCpuLoadValue " + processCpuLoadValue);

        double usagePercent = (processCpuLoadValue) * 100;
        System.out.println(usagePercent);

        HealthCheckResponseBuilder builder = HealthCheckResponse.builder().name("cpu_usage");
        builder.withData("CPU_USAGE", usagePercent + "%");

        if (usagePercent > usagePercentTreshold) {
            builder.down();
        } else {
            builder.up();
        }
        System.out.println("Usage percent " + usagePercent);

        return builder.build();
    }

}
