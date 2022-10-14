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
package hu.icellmobilsoft.sampler.sample.restservice.test.action;

import javax.inject.Inject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import hu.icellmobilsoft.coffee.dto.common.commonservice.FunctionCodeType;
import hu.icellmobilsoft.roaster.api.TestSuiteGroup;
import hu.icellmobilsoft.roaster.weldunit.BaseWeldUnitType;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleResponse;
import hu.icellmobilsoft.sampler.dto.sample.rest.post.SampleStatusEnumType;
import hu.icellmobilsoft.sampler.sample.restservice.action.RestSampleGetAction;

/**
 * Weld unit test for SampleAction
 *
 * @author Imre Scheffer
 * @since 0.1.0
 */
@DisplayName("Testing hu.icellmobilsoft.sampler.sample.restservice.action.RestSampleAction")
@Tag(TestSuiteGroup.WELD_UNIT)
class SampleActionIT extends BaseWeldUnitType {

    @Inject
    private RestSampleGetAction underTest;

    @Test
    void getSample() throws Exception {
        // given

        // when
        SampleResponse actual = underTest.sample();
        // then
        Assertions.assertEquals(SampleStatusEnumType.DONE, actual.getSample().getSampleStatus());
        Assertions.assertEquals(FunctionCodeType.OK, actual.getFuncCode());
    }

}
