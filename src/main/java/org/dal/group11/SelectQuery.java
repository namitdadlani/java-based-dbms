package org.dal.group11;

import java.util.List;

public class SelectQuery {
    public String selectString;
    public String tableName;
    public String attributes;
    public String conditions;

    public String getSelectString() {
        return selectString;
    }

    public void setSelectString(String selectString) {
        this.selectString = selectString;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "SelectQuery{" +
                "selectString='" + selectString + '\'' +
                ", tableName='" + tableName + '\'' +
                ", attributes='" + attributes + '\'' +
                ", conditions='" + conditions + '\'' +
                '}';
    }
}
