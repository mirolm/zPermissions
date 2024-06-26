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

import static org.tyrannyofheaven.bukkit.zPermissions.util.ToHStringUtils.hasText;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Metadata for a command.
 *
 * @author zerothangel
 */
final class CommandMetaData {

    private final Object handler;

    private final Method method;

    private final List<MethodParameter> parameters;

    private final String[] permissions;

    private final boolean requireAll;

    private final boolean checkNegations;

    private final String description;

    private final boolean hasRest;

    private final String rest;

    private final String completer;

    private final List<OptionMetaData> flagOptions;

    private final List<OptionMetaData> positionalArguments;

    /**
     * Create a CommandMetaData with the given arguments.
     *
     * @param handler     the handler object
     * @param method      the associated method in the handler object
     * @param options     method parameters
     * @param permissions required permissions, if any
     * @param requireAll  true if all permissions are required
     */
    public CommandMetaData(Object handler, Method method, List<MethodParameter> options, String[] permissions, boolean requireAll, boolean checkNegations, String description, boolean hasRest, String rest, String completer) {
        if (handler == null)
            throw new IllegalArgumentException("handler cannot be null");
        if (method == null)
            throw new IllegalArgumentException("method cannot be null");

        if (options == null)
            options = Collections.emptyList();
        if (permissions == null)
            permissions = new String[0];
        if (!hasText(description))
            description = null;
        if (!hasText(rest))
            rest = null;
        if (!hasText(completer))
            completer = null;

        this.handler = handler;
        this.method = method;
        this.permissions = Arrays.copyOf(permissions, permissions.length);
        this.requireAll = requireAll;
        this.checkNegations = checkNegations;
        this.description = description;
        this.hasRest = hasRest;
        this.rest = rest;
        this.completer = completer;

        this.parameters = List.copyOf(options);

        List<OptionMetaData> flagOptions = new ArrayList<>();
        List<OptionMetaData> positionalArguments = new ArrayList<>();
        for (MethodParameter mp : this.parameters) {
            if (mp instanceof OptionMetaData) {
                OptionMetaData omd = (OptionMetaData) mp;
                if (omd.isArgument()) {
                    positionalArguments.add(omd);
                } else {
                    flagOptions.add(omd);
                }
            }
        }

        this.flagOptions = Collections.unmodifiableList(flagOptions);
        this.positionalArguments = Collections.unmodifiableList(positionalArguments);
    }

    /**
     * Return the method parameter metadata.
     *
     * @return list of MethodParameters
     */
    public List<MethodParameter> getParameters() {
        return parameters;
    }

    /**
     * Return metadata for any flags.
     *
     * @return list of OptionMetaData for any associated flags
     */
    public List<OptionMetaData> getFlagOptions() {
        return flagOptions;
    }

    /**
     * Return metadata for any positional arguments.
     *
     * @return list of OptionMetaData for any positional arguments
     */
    public List<OptionMetaData> getPositionalArguments() {
        return positionalArguments;
    }

    /**
     * Returns the handler object.
     *
     * @return the handler object
     */
    public Object getHandler() {
        return handler;
    }

    /**
     * Returns the handler method.
     *
     * @return the handler method.
     */
    public Method getMethod() {
        return method;
    }

    /**
     * Returns the permissions, if any.
     *
     * @return an array of 0 or more permission names. Will never be
     * <code>null</code>.
     */
    public String[] getPermissions() {
        return permissions;
    }

    /**
     * Returns whether or not all permissions are required.
     *
     * @return true if all permissions are required
     */
    public boolean isRequireAll() {
        return requireAll;
    }

    /**
     * Returns whether or not permission negations should be explicitly checked.
     *
     * @return true if permissions negations should be explicitly checked
     */
    public boolean isCheckNegations() {
        return checkNegations;
    }

    /**
     * Returns the associated command description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether or not this command has a varargs parameter.
     *
     * @return true if a varargs parameter is present
     */
    public boolean hasRest() {
        return hasRest;
    }

    /**
     * Returns the description of the varargs parameter.
     *
     * @return the description of the varargs parameter
     */
    public String getRest() {
        return rest;
    }

    /**
     * Returns the name of the registered TypeCompleter for each vararg word.
     *
     * @return the TypeCompleter to use for varargs
     */
    public String getCompleter() {
        return completer;
    }

}
