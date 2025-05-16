package com.tallerwebi.components;

public class InputField {
    private String type;
    private String name;
    private String label;
    private Boolean readOnly;
    private String value;

    public InputField(String type, String name,String label,String value,Boolean readOnly) {
        this.type = type;
        this.name = name;
        this.label = label;
        this.readOnly = readOnly;
        this.value = value;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

