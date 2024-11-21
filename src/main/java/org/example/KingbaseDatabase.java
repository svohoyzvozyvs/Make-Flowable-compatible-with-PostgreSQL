//package org.example;
//
//import liquibase.database.DatabaseConnection;
//import liquibase.database.core.OracleDatabase;
//import liquibase.exception.DatabaseException;
//
//public class KingbaseDatabase extends OracleDatabase {
//    @Override
//    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
//        return "KingbaseES".equals(conn.getDatabaseProductName());
//    }
//
//    @Override
//    public String getShortName() {
//        return "kingbase";
//    }
//
//    @Override
//    protected String getDefaultDatabaseProductName() {
//        return "KingbaseES";
//    }
//}