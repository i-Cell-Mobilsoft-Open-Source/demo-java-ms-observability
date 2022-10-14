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
package hu.icellmobilsoft.sampler.dto.exception.enums;

import hu.icellmobilsoft.coffee.dto.error.IFaultType;

/**
 * Project specific faults
 * 
 * @author imre.scheffer
 * @since 0.1.0
 *
 */
public enum FaultType implements IFaultType<FaultType> {
    /**
     * Invalid response - xsd validation failed
     */
    SAMPLE_INVALID_RESPONSE,

    /**
     * HTTP 400 Response.Status.BAD_REQUEST<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.BadRequestException
     */
    REST_BAD_REQUEST,
    /**
     * HTTP 401 Response.Status.UNAUTHORIZED<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.NotAuthorizedException
     */
    REST_UNAUTHORIZED,
    /**
     * HTTP 403 Response.Status.FORBIDDEN<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.ForbiddenException
     */
    REST_FORBIDDEN,
    /**
     * HTTP 404 Response.Status.NOT_FOUND<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.NotFoundException
     */
    REST_NOT_FOUND,
    /**
     * HTTP 405 Response.Status.METHOD_NOT_ALLOWED<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.NotAllowedException
     */
    REST_METHOD_NOT_ALLOWED,
    /**
     * HTTP 406 Response.Status.NOT_ACCEPTABLE<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.NotAcceptableException
     */
    REST_NOT_ACCEPTABLE,
    /**
     * HTTP 415 Response.Status.UNSUPPORTED_MEDIA_TYPE<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.NotSupportedException
     */
    REST_UNSUPPORTED_MEDIA_TYPE,
    /**
     * HTTP 500 Response.Status.INTERNAL_SERVER_ERROR<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.InternalServerErrorException
     */
    REST_INTERNAL_SERVER_ERROR,
    /**
     * HTTP 503 Response.Status.SERVICE_UNAVAILABLE<br>
     * TechnicalFault nyelvesített lekezelése<br>
     * Eredeti forrása a javax.ws.rs.ServiceUnavailableException
     */
    REST_SERVICE_UNAVAILABLE,
}
