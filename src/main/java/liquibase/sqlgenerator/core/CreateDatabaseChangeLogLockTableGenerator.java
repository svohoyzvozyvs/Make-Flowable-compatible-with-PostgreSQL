//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.database.core.MSSQLDatabase;
import liquibase.datatype.DataTypeFactory;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.ColumnConstraint;
import liquibase.statement.NotNullConstraint;
import liquibase.statement.core.CreateDatabaseChangeLogLockTableStatement;
import liquibase.statement.core.CreateTableStatement;

public class CreateDatabaseChangeLogLockTableGenerator extends AbstractSqlGenerator<CreateDatabaseChangeLogLockTableStatement> {
    public CreateDatabaseChangeLogLockTableGenerator() {
    }

    public ValidationErrors validate(CreateDatabaseChangeLogLockTableStatement createDatabaseChangeLogLockTableStatement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new ValidationErrors();
    }

    public Sql[] generateSql(CreateDatabaseChangeLogLockTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        String charTypeName = this.getCharTypeName(database);
        String dateTimeTypeString = this.getDateTimeTypeString(database);
        CreateTableStatement createTableStatement = (new CreateTableStatement(database.getLiquibaseCatalogName(), database.getLiquibaseSchemaName(), database.getDatabaseChangeLogLockTableName())).setTablespace(database.getLiquibaseTablespaceName()).addPrimaryKeyColumn("ID", DataTypeFactory.getInstance().fromDescription("int", database), (Object)null, (String)null, (String)null, new ColumnConstraint[]{new NotNullConstraint()}).addColumn("LOCKED", DataTypeFactory.getInstance().fromDescription("boolean", database), (Object)null, (String)null, new ColumnConstraint[]{new NotNullConstraint()}).addColumn("LOCKGRANTED", DataTypeFactory.getInstance().fromDescription(dateTimeTypeString, database)).addColumn("LOCKEDBY", DataTypeFactory.getInstance().fromDescription(charTypeName + "(255)", database));
        ObjectQuotingStrategy currentStrategy = database.getObjectQuotingStrategy();
        database.setObjectQuotingStrategy(ObjectQuotingStrategy.LEGACY);

        Sql[] var8;
        try {
            var8 = SqlGeneratorFactory.getInstance().generateSql(createTableStatement, database);
        } finally {
            database.setObjectQuotingStrategy(currentStrategy);
        }

        return var8;
    }

    protected String getCharTypeName(Database database) {
        return database instanceof MSSQLDatabase && ((MSSQLDatabase)database).sendsStringParametersAsUnicode() ? "nvarchar" : "varchar";
    }

    protected String getDateTimeTypeString(Database database) {
        return "TIMESTAMP";
    }
}
