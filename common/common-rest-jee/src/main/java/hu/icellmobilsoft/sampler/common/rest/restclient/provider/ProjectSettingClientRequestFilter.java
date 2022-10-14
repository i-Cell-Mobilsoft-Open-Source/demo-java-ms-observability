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
package hu.icellmobilsoft.sampler.common.rest.restclient.provider;

import java.io.IOException;

import javax.annotation.Priority;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import hu.icellmobilsoft.coffee.module.mp.restclient.RestClientPriority;
import hu.icellmobilsoft.sampler.common.rest.header.ProjectHeader;
import hu.icellmobilsoft.sampler.dto.constant.IHttpHeaderConstants;

/**
 * Project Rest Client request setting filter
 * 
 * @author imre.scheffer
 * @since 0.1.0
 */
@Priority(value = RestClientPriority.REQUEST_SETTING - 100)
public class ProjectSettingClientRequestFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        Instance<ProjectHeader> instance = CDI.current().select(ProjectHeader.class);
        if (instance.isResolvable()) {
            ProjectHeader projectHeader = instance.get();
            if (!requestContext.getHeaders().containsKey(IHttpHeaderConstants.HEADER_SESSION_TOKEN)) {
                requestContext.getHeaders().add(IHttpHeaderConstants.HEADER_SESSION_TOKEN, projectHeader.getSessionToken());
            }
        }
    }
}
