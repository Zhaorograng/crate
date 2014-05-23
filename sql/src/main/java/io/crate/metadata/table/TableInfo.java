/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.metadata.table;

import io.crate.PartitionName;
import io.crate.analyze.WhereClause;
import io.crate.metadata.ColumnIdent;
import io.crate.metadata.ReferenceInfo;
import io.crate.metadata.Routing;
import io.crate.metadata.TableIdent;
import io.crate.planner.RowGranularity;
import io.crate.planner.symbol.DynamicReference;
import org.apache.lucene.util.BytesRef;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public interface TableInfo extends Iterable<ReferenceInfo> {

    /**
     * returns information about a column with the given ident.
     * returns null if this table contains no such column.
     */
    @Nullable
    public ReferenceInfo getColumnInfo(ColumnIdent columnIdent);

    /**
     * returns the top level columns of this table with predictable order
     */
    public Collection<ReferenceInfo> columns();

    public List<ReferenceInfo> partitionedByColumns();

    public RowGranularity rowGranularity();

    public TableIdent ident();

    public Routing getRouting(WhereClause whereClause);

    public List<ColumnIdent> primaryKey();

    public int numberOfShards();

    public BytesRef numberOfReplicas();

    public boolean hasAutoGeneratedPrimaryKey();

    @Nullable
    public ColumnIdent clusteredBy();

    /**
     * @return true if this <code>TableInfo</code> is referenced by an alias name, false otherwise
     */
    public boolean isAlias();

    public String[] concreteIndices();

    public List<PartitionName> partitions();

    /**
     * column idents of columns this table is partitioned by.
     *
     * guaranteed to be in the same order as defined in CREATE TABLE statement
     */
    public List<ColumnIdent> partitionedBy();

    /**
     * returns <code>true</code> if this table is a partitioned table,
     * <code>false</code> otherwise
     *
     * if so, {@linkplain #partitions()} returns the concrete indices that make
     * up this virtual partitioned table
     */
    public boolean isPartitioned();

    /**
     * return a Dynamic Reference used when a column does not exist in the table mapping
     * but we need a reference
     *
     * @param ident
     * @return
     */
    DynamicReference getDynamic(ColumnIdent ident);

}
