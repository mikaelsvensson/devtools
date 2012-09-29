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

package info.mikaelsvensson.doctools;

import javax.mail.internet.InternetAddress;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Pojo(name = "ca")
public class Contact {
    @Required
    private InternetAddress emailAddress;

    @Required
    private String name;

    @NumericRange(min = 1900)
    private int birthYear;

    @Text(condition = "^[0-9\\s]+$", conditionType = TextConditionType.REGEXP)
    @Required
    private String zipCode;

    @Texts({@Text(condition = "sv_SE", conditionType = TextConditionType.EQUALS), @Text(condition = "en", conditionType = TextConditionType.STARTS_WITH)})
    private String preferredLocale;
}

@Target(ElementType.TYPE)
@interface Pojo {
    String name();
}

@Target(ElementType.FIELD)
@interface Text {
    String condition();

    TextConditionType conditionType() default TextConditionType.EQUALS;
}

@Target(ElementType.FIELD)
@interface Texts {
    Text[] value();
}

@Target(ElementType.FIELD)
@interface NumericRange {
    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;
}

@interface Required {

}

enum TextConditionType {
    EQUALS,
    STARTS_WITH,
    REGEXP
}
