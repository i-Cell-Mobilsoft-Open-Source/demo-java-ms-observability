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

import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
public class BRest extends BaseRestService implements IBRest {

    @Inject
    private MetricRegistry metricRegistry;

    @Inject
    @RestClient
    private ICRestRegisteredClient client;

    @Inject
    private DemoEntityService demoEntityService;

    @Inject
    private Event<Object> event;

    @Override
    public SampleResponse getb() throws BaseException {
        System.out.println("b called");

        // custom metric
        Tag keyTag = new Tag("B_SERVICE", "B_SERVICE");
        Metadata metadata = Metadata.builder().withName("B_SERVICE").withDescription("B_SERVICE").withType(MetricType.COUNTER).build();
        metricRegistry.counter(metadata, keyTag).inc();

        // save entity
        // CDI.current().select(BRest.class).get().createEntity();

        // non blocking TODO trheads...
        // event.fireAsync(new Object());

        client.getc();

        return new SampleResponse();
    }

    public void onEvent(@ObservesAsync Object event) throws BaseException {
        client.getc();
    }

    @Transactional
    public void createEntity() throws BaseException {
        DemoEntity entity = new DemoEntity();
        entity.setId(RandomUtil.generateId());
        demoEntityService.save(entity);
        try {
            // Thread.sleep(ThreadLocalRandom.current().nextInt(2, 10) * 1000);
            Thread.sleep(ThreadLocalRandom.current().nextInt(1, 2) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
