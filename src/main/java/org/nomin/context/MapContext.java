package org.nomin.context;

import java.util.Map;

/**
 * Provides resources from the specified Map instance.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 Time: 16:17:43
 */
public class MapContext implements Context {
    private final Map<String, Object> resources;

    public MapContext(Map<String, Object> resources) { this.resources = resources; }

    public Object getResource(String resource) { return resources.get(resource); }
}
