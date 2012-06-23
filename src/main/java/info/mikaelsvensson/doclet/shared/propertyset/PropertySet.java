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

package info.mikaelsvensson.doclet.shared.propertyset;

import java.util.HashMap;
import java.util.Map;

public class PropertySet {
    public static final char SEPARATOR = '.';
    private Map<String, PropertySet> childSets = new HashMap<String, PropertySet>();
    private Map<String, String> properties = new HashMap<String, String>();

    public void setProperty(String key, String value) throws PropertySetException {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            if (childSets.containsKey(key)) {
                throw new PropertySetException(key + " cannot be set to a single value since it is already a property set, " + childSets.get(key));
            }
            properties.put(key, value);
        } else {
            String childKey = key.substring(0, pos);
            if (!childSets.containsKey(childKey)) {
                if (properties.containsKey(childKey)) {
                    throw new PropertySetException(childKey + " cannot be the name of a collection since it is already a single value, " + properties.get(childKey));
                }
                childSets.put(childKey, new PropertySet());
            }
            PropertySet childSet = childSets.get(childKey);

            childSet.setProperty(key.substring(pos + 1), value);
        }
    }

    public String getProperty(String key) {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            return properties.get(key);
        } else {
            PropertySet childSet = childSets.get(key.substring(0, pos));
            if (childSet != null) {
                return childSet.getProperty(key.substring(pos + 1));
            } else {
                return null;
            }
        }
    }

    public Map<String, PropertySet> getCollection(String key) {
        int pos = key.indexOf(SEPARATOR);
        if (pos == -1) {
            return childSets.get(key).childSets;
        } else {
            PropertySet childSet = childSets.get(key.substring(0, pos));

            return childSet.getCollection(key.substring(pos + 1));
        }
    }

    @Override
    public String toString() {
        return "{" +
                "properties=" + properties + ", " +
                "collections=" + childSets +
                '}';
    }
}
