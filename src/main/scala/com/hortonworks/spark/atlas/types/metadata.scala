/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hortonworks.spark.atlas.types

import com.google.common.collect.{ImmutableMap, ImmutableSet}
import org.apache.atlas.AtlasClient
import org.apache.atlas.`type`.AtlasBuiltInTypes.{AtlasBooleanType, AtlasLongType, AtlasStringType}
import org.apache.atlas.`type`.{AtlasArrayType, AtlasMapType, AtlasTypeUtil}
import org.apache.atlas.model.typedef.AtlasStructDef.AtlasConstraintDef

object metadata {
  val METADATA_VERSION = "1.0"
  val DB_TYPE_STRING = "spark_db"
  val STORAGEDESC_TYPE_STRING = "spark_storagedesc"
  val COLUMN_TYPE_STRING = "spark_column"
  val TABLE_TYPE_STRING = "spark_table"
  val PROCESS_TYPE_STRING = "spark_process"

  // ========= DB type =========
  val DB_TYPE = AtlasTypeUtil.createClassTypeDef(
    DB_TYPE_STRING,
    "",
    METADATA_VERSION,
    ImmutableSet.of("DataSet"),
    AtlasTypeUtil.createUniqueRequiredAttrDef(
      AtlasClient.REFERENCEABLE_ATTRIBUTE_NAME, new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("description", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("locationUri", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef(
      "properties", new AtlasMapType(new AtlasStringType, new AtlasStringType)))

  // ========= Storage description type =========
  val STORAGEDESC_TYPE = AtlasTypeUtil.createClassTypeDef(
    STORAGEDESC_TYPE_STRING,
    "",
    METADATA_VERSION,
    ImmutableSet.of("Referenceable"),
    AtlasTypeUtil.createUniqueRequiredAttrDef(
      AtlasClient.REFERENCEABLE_ATTRIBUTE_NAME, new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("locationUri", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("inputFormat", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("outputFormat", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("serde", new AtlasStringType),
    AtlasTypeUtil.createRequiredAttrDef("compressed", new AtlasBooleanType),
    AtlasTypeUtil.createOptionalAttrDef(
      "properties", new AtlasMapType(new AtlasStringType, new AtlasStringType)),
    AtlasTypeUtil.createOptionalAttrDefWithConstraint(
      "table",
      TABLE_TYPE_STRING,
      AtlasConstraintDef.CONSTRAINT_TYPE_INVERSE_REF,
      ImmutableMap.of(AtlasConstraintDef.CONSTRAINT_PARAM_ATTRIBUTE, "storage")))

  // ========= Column type =========
  val COLUMN_TYPE = AtlasTypeUtil.createClassTypeDef(
    COLUMN_TYPE_STRING,
    "",
    METADATA_VERSION,
    ImmutableSet.of("DataSet"),
    AtlasTypeUtil.createUniqueRequiredAttrDef(
      AtlasClient.REFERENCEABLE_ATTRIBUTE_NAME, new AtlasStringType),
    AtlasTypeUtil.createRequiredAttrDef("type", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("nullable", new AtlasBooleanType),
    AtlasTypeUtil.createOptionalAttrDef("metadata", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDefWithConstraint(
      "table",
      TABLE_TYPE_STRING,
      AtlasConstraintDef.CONSTRAINT_TYPE_INVERSE_REF,
      ImmutableMap.of(AtlasConstraintDef.CONSTRAINT_PARAM_ATTRIBUTE, "schema")))

  // ========= Table type =========
  val TABLE_TYPE = AtlasTypeUtil.createClassTypeDef(
    TABLE_TYPE_STRING,
    "",
    METADATA_VERSION,
    ImmutableSet.of("DataSet"),
    AtlasTypeUtil.createUniqueRequiredAttrDef(
      AtlasClient.REFERENCEABLE_ATTRIBUTE_NAME, new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("database", DB_TYPE_STRING),
    AtlasTypeUtil.createOptionalAttrDef("tableType", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDefWithConstraint(
      "storage", STORAGEDESC_TYPE_STRING, AtlasConstraintDef.CONSTRAINT_TYPE_OWNED_REF, null),
    AtlasTypeUtil.createOptionalAttrDefWithConstraint(
      "schema",
      "array<spark_column>",
      AtlasConstraintDef.CONSTRAINT_TYPE_OWNED_REF, null),
    AtlasTypeUtil.createOptionalAttrDef("provider", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef(
      "partitionColumnNames", new AtlasArrayType(new AtlasStringType)),
    AtlasTypeUtil.createOptionalAttrDef(
      "bucketSpec", new AtlasMapType(new AtlasStringType, new AtlasStringType)),
    AtlasTypeUtil.createOptionalAttrDef("owner", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("createTime", new AtlasLongType),
    AtlasTypeUtil.createOptionalAttrDef("lastAccessTime", new AtlasLongType),
    AtlasTypeUtil.createOptionalAttrDef(
      "properties", new AtlasMapType(new AtlasStringType, new AtlasStringType)),
    AtlasTypeUtil.createOptionalAttrDef("viewText", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("comment", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef(
      "unsupportedFeatures", new AtlasArrayType(new AtlasStringType)))

  // ========= Process type =========
  val PROCESS_TYPE = AtlasTypeUtil.createClassTypeDef(
    PROCESS_TYPE_STRING,
    "",
    METADATA_VERSION,
    ImmutableSet.of("Process"),
    AtlasTypeUtil.createUniqueRequiredAttrDef(
      AtlasClient.REFERENCEABLE_ATTRIBUTE_NAME, new AtlasStringType),
    AtlasTypeUtil.createRequiredAttrDef("executionId", new AtlasLongType),
    AtlasTypeUtil.createOptionalAttrDef("startTime", new AtlasLongType),
    AtlasTypeUtil.createOptionalAttrDef("endTime", new AtlasLongType),
    AtlasTypeUtil.createOptionalAttrDef("description", new AtlasStringType),
    AtlasTypeUtil.createOptionalAttrDef("details", new AtlasStringType),
    AtlasTypeUtil.createRequiredAttrDef("physicalPlanDescription", new AtlasStringType))
}

