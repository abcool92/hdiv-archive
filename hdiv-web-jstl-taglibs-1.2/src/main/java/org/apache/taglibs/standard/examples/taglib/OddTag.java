/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package org.apache.taglibs.standard.examples.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>Tag handler for &lt;odd&gt;
 *
 * @author Pierre Delisle
 * @version $Revision: 602107 $ $Date: 2007-12-07 15:27:35 +0100 (vie, 07 dic 2007) $
 */
public class OddTag extends TagSupport {
    
    //*********************************************************************
    // TagSupport methods
    
    public int doStartTag() throws JspException {
        LoopTag iteratorTag = (LoopTag)findAncestorWithClass(
                this, LoopTag.class);
        if (iteratorTag == null) {
            throw new JspTagException("<odd> must be nested within a LoopTag");
        }
        
        int count = iteratorTag.getLoopStatus().getCount();
        System.out.println("count: " + count);
                System.out.println("count odd/even: " + (count % 2));
        return (count % 2 == 1) ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }
}


