package org.nomin.entity;

import java.util.List;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created: 02.05.2010 17:04:16
 */
public class Device {
    private String model;
    private String name;
    private List<Device> integrated;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public List<Device> getIntegrated() { return integrated; }

    public void setIntegrated(List<Device> integrated) { this.integrated = integrated; }
}
