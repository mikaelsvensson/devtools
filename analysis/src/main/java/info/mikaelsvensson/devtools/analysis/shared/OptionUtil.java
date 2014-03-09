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

package info.mikaelsvensson.devtools.analysis.shared;

import org.apache.commons.cli.Option;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OptionUtil {
    private static OptionUtil ourInstance = new OptionUtil();

    public static OptionUtil getInstance() {
        return ourInstance;
    }

    private OptionUtil() {
    }

    public static final int UNLIMITED = Option.UNLIMITED_VALUES;
    public static final int OPTIONAL = -3;

    public void populateOptionFields(AbstractAnalyzer owner) throws IllegalAccessException {
        Class<?> cls = owner.getClass().getSuperclass();
        for (Field field : cls.getDeclaredFields()) {
            CliOptionConfig config = field.getAnnotation(CliOptionConfig.class);
            if (config != null) {
                Option option = new Option(config.name(), config.description());
                if (config.longName().length() > 0) {
                    option.setLongOpt(config.longName());
                }
                if (config.numArgs() == OPTIONAL) {
                    option.setOptionalArg(true);
                } else {
                    option.setArgs(config.numArgs());
                }
                option.setRequired(config.required());
                option.setValueSeparator(config.separator());
                field.set(owner, option);
            }
        }
    }

    public List<Option> getOptions(AbstractAnalyzer owner) throws IllegalAccessException {
        List<Option> options = new ArrayList<Option>();
        Class<?> cls = owner.getClass();
        do {
            CliOptions cliOptions = cls.getAnnotation(CliOptions.class);
            if (cliOptions != null) {
                for (CliOptionConfig config : cliOptions.opts()) {

                    if (config != null) {
                        Option option = new Option(config.name(), config.description());
                        if (config.longName().length() > 0) {
                            option.setLongOpt(config.longName());
                        }
                        if (config.numArgs() == OPTIONAL) {
                            option.setOptionalArg(true);
                        } else {
                            option.setArgs(config.numArgs());
                        }
                        option.setRequired(config.required());
                        option.setValueSeparator(config.separator());
                        options.add(option);
                    }
                }
            }
        } while ((cls = cls.getSuperclass()) != null);
        return options;
    }

    public String getCliHelp(AbstractAnalyzer owner) {
        CliHelp cliHelp = owner.getClass().getAnnotation(CliHelp.class);
        if (cliHelp != null) {
            return cliHelp.text();
        }
        return null;
    }
}
