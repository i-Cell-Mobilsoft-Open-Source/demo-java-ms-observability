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
package hu.icellmobilsoft.sampler.sample.restservice.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.api.jee.rest.ISampleRest;
import hu.icellmobilsoft.sampler.dto.path.SamplePath;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;

/**
 * REST Registered rest client
 * 
 * @author czenczl
 * @since 0.1.0
 */
@RegisterRestClient
@Path(SamplePath.REST_SAMPLE_SERVICE)
public interface ISampleRestRegisteredClient extends ISampleRest {

	/**
	 * demo
	 * 
	 * @return test
	 * @throws BaseException error
	 */
	@Operation(summary = "Sample GET", //
			description = "* Sample.\n" //
					+ "* Minta")
	@Tag(ref = ISampleRest.TAG_SAMPLE)
	@Path("/demometrics")
	@GET
	@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	public SampleResponse getDemoMetrics() throws BaseException;

	/**
	 * demo
	 * 
	 * @return test
	 * @throws BaseException error
	 */
	@Operation(summary = "Sample GET", //
			description = "* Sample.\n" //
					+ "* Minta")
	@Tag(ref = ISampleRest.TAG_SAMPLE)
	@Path("/slowdemo")
	@GET
	@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
	public SampleResponse getSlowDemo() throws BaseException;

}
