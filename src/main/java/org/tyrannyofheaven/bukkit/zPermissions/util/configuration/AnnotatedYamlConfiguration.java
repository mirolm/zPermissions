package org.tyrannyofheaven.bukkit.zPermissions.util.configuration;

import com.google.common.base.Joiner;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConstructor;
import org.bukkit.configuration.file.YamlRepresenter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Version of {@link YamlConfiguration} that supports comments with every root-level
 * property.
 *
 * @author zerothangel
 */
public class AnnotatedYamlConfiguration extends YamlConfiguration {

    private final Yaml yaml;

    // Map from property key to comment. Comment may have multiple lines that are newline-separated.
    private final Map<String, String> comments = new HashMap<>();

    public AnnotatedYamlConfiguration() {
        DumperOptions yamlOptions = new DumperOptions();
        yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        LoaderOptions yamlLoader = new LoaderOptions();
        yamlLoader.setMaxAliasesForCollections(Integer.MAX_VALUE);
        yamlLoader.setCodePointLimit(Integer.MAX_VALUE);

        YamlConstructor yamlConstructor = new YamlConstructor(yamlLoader);

        YamlRepresenter yamlRepresenter = new YamlRepresenter(yamlOptions);
        yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yaml = new Yaml(yamlConstructor, yamlRepresenter, yamlOptions, yamlLoader);
    }

    @NotNull
    @Override
    public String saveToString() {
        StringBuilder builder = new StringBuilder();

        // Iterate over each root-level property and dump
        for (Iterator<Map.Entry<String, Object>> i = getValues(false).entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, Object> entry = i.next();

            // Output comment, if present
            String comment = comments.get(entry.getKey());
            if (comment != null) {
                builder.append(buildComment(comment));
            }

            // Dump property
            builder.append(yaml.dump(Collections.singletonMap(entry.getKey(), entry.getValue())));

            // Output newline, if not the last
            if (i.hasNext())
                builder.append('\n');
        }

        String dump = builder.toString();

        if (dump.equals("{}\n")) {
            dump = "";
        }

        return dump;
    }

    /**
     * Format a multi-line property comment.
     *
     * @param comment the original comment string
     * @return the formatted comment string
     */
    protected String buildComment(String comment) {
        StringBuilder builder = new StringBuilder();
        for (String line : comment.split("\r?\n")) {
            builder.append("# ");
            builder.append(line);
            builder.append('\n');
        }
        return builder.toString();
    }

    /**
     * Returns a root-level comment.
     *
     * @param key the property key
     * @return the comment or <code>null</code>
     */
    public String getComment(String key) {
        return comments.get(key);
    }

    /**
     * Set a root-level comment.
     *
     * @param key     the property key
     * @param comment the comment. May be <code>null</code>, in which case the comment
     *                is removed.
     */
    public void setComment(String key, String... comment) {
        if (comment != null && comment.length > 0) {
            String s = Joiner.on('\n').join(comment);
            comments.put(key, s);
        } else {
            comments.remove(key);
        }
    }

    /**
     * Returns root-level comments.
     *
     * @return map of root-level comments
     */
    public Map<String, String> getComments() {
        return Collections.unmodifiableMap(comments);
    }

    /**
     * Set root-level comments from a map.
     *
     * @param comments comment map
     */
    public void setComments(Map<String, String> comments) {
        this.comments.clear();
        if (comments != null)
            this.comments.putAll(comments);
    }

}
