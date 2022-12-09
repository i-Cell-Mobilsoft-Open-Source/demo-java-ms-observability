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

import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.Tag;
import org.eclipse.microprofile.metrics.annotation.Timed;
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
public class CallBRest extends BaseRestService implements ICallBRest {

    @Inject
    @RestClient
    private IBRestRegisteredClient client;

    @Inject
    private MetricRegistry metricRegistry;

    @Inject
    private DemoEntityService demoEntityService;

    @Inject
    private Event<Object> event;

    @Override
    @Timed(name = "rest_time", description = "rest_time", unit = MetricUnits.SECONDS, absolute = true)
    public SampleResponse getCallb() throws BaseException {
        System.out.println("a called");

        // custom metric
        Tag keyTag = new Tag("A_SERVICE", "A_SERVICE");
        Metadata metadata = Metadata.builder().withName("A_SERVICE").withDescription("A_SERVICE").withType(MetricType.COUNTER).build();
        metricRegistry.counter(metadata, keyTag).inc();

        // save entity
        // CDI.current().select(CallBRest.class).get().createEntity();

        // non blocking TODO trheads...
        // event.fireAsync(new Object());

        client.getb();

        return new SampleResponse();
    }

    public void onEvent(@ObservesAsync Object event) throws BaseException {
        client.getb();
    }

    /**
     * test
     * 
     * @throws BaseException
     *             ex
     */
    @Transactional
    public void createEntity() throws BaseException {
        DemoEntity entity = new DemoEntity();
        entity.setId(RandomUtil.generateId());
        demoEntityService.save(entity);
    }

}
