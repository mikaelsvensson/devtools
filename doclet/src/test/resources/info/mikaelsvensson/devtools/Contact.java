package info.mikaelsvensson.devtools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Pojo(name = "ca")
public class Contact {
    @Required
    private String emailAddress;

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
