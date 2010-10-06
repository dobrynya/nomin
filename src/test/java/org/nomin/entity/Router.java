package org.nomin.entity;

import java.util.List;

/**
 * Represents a business entity.
 * @author Dmitry Dobrynin
 *         Created: 02.05.2010 17:05:20
 */
public class Router {
    private String vendor;
    private String model;
    private String software;
    private Integer portCount;
    private String supportedProtocol;
    private String portNames;
    private List<String> portModels;
    private String importDate;
    private List<Integer> frequencies;

    public List<Integer> getFrequencies() {
        return frequencies;
    }

    public void setFrequencies(List<Integer> frequencies) {
        this.frequencies = frequencies;
    }

    public List<String> getPortModels() {
        return portModels;
    }

    public void setPortModels(List<String> portModels) {
        this.portModels = portModels;
    }

    public String getImportDate() {
        return importDate;
    }

    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    public String getPortNames() {
        return portNames;
    }

    public void setPortNames(String portNames) {
        this.portNames = portNames;
    }

    public String getSupportedProtocol() {
        return supportedProtocol;
    }

    public void setSupportedProtocol(String supportedProtocol) {
        this.supportedProtocol = supportedProtocol;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPortCount() {
        return portCount;
    }

    public void setPortCount(Integer portCount) {
        this.portCount = portCount;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }
}
