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
package hu.icellmobilsoft.sampler.sample.readiness.restservice.rest;

import javax.crypto.Cipher;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.Tag;
import org.hibernate.internal.build.AllowSysOut;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.rest.BaseRestService;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.model.sample.DemoEntity;

/**
 * sample service rest implementation
 * 
 * @author czenczl
 *
 * @since 0.1.0
 */
@Model
public class CRest extends BaseRestService implements ICRest {

    @Inject
    private MetricRegistry metricRegistry;

    @Inject
    private DemoEntityService demoEntityService;

    @Inject
    private TimeCache cache;

    @Override
    public SampleResponse getc() throws BaseException {
        System.out.println("c called");

        // custom metric
        Tag keyTag = new Tag("C_SERVICE", "C_SERVICE");
        Metadata metadata = Metadata.builder().withName("C_SERVICE").withDescription("C_SERVICE").withType(MetricType.COUNTER).build();
        metricRegistry.counter(metadata, keyTag).inc();

        // save entity
        try {
            CDI.current().select(CRest.class).get().createEntity();
            // custom metric
            Tag savedEntityTag = new Tag("C_SAVED_ENTITY", "C_SAVED_ENTITY");
            Metadata metadataSavedEntity = Metadata.builder().withName("C_SAVED_ENTITY").withDescription("C_SAVED_ENTITY")
                    .withType(MetricType.COUNTER).build();
            metricRegistry.counter(metadataSavedEntity, savedEntityTag).inc();
        } catch (Throwable e) {
            Tag errorTag = new Tag("C_DB_ERROR", "C_DB_ERROR");
            Metadata metadataError = Metadata.builder().withName("C_DB_ERROR").withDescription("C_DB_ERROR").withType(MetricType.COUNTER).build();
            metricRegistry.counter(metadataError, errorTag).inc();
            throw e;
        }

        // cpu heavy
        int waitTimeInMilli = cache.getWaitTime();
        if (waitTimeInMilli != 100) {
//            for (int i = 0; i < 10000; i++) {
//                RandomUtil.generateId();
//                double result = Math.tan(598459D) * Math.tan(598459D);
//                System.out.println("tan " + Math.tan(598459D)); 
//                System.out.println("tan " + Math.tan(598459D));
//                
//            }
            
            
            int numCore = 3;
            int numThreadsPerCore = 2;
            double load = 0.8;
            final long duration = 10000;
            for (int thread = 0; thread < numCore * numThreadsPerCore; thread++) {
                new BusyThread("Thread" + thread, load, duration).start();
            }
            
        }

        return new SampleResponse();
    }

    @Transactional
    public void createEntity() throws BaseException {
        DemoEntity entity = new DemoEntity();
        entity.setId(RandomUtil.generateId());
        demoEntityService.save(entity);
        try {
            int waitTimeInMilli = cache.getWaitTime();
            System.out.println("DB wait time...: " + waitTimeInMilli);
            Thread.sleep(waitTimeInMilli);
            // Thread.sleep(ThreadLocalRandom.current().nextInt(5, 6) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SampleResponse slow(Integer time) throws BaseException {

        cache.setWaitTime(time);
        return null;
    }
    
    /**
     * Thread that actually generates the given load
     * @author Sriram
     */
    private static class BusyThread extends Thread {
        private double load;
        private long duration;

        /**
         * Constructor which creates the thread
         * @param name Name of this thread
         * @param load Load % that this thread should generate
         * @param duration Duration that this thread should generate the load for
         */
        public BusyThread(String name, double load, long duration) {
            super(name);
            this.load = load;
            this.duration = duration;
        }

        /**
         * Generates the load when run
         */
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try {
                // Loop for the given duration
                while (System.currentTimeMillis() - startTime < duration) {
                    // Every 100ms, sleep for the percentage of unladen time
                    if (System.currentTimeMillis() % 100 == 0) {
                        Thread.sleep((long) Math.floor((1 - load) * 100));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
