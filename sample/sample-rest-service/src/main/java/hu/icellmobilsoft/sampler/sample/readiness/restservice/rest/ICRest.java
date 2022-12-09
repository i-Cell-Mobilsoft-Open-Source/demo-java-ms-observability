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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import hu.icellmobilsoft.coffee.dto.exception.BaseException;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;

@Tag(name = ICRest.TAG_SAMPLE, description = "Minta folyamattal kapcsolatos REST operációk")
@Path("/rest/c")
public interface ICRest {

    String TAG_SAMPLE = "Sample";

    @Operation(summary = "Sample GET", //
            description = "* Sample.\n" //
                    + "* Minta")
    @Tag(ref = ICRest.TAG_SAMPLE)
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    public SampleResponse getc() throws BaseException;
    

    @Operation(summary = "Sample GET", //
            description = "* Sample.\n" //
                    + "* Minta")
    @Tag(ref = ICRest.TAG_SAMPLE)
    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML })
    @Path("/slow/{time}")
    public SampleResponse slow(@PathParam("time") Integer time) throws BaseException;

}
