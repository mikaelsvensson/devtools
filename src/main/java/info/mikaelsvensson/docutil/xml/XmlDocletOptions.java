/*
 * Copyright (c) 2012, Mikael Svensson
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the copyright holder nor the names of the
 *       contributors of this software may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL MIKAEL SVENSSON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package info.mikaelsvensson.docutil.xml;


import info.mikaelsvensson.docutil.shared.propertyset.PropertySet;
import info.mikaelsvensson.docutil.shared.propertyset.PropertySetException;

import java.util.HashMap;
import java.util.Map;

public class XmlDocletOptions {
    public static final String ACTION = "action";
    public static final String PROPERTIES_FILE = "propertiesfile";

    private Map<String, XmlDocletAction> actions = new HashMap<String, XmlDocletAction>();

    protected PropertySet propertySet;

    public Map<String, XmlDocletAction> getActions() {
        return actions;
    }

    public XmlDocletOptions(PropertySet propertySet) throws PropertySetException {
//        System.out.println(propertySet);
        String propertiesfile = propertySet.getProperty(PROPERTIES_FILE);
        if (null != propertiesfile) {
            propertySet.loadFromFile(propertiesfile);

        }
        for (Map.Entry<String, PropertySet> entry : propertySet.getCollection(ACTION).entrySet()) {
            actions.put(entry.getKey(), new XmlDocletAction(entry.getValue()));
        }
    }

    public static int optionLength(String option) {
        if (option.startsWith("-" + ACTION)) {
            return 2;
        }
        return 0;
    }

}
