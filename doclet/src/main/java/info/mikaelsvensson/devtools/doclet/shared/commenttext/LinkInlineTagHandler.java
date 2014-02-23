/*
 * Copyright 2014 Mikael Svensson
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package info.mikaelsvensson.devtools.doclet.shared.commenttext;

import com.sun.javadoc.SeeTag;
import com.sun.javadoc.Tag;

//TODO Should not tag handlers return proper HTML code?
/**
 * @taghandler
 */
public class LinkInlineTagHandler implements InlineTagHandler {

    @Override
    public boolean handles(final Tag tag) {
        return tag instanceof SeeTag;
    }

    @Override
    public String toString(final Tag tag) {
        SeeTag seeTag = (SeeTag) tag;
        String refClass = seeTag.referencedClass() != null ? seeTag.referencedClass().qualifiedName() : null;
        String refMember = seeTag.referencedMember() != null ? seeTag.referencedMember().name() : null;
        if (refClass != null) {
            String result = "<link class=\"" + refClass + '"';
            if (refMember != null) {
                result += " member=\"" + refMember + '"';
            }
            result += " />";
            return result;
        } else {
            return "";
        }
    }

}
