package org.dal.group11;

import java.util.List;

public class DeleteQuery {
    public String deleteString;
    public String tableName;
    public String conditions;

    public String getDeleteString() {
        return deleteString;
    }

    public void setDeleteString(String deleteString) {
        this.deleteString = deleteString;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "DeleteQuery{" +
                "deleteString='" + deleteString + '\'' +
                ", tableName='" + tableName + '\'' +
                ", conditions='" + conditions + '\'' +
                '}';
    }
}
