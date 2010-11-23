package org.nomin.context;

/**
 * Provides the operation to retrieve a resource from the configured context by the specified resource name.
 * @author Dmitry Dobrynin
 *         Date: 23.11.2010 Time: 16:14:09
 */
public interface Context {
    /**
     * Retrieve a resource by the specified resource identifier.
     * @param resource specifies resource identifier
     * @return resource if it exists or null otherwise
     */
    Object getResource(String resource);
}
