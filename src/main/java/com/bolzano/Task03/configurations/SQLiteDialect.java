package com.bolzano.Task03.configurations;

import java.sql.Types;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StringType;

/**
 * This class extends org.hibernate.dialect.Dialect class that defines APIs for configuring Hibernate,
 * and classes for building the Hibernate configuration-time metamodel.
 * A framework for defining database-specific SQL functions that are available via the dialect.
 *  @author Tharindu Madhusankha - 3198602
 */

public class SQLiteDialect extends Dialect {
  public SQLiteDialect() {
    registerColumnType(Types.BIT, "integer");
    registerColumnType(Types.TINYINT, "tinyint");
    registerColumnType(Types.SMALLINT, "smallint");
    registerColumnType(Types.INTEGER, "integer");
    registerColumnType(Types.BIGINT, "bigint");
    registerColumnType(Types.FLOAT, "float");
    registerColumnType(Types.REAL, "real");
    registerColumnType(Types.DOUBLE, "double");
    registerColumnType(Types.NUMERIC, "numeric");
    registerColumnType(Types.DECIMAL, "decimal");
    registerColumnType(Types.CHAR, "char");
    registerColumnType(Types.VARCHAR, "varchar");
    registerColumnType(Types.LONGVARCHAR, "longvarchar");
    registerColumnType(Types.DATE, "date");
    registerColumnType(Types.TIME, "time");
    registerColumnType(Types.TIMESTAMP, "timestamp");
    registerColumnType(Types.BINARY, "blob");
    registerColumnType(Types.VARBINARY, "blob");
    registerColumnType(Types.LONGVARBINARY, "blob");
    // registerColumnType(Types.NULL, "null");
    registerColumnType(Types.BLOB, "blob");
    registerColumnType(Types.CLOB, "clob");
    registerColumnType(Types.BOOLEAN, "integer");

    registerFunction( "concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", "") );
    registerFunction( "mod", new SQLFunctionTemplate( StringType.INSTANCE, "?1 % ?2" ) );
    registerFunction( "substr", new StandardSQLFunction("substr", StringType.INSTANCE) );
    registerFunction( "substring", new StandardSQLFunction( "substr", StringType.INSTANCE) );
  }

  /**
   *Database identity support.
   * return true;  As specify in NHibernate dialect.
   */

  public boolean supportsIdentityColumns() {
    return true;
  }


  /**
   * @return true As specify in NHibernate dialect.
   */

  public boolean hasDataTypeInIdentityColumn() {
    return false;
  }


  /**
   * @return "Integer primary key autoincrement".
   */

  public String getIdentityColumnString() {
    return "integer";
  }

  /**
   * For selecting the last inserted row id.
   */

  public String getIdentitySelectString() {
    return "select last_insert_rowid()";
  }

  /**
   * Limit/offset support.
   * @return
   */

  public boolean supportsLimit() {
    return true;
  }

  /**
   * Limit/offset support.
   */

  protected String getLimitString(String query, boolean hasOffset) {
    return new StringBuffer(query.length()+20).
            append(query).
            append(hasOffset ? " limit ? offset ?" : " limit ?").
            toString();
  }

  /**
   * Temporary table support.
   * @return true
   */

  public boolean supportsTemporaryTables() {
    return true;
  }

  /**
   * Creates a temporary table if not exists.
   */

  public String getCreateTemporaryTableString() {
    return "create temporary table if not exists";
  }

  /**
   * Drop the temporary table after using it.
   */

  public boolean dropTemporaryTableAfterUse() {
    return false;
  }

  public boolean supportsCurrentTimestampSelection() {
    return true;
  }

  /**
   * Current timestamp support.
   */

  public boolean isCurrentTimestampSelectStringCallable() {
    return false;
  }

  public String getCurrentTimestampSelectString() {
    return "select current_timestamp";
  }

  /**
   *  Supports union.
   */

  public boolean supportsUnionAll() {
    return true;
  }

  /**
   * Alter table.
   * @return false
   */

  public boolean hasAlterTable() {
    return false; // As specify in NHibernate dialect
  }

  /**
   * Drop the constraints.
   */

  public boolean dropConstraints() {
    return false;
  }

  /**
   * Add column strings
   * @return adds a column.
   */

  public String getAddColumnString() {
    return "add column";
  }

  public String getForUpdateString() {
    return "";
  }

  public boolean supportsOuterJoinForUpdate() {
    return false;
  }

  /**
   * @return Drops foreign key if not throws exception.
   */

  public String getDropForeignKeyString() {
    throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect.");
  }

  /**
   * @param constraintName : Constraint Name
   * @param foreignKey : The Foreign Key
   * @param referencedTable : Referenced Table
   * @param primaryKey : The Primary Key
   * @param referencesPrimaryKey : Primary Key references
   * @return Adds a foreign key if not throws exception.
   */

  public String getAddForeignKeyConstraintString(String constraintName,
                                                 String[] foreignKey, String referencedTable, String[] primaryKey,
                                                 boolean referencesPrimaryKey) {
    throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect.");
  }

  public String getAddPrimaryKeyConstraintString(String constraintName) {
    throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect.");
  }

  public boolean supportsIfExistsBeforeTableName() {
    return true;
  }

  public boolean supportsCascadeDelete() {
    return false;
  }
}