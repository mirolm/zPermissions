/*
 * Copyright 2013 ZerothAngel <zerothangel@tyrannyofheaven.org>
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
package org.tyrannyofheaven.bukkit.zPermissions.model;

public class EntityMetadata {

    private Long id;

    private PermissionEntity entity;

    private String name;

    private String stringValue;

    private Long integerValue;

    private Double realValue;

    private Boolean booleanValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionEntity getEntity() {
        return entity;
    }

    public void setEntity(PermissionEntity entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Long getIntegerValue() {
        return integerValue;
    }

    public void setIntegerValue(Long integerValue) {
        this.integerValue = integerValue;
    }

    public Double getRealValue() {
        return realValue;
    }

    public void setRealValue(Double realValue) {
        this.realValue = realValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Object getValue() {
        if (getStringValue() != null)
            return getStringValue();
        else if (getIntegerValue() != null)
            return getIntegerValue();
        else if (getRealValue() != null)
            return getRealValue();
        else if (getBooleanValue() != null)
            return getBooleanValue();
        else
            return null;
    }

    public void setValue(Object value) {
        setStringValue(null);
        setIntegerValue(null);
        setRealValue(null);
        setBooleanValue(null);

        if (value instanceof String) {
            setStringValue((String) value);
        } else if (value instanceof Integer) {
            setIntegerValue(((Number) value).longValue());
        } else if (value instanceof Long) {
            setIntegerValue((Long) value);
        } else if (value instanceof Float) {
            setRealValue(((Number) value).doubleValue());
        } else if (value instanceof Double) {
            setRealValue((Double) value);
        } else if (value instanceof Boolean) {
            setBooleanValue((Boolean) value);
        } else
            throw new IllegalArgumentException("Invalid metadata value");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof EntityMetadata)) return false;
        EntityMetadata o = (EntityMetadata) obj;
        return getEntity().equals(o.getEntity()) &&
                getName().equals(o.getName());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + getEntity().hashCode();
        result = 37 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("EntityMetadata[%s]", getName());
    }

}
