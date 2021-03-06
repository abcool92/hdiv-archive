/**
 * Copyright 2005-2010 hdiv.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hdiv.web.servlet.tags.form;

import java.util.Collection;

import javax.servlet.jsp.JspException;

import org.hdiv.dataComposer.IDataComposer;
import org.hdiv.web.util.TagUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag;
import org.springframework.web.servlet.tags.form.TagWriter;

/**
 * Databinding-aware JSP tag for rendering an HTML '<code>input</code>'
 * element with a '<code>type</code>' of '<code>checkbox</code>'.
 *
 * <p>May be used in one of three different approaches depending on the
 * type of the {@link #getValue bound value}.
 *
 * <h3>Approach One</h3>
 * When the bound value is of type {@link Boolean} then the '<code>input(checkbox)</code>'
 * is marked as 'checked' if the bound value is <code>true</code>. The '<code>value</code>'
 * attribute corresponds to the resolved value of the {@link #setValue(Object) value} property.
 * <h3>Approach Two</h3>
 * When the bound value is of type {@link Collection} then the '<code>input(checkbox)</code>'
 * is marked as 'checked' if the configured {@link #setValue(Object) value} is present in
 * the bound {@link Collection}.
 * <h3>Approach Three</h3>
 * For any other bound value type, the '<code>input(checkbox)</code>' is marked as 'checked'
 * if the the configured {@link #setValue(Object) value} is equal to the bound value.
 *
 * @author Gorka Vicente
 * @since HDIV 2.0.6
 * @see org.springframework.web.servlet.tags.form.CheckboxTag
 */
public class CheckboxTagHDIV extends AbstractSingleCheckedElementTag {
	
	private IDataComposer dataComposer;
	
	
	/**
	 * Writes the '<code>input(checkbox)</code>' to the supplied {@link TagWriter},
	 * marking it as 'checked' if appropriate.
	 */
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
				
		dataComposer = (IDataComposer) this.pageContext.getRequest().getAttribute(TagUtils.DATA_COMPOSER);		
		super.writeTagContent(tagWriter);
		
		if (!isDisabled()) {
			
			String hdivValue = dataComposer.compose(WebDataBinder.DEFAULT_FIELD_MARKER_PREFIX + getName(), "on", false);

			// Write out the 'field was present' marker.
			tagWriter.startTag("input");
			tagWriter.writeAttribute("type", "hidden");
			tagWriter.writeAttribute("name", WebDataBinder.DEFAULT_FIELD_MARKER_PREFIX + getName());
			tagWriter.writeAttribute("value", hdivValue);
			tagWriter.endTag();
		}

		return SKIP_BODY;
	}	
	
	@Override
	protected void writeTagDetails(TagWriter tagWriter) throws JspException {
		tagWriter.writeAttribute("type", "checkbox");

		Object boundValue = getBoundValue();
		Class valueType = getBindStatus().getValueType();

		if (Boolean.class.equals(valueType) || boolean.class.equals(valueType)) {
			// the concrete type may not be a Boolean - can be String
			if (boundValue instanceof String) {
				boundValue = Boolean.valueOf((String) boundValue);
			}
			Boolean booleanValue = (boundValue != null ? (Boolean) boundValue : Boolean.FALSE);
			
			dataComposer.compose(getName(), "true", true);			
			renderFromBoolean(booleanValue, tagWriter);
		}

		else {
			Object value = getValue();
			if (value == null) {
				throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
			}
			Object resolvedValue = (value instanceof String ? evaluate("value", (String) value) : value);
			
			dataComposer.compose(getName(), getDisplayString(resolvedValue), true);
			renderFromValue(resolvedValue, tagWriter);
		}
	}

}
