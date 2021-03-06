/*
 * Copyright 2011 ZerothAngel <zerothangel@tyrannyofheaven.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tyrannyofheaven.bukkit.zPermissions.util.command;

/**
 * Delimiter strings used when generating usage strings. Can use colors.
 *
 * @author zerothangel
 */
public interface UsageOptions {

    /**
     * The usage preamble. Can be used to set starting color.
     *
     * @return the usage preamble
     */
    String getPreamble();

    /**
     * The usage postamble. Outputted at the end of the line. (After the
     * description, if one is present.)
     *
     * @return the usage postamble
     */
    String getPostamble();

    /**
     * Starting delimiter for flags.
     *
     * @return delimiter string
     */
    String getFlagStart();

    /**
     * Ending delimiter for flags.
     *
     * @return delimiter string
     */
    String getFlagEnd();

    /**
     * Starting delimiter for flag values.
     *
     * @return delimiter string
     */
    String getFlagValueStart();

    /**
     * Ending delimiter for flag values.
     *
     * @return delimiter string
     */
    String getFlagValueEnd();

    /**
     * Starting delimiter for positional parameters.
     *
     * @param optional true if optional parameter
     * @return delimiter string
     */
    String getParameterStart(boolean optional);

    /**
     * Ending delimiter for positional parameters.
     *
     * @param optional true if optional parameter
     * @return delimiter string
     */
    String getParameterEnd(boolean optional);

    /**
     * Delimiter between usage and its description.
     *
     * @return delimiter string
     */
    String getDescriptionDelimiter();

    /**
     * Starting delimiter for varargs parameter.
     *
     * @return delimiter string
     */
    String getVarargsStart();

    /**
     * Ending delimiter for varargs parameter.
     *
     * @return delimiter string
     */
    String getVarargsEnd();

}
