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

import java.util.concurrent.ThreadLocalRandom;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.spi.CDI;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.tool.utils.string.RandomUtil;
import hu.icellmobilsoft.sampler.common.system.rest.action.BaseAction;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.model.sample.DemoEntity;
import hu.icellmobilsoft.sampler.sample.restservice.rest.ISampleRestRegisteredClient;

/**
 * 
 * 
 * @author czenczl
 *
 */
@Model
public class RestSlowDemoAction extends BaseAction {

	@Inject
	private DemoEntityService demoEntityService;

	@Inject
	@RestClient
	private ISampleRestRegisteredClient sampleRest;

	/**
	 * Dummy sample reponse
	 * 
	 * @return SampleResponse Sample response with random id
	 * @throws BaseException if error
	 */
	public SampleResponse slowDemo() throws BaseException {
		SampleResponse response = new SampleResponse();

		// business logic
		CDI.current().select(RestSlowDemoAction.class).get().slowBusinessLogic();

		// business logic
		CDI.current().select(RestSlowDemoAction.class).get().businessLogic();

		// entity save
		CDI.current().select(RestSlowDemoAction.class).get().createEntity();

		handleSuccessResultType(response);
		return response;
	}

	/**
	 * logic
	 */
	@Traced(operationName = "B_logic")
	public void businessLogic() {
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * demo logic
	 */
	@Traced(operationName = "B_slowLogic")
	public void slowBusinessLogic() {
		try {
			Thread.sleep(ThreadLocalRandom.current().nextInt(2, 10) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * test
	 * 
	 * @throws BaseException ex
	 */
	@Traced
	@Transactional
	public void createEntity() throws BaseException {
		DemoEntity entity = new DemoEntity();
		entity.setId(RandomUtil.generateId());
		demoEntityService.save(entity);
	}
}
