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
package hu.icellmobilsoft.sampler.api.jee.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.coffee.rest.validation.xml.annotation.ValidateXML;
import hu.icellmobilsoft.sampler.dto.constant.XsdConstants;
import hu.icellmobilsoft.sampler.dto.path.SamplePath;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleRequest;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;

/**
 * REST endpoint for sample-service
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Tag(name = ISampleRest.TAG_SAMPLE, description = "Minta folyamattal kapcsolatos REST operációk")
@Path(SamplePath.REST_SAMPLE_SERVICE)
public interface ISampleRest {

    /**
     * Sample tag for openapi
     */
    String TAG_SAMPLE = "Sample";

    /**
     * Sample http GET method
     * 
     * @return response
     * @throws BaseException
     *             if error
     */
    @Operation(summary = "Sample GET", //
            description = "* Sample.\n" //
                    + "* Minta")
    @Tag(ref = ISampleRest.TAG_SAMPLE)
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    public SampleResponse getSample() throws BaseException;

    /**
     * Sample http POST method
     * 
     * @param sampleRequest
     *            sample request entity
     * @return response
     * @throws BaseException
     *             if error
     */
    @Operation(summary = "Sample POST", //
            description = "* Sample Http POST.\n" //
                    + "* element 1")
    @Tag(ref = ISampleRest.TAG_SAMPLE)
    @POST
    @Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    @Consumes(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    public SampleResponse postSample(@ValidateXML(xsdPath = XsdConstants.SUPER_XSD_PATH) SampleRequest sampleRequest) throws BaseException;
}
