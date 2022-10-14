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
package hu.icellmobilsoft.sampler.model.sample;

import javax.enterprise.inject.Vetoed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Vetoed
@Entity
@Table(name = "DEMO")
public class DemoEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Primary key of the entity
	 */
	@Id
	@Column(name = "X__ID", length = 30)
	private String id;

	/**
	 * Getter for the field {@code id}.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter for the field {@code id}.
	 *
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Value of sample input data value
	 */
	@Column(name = "INPUT_VALUE", length = 30)
	@Size(max = 30)
	private String inputValue;

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

}
