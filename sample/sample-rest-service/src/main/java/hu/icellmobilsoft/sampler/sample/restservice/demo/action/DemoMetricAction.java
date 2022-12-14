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
package hu.icellmobilsoft.sampler.sample.restservice.demo.action;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.metrics.Metadata;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.MetricType;
import org.eclipse.microprofile.metrics.Tag;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.model.sample.DemoEntity;

/**
 * Sample action
 * 
 * @author czenczl
 * @since 0.1.0
 */
@Model
public class DemoMetricAction extends BaseAction {

	@Inject
	private DemoEntityService demoEntityService;

	@Inject
	private MetricRegistry metricRegistry;

	/**
	 * Dummy sample reponse
	 * 
	 * @return SampleResponse Sample response with random id
	 * @throws BaseException if error
	 */
	public SampleResponse demo() throws BaseException {
		SampleResponse response = new SampleResponse();

		// db save
		CDI.current().select(DemoMetricAction.class).get().createEntity();

		// add metric
		Tag keyTag = new Tag("DEMO", "DEMO");
		Metadata metadata = Metadata.builder().withName("DEMO").withDescription("DEMO").withType(MetricType.COUNTER)
				.build();
		metricRegistry.counter(metadata, keyTag).inc();

		handleSuccessResultType(response);
		return response;
	}

	/**
	 * test
	 * 
	 * @throws BaseException ex
	 */
	@Transactional
	public void createEntity() throws BaseException {
		DemoEntity entity = new DemoEntity();
		entity.setId(RandomUtil.generateId());
		demoEntityService.save(entity);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
