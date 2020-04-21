package com.breakoutms.lfs.server.core;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;
import org.hibernate.tool.schema.spi.SchemaFilterProvider;

public class SchemaFilterProviderImpl implements SchemaFilterProvider {

	static final String TABLE_TO_EXCLUDE_FROM_DLL = "branch";
	
    @Override
    public SchemaFilter getCreateFilter() {
        return MySchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getDropFilter() {
        return MySchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getMigrateFilter() {
        return MySchemaFilter.INSTANCE;
    }

    @Override
    public SchemaFilter getValidateFilter() {
        return MySchemaFilter.INSTANCE;
    }
}

class MySchemaFilter implements SchemaFilter {

    public static final MySchemaFilter INSTANCE = new MySchemaFilter();

    @Override
    public boolean includeNamespace(Namespace namespace) {
        return true;
    }

    @Override
    public boolean includeTable(Table table) {
        if (table.getName().equals(SchemaFilterProviderImpl.TABLE_TO_EXCLUDE_FROM_DLL)){
            return false;
        }
        return true;
    }

    @Override
    public boolean includeSequence(Sequence sequence) {
        return true;
    }
}