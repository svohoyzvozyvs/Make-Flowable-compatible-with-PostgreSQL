//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package liquibase.database;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import liquibase.CatalogAndSchema;
import liquibase.GlobalConfiguration;
import liquibase.Scope;
import liquibase.CatalogAndSchema.CatalogAndSchemaCase;
import liquibase.change.Change;
import liquibase.change.core.DropTableChange;
import liquibase.changelog.ChangeLogHistoryServiceFactory;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.changelog.RanChangeSet;
import liquibase.changelog.StandardChangeLogHistoryService;
import liquibase.changelog.ChangeSet.ExecType;
import liquibase.changelog.ChangeSet.RunStatus;
import liquibase.configuration.ConfiguredValue;
import liquibase.database.core.OracleDatabase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.core.SQLiteDatabase;
import liquibase.database.core.SybaseASADatabase;
import liquibase.database.core.SybaseDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.compare.DatabaseObjectComparatorFactory;
import liquibase.diff.compare.CompareControl.SchemaComparison;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.exception.DatabaseHistoryException;
import liquibase.exception.DateParseException;
import liquibase.exception.LiquibaseException;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.exception.ValidationErrors;
import liquibase.executor.ExecutorService;
import liquibase.lockservice.LockServiceFactory;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.EmptyDatabaseSnapshot;
import liquibase.snapshot.SnapshotControl;
import liquibase.snapshot.SnapshotGeneratorFactory;
import liquibase.sql.Sql;
import liquibase.sql.visitor.SqlVisitor;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.DatabaseFunction;
import liquibase.statement.SequenceCurrentValueFunction;
import liquibase.statement.SequenceNextValueFunction;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.GetViewDefinitionStatement;
import liquibase.statement.core.RawCallStatement;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Catalog;
import liquibase.structure.core.Column;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Schema;
import liquibase.structure.core.Sequence;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import liquibase.structure.core.View;
import liquibase.util.ISODateFormat;
import liquibase.util.NowAndTodayUtil;
import liquibase.util.StreamUtil;
import liquibase.util.StringUtil;

public abstract class AbstractJdbcDatabase implements Database {
    private static final Pattern startsWithNumberPattern = Pattern.compile("^[0-9].*");
    private static final int FETCH_SIZE = 1000;
    private static final int DEFAULT_MAX_TIMESTAMP_FRACTIONAL_DIGITS = 9;
    private static Pattern CREATE_VIEW_AS_PATTERN = Pattern.compile("^CREATE\\s+.*?VIEW\\s+.*?AS\\s+", 34);
    private final Set<String> reservedWords = new HashSet();
    protected String defaultCatalogName;
    protected String defaultSchemaName;
    protected String currentDateTimeFunction;
    protected String sequenceNextValueFunction;
    protected String sequenceCurrentValueFunction;
    protected List<DatabaseFunction> dateFunctions = new ArrayList();
    protected List<String> unmodifiableDataTypes = new ArrayList();
    protected BigInteger defaultAutoIncrementStartWith;
    protected BigInteger defaultAutoIncrementBy;
    protected Boolean unquotedObjectsAreUppercased;
    protected ObjectQuotingStrategy quotingStrategy;
    protected Boolean caseSensitive;
    private String databaseChangeLogTableName;
    private String databaseChangeLogLockTableName;
    private String liquibaseTablespaceName;
    private String liquibaseSchemaName;
    private String liquibaseCatalogName;
    private Boolean previousAutoCommit;
    private boolean canCacheLiquibaseTableInfo;
    private DatabaseConnection connection;
    private boolean outputDefaultSchema;
    private boolean outputDefaultCatalog;
    private boolean defaultCatalogSet;
    private Map<String, Object> attributes;

    public AbstractJdbcDatabase() {
        this.defaultAutoIncrementStartWith = BigInteger.ONE;
        this.defaultAutoIncrementBy = BigInteger.ONE;
        this.quotingStrategy = ObjectQuotingStrategy.LEGACY;
        this.canCacheLiquibaseTableInfo = false;
        this.outputDefaultSchema = true;
        this.outputDefaultCatalog = true;
        this.attributes = new HashMap();
    }

    public String getName() {
        return this.toString();
    }

    public boolean requiresPassword() {
        return true;
    }

    public boolean requiresUsername() {
        return true;
    }

    public DatabaseObject[] getContainingObjects() {
        return null;
    }

    public DatabaseConnection getConnection() {
        return this.connection;
    }

    public void setConnection(DatabaseConnection conn) {
        Scope.getCurrentScope().getLog(this.getClass()).fine("Connected to " + conn.getConnectionUserName() + "@" + conn.getURL());
        this.connection = conn;

        try {
            boolean autoCommit = conn.getAutoCommit();
            if (autoCommit == this.getAutoCommitMode()) {
                Scope.getCurrentScope().getLog(this.getClass()).fine("Not adjusting the auto commit mode; it is already " + autoCommit);
            } else {
                this.previousAutoCommit = autoCommit;
                Scope.getCurrentScope().getLog(this.getClass()).fine("Setting auto commit to " + this.getAutoCommitMode() + " from " + autoCommit);
                this.connection.setAutoCommit(this.getAutoCommitMode());
            }
        } catch (DatabaseException var3) {
            Scope.getCurrentScope().getLog(this.getClass()).warning("Cannot set auto commit to " + this.getAutoCommitMode() + " on connection");
        }

        this.connection.attached(this);
    }

    public boolean getAutoCommitMode() {
        return !this.supportsDDLInTransaction();
    }

    public final void addReservedWords(Collection<String> words) {
        this.reservedWords.addAll(words);
    }

    public boolean supportsDDLInTransaction() {
        return true;
    }

    public String getDatabaseProductName() {
        if (this.connection == null) {
            return this.getDefaultDatabaseProductName();
        } else {
            try {
                return this.connection.getDatabaseProductName();
            } catch (DatabaseException var2) {
                throw new RuntimeException("Cannot get database name");
            }
        }
    }

    protected abstract String getDefaultDatabaseProductName();

    public String getDatabaseProductVersion() throws DatabaseException {
        if (this.connection == null) {
            return null;
        } else {
            try {
                return this.connection.getDatabaseProductVersion();
            } catch (DatabaseException var2) {
                throw new DatabaseException(var2);
            }
        }
    }

    public int getDatabaseMajorVersion() throws DatabaseException {
        if (this.connection == null) {
            return 999;
        } else {
            try {
                return this.connection.getDatabaseMajorVersion();
            } catch (DatabaseException var2) {
                throw new DatabaseException(var2);
            }
        }
    }

    public int getDatabaseMinorVersion() throws DatabaseException {
        if (this.connection == null) {
            return -1;
        } else {
            try {
                return this.connection.getDatabaseMinorVersion();
            } catch (DatabaseException var2) {
                throw new DatabaseException(var2);
            }
        }
    }

    public String getDefaultCatalogName() {
        if (this.defaultCatalogName == null) {
            if (this.defaultSchemaName != null && !this.supportsSchemas()) {
                return this.defaultSchemaName;
            }

            if (this.connection != null) {
                try {
                    this.defaultCatalogName = this.getConnectionCatalogName();
                } catch (DatabaseException var2) {
                    Scope.getCurrentScope().getLog(this.getClass()).info("Error getting default catalog", var2);
                }
            }
        }

        return this.defaultCatalogName;
    }

    public void setDefaultCatalogName(String defaultCatalogName) {
        this.defaultCatalogName = this.correctObjectName(defaultCatalogName, Catalog.class);
        this.defaultCatalogSet = defaultCatalogName != null;
    }

    protected String getConnectionCatalogName() throws DatabaseException {
        return this.connection.getCatalog();
    }

    /** @deprecated */
    @Deprecated
    public CatalogAndSchema correctSchema(String catalog, String schema) {
        return (new CatalogAndSchema(catalog, schema)).standardize(this);
    }

    /** @deprecated */
    @Deprecated
    public CatalogAndSchema correctSchema(CatalogAndSchema schema) {
        return schema == null ? new CatalogAndSchema(this.getDefaultCatalogName(), this.getDefaultSchemaName()) : schema.standardize(this);
    }

    public String correctObjectName(String objectName, Class<? extends DatabaseObject> objectType) {
        if (this.quotingStrategy != ObjectQuotingStrategy.QUOTE_ALL_OBJECTS && this.unquotedObjectsAreUppercased != null && objectName != null && (!objectName.startsWith(this.getQuotingStartCharacter()) || !objectName.endsWith(this.getQuotingEndCharacter()))) {
            return Boolean.TRUE.equals(this.unquotedObjectsAreUppercased) ? objectName.toUpperCase(Locale.US) : objectName.toLowerCase(Locale.US);
        } else {
            return objectName;
        }
    }

    public CatalogAndSchema getDefaultSchema() {
        return new CatalogAndSchema(this.getDefaultCatalogName(), this.getDefaultSchemaName());
    }

    public String getDefaultSchemaName() {
        if (!this.supportsSchemas()) {
            return this.getDefaultCatalogName();
        } else {
            if (this.defaultSchemaName == null && this.connection != null) {
                this.defaultSchemaName = this.getConnectionSchemaName();
                Scope.getCurrentScope().getLog(this.getClass()).info("Set default schema name to " + this.defaultSchemaName);
            }

            return this.defaultSchemaName;
        }
    }

    public Integer getDefaultScaleForNativeDataType(String nativeDataType) {
        return null;
    }

    public void setDefaultSchemaName(String schemaName) {
        this.defaultSchemaName = this.correctObjectName(schemaName, Schema.class);
        if (!this.supportsSchemas()) {
            this.defaultCatalogSet = schemaName != null;
        }

    }

    protected String getConnectionSchemaName() {
        if (this.connection == null) {
            return null;
        } else if (this.connection instanceof OfflineConnection) {
            return ((OfflineConnection)this.connection).getSchema();
        } else if (!(this.connection instanceof JdbcConnection)) {
            return this.defaultSchemaName;
        } else {
            try {
                SqlStatement currentSchemaStatement = this.getConnectionSchemaNameCallStatement();
                return (String)((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).queryForObject(currentSchemaStatement, String.class);
            } catch (Exception var2) {
                Scope.getCurrentScope().getLog(this.getClass()).info("Error getting default schema", var2);
                return null;
            }
        }
    }

    protected SqlStatement getConnectionSchemaNameCallStatement() {
        return new RawCallStatement("call current_schema()");
    }

    public Integer getFetchSize() {
        return 1000;
    }

    protected Set<String> getSystemTables() {
        return new HashSet();
    }

    protected Set<String> getSystemViews() {
        return new HashSet();
    }

    public boolean supportsSequences() {
        return true;
    }

    public boolean supportsAutoIncrement() {
        return true;
    }

    public String getDateLiteral(String isoDate) {
        if (!this.isDateOnly(isoDate) && !this.isTimeOnly(isoDate)) {
            return this.isDateTime(isoDate) ? "'" + isoDate.replace('T', ' ') + "'" : "BAD_DATE_FORMAT:" + isoDate;
        } else {
            return "'" + isoDate + "'";
        }
    }

    public String getDateTimeLiteral(Timestamp date) {
        return this.getDateLiteral((new ISODateFormat()).format(date).replaceFirst("^'", "").replaceFirst("'$", ""));
    }

    public String getDateLiteral(Date date) {
        return this.getDateLiteral((new ISODateFormat()).format(date).replaceFirst("^'", "").replaceFirst("'$", ""));
    }

    public String getTimeLiteral(Time date) {
        return this.getDateLiteral((new ISODateFormat()).format(date).replaceFirst("^'", "").replaceFirst("'$", ""));
    }

    public String getDateLiteral(java.util.Date date) {
        if (date instanceof Date) {
            return this.getDateLiteral((Date)date);
        } else if (date instanceof Time) {
            return this.getTimeLiteral((Time)date);
        } else if (date instanceof Timestamp) {
            return this.getDateTimeLiteral((Timestamp)date);
        } else if (date instanceof java.util.Date) {
            return this.getDateTimeLiteral(new Timestamp(date.getTime()));
        } else {
            throw new RuntimeException("Unexpected type: " + date.getClass().getName());
        }
    }

    public java.util.Date parseDate(String dateAsString) throws DateParseException {
        try {
            if (dateAsString.indexOf(" ") > 0) {
                return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateAsString);
            } else if (dateAsString.indexOf("T") > 0) {
                return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(dateAsString);
            } else {
                return dateAsString.indexOf(":") > 0 ? (new SimpleDateFormat("HH:mm:ss")).parse(dateAsString) : (new SimpleDateFormat("yyyy-MM-dd")).parse(dateAsString);
            }
        } catch (ParseException var3) {
            throw new DateParseException(dateAsString);
        }
    }

    protected boolean isDateOnly(String isoDate) {
        return isoDate.matches("^\\d{4}\\-\\d{2}\\-\\d{2}$") || NowAndTodayUtil.isNowOrTodayFormat(isoDate);
    }

    protected boolean isDateTime(String isoDate) {
        return isoDate.matches("^\\d{4}\\-\\d{2}\\-\\d{2}[T ]\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?$") || NowAndTodayUtil.isNowOrTodayFormat(isoDate);
    }

    protected boolean isTimestamp(String isoDate) {
        return isoDate.matches("^\\d{4}\\-\\d{2}\\-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+$") || NowAndTodayUtil.isNowOrTodayFormat(isoDate);
    }

    protected boolean isTimeOnly(String isoDate) {
        return isoDate.matches("^\\d{2}:\\d{2}:\\d{2}$") || NowAndTodayUtil.isNowOrTodayFormat(isoDate);
    }

    public String getLineComment() {
        return "--";
    }

    public String getAutoIncrementClause(BigInteger startWith, BigInteger incrementBy, String generationType, Boolean defaultOnNull) {
        if (!this.supportsAutoIncrement()) {
            return "";
        } else {
            String autoIncrementClause = this.getAutoIncrementClause(generationType, defaultOnNull);
            boolean generateStartWith = this.generateAutoIncrementStartWith(startWith);
            boolean generateIncrementBy = this.generateAutoIncrementBy(incrementBy);
            if (generateStartWith || generateIncrementBy) {
                autoIncrementClause = autoIncrementClause + this.getAutoIncrementOpening();
                if (generateStartWith) {
                    autoIncrementClause = autoIncrementClause + String.format(this.getAutoIncrementStartWithClause(), startWith == null ? this.defaultAutoIncrementStartWith : startWith);
                }

                if (generateIncrementBy) {
                    if (generateStartWith) {
                        autoIncrementClause = autoIncrementClause + ", ";
                    }

                    autoIncrementClause = autoIncrementClause + String.format(this.getAutoIncrementByClause(), incrementBy == null ? this.defaultAutoIncrementBy : incrementBy);
                }

                autoIncrementClause = autoIncrementClause + this.getAutoIncrementClosing();
            }

            return autoIncrementClause;
        }
    }

    protected String getAutoIncrementClause() {
        return "GENERATED BY DEFAULT AS IDENTITY";
    }

    protected String getAutoIncrementClause(String generationType, Boolean defaultOnNull) {
        return this.getAutoIncrementClause();
    }

    protected boolean generateAutoIncrementStartWith(BigInteger startWith) {
        return startWith != null && !startWith.equals(this.defaultAutoIncrementStartWith);
    }

    protected boolean generateAutoIncrementBy(BigInteger incrementBy) {
        return incrementBy != null && !incrementBy.equals(this.defaultAutoIncrementBy);
    }

    protected String getAutoIncrementOpening() {
        return " (";
    }

    protected String getAutoIncrementClosing() {
        return ")";
    }

    protected String getAutoIncrementStartWithClause() {
        return "START WITH %d";
    }

    protected String getAutoIncrementByClause() {
        return "INCREMENT BY %d";
    }

    public String getConcatSql(String... values) {
        return StringUtil.join(values, " || ");
    }

    public String getDatabaseChangeLogTableName() {
        return this.databaseChangeLogTableName != null ? this.databaseChangeLogTableName : (String)GlobalConfiguration.DATABASECHANGELOG_TABLE_NAME.getCurrentValue();
    }

    public void setDatabaseChangeLogTableName(String tableName) {
        this.databaseChangeLogTableName = tableName;
    }

    public String getDatabaseChangeLogLockTableName() {
        return this.databaseChangeLogLockTableName != null ? this.databaseChangeLogLockTableName : (String)GlobalConfiguration.DATABASECHANGELOGLOCK_TABLE_NAME.getCurrentValue();
    }

    public void setDatabaseChangeLogLockTableName(String tableName) {
        this.databaseChangeLogLockTableName = tableName;
    }

    public String getLiquibaseTablespaceName() {
        return this.liquibaseTablespaceName != null ? this.liquibaseTablespaceName : (String)GlobalConfiguration.LIQUIBASE_TABLESPACE_NAME.getCurrentValue();
    }

    public void setLiquibaseTablespaceName(String tablespace) {
        this.liquibaseTablespaceName = tablespace;
    }

    protected boolean canCreateChangeLogTable() throws DatabaseException {
        return ((StandardChangeLogHistoryService)ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this)).canCreateChangeLogTable();
    }

    public void setCanCacheLiquibaseTableInfo(boolean canCacheLiquibaseTableInfo) {
        this.canCacheLiquibaseTableInfo = canCacheLiquibaseTableInfo;
    }

    public String getLiquibaseCatalogName() {
        if (this.liquibaseCatalogName != null) {
            return this.liquibaseCatalogName;
        } else {
            String configuredCatalogName = (String)GlobalConfiguration.LIQUIBASE_CATALOG_NAME.getCurrentValue();
            return configuredCatalogName != null ? configuredCatalogName : this.getDefaultCatalogName();
        }
    }

    public void setLiquibaseCatalogName(String catalogName) {
        this.liquibaseCatalogName = catalogName;
    }

    public String getLiquibaseSchemaName() {
        if (this.liquibaseSchemaName != null) {
            return this.liquibaseSchemaName;
        } else {
            ConfiguredValue<String> configuredValue = GlobalConfiguration.LIQUIBASE_SCHEMA_NAME.getCurrentConfiguredValue();
            return !configuredValue.wasDefaultValueUsed() ? (String)configuredValue.getValue() : this.getDefaultSchemaName();
        }
    }

    public void setLiquibaseSchemaName(String schemaName) {
        this.liquibaseSchemaName = schemaName;
    }

    public boolean isCaseSensitive() {
        if (this.caseSensitive == null && this.connection != null && this.connection instanceof JdbcConnection) {
            try {
                this.caseSensitive = ((JdbcConnection)this.connection).getUnderlyingConnection().getMetaData().supportsMixedCaseIdentifiers();
            } catch (SQLException var2) {
                Scope.getCurrentScope().getLog(this.getClass()).warning("Cannot determine case sensitivity from JDBC driver", var2);
            }
        }

        return this.caseSensitive == null ? false : this.caseSensitive;
    }

    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isReservedWord(String string) {
        return this.reservedWords.contains(string.toUpperCase());
    }

    protected boolean startsWithNumeric(String objectName) {
        return startsWithNumberPattern.matcher(objectName).matches();
    }

    public void dropDatabaseObjects(CatalogAndSchema schemaToDrop) throws LiquibaseException {
        ObjectQuotingStrategy currentStrategy = this.getObjectQuotingStrategy();
        this.setObjectQuotingStrategy(ObjectQuotingStrategy.QUOTE_ALL_OBJECTS);

        try {
            DatabaseSnapshot snapshot;
            try {
                SnapshotControl snapshotControl = new SnapshotControl(this);
                Set<Class<? extends DatabaseObject>> typesToInclude = snapshotControl.getTypesToInclude();
                typesToInclude.remove(Index.class);
                typesToInclude.remove(PrimaryKey.class);
                typesToInclude.remove(UniqueConstraint.class);
                if (this.supportsForeignKeyDisable() || this.getShortName().equals("postgresql")) {
                    typesToInclude.remove(ForeignKey.class);
                }

                long createSnapshotStarted = System.currentTimeMillis();
                snapshot = SnapshotGeneratorFactory.getInstance().createSnapshot(schemaToDrop, this, snapshotControl);
                Scope.getCurrentScope().getLog(this.getClass()).fine(String.format("Database snapshot generated in %d ms. Snapshot includes: %s", System.currentTimeMillis() - createSnapshotStarted, typesToInclude));
            } catch (LiquibaseException var28) {
                throw new UnexpectedLiquibaseException(var28);
            }

            long changeSetStarted = System.currentTimeMillis();
            CompareControl compareControl = new CompareControl(new SchemaComparison[]{new SchemaComparison(CatalogAndSchema.DEFAULT, schemaToDrop)}, snapshot.getSnapshotControl().getTypesToInclude());
            DiffResult diffResult = DiffGeneratorFactory.getInstance().compare(new EmptyDatabaseSnapshot(this), snapshot, compareControl);
            List<ChangeSet> changeSets = (new DiffToChangeLog(diffResult, (new DiffOutputControl(true, true, false, (SchemaComparison[])null)).addIncludedSchema(schemaToDrop))).generateChangeSets();
            Scope.getCurrentScope().getLog(this.getClass()).fine(String.format("ChangeSet to Remove Database Objects generated in %d ms.", System.currentTimeMillis() - changeSetStarted));
            boolean previousAutoCommit = this.getAutoCommitMode();
            this.commit();
            this.setAutoCommit(false);
            boolean reEnableFK = this.supportsForeignKeyDisable() && this.disableForeignKeyChecks();

            try {
                Iterator var11 = changeSets.iterator();

                while(var11.hasNext()) {
                    ChangeSet changeSet = (ChangeSet)var11.next();
                    changeSet.setFailOnError(false);
                    Iterator var13 = changeSet.getChanges().iterator();

                    while(var13.hasNext()) {
                        Change change = (Change)var13.next();
                        if (change instanceof DropTableChange) {
                            ((DropTableChange)change).setCascadeConstraints(true);
                        }

                        SqlStatement[] sqlStatements = change.generateStatements(this);
                        SqlStatement[] var16 = sqlStatements;
                        int var17 = sqlStatements.length;

                        for(int var18 = 0; var18 < var17; ++var18) {
                            SqlStatement statement = var16[var18];
                            ((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).execute(statement);
                        }
                    }

                    this.commit();
                }
            } finally {
                if (reEnableFK) {
                    this.enableForeignKeyChecks();
                }

            }

            ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).destroy();
            LockServiceFactory.getInstance().getLockService(this).destroy();
            this.setAutoCommit(previousAutoCommit);
            Scope.getCurrentScope().getLog(this.getClass()).info(String.format("Successfully deleted all supported object types in schema %s.", schemaToDrop.toString()));
        } finally {
            this.setObjectQuotingStrategy(currentStrategy);
            this.commit();
        }

    }

    public boolean supportsDropTableCascadeConstraints() {
        return this instanceof SQLiteDatabase || this instanceof SybaseDatabase || this instanceof SybaseASADatabase || this instanceof PostgresDatabase || this instanceof OracleDatabase;
    }

    public boolean isSystemObject(DatabaseObject example) {
        if (example == null) {
            return false;
        } else if (example.getSchema() != null && example.getSchema().getName() != null && "information_schema".equalsIgnoreCase(example.getSchema().getName())) {
            return true;
        } else if (example instanceof Table && this.getSystemTables().contains(example.getName())) {
            return true;
        } else {
            return example instanceof View && this.getSystemViews().contains(example.getName());
        }
    }

    public boolean isSystemView(CatalogAndSchema schema, String viewName) {
        schema = schema.customize(this);
        return "information_schema".equalsIgnoreCase(schema.getSchemaName()) ? true : this.getSystemViews().contains(viewName);
    }

    public boolean isLiquibaseObject(DatabaseObject object) {
        if (object instanceof Table) {
            Schema liquibaseSchema = new Schema(this.getLiquibaseCatalogName(), this.getLiquibaseSchemaName());
            return DatabaseObjectComparatorFactory.getInstance().isSameObject(object, (new Table()).setName(this.getDatabaseChangeLogTableName()).setSchema(liquibaseSchema), (SchemaComparison[])null, this) ? true : DatabaseObjectComparatorFactory.getInstance().isSameObject(object, (new Table()).setName(this.getDatabaseChangeLogLockTableName()).setSchema(liquibaseSchema), (SchemaComparison[])null, this);
        } else if (object instanceof Column) {
            return this.isLiquibaseObject(((Column)object).getRelation());
        } else if (object instanceof Index) {
            return this.isLiquibaseObject(((Index)object).getRelation());
        } else {
            return object instanceof PrimaryKey ? this.isLiquibaseObject(((PrimaryKey)object).getTable()) : false;
        }
    }

    public void tag(String tagString) throws DatabaseException {
        ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).tag(tagString);
    }

    public boolean doesTagExist(String tag) throws DatabaseException {
        return ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).tagExists(tag);
    }

    public String toString() {
        return this.getConnection() == null ? this.getShortName() + " Database" : this.getConnection().getConnectionUserName() + " @ " + this.getConnection().getURL() + (this.getDefaultSchemaName() == null ? "" : " (Default Schema: " + this.getDefaultSchemaName() + ")");
    }

    public String getViewDefinition(CatalogAndSchema schema, String viewName) throws DatabaseException {
        schema = schema.customize(this);
        String definition = (String)((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).queryForObject(new GetViewDefinitionStatement(schema.getCatalogName(), schema.getSchemaName(), viewName), String.class);
        return definition == null ? null : CREATE_VIEW_AS_PATTERN.matcher(definition).replaceFirst("");
    }

    public String escapeTableName(String catalogName, String schemaName, String tableName) {
        return this.escapeObjectName(catalogName, schemaName, tableName, Table.class);
    }

    public String escapeObjectName(String catalogName, String schemaName, String objectName, Class<? extends DatabaseObject> objectType) {
        if (this.supportsSchemas()) {
            catalogName = StringUtil.trimToNull(catalogName);
            schemaName = StringUtil.trimToNull(schemaName);
            if (catalogName == null) {
                catalogName = this.getDefaultCatalogName();
            }

            if (schemaName == null) {
                schemaName = this.getDefaultSchemaName();
            }

            if (!this.supportsCatalogInObjectName(objectType)) {
                catalogName = null;
            }

            if (catalogName == null && schemaName == null) {
                return this.escapeObjectName(objectName, objectType);
            } else if (catalogName != null && this.supportsCatalogInObjectName(objectType)) {
                if (this.isDefaultSchema(catalogName, schemaName) && !this.getOutputDefaultSchema() && !this.getOutputDefaultCatalog()) {
                    return this.escapeObjectName(objectName, objectType);
                } else {
                    return this.isDefaultSchema(catalogName, schemaName) && !this.getOutputDefaultCatalog() ? this.escapeObjectName(schemaName, Schema.class) + "." + this.escapeObjectName(objectName, objectType) : this.escapeObjectName(catalogName, Catalog.class) + "." + this.escapeObjectName(schemaName, Schema.class) + "." + this.escapeObjectName(objectName, objectType);
                }
            } else {
                return this.isDefaultSchema(catalogName, schemaName) && !this.getOutputDefaultSchema() ? this.escapeObjectName(objectName, objectType) : this.escapeObjectName(schemaName, Schema.class) + "." + this.escapeObjectName(objectName, objectType);
            }
        } else if (this.supportsCatalogs()) {
            catalogName = StringUtil.trimToNull(catalogName);
            schemaName = StringUtil.trimToNull(schemaName);
            if (catalogName != null) {
                if (this.getOutputDefaultCatalog()) {
                    return this.escapeObjectName(catalogName, Catalog.class) + "." + this.escapeObjectName(objectName, objectType);
                } else {
                    return !this.defaultCatalogSet && this.isDefaultCatalog(catalogName) ? this.escapeObjectName(objectName, objectType) : this.escapeObjectName(catalogName, Catalog.class) + "." + this.escapeObjectName(objectName, objectType);
                }
            } else if (schemaName != null) {
                if (this.getOutputDefaultCatalog()) {
                    return this.escapeObjectName(schemaName, Catalog.class) + "." + this.escapeObjectName(objectName, objectType);
                } else {
                    return !this.defaultCatalogSet && this.isDefaultCatalog(schemaName) ? this.escapeObjectName(objectName, objectType) : this.escapeObjectName(schemaName, Catalog.class) + "." + this.escapeObjectName(objectName, objectType);
                }
            } else {
                catalogName = this.getDefaultCatalogName();
                if (catalogName == null) {
                    return this.escapeObjectName(objectName, objectType);
                } else {
                    return !this.defaultCatalogSet && (!this.isDefaultCatalog(catalogName) || !this.getOutputDefaultCatalog()) ? this.escapeObjectName(objectName, objectType) : this.escapeObjectName(catalogName, Catalog.class) + "." + this.escapeObjectName(objectName, objectType);
                }
            }
        } else {
            return this.escapeObjectName(objectName, objectType);
        }
    }

    public String escapeObjectName(String objectName, Class<? extends DatabaseObject> objectType) {
        if (objectName != null) {
            if (this.mustQuoteObjectName(objectName, objectType)) {
                return this.quoteObject(objectName, objectType).trim();
            }

            if (this.quotingStrategy == ObjectQuotingStrategy.QUOTE_ALL_OBJECTS) {
                return this.quoteObject(objectName, objectType).trim();
            }

            objectName = objectName.trim();
        }

        return objectName;
    }

    protected boolean mustQuoteObjectName(String objectName, Class<? extends DatabaseObject> objectType) {
        return objectName.contains("-") || this.startsWithNumeric(objectName) || this.isReservedWord(objectName) || objectName.matches(".*\\W.*");
    }

    protected String getQuotingStartCharacter() {
        return "\"";
    }

    protected String getQuotingEndCharacter() {
        return "\"";
    }

    protected String getQuotingEndReplacement() {
        return "\"\"";
    }

    public String quoteObject(String objectName, Class<? extends DatabaseObject> objectType) {
        return objectName == null ? null : this.getQuotingStartCharacter() + objectName.replace(this.getQuotingEndCharacter(), this.getQuotingEndReplacement()) + this.getQuotingEndCharacter();
    }

    public String escapeIndexName(String catalogName, String schemaName, String indexName) {
        return this.escapeObjectName(catalogName, schemaName, indexName, Index.class);
    }

    public String escapeSequenceName(String catalogName, String schemaName, String sequenceName) {
        return this.escapeObjectName(catalogName, schemaName, sequenceName, Sequence.class);
    }

    public String escapeConstraintName(String constraintName) {
        return this.escapeObjectName(constraintName, Index.class);
    }

    public String escapeColumnName(String catalogName, String schemaName, String tableName, String columnName) {
        return this.escapeObjectName(columnName, Column.class);
    }

    public String escapeColumnName(String catalogName, String schemaName, String tableName, String columnName, boolean quoteNamesThatMayBeFunctions) {
        if (this.quotingStrategy == ObjectQuotingStrategy.QUOTE_ALL_OBJECTS) {
            return this.quoteObject(columnName, Column.class);
        } else if (columnName.contains("(")) {
            return quoteNamesThatMayBeFunctions ? this.quoteObject(columnName, Column.class) : columnName;
        } else {
            return this.escapeObjectName(columnName, Column.class);
        }
    }

    public String escapeColumnNameList(String columnNames) {
        StringBuilder sb = new StringBuilder();
        Iterator var3 = StringUtil.splitAndTrim(columnNames, ",").iterator();

        while(var3.hasNext()) {
            String columnName = (String)var3.next();
            if (sb.length() > 0) {
                sb.append(", ");
            }

            boolean descending = false;
            if (columnName.matches("(?i).*\\s+DESC")) {
                columnName = columnName.replaceFirst("(?i)\\s+DESC$", "");
                descending = true;
            } else if (columnName.matches("(?i).*\\s+ASC")) {
                columnName = columnName.replaceFirst("(?i)\\s+ASC$", "");
            }

            sb.append(this.escapeObjectName(columnName, Column.class));
            if (descending) {
                sb.append(" DESC");
            }
        }

        return sb.toString();
    }

    public boolean supportsSchemas() {
        return true;
    }

    public boolean supportsCatalogs() {
        return true;
    }

    public boolean jdbcCallsCatalogsSchemas() {
        return false;
    }

    public boolean supportsCatalogInObjectName(Class<? extends DatabaseObject> type) {
        return false;
    }

    public String generatePrimaryKeyName(String tableName) {
        return "PK_" + tableName.toUpperCase(Locale.US);
    }

    public String escapeViewName(String catalogName, String schemaName, String viewName) {
        return this.escapeObjectName(catalogName, schemaName, viewName, View.class);
    }

    public RunStatus getRunStatus(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).getRunStatus(changeSet);
    }

    public RanChangeSet getRanChangeSet(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).getRanChangeSet(changeSet);
    }

    public List<RanChangeSet> getRanChangeSetList() throws DatabaseException {
        return ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).getRanChangeSets();
    }

    public java.util.Date getRanDate(ChangeSet changeSet) throws DatabaseException, DatabaseHistoryException {
        return ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).getRanDate(changeSet);
    }

    public void markChangeSetExecStatus(ChangeSet changeSet, ExecType execType) throws DatabaseException {
        ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).setExecType(changeSet, execType);
    }

    public void removeRanStatus(ChangeSet changeSet) throws DatabaseException {
        ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).removeFromHistory(changeSet);
    }

    public String escapeStringForDatabase(String string) {
        return string == null ? null : string.replaceAll("'", "''");
    }

    public void commit() throws DatabaseException {
        try {
            this.getConnection().commit();
        } catch (DatabaseException var2) {
            throw new DatabaseException(var2);
        }
    }

    public void rollback() throws DatabaseException {
        try {
            this.getConnection().rollback();
        } catch (DatabaseException var2) {
            throw new DatabaseException(var2);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            AbstractJdbcDatabase that = (AbstractJdbcDatabase)o;
            if (this.connection == null) {
                if (that.connection == null) {
                    return this == that;
                } else {
                    return false;
                }
            } else {
                return this.connection.equals(that.connection);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.connection != null ? this.connection.hashCode() : super.hashCode();
    }

    public void close() throws DatabaseException {
        ((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).clearExecutor("jdbc", this);
        DatabaseConnection connection = this.getConnection();
        if (connection != null) {
            if (this.previousAutoCommit != null) {
                try {
                    connection.setAutoCommit(this.previousAutoCommit);
                } catch (DatabaseException var3) {
                    Scope.getCurrentScope().getLog(this.getClass()).warning("Failed to restore the auto commit to " + this.previousAutoCommit);
                    throw var3;
                }
            }

            connection.close();
        }

    }

    public boolean supportsRestrictForeignKeys() {
        return true;
    }

    public boolean isAutoCommit() throws DatabaseException {
        try {
            return this.getConnection().getAutoCommit();
        } catch (DatabaseException var2) {
            throw new DatabaseException(var2);
        }
    }

    public void setAutoCommit(boolean b) throws DatabaseException {
        try {
            this.getConnection().setAutoCommit(b);
        } catch (DatabaseException var3) {
            throw new DatabaseException(var3);
        }
    }

    public boolean isSafeToRunUpdate() throws DatabaseException {
        DatabaseConnection connection = this.getConnection();
        if (connection == null) {
            return true;
        } else {
            String url = connection.getURL();
            if (url == null) {
                return false;
            } else {
                return url.contains("localhost") || url.contains("127.0.0.1");
            }
        }
    }

    public void executeStatements(Change change, DatabaseChangeLog changeLog, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        SqlStatement[] statements = change.generateStatements(this);
        this.execute(statements, sqlVisitors);
    }

    public void execute(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        SqlStatement[] var3 = statements;
        int var4 = statements.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            SqlStatement statement = var3[var5];
            if (!statement.skipOnUnsupported() || SqlGeneratorFactory.getInstance().supports(statement, this)) {
                Scope.getCurrentScope().getLog(this.getClass()).fine("Executing Statement: " + statement);

                try {
                    ((ExecutorService)Scope.getCurrentScope().getSingleton(ExecutorService.class)).getExecutor("jdbc", this).execute(statement, sqlVisitors);
                } catch (DatabaseException var8) {
                    if (!statement.continueOnError()) {
                        throw var8;
                    }

                    Scope.getCurrentScope().getLog(this.getClass()).severe("Error executing statement '" + statement.toString() + "', but continuing", var8);
                }
            }
        }

    }

    public void saveStatements(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException {
        SqlStatement[] statements = change.generateStatements(this);
        SqlStatement[] var5 = statements;
        int var6 = statements.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            SqlStatement statement = var5[var7];
            Sql[] var9 = SqlGeneratorFactory.getInstance().generateSql(statement, this);
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                Sql sql = var9[var11];
                writer.append(sql.toSql()).append(sql.getEndDelimiter()).append(StreamUtil.getLineSeparator()).append(StreamUtil.getLineSeparator());
            }
        }

    }

    public void executeRollbackStatements(SqlStatement[] statements, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        this.execute(statements, this.filterRollbackVisitors(sqlVisitors));
    }

    public void executeRollbackStatements(Change change, List<SqlVisitor> sqlVisitors) throws LiquibaseException {
        SqlStatement[] statements = change.generateRollbackStatements(this);
        this.executeRollbackStatements(statements, sqlVisitors);
    }

    public void saveRollbackStatement(Change change, List<SqlVisitor> sqlVisitors, Writer writer) throws IOException, LiquibaseException {
        SqlStatement[] statements = change.generateRollbackStatements(this);
        SqlStatement[] var5 = statements;
        int var6 = statements.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            SqlStatement statement = var5[var7];
            Sql[] var9 = SqlGeneratorFactory.getInstance().generateSql(statement, this);
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                Sql sql = var9[var11];
                writer.append(sql.toSql()).append(sql.getEndDelimiter()).append("\n\n");
            }
        }

    }

    protected List<SqlVisitor> filterRollbackVisitors(List<SqlVisitor> visitors) {
        List<SqlVisitor> rollbackVisitors = new ArrayList();
        if (visitors != null) {
            Iterator var3 = visitors.iterator();

            while(var3.hasNext()) {
                SqlVisitor visitor = (SqlVisitor)var3.next();
                if (visitor.isApplyToRollback()) {
                    rollbackVisitors.add(visitor);
                }
            }
        }

        return rollbackVisitors;
    }

    public List<DatabaseFunction> getDateFunctions() {
        return this.dateFunctions;
    }

    public boolean isFunction(String string) {
        if (string.endsWith("()")) {
            return true;
        } else {
            Iterator var2 = this.getDateFunctions().iterator();

            DatabaseFunction function;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                function = (DatabaseFunction)var2.next();
            } while(!function.toString().equalsIgnoreCase(string));

            return true;
        }
    }

    public void resetInternalState() {
        ChangeLogHistoryServiceFactory.getInstance().getChangeLogService(this).reset();
        LockServiceFactory.getInstance().getLockService(this).reset();
    }

    public boolean supportsForeignKeyDisable() {
        return false;
    }

    public boolean disableForeignKeyChecks() throws DatabaseException {
        throw new DatabaseException("ForeignKeyChecks Management not supported");
    }

    public void enableForeignKeyChecks() throws DatabaseException {
        throw new DatabaseException("ForeignKeyChecks Management not supported");
    }

    public boolean createsIndexesForForeignKeys() {
        return false;
    }

    public int getDataTypeMaxParameters(String dataTypeName) {
        return 2;
    }

    public CatalogAndSchema getSchemaFromJdbcInfo(String rawCatalogName, String rawSchemaName) {
        return (new CatalogAndSchema(rawCatalogName, rawSchemaName)).customize(this);
    }

    public String getJdbcCatalogName(CatalogAndSchema schema) {
        return this.correctObjectName(schema.getCatalogName(), Catalog.class);
    }

    public String getJdbcSchemaName(CatalogAndSchema schema) {
        return this.correctObjectName(schema.getSchemaName(), Schema.class);
    }

    public final String getJdbcCatalogName(Schema schema) {
        return schema == null ? this.getJdbcCatalogName(this.getDefaultSchema()) : this.getJdbcCatalogName(new CatalogAndSchema(schema.getCatalogName(), schema.getName()));
    }

    public final String getJdbcSchemaName(Schema schema) {
        return schema == null ? this.getJdbcSchemaName(this.getDefaultSchema()) : this.getJdbcSchemaName(new CatalogAndSchema(schema.getCatalogName(), schema.getName()));
    }

    public boolean dataTypeIsNotModifiable(String typeName) {
        return this.unmodifiableDataTypes.contains(typeName.toLowerCase());
    }

    public ObjectQuotingStrategy getObjectQuotingStrategy() {
        return this.quotingStrategy;
    }

    public void setObjectQuotingStrategy(ObjectQuotingStrategy quotingStrategy) {
        this.quotingStrategy = quotingStrategy;
    }

    public String generateDatabaseFunctionValue(DatabaseFunction databaseFunction) {
        if (databaseFunction.getValue() == null) {
            return null;
        } else if (this.isCurrentTimeFunction(databaseFunction.getValue().toLowerCase())) {
            return this.getCurrentDateTimeFunction();
        } else {
            String sequenceSchemaName;
            String sequenceName;
            if (databaseFunction instanceof SequenceNextValueFunction) {
                if (this.sequenceNextValueFunction == null) {
                    throw new RuntimeException(String.format("next value function for a sequence is not configured for database %s", this.getDefaultDatabaseProductName()));
                } else {
                    sequenceSchemaName = databaseFunction.getValue();
                    sequenceName = ((SequenceNextValueFunction)databaseFunction).getSequenceSchemaName();
                    sequenceSchemaName = this.escapeObjectName((String)null, sequenceName, sequenceSchemaName, Sequence.class);
                    if (this.sequenceNextValueFunction.contains("'") && !(this instanceof PostgresDatabase)) {
                        sequenceSchemaName = sequenceSchemaName.replace("\"", "");
                    }

                    return String.format(this.sequenceNextValueFunction, sequenceSchemaName);
                }
            } else if (databaseFunction instanceof SequenceCurrentValueFunction) {
                if (this.sequenceCurrentValueFunction == null) {
                    throw new RuntimeException(String.format("current value function for a sequence is not configured for database %s", this.getDefaultDatabaseProductName()));
                } else {
                    sequenceSchemaName = ((SequenceCurrentValueFunction)databaseFunction).getSequenceSchemaName();
                    sequenceName = databaseFunction.getValue();
                    sequenceName = this.escapeObjectName((String)null, sequenceSchemaName, sequenceName, Sequence.class);
                    if (this.sequenceCurrentValueFunction.contains("'") && !(this instanceof PostgresDatabase)) {
                        sequenceName = sequenceName.replace("\"", "");
                    }

                    return String.format(this.sequenceCurrentValueFunction, sequenceName);
                }
            } else {
                return databaseFunction.getValue();
            }
        }
    }

    private boolean isCurrentTimeFunction(String functionValue) {
        if (functionValue == null) {
            return false;
        } else {
            return functionValue.startsWith("current_timestamp") || functionValue.startsWith("current_datetime") || functionValue.equalsIgnoreCase(this.getCurrentDateTimeFunction());
        }
    }

    public String getCurrentDateTimeFunction() {
        return this.currentDateTimeFunction;
    }

    public void setCurrentDateTimeFunction(String function) {
        if (function != null) {
            this.currentDateTimeFunction = function;
            this.dateFunctions.add(new DatabaseFunction(function));
        }

    }

    public boolean isDefaultSchema(String catalog, String schema) {
        if (!this.supportsSchemas()) {
            return true;
        } else if (!this.isDefaultCatalog(catalog)) {
            return false;
        } else {
            return schema == null || schema.equalsIgnoreCase(this.getDefaultSchemaName());
        }
    }

    public boolean isDefaultCatalog(String catalog) {
        if (!this.supportsCatalogs()) {
            return true;
        } else {
            return catalog == null || catalog.equalsIgnoreCase(this.getDefaultCatalogName());
        }
    }

    public boolean getOutputDefaultSchema() {
        return this.outputDefaultSchema;
    }

    public void setOutputDefaultSchema(boolean outputDefaultSchema) {
        this.outputDefaultSchema = outputDefaultSchema;
    }

    public boolean getOutputDefaultCatalog() {
        return this.outputDefaultCatalog;
    }

    public void setOutputDefaultCatalog(boolean outputDefaultCatalog) {
        this.outputDefaultCatalog = outputDefaultCatalog;
    }

    public boolean supportsPrimaryKeyNames() {
        return true;
    }

    public String getSystemSchema() {
        return "information_schema";
    }

    public String escapeDataTypeName(String dataTypeName) {
        return dataTypeName;
    }

    public String unescapeDataTypeName(String dataTypeName) {
        return dataTypeName;
    }

    public String unescapeDataTypeString(String dataTypeString) {
        return dataTypeString;
    }

    public Object get(String key) {
        return this.attributes.get(key);
    }

    public AbstractJdbcDatabase set(String key, Object value) {
        if (value == null) {
            this.attributes.remove(key);
        } else {
            this.attributes.put(key, value);
        }

        return this;
    }

    public ValidationErrors validate() {
        return new ValidationErrors();
    }

    public int getMaxFractionalDigitsForTimestamp() {
        if (this.getConnection() == null) {
            Scope.getCurrentScope().getLog(this.getClass()).warning("No database connection available - specified DATETIME/TIMESTAMP precision will be tried");
            return 9;
        } else {
            return 9;
        }
    }

    public int getDefaultFractionalDigitsForTimestamp() {
        return this.getMaxFractionalDigitsForTimestamp() >= 6 ? 6 : this.getMaxFractionalDigitsForTimestamp();
    }

    public boolean supportsBatchUpdates() throws DatabaseException {
        if (this.connection instanceof OfflineConnection) {
            return false;
        } else {
            return this.connection instanceof JdbcConnection ? ((JdbcConnection)this.getConnection()).supportsBatchUpdates() : false;
        }
    }

    public boolean supportsNotNullConstraintNames() {
        return false;
    }

    public boolean requiresExplicitNullForColumns() {
        return false;
    }

    public CatalogAndSchemaCase getSchemaAndCatalogCase() {
        return CatalogAndSchemaCase.UPPER_CASE;
    }
}
