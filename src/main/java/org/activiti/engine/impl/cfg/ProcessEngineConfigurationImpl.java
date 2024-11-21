//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.activiti.engine.impl.cfg;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.DynamicBpmnService;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.impl.DynamicBpmnServiceImpl;
import org.activiti.engine.impl.FormServiceImpl;
import org.activiti.engine.impl.HistoryServiceImpl;
import org.activiti.engine.impl.IdentityServiceImpl;
import org.activiti.engine.impl.ManagementServiceImpl;
import org.activiti.engine.impl.ProcessEngineImpl;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.ServiceImpl;
import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
import org.activiti.engine.impl.bpmn.parser.BpmnParseHandlers;
import org.activiti.engine.impl.bpmn.parser.BpmnParser;
import org.activiti.engine.impl.bpmn.parser.factory.AbstractBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultListenerFactory;
import org.activiti.engine.impl.bpmn.parser.factory.ListenerFactory;
import org.activiti.engine.impl.bpmn.parser.handler.BoundaryEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.BusinessRuleParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CallActivityParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CancelEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.CompensateEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EndEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ErrorEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EventBasedGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.EventSubProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ExclusiveGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.InclusiveGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.IntermediateCatchEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.IntermediateThrowEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ManualTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.MessageEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ParallelGatewayParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ReceiveTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ScriptTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SendTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SequenceFlowParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.ServiceTaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SignalEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.StartEventParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.SubProcessParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TaskParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TimerEventDefinitionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.TransactionParseHandler;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.activiti.engine.impl.cfg.standalone.StandaloneMybatisTransactionContextFactory;
import org.activiti.engine.impl.db.DbIdGenerator;
import org.activiti.engine.impl.db.DbSqlSessionFactory;
import org.activiti.engine.impl.db.IbatisVariableTypeHandler;
import org.activiti.engine.impl.delegate.DefaultDelegateInterceptor;
import org.activiti.engine.impl.el.ExpressionManager;
import org.activiti.engine.impl.event.CompensationEventHandler;
import org.activiti.engine.impl.event.EventHandler;
import org.activiti.engine.impl.event.MessageEventHandler;
import org.activiti.engine.impl.event.SignalEventHandler;
import org.activiti.engine.impl.event.logger.EventLogger;
import org.activiti.engine.impl.form.BooleanFormType;
import org.activiti.engine.impl.form.DateFormType;
import org.activiti.engine.impl.form.DoubleFormType;
import org.activiti.engine.impl.form.FormEngine;
import org.activiti.engine.impl.form.FormTypes;
import org.activiti.engine.impl.form.JuelFormEngine;
import org.activiti.engine.impl.form.LongFormType;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.impl.history.parse.FlowNodeHistoryParseHandler;
import org.activiti.engine.impl.history.parse.ProcessHistoryParseHandler;
import org.activiti.engine.impl.history.parse.StartEventHistoryParseHandler;
import org.activiti.engine.impl.history.parse.UserTaskHistoryParseHandler;
import org.activiti.engine.impl.interceptor.CommandConfig;
import org.activiti.engine.impl.interceptor.CommandContextFactory;
import org.activiti.engine.impl.interceptor.CommandContextInterceptor;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.interceptor.CommandInterceptor;
import org.activiti.engine.impl.interceptor.CommandInvoker;
import org.activiti.engine.impl.interceptor.DelegateInterceptor;
import org.activiti.engine.impl.interceptor.LogInterceptor;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.jobexecutor.AsyncContinuationJobHandler;
import org.activiti.engine.impl.jobexecutor.DefaultFailedJobCommandFactory;
import org.activiti.engine.impl.jobexecutor.FailedJobCommandFactory;
import org.activiti.engine.impl.jobexecutor.JobHandler;
import org.activiti.engine.impl.jobexecutor.ProcessEventJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerActivateProcessDefinitionHandler;
import org.activiti.engine.impl.jobexecutor.TimerCatchIntermediateEventJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerExecuteNestedActivityJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerStartEventJobHandler;
import org.activiti.engine.impl.jobexecutor.TimerSuspendProcessDefinitionHandler;
import org.activiti.engine.impl.persistence.DefaultHistoryManagerSessionFactory;
import org.activiti.engine.impl.persistence.GenericManagerFactory;
import org.activiti.engine.impl.persistence.deploy.Deployer;
import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionInfoCache;
import org.activiti.engine.impl.persistence.entity.AttachmentEntityManager;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntityManager;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;
import org.activiti.engine.impl.persistence.entity.DeadLetterJobEntityManager;
import org.activiti.engine.impl.persistence.entity.DeploymentEntityManager;
import org.activiti.engine.impl.persistence.entity.EventLogEntryEntityManager;
import org.activiti.engine.impl.persistence.entity.EventSubscriptionEntityManager;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricDetailEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricIdentityLinkEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntityManager;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntityManager;
import org.activiti.engine.impl.persistence.entity.JobEntityManager;
import org.activiti.engine.impl.persistence.entity.ModelEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityManager;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionInfoEntityManager;
import org.activiti.engine.impl.persistence.entity.PropertyEntityManager;
import org.activiti.engine.impl.persistence.entity.ResourceEntityManager;
import org.activiti.engine.impl.persistence.entity.SuspendedJobEntityManager;
import org.activiti.engine.impl.persistence.entity.TableDataManager;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.persistence.entity.TimerJobEntityManager;
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntityManager;
import org.activiti.engine.impl.scripting.BeansResolverFactory;
import org.activiti.engine.impl.scripting.ResolverFactory;
import org.activiti.engine.impl.scripting.ScriptBindingsFactory;
import org.activiti.engine.impl.scripting.ScriptingEngines;
import org.activiti.engine.impl.scripting.VariableScopeResolverFactory;
import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.impl.util.ReflectUtil;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.impl.variable.EntityManagerSessionFactory;
import org.activiti.engine.impl.variable.JPAEntityListVariableType;
import org.activiti.engine.impl.variable.JPAEntityVariableType;
import org.activiti.engine.impl.variable.SerializableType;
import org.activiti.engine.parse.BpmnParseHandler;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.ibatis.type.JdbcType;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.api.delegate.event.FlowableEventDispatcher;
import org.flowable.common.engine.impl.calendar.BusinessCalendarManager;
import org.flowable.common.engine.impl.calendar.CycleBusinessCalendar;
import org.flowable.common.engine.impl.calendar.DueDateBusinessCalendar;
import org.flowable.common.engine.impl.calendar.DurationBusinessCalendar;
import org.flowable.common.engine.impl.calendar.MapBusinessCalendarManager;
import org.flowable.common.engine.impl.history.HistoryLevel;
import org.flowable.common.engine.impl.persistence.deploy.DefaultDeploymentCache;
import org.flowable.common.engine.impl.persistence.deploy.DeploymentCache;
import org.flowable.common.engine.impl.util.DefaultClockImpl;
import org.flowable.engine.compatibility.Flowable5CompatibilityHandler;
import org.flowable.engine.form.AbstractFormType;
import org.flowable.engine.impl.cfg.DelegateExpressionFieldInjectionMode;
import org.flowable.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.variable.api.types.VariableType;
import org.flowable.variable.api.types.VariableTypes;
import org.flowable.variable.service.impl.types.BooleanType;
import org.flowable.variable.service.impl.types.ByteArrayType;
import org.flowable.variable.service.impl.types.DateType;
import org.flowable.variable.service.impl.types.DefaultVariableTypes;
import org.flowable.variable.service.impl.types.DoubleType;
import org.flowable.variable.service.impl.types.IntegerType;
import org.flowable.variable.service.impl.types.JsonType;
import org.flowable.variable.service.impl.types.LongJsonType;
import org.flowable.variable.service.impl.types.LongStringType;
import org.flowable.variable.service.impl.types.LongType;
import org.flowable.variable.service.impl.types.NullType;
import org.flowable.variable.service.impl.types.ShortType;
import org.flowable.variable.service.impl.types.StringType;
import org.flowable.variable.service.impl.types.UUIDType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ProcessEngineConfigurationImpl extends ProcessEngineConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessEngineConfigurationImpl.class);
    public static final String DB_SCHEMA_UPDATE_CREATE = "create";
    public static final String DB_SCHEMA_UPDATE_DROP_CREATE = "drop-create";
    public static final String DEFAULT_WS_SYNC_FACTORY = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
    public static final String DEFAULT_MYBATIS_MAPPING_FILE = "org/activiti/db/mapping/mappings.xml";
    protected RepositoryService repositoryService = new RepositoryServiceImpl();
    protected RuntimeService runtimeService = new RuntimeServiceImpl();
    protected HistoryService historyService = new HistoryServiceImpl(this);
    protected IdentityService identityService = new IdentityServiceImpl(this);
    protected TaskService taskService = new TaskServiceImpl(this);
    protected FormService formService = new FormServiceImpl();
    protected ManagementService managementService = new ManagementServiceImpl();
    protected DynamicBpmnService dynamicBpmnService = new DynamicBpmnServiceImpl(this);
    protected FlowableEventDispatcher idmEventDispatcher;
    protected CommandConfig defaultCommandConfig;
    protected CommandConfig schemaCommandConfig;
    protected CommandInterceptor commandInvoker;
    protected List<CommandInterceptor> customPreCommandInterceptors;
    protected List<CommandInterceptor> customPostCommandInterceptors;
    protected List<CommandInterceptor> commandInterceptors;
    protected CommandExecutor commandExecutor;
    protected List<SessionFactory> customSessionFactories;
    protected DbSqlSessionFactory dbSqlSessionFactory;
    protected Map<Class<?>, SessionFactory> sessionFactories;
    protected boolean enableConfiguratorServiceLoader = true;
    protected List<ProcessEngineConfigurator> configurators;
    protected List<ProcessEngineConfigurator> allConfigurators;
    protected BpmnDeployer bpmnDeployer;
    protected BpmnParser bpmnParser;
    protected List<Deployer> customPreDeployers;
    protected List<Deployer> customPostDeployers;
    protected List<Deployer> deployers;
    protected DeploymentManager deploymentManager;
    protected int processDefinitionCacheLimit = -1;
    protected DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache;
    protected int bpmnModelCacheLimit = -1;
    protected DeploymentCache<BpmnModel> bpmnModelCache;
    protected int processDefinitionInfoCacheLimit = -1;
    protected ProcessDefinitionInfoCache processDefinitionInfoCache;
    protected int knowledgeBaseCacheLimit = -1;
    protected DeploymentCache<Object> knowledgeBaseCache;
    protected List<JobHandler> customJobHandlers;
    protected Map<String, JobHandler> jobHandlers;
    protected long asyncExecutorThreadKeepAliveTime = 5000L;
    protected int asyncExecutorThreadPoolQueueSize = 100;
    protected BlockingQueue<Runnable> asyncExecutorThreadPoolQueue;
    protected long asyncExecutorSecondsToWaitOnShutdown = 60L;
    protected int asyncExecutorMaxTimerJobsPerAcquisition = 1;
    protected int asyncExecutorMaxAsyncJobsDuePerAcquisition = 1;
    protected int asyncExecutorDefaultTimerJobAcquireWaitTime = 10000;
    protected int asyncExecutorDefaultAsyncJobAcquireWaitTime = 10000;
    protected int asyncExecutorDefaultQueueSizeFullWaitTime;
    protected String asyncExecutorLockOwner;
    protected int asyncExecutorTimerLockTimeInMillis = 300000;
    protected int asyncExecutorAsyncJobLockTimeInMillis = 300000;
    protected int asyncExecutorLockRetryWaitTimeInMillis = 500;
    protected SqlSessionFactory sqlSessionFactory;
    protected TransactionFactory transactionFactory;
    protected Set<Class<?>> customMybatisMappers;
    protected Set<String> customMybatisXMLMappers;
    protected IdGenerator idGenerator;
    protected List<BpmnParseHandler> preBpmnParseHandlers;
    protected List<BpmnParseHandler> postBpmnParseHandlers;
    protected List<BpmnParseHandler> customDefaultBpmnParseHandlers;
    protected ActivityBehaviorFactory activityBehaviorFactory;
    protected ListenerFactory listenerFactory;
    protected BpmnParseFactory bpmnParseFactory;
    protected ProcessValidator processValidator;
    protected List<FormEngine> customFormEngines;
    protected Map<String, FormEngine> formEngines;
    protected List<AbstractFormType> customFormTypes;
    protected FormTypes formTypes;
    protected List<VariableType> customPreVariableTypes;
    protected List<VariableType> customPostVariableTypes;
    protected VariableTypes variableTypes;
    protected ExpressionManager expressionManager;
    protected List<String> customScriptingEngineClasses;
    protected ScriptingEngines scriptingEngines;
    protected List<ResolverFactory> resolverFactories;
    protected BusinessCalendarManager businessCalendarManager;
    protected int executionQueryLimit = 20000;
    protected int taskQueryLimit = 20000;
    protected int historicTaskQueryLimit = 20000;
    protected int historicProcessInstancesQueryLimit = 20000;
    protected String wsSyncFactoryClassName = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
    protected ConcurrentMap<QName, URL> wsOverridenEndpointAddresses = new ConcurrentHashMap();
    protected CommandContextFactory commandContextFactory;
    protected TransactionContextFactory transactionContextFactory;
    protected Map<Object, Object> beans;
    protected DelegateInterceptor delegateInterceptor;
    protected Map<String, EventHandler> eventHandlers;
    protected List<EventHandler> customEventHandlers;
    protected FailedJobCommandFactory failedJobCommandFactory;
    protected boolean enableSafeBpmnXml;
    protected int batchSizeProcessInstances = 25;
    protected int batchSizeTasks = 25;
    protected boolean isBulkInsertEnabled = true;
    protected int maxNrOfStatementsInBulkInsert = 100;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected boolean enableEventDispatcher = true;
    protected FlowableEventDispatcher eventDispatcher;
    protected boolean enableDatabaseEventLogging;
    protected DelegateExpressionFieldInjectionMode delegateExpressionFieldInjectionMode;
    protected int maxLengthStringVariableType;
    protected boolean enableProcessDefinitionInfoCache;
    protected Flowable5CompatibilityHandler flowable5CompatibilityHandler;
    protected static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();
    public static final String DATABASE_TYPE_H2 = "h2";
    public static final String DATABASE_TYPE_HSQL = "hsql";
    public static final String DATABASE_TYPE_MYSQL = "mysql";
    public static final String DATABASE_TYPE_ORACLE = "oracle";
    public static final String DATABASE_TYPE_POSTGRES = "postgres";
    public static final String DATABASE_TYPE_MSSQL = "mssql";
    public static final String DATABASE_TYPE_DB2 = "db2";

    public ProcessEngineConfigurationImpl() {
        this.delegateExpressionFieldInjectionMode = DelegateExpressionFieldInjectionMode.COMPATIBILITY;
        this.maxLengthStringVariableType = 4000;
    }

    public ProcessEngine buildProcessEngine() {
        this.init();
        return new ProcessEngineImpl(this);
    }

    protected void init() {
        this.initConfigurators();
        this.configuratorsBeforeInit();
        this.initProcessDiagramGenerator();
        this.initHistoryLevel();
        this.initExpressionManager();
        this.initVariableTypes();
        this.initBeans();
        this.initFormEngines();
        this.initFormTypes();
        this.initScriptingEngines();
        this.initClock();
        this.initBusinessCalendarManager();
        this.initCommandContextFactory();
        this.initTransactionContextFactory();
        this.initCommandExecutors();
        this.initServices();
        this.initIdGenerator();
        this.initDeployers();
        this.initJobHandlers();
        this.initDataSource();
        this.initTransactionFactory();
        this.initSqlSessionFactory();
        this.initSessionFactories();
        this.initJpa();
        this.initDelegateInterceptor();
        this.initEventHandlers();
        this.initFailedJobCommandFactory();
        this.initProcessValidator();
        this.initDatabaseEventLogging();
        this.configuratorsAfterInit();
    }

    protected void initFailedJobCommandFactory() {
        if (this.failedJobCommandFactory == null) {
            this.failedJobCommandFactory = new DefaultFailedJobCommandFactory();
        }

    }

    protected void initCommandExecutors() {
        this.initDefaultCommandConfig();
        this.initSchemaCommandConfig();
        this.initCommandInvoker();
        this.initCommandInterceptors();
        this.initCommandExecutor();
    }

    protected void initDefaultCommandConfig() {
        if (this.defaultCommandConfig == null) {
            this.defaultCommandConfig = new CommandConfig();
        }

    }

    private void initSchemaCommandConfig() {
        if (this.schemaCommandConfig == null) {
            this.schemaCommandConfig = (new CommandConfig()).transactionNotSupported();
        }

    }

    protected void initCommandInvoker() {
        if (this.commandInvoker == null) {
            this.commandInvoker = new CommandInvoker();
        }

    }

    protected void initCommandInterceptors() {
        if (this.commandInterceptors == null) {
            this.commandInterceptors = new ArrayList();
            if (this.customPreCommandInterceptors != null) {
                this.commandInterceptors.addAll(this.customPreCommandInterceptors);
            }

            this.commandInterceptors.addAll(this.getDefaultCommandInterceptors());
            if (this.customPostCommandInterceptors != null) {
                this.commandInterceptors.addAll(this.customPostCommandInterceptors);
            }

            this.commandInterceptors.add(this.commandInvoker);
        }

    }

    protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptors() {
        List<CommandInterceptor> interceptors = new ArrayList();
        interceptors.add(new LogInterceptor());
        CommandInterceptor transactionInterceptor = this.createTransactionInterceptor();
        if (transactionInterceptor != null) {
            interceptors.add(transactionInterceptor);
        }

        interceptors.add(new CommandContextInterceptor(this.commandContextFactory, this));
        return interceptors;
    }

    protected void initCommandExecutor() {
        if (this.commandExecutor == null) {
            CommandInterceptor first = this.initInterceptorChain(this.commandInterceptors);
            this.commandExecutor = new CommandExecutorImpl(this.getDefaultCommandConfig(), first);
        }

    }

    protected CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
        if (chain != null && !chain.isEmpty()) {
            for(int i = 0; i < chain.size() - 1; ++i) {
                ((CommandInterceptor)chain.get(i)).setNext((CommandInterceptor)chain.get(i + 1));
            }

            return (CommandInterceptor)chain.get(0);
        } else {
            throw new ActivitiException("invalid command interceptor chain configuration: " + chain);
        }
    }

    protected abstract CommandInterceptor createTransactionInterceptor();

    protected void initServices() {
        this.initService(this.repositoryService);
        this.initService(this.runtimeService);
        this.initService(this.historyService);
        this.initService(this.identityService);
        this.initService(this.taskService);
        this.initService(this.formService);
        this.initService(this.managementService);
        this.initService(this.dynamicBpmnService);
    }

    protected void initService(Object service) {
        if (service instanceof ServiceImpl) {
            ((ServiceImpl)service).setCommandExecutor(this.commandExecutor);
        }

    }

    protected void initDataSource() {
        if (this.dataSource == null) {
            if (this.dataSourceJndiName != null) {
                try {
                    this.dataSource = (DataSource)(new InitialContext()).lookup(this.dataSourceJndiName);
                } catch (Exception var2) {
                    throw new ActivitiException("couldn't lookup datasource from " + this.dataSourceJndiName + ": " + var2.getMessage(), var2);
                }
            } else if (this.jdbcUrl != null) {
                if (this.jdbcDriver == null || this.jdbcUsername == null) {
                    throw new ActivitiException("DataSource or JDBC properties have to be specified in a process engine configuration");
                }

                LOGGER.debug("initializing datasource to db: {}", this.jdbcUrl);
                PooledDataSource pooledDataSource = new PooledDataSource(ReflectUtil.getClassLoader(), this.jdbcDriver, this.jdbcUrl, this.jdbcUsername, this.jdbcPassword);
                if (this.jdbcMaxActiveConnections > 0) {
                    pooledDataSource.setPoolMaximumActiveConnections(this.jdbcMaxActiveConnections);
                }

                if (this.jdbcMaxIdleConnections > 0) {
                    pooledDataSource.setPoolMaximumIdleConnections(this.jdbcMaxIdleConnections);
                }

                if (this.jdbcMaxCheckoutTime > 0) {
                    pooledDataSource.setPoolMaximumCheckoutTime(this.jdbcMaxCheckoutTime);
                }

                if (this.jdbcMaxWaitTime > 0) {
                    pooledDataSource.setPoolTimeToWait(this.jdbcMaxWaitTime);
                }

                if (this.jdbcPingEnabled) {
                    pooledDataSource.setPoolPingEnabled(true);
                    if (this.jdbcPingQuery != null) {
                        pooledDataSource.setPoolPingQuery(this.jdbcPingQuery);
                    }

                    pooledDataSource.setPoolPingConnectionsNotUsedFor(this.jdbcPingConnectionNotUsedFor);
                }

                if (this.jdbcDefaultTransactionIsolationLevel > 0) {
                    pooledDataSource.setDefaultTransactionIsolationLevel(this.jdbcDefaultTransactionIsolationLevel);
                }

                this.dataSource = pooledDataSource;
            }

            if (this.dataSource instanceof PooledDataSource) {
                ((PooledDataSource)this.dataSource).forceCloseAll();
            }
        }

        if (this.databaseType == null) {
            this.initDatabaseType();
        }

    }

    protected static Properties getDefaultDatabaseTypeMappings() {
        Properties databaseTypeMappings = new Properties();
        databaseTypeMappings.setProperty("H2", "h2");
        databaseTypeMappings.setProperty("HSQL Database Engine", "hsql");
        databaseTypeMappings.setProperty("MySQL", "mysql");
        databaseTypeMappings.setProperty("Oracle", "oracle");
        databaseTypeMappings.setProperty("PostgreSQL", "postgres");
        databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
        databaseTypeMappings.setProperty("db2", "db2");
        databaseTypeMappings.setProperty("DB2", "db2");
        databaseTypeMappings.setProperty("DB2/NT", "db2");
        databaseTypeMappings.setProperty("DB2/NT64", "db2");
        databaseTypeMappings.setProperty("DB2 UDP", "db2");
        databaseTypeMappings.setProperty("DB2/LINUX", "db2");
        databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXX8664", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXPPC64", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXPPC64LE", "db2");
        databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
        databaseTypeMappings.setProperty("DB2/6000", "db2");
        databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
        databaseTypeMappings.setProperty("DB2/AIX64", "db2");
        databaseTypeMappings.setProperty("DB2/HPUX", "db2");
        databaseTypeMappings.setProperty("DB2/HP64", "db2");
        databaseTypeMappings.setProperty("DB2/SUN", "db2");
        databaseTypeMappings.setProperty("DB2/SUN64", "db2");
        databaseTypeMappings.setProperty("DB2/PTX", "db2");
        databaseTypeMappings.setProperty("DB2/2", "db2");
        databaseTypeMappings.setProperty("DB2 UDB AS400", "db2");
        return databaseTypeMappings;
    }

    public void initDatabaseType() {
        Connection connection = null;

        try {
            connection = this.dataSource.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String databaseProductName = databaseMetaData.getDatabaseProductName();
            LOGGER.debug("database product name: '{}'", databaseProductName);
            this.databaseType = databaseTypeMappings.getProperty(databaseProductName);
            if (this.databaseType == null) {
                throw new ActivitiException("couldn't deduct database type from database product name '" + databaseProductName + "'");
            }

            LOGGER.debug("using database type: {}", this.databaseType);
        } catch (SQLException var12) {
            LOGGER.error("Exception while initializing Database connection", var12);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var11) {
                LOGGER.error("Exception while closing the Database connection", var11);
            }

        }

    }

    protected void initTransactionFactory() {
        if (this.transactionFactory == null) {
            if (this.transactionsExternallyManaged) {
                this.transactionFactory = new ManagedTransactionFactory();
            } else {
                this.transactionFactory = new JdbcTransactionFactory();
            }
        }

    }

    protected void initSqlSessionFactory() {
        if (this.sqlSessionFactory == null) {
            InputStream inputStream = null;

            try {
                inputStream = this.getMyBatisXmlConfigurationSteam();
                Environment environment = new Environment("default", this.transactionFactory, this.dataSource);
                Reader reader = new InputStreamReader(inputStream);
                Properties properties = new Properties();
                properties.put("prefix", this.databaseTablePrefix);
                String wildcardEscapeClause = "";
                if (this.databaseWildcardEscapeCharacter != null && this.databaseWildcardEscapeCharacter.length() != 0) {
                    wildcardEscapeClause = " escape '" + this.databaseWildcardEscapeCharacter + "'";
                }

                properties.put("wildcardEscapeClause", wildcardEscapeClause);
                if (this.databaseType != null) {
                    properties.put("limitBefore", DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(this.databaseType));
                    properties.put("limitAfter", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(this.databaseType));
                    properties.put("limitBetween", DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(this.databaseType));
                    properties.put("limitOuterJoinBetween", DbSqlSessionFactory.databaseOuterJoinLimitBetweenStatements.get(this.databaseType));
                    properties.put("orderBy", DbSqlSessionFactory.databaseSpecificOrderByStatements.get(this.databaseType));
                    properties.put("limitBeforeNativeQuery", Objects.toString(DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.get(this.databaseType), ""));
                }

                Configuration configuration = this.initMybatisConfiguration(environment, reader, properties);
                this.sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
            } catch (Exception var10) {
                throw new ActivitiException("Error while building ibatis SqlSessionFactory: " + var10.getMessage(), var10);
            } finally {
                IoUtil.closeSilently(inputStream);
            }
        }

    }

    protected Configuration initMybatisConfiguration(Environment environment, Reader reader, Properties properties) {
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, "", properties);
        Configuration configuration = parser.getConfiguration();
        configuration.setEnvironment(environment);
        this.initMybatisTypeHandlers(configuration);
        this.initCustomMybatisMappers(configuration);
        configuration = this.parseMybatisConfiguration(configuration, parser);
        return configuration;
    }

    protected void initMybatisTypeHandlers(Configuration configuration) {
        configuration.getTypeHandlerRegistry().register(VariableType.class, JdbcType.VARCHAR, new IbatisVariableTypeHandler());
    }

    protected void initCustomMybatisMappers(Configuration configuration) {
        if (this.getCustomMybatisMappers() != null) {
            Iterator var2 = this.getCustomMybatisMappers().iterator();

            while(var2.hasNext()) {
                Class<?> clazz = (Class)var2.next();
                configuration.addMapper(clazz);
            }
        }

    }

    protected Configuration parseMybatisConfiguration(Configuration configuration, XMLConfigBuilder parser) {
        return this.parseCustomMybatisXMLMappers(parser.parse());
    }

    protected Configuration parseCustomMybatisXMLMappers(Configuration configuration) {
        if (this.getCustomMybatisXMLMappers() != null) {
            Iterator var2 = this.getCustomMybatisXMLMappers().iterator();

            while(var2.hasNext()) {
                String resource = (String)var2.next();
                XMLMapperBuilder mapperParser = new XMLMapperBuilder(this.getResourceAsStream(resource), configuration, resource, configuration.getSqlFragments());
                mapperParser.parse();
            }
        }

        return configuration;
    }

    protected InputStream getResourceAsStream(String resource) {
        return ReflectUtil.getResourceAsStream(resource);
    }

    protected InputStream getMyBatisXmlConfigurationSteam() {
        return this.getResourceAsStream("org/activiti/db/mapping/mappings.xml");
    }

    public Set<Class<?>> getCustomMybatisMappers() {
        return this.customMybatisMappers;
    }

    public void setCustomMybatisMappers(Set<Class<?>> customMybatisMappers) {
        this.customMybatisMappers = customMybatisMappers;
    }

    public Set<String> getCustomMybatisXMLMappers() {
        return this.customMybatisXMLMappers;
    }

    public void setCustomMybatisXMLMappers(Set<String> customMybatisXMLMappers) {
        this.customMybatisXMLMappers = customMybatisXMLMappers;
    }

    protected void initSessionFactories() {
        if (this.sessionFactories == null) {
            this.sessionFactories = new HashMap();
            if (this.dbSqlSessionFactory == null) {
                this.dbSqlSessionFactory = new DbSqlSessionFactory();
            }

            this.dbSqlSessionFactory.setDatabaseType(this.databaseType);
            this.dbSqlSessionFactory.setIdGenerator(this.idGenerator);
            this.dbSqlSessionFactory.setSqlSessionFactory(this.sqlSessionFactory);
            this.dbSqlSessionFactory.setDbHistoryUsed(this.isDbHistoryUsed);
            this.dbSqlSessionFactory.setDatabaseTablePrefix(this.databaseTablePrefix);
            this.dbSqlSessionFactory.setTablePrefixIsSchema(this.tablePrefixIsSchema);
            this.dbSqlSessionFactory.setDatabaseCatalog(this.databaseCatalog);
            this.dbSqlSessionFactory.setDatabaseSchema(this.databaseSchema);
            this.dbSqlSessionFactory.setBulkInsertEnabled(this.isBulkInsertEnabled, this.databaseType);
            this.dbSqlSessionFactory.setMaxNrOfStatementsInBulkInsert(this.maxNrOfStatementsInBulkInsert);
            this.addSessionFactory(this.dbSqlSessionFactory);
            this.addSessionFactory(new GenericManagerFactory(AttachmentEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(CommentEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(DeadLetterJobEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(DeploymentEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(ModelEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(ExecutionEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricActivityInstanceEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricDetailEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricProcessInstanceEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricVariableInstanceEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricTaskInstanceEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricIdentityLinkEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(IdentityLinkEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(JobEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(ProcessDefinitionEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(ProcessDefinitionInfoEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(PropertyEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(ResourceEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(ByteArrayEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(SuspendedJobEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(TableDataManager.class));
            this.addSessionFactory(new GenericManagerFactory(TaskEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(TimerJobEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(VariableInstanceEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(EventSubscriptionEntityManager.class));
            this.addSessionFactory(new GenericManagerFactory(EventLogEntryEntityManager.class));
            this.addSessionFactory(new DefaultHistoryManagerSessionFactory());
        }

        if (this.customSessionFactories != null) {
            Iterator var1 = this.customSessionFactories.iterator();

            while(var1.hasNext()) {
                SessionFactory sessionFactory = (SessionFactory)var1.next();
                this.addSessionFactory(sessionFactory);
            }
        }

    }

    protected void addSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactories.put(sessionFactory.getSessionType(), sessionFactory);
    }

    protected void initConfigurators() {
        this.allConfigurators = new ArrayList();
        if (this.configurators != null) {
            this.allConfigurators.addAll(this.configurators);
        }

        if (this.enableConfiguratorServiceLoader) {
            ClassLoader classLoader = this.getClassLoader();
            if (classLoader == null) {
                classLoader = ReflectUtil.getClassLoader();
            }

            ServiceLoader<ProcessEngineConfigurator> configuratorServiceLoader = ServiceLoader.load(ProcessEngineConfigurator.class, classLoader);
            int nrOfServiceLoadedConfigurators = 0;

            Iterator var4;
            ProcessEngineConfigurator configurator;
            for(var4 = configuratorServiceLoader.iterator(); var4.hasNext(); ++nrOfServiceLoadedConfigurators) {
                configurator = (ProcessEngineConfigurator)var4.next();
                this.allConfigurators.add(configurator);
            }

            if (nrOfServiceLoadedConfigurators > 0) {
                LOGGER.info("Found {} auto-discoverable Process Engine Configurator{}", nrOfServiceLoadedConfigurators++, nrOfServiceLoadedConfigurators > 1 ? "s" : "");
            }

            if (!this.allConfigurators.isEmpty()) {
                Collections.sort(this.allConfigurators, new Comparator<ProcessEngineConfigurator>() {
                    public int compare(ProcessEngineConfigurator configurator1, ProcessEngineConfigurator configurator2) {
                        int priority1 = configurator1.getPriority();
                        int priority2 = configurator2.getPriority();
                        if (priority1 < priority2) {
                            return -1;
                        } else {
                            return priority1 > priority2 ? 1 : 0;
                        }
                    }
                });
                LOGGER.info("Found {} Process Engine Configurators in total:", this.allConfigurators.size());
                var4 = this.allConfigurators.iterator();

                while(var4.hasNext()) {
                    configurator = (ProcessEngineConfigurator)var4.next();
                    LOGGER.info("{} (priority:{})", configurator.getClass(), configurator.getPriority());
                }
            }
        }

    }

    protected void configuratorsBeforeInit() {
        Iterator var1 = this.allConfigurators.iterator();

        while(var1.hasNext()) {
            ProcessEngineConfigurator configurator = (ProcessEngineConfigurator)var1.next();
            LOGGER.info("Executing beforeInit() of {} (priority:{})", configurator.getClass(), configurator.getPriority());
            configurator.beforeInit(this);
        }

    }

    protected void configuratorsAfterInit() {
        Iterator var1 = this.allConfigurators.iterator();

        while(var1.hasNext()) {
            ProcessEngineConfigurator configurator = (ProcessEngineConfigurator)var1.next();
            LOGGER.info("Executing configure() of {} (priority:{})", configurator.getClass(), configurator.getPriority());
            configurator.configure(this);
        }

    }

    protected void initDeployers() {
        if (this.deployers == null) {
            this.deployers = new ArrayList();
            if (this.customPreDeployers != null) {
                this.deployers.addAll(this.customPreDeployers);
            }

            this.deployers.addAll(this.getDefaultDeployers());
            if (this.customPostDeployers != null) {
                this.deployers.addAll(this.customPostDeployers);
            }
        }

        if (this.deploymentManager == null) {
            this.deploymentManager = new DeploymentManager();
            this.deploymentManager.setDeployers(this.deployers);
            if (this.bpmnModelCache == null) {
                if (this.bpmnModelCacheLimit <= 0) {
                    this.bpmnModelCache = new DefaultDeploymentCache();
                } else {
                    this.bpmnModelCache = new DefaultDeploymentCache(this.bpmnModelCacheLimit);
                }
            }

            if (this.processDefinitionInfoCache == null) {
                if (this.processDefinitionInfoCacheLimit <= 0) {
                    this.processDefinitionInfoCache = new ProcessDefinitionInfoCache(this.commandExecutor);
                } else {
                    this.processDefinitionInfoCache = new ProcessDefinitionInfoCache(this.commandExecutor, this.processDefinitionInfoCacheLimit);
                }
            }

            if (this.knowledgeBaseCache == null) {
                if (this.knowledgeBaseCacheLimit <= 0) {
                    this.knowledgeBaseCache = new DefaultDeploymentCache();
                } else {
                    this.knowledgeBaseCache = new DefaultDeploymentCache(this.knowledgeBaseCacheLimit);
                }
            }

            this.deploymentManager.setProcessDefinitionCache(this.processDefinitionCache);
            this.deploymentManager.setBpmnModelCache(this.bpmnModelCache);
            this.deploymentManager.setProcessDefinitionInfoCache(this.processDefinitionInfoCache);
            this.deploymentManager.setKnowledgeBaseCache(this.knowledgeBaseCache);
        }

    }

    protected Collection<? extends Deployer> getDefaultDeployers() {
        List<Deployer> defaultDeployers = new ArrayList();
        if (this.bpmnDeployer == null) {
            this.bpmnDeployer = new BpmnDeployer();
        }

        this.bpmnDeployer.setExpressionManager(this.expressionManager);
        this.bpmnDeployer.setIdGenerator(this.idGenerator);
        if (this.bpmnParseFactory == null) {
            this.bpmnParseFactory = new DefaultBpmnParseFactory();
        }

        if (this.activityBehaviorFactory == null) {
            DefaultActivityBehaviorFactory defaultActivityBehaviorFactory = new DefaultActivityBehaviorFactory();
            defaultActivityBehaviorFactory.setExpressionManager(this.expressionManager);
            this.activityBehaviorFactory = defaultActivityBehaviorFactory;
        } else if (this.activityBehaviorFactory instanceof AbstractBehaviorFactory && ((AbstractBehaviorFactory)this.activityBehaviorFactory).getExpressionManager() == null) {
            ((AbstractBehaviorFactory)this.activityBehaviorFactory).setExpressionManager(this.expressionManager);
        }

        if (this.listenerFactory == null) {
            DefaultListenerFactory defaultListenerFactory = new DefaultListenerFactory();
            defaultListenerFactory.setExpressionManager(this.expressionManager);
            this.listenerFactory = defaultListenerFactory;
        } else if (this.listenerFactory instanceof AbstractBehaviorFactory && ((AbstractBehaviorFactory)this.listenerFactory).getExpressionManager() == null) {
            ((AbstractBehaviorFactory)this.listenerFactory).setExpressionManager(this.expressionManager);
        }

        if (this.bpmnParser == null) {
            this.bpmnParser = new BpmnParser();
        }

        this.bpmnParser.setExpressionManager(this.expressionManager);
        this.bpmnParser.setBpmnParseFactory(this.bpmnParseFactory);
        this.bpmnParser.setActivityBehaviorFactory(this.activityBehaviorFactory);
        this.bpmnParser.setListenerFactory(this.listenerFactory);
        List<BpmnParseHandler> parseHandlers = new ArrayList();
        if (this.getPreBpmnParseHandlers() != null) {
            parseHandlers.addAll(this.getPreBpmnParseHandlers());
        }

        parseHandlers.addAll(this.getDefaultBpmnParseHandlers());
        if (this.getPostBpmnParseHandlers() != null) {
            parseHandlers.addAll(this.getPostBpmnParseHandlers());
        }

        BpmnParseHandlers bpmnParseHandlers = new BpmnParseHandlers();
        bpmnParseHandlers.addHandlers(parseHandlers);
        this.bpmnParser.setBpmnParserHandlers(bpmnParseHandlers);
        this.bpmnDeployer.setBpmnParser(this.bpmnParser);
        defaultDeployers.add(this.bpmnDeployer);
        return defaultDeployers;
    }

    protected List<BpmnParseHandler> getDefaultBpmnParseHandlers() {
        List<BpmnParseHandler> bpmnParserHandlers = new ArrayList();
        bpmnParserHandlers.add(new BoundaryEventParseHandler());
        bpmnParserHandlers.add(new BusinessRuleParseHandler());
        bpmnParserHandlers.add(new CallActivityParseHandler());
        bpmnParserHandlers.add(new CancelEventDefinitionParseHandler());
        bpmnParserHandlers.add(new CompensateEventDefinitionParseHandler());
        bpmnParserHandlers.add(new EndEventParseHandler());
        bpmnParserHandlers.add(new ErrorEventDefinitionParseHandler());
        bpmnParserHandlers.add(new EventBasedGatewayParseHandler());
        bpmnParserHandlers.add(new ExclusiveGatewayParseHandler());
        bpmnParserHandlers.add(new InclusiveGatewayParseHandler());
        bpmnParserHandlers.add(new IntermediateCatchEventParseHandler());
        bpmnParserHandlers.add(new IntermediateThrowEventParseHandler());
        bpmnParserHandlers.add(new ManualTaskParseHandler());
        bpmnParserHandlers.add(new MessageEventDefinitionParseHandler());
        bpmnParserHandlers.add(new ParallelGatewayParseHandler());
        bpmnParserHandlers.add(new ProcessParseHandler());
        bpmnParserHandlers.add(new ReceiveTaskParseHandler());
        bpmnParserHandlers.add(new ScriptTaskParseHandler());
        bpmnParserHandlers.add(new SendTaskParseHandler());
        bpmnParserHandlers.add(new SequenceFlowParseHandler());
        bpmnParserHandlers.add(new ServiceTaskParseHandler());
        bpmnParserHandlers.add(new SignalEventDefinitionParseHandler());
        bpmnParserHandlers.add(new StartEventParseHandler());
        bpmnParserHandlers.add(new SubProcessParseHandler());
        bpmnParserHandlers.add(new EventSubProcessParseHandler());
        bpmnParserHandlers.add(new TaskParseHandler());
        bpmnParserHandlers.add(new TimerEventDefinitionParseHandler());
        bpmnParserHandlers.add(new TransactionParseHandler());
        bpmnParserHandlers.add(new UserTaskParseHandler());
        if (this.customDefaultBpmnParseHandlers != null) {
            Map<Class<?>, BpmnParseHandler> customParseHandlerMap = new HashMap();
            Iterator var3 = this.customDefaultBpmnParseHandlers.iterator();

            BpmnParseHandler defaultBpmnParseHandler;
            while(var3.hasNext()) {
                defaultBpmnParseHandler = (BpmnParseHandler)var3.next();
                Iterator var5 = defaultBpmnParseHandler.getHandledTypes().iterator();

                while(var5.hasNext()) {
                    Class<?> handledType = (Class)var5.next();
                    customParseHandlerMap.put(handledType, defaultBpmnParseHandler);
                }
            }

            for(int i = 0; i < bpmnParserHandlers.size(); ++i) {
                defaultBpmnParseHandler = (BpmnParseHandler)bpmnParserHandlers.get(i);
                if (defaultBpmnParseHandler.getHandledTypes().size() != 1) {
                    StringBuilder supportedTypes = new StringBuilder();
                    Iterator var12 = defaultBpmnParseHandler.getHandledTypes().iterator();

                    while(var12.hasNext()) {
                        Class<?> type = (Class)var12.next();
                        supportedTypes.append(" ").append(type.getCanonicalName()).append(" ");
                    }

                    throw new ActivitiException("The default BPMN parse handlers should only support one type, but " + defaultBpmnParseHandler.getClass() + " supports " + supportedTypes + ". This is likely a programmatic error");
                }

                Class<?> handledType = (Class)defaultBpmnParseHandler.getHandledTypes().iterator().next();
                if (customParseHandlerMap.containsKey(handledType)) {
                    BpmnParseHandler newBpmnParseHandler = (BpmnParseHandler)customParseHandlerMap.get(handledType);
                    LOGGER.info("Replacing default BpmnParseHandler {} with {}", defaultBpmnParseHandler.getClass().getName(), newBpmnParseHandler.getClass().getName());
                    bpmnParserHandlers.set(i, newBpmnParseHandler);
                }
            }
        }

        bpmnParserHandlers.addAll(this.getDefaultHistoryParseHandlers());
        return bpmnParserHandlers;
    }

    protected List<BpmnParseHandler> getDefaultHistoryParseHandlers() {
        List<BpmnParseHandler> parseHandlers = new ArrayList();
        parseHandlers.add(new FlowNodeHistoryParseHandler());
        parseHandlers.add(new ProcessHistoryParseHandler());
        parseHandlers.add(new StartEventHistoryParseHandler());
        parseHandlers.add(new UserTaskHistoryParseHandler());
        return parseHandlers;
    }

    private void initClock() {
        if (this.clock == null) {
            this.clock = new DefaultClockImpl();
        }

    }

    protected void initProcessDiagramGenerator() {
        if (this.processDiagramGenerator == null) {
            this.processDiagramGenerator = new DefaultProcessDiagramGenerator();
        }

    }

    protected void initJobHandlers() {
        this.jobHandlers = new HashMap();
        TimerExecuteNestedActivityJobHandler timerExecuteNestedActivityJobHandler = new TimerExecuteNestedActivityJobHandler();
        this.jobHandlers.put(timerExecuteNestedActivityJobHandler.getType(), timerExecuteNestedActivityJobHandler);
        TimerCatchIntermediateEventJobHandler timerCatchIntermediateEvent = new TimerCatchIntermediateEventJobHandler();
        this.jobHandlers.put(timerCatchIntermediateEvent.getType(), timerCatchIntermediateEvent);
        TimerStartEventJobHandler timerStartEvent = new TimerStartEventJobHandler();
        this.jobHandlers.put(timerStartEvent.getType(), timerStartEvent);
        AsyncContinuationJobHandler asyncContinuationJobHandler = new AsyncContinuationJobHandler();
        this.jobHandlers.put(asyncContinuationJobHandler.getType(), asyncContinuationJobHandler);
        ProcessEventJobHandler processEventJobHandler = new ProcessEventJobHandler();
        this.jobHandlers.put(processEventJobHandler.getType(), processEventJobHandler);
        TimerSuspendProcessDefinitionHandler suspendProcessDefinitionHandler = new TimerSuspendProcessDefinitionHandler();
        this.jobHandlers.put(suspendProcessDefinitionHandler.getType(), suspendProcessDefinitionHandler);
        TimerActivateProcessDefinitionHandler activateProcessDefinitionHandler = new TimerActivateProcessDefinitionHandler();
        this.jobHandlers.put(activateProcessDefinitionHandler.getType(), activateProcessDefinitionHandler);
        if (this.getCustomJobHandlers() != null) {
            Iterator var8 = this.getCustomJobHandlers().iterator();

            while(var8.hasNext()) {
                JobHandler customJobHandler = (JobHandler)var8.next();
                this.jobHandlers.put(customJobHandler.getType(), customJobHandler);
            }
        }

    }

    public void initHistoryLevel() {
        if (this.historyLevel == null) {
            this.historyLevel = HistoryLevel.getHistoryLevelForKey(this.getHistory());
        }

    }

    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            CommandExecutor idGeneratorCommandExecutor = this.getCommandExecutor();
            DbIdGenerator dbIdGenerator = new DbIdGenerator();
            dbIdGenerator.setIdBlockSize(this.idBlockSize);
            dbIdGenerator.setCommandExecutor(idGeneratorCommandExecutor);
            dbIdGenerator.setCommandConfig(this.getDefaultCommandConfig().transactionRequiresNew());
            this.idGenerator = dbIdGenerator;
        }

    }

    protected void initCommandContextFactory() {
        if (this.commandContextFactory == null) {
            this.commandContextFactory = new CommandContextFactory();
        }

        this.commandContextFactory.setProcessEngineConfiguration(this);
    }

    protected void initTransactionContextFactory() {
        if (this.transactionContextFactory == null) {
            this.transactionContextFactory = new StandaloneMybatisTransactionContextFactory();
        }

    }

    protected void initVariableTypes() {
        if (this.variableTypes == null) {
            this.variableTypes = new DefaultVariableTypes();
            Iterator var1;
            VariableType customVariableType;
            if (this.customPreVariableTypes != null) {
                var1 = this.customPreVariableTypes.iterator();

                while(var1.hasNext()) {
                    customVariableType = (VariableType)var1.next();
                    this.variableTypes.addType(customVariableType);
                }
            }

            this.variableTypes.addType(new NullType());
            this.variableTypes.addType(new StringType(this.maxLengthStringVariableType));
            this.variableTypes.addType(new LongStringType(this.maxLengthStringVariableType + 1));
            this.variableTypes.addType(new BooleanType());
            this.variableTypes.addType(new ShortType());
            this.variableTypes.addType(new IntegerType());
            this.variableTypes.addType(new LongType());
            this.variableTypes.addType(new DateType());
            this.variableTypes.addType(new DoubleType());
            this.variableTypes.addType(new UUIDType());
            this.variableTypes.addType(new JsonType(this.maxLengthStringVariableType, this.objectMapper));
            this.variableTypes.addType(new LongJsonType(this.maxLengthStringVariableType + 1, this.objectMapper));
            this.variableTypes.addType(new ByteArrayType());
            this.variableTypes.addType(new SerializableType());
            if (this.customPostVariableTypes != null) {
                var1 = this.customPostVariableTypes.iterator();

                while(var1.hasNext()) {
                    customVariableType = (VariableType)var1.next();
                    this.variableTypes.addType(customVariableType);
                }
            }
        }

    }

    protected void initFormEngines() {
        if (this.formEngines == null) {
            this.formEngines = new HashMap();
            FormEngine defaultFormEngine = new JuelFormEngine();
            this.formEngines.put(null, defaultFormEngine);
            this.formEngines.put(defaultFormEngine.getName(), defaultFormEngine);
        }

        if (this.customFormEngines != null) {
            Iterator var3 = this.customFormEngines.iterator();

            while(var3.hasNext()) {
                FormEngine formEngine = (FormEngine)var3.next();
                this.formEngines.put(formEngine.getName(), formEngine);
            }
        }

    }

    protected void initFormTypes() {
        if (this.formTypes == null) {
            this.formTypes = new FormTypes();
            this.formTypes.addFormType(new StringFormType());
            this.formTypes.addFormType(new LongFormType());
            this.formTypes.addFormType(new DateFormType("dd/MM/yyyy"));
            this.formTypes.addFormType(new BooleanFormType());
            this.formTypes.addFormType(new DoubleFormType());
        }

        if (this.customFormTypes != null) {
            Iterator var1 = this.customFormTypes.iterator();

            while(var1.hasNext()) {
                AbstractFormType customFormType = (AbstractFormType)var1.next();
                this.formTypes.addFormType(customFormType);
            }
        }

    }

    protected void initScriptingEngines() {
        if (this.resolverFactories == null) {
            this.resolverFactories = new ArrayList();
            this.resolverFactories.add(new VariableScopeResolverFactory());
            this.resolverFactories.add(new BeansResolverFactory());
        }

        if (this.scriptingEngines == null) {
            this.scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(this.resolverFactories));
        }

    }

    protected void initExpressionManager() {
        if (this.expressionManager == null) {
            this.expressionManager = new ExpressionManager(this.beans);
        }

    }

    protected void initBusinessCalendarManager() {
        if (this.businessCalendarManager == null) {
            MapBusinessCalendarManager mapBusinessCalendarManager = new MapBusinessCalendarManager();
            mapBusinessCalendarManager.addBusinessCalendar(DurationBusinessCalendar.NAME, new DurationBusinessCalendar(this.clock));
            mapBusinessCalendarManager.addBusinessCalendar("dueDate", new DueDateBusinessCalendar(this.clock));
            mapBusinessCalendarManager.addBusinessCalendar(CycleBusinessCalendar.NAME, new CycleBusinessCalendar(this.clock));
            this.businessCalendarManager = mapBusinessCalendarManager;
        }

    }

    protected void initDelegateInterceptor() {
        if (this.delegateInterceptor == null) {
            this.delegateInterceptor = new DefaultDelegateInterceptor();
        }

    }

    protected void initEventHandlers() {
        if (this.eventHandlers == null) {
            this.eventHandlers = new HashMap();
            SignalEventHandler signalEventHander = new SignalEventHandler();
            this.eventHandlers.put(signalEventHander.getEventHandlerType(), signalEventHander);
            CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
            this.eventHandlers.put(compensationEventHandler.getEventHandlerType(), compensationEventHandler);
            MessageEventHandler messageEventHandler = new MessageEventHandler();
            this.eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
        }

        if (this.customEventHandlers != null) {
            Iterator var4 = this.customEventHandlers.iterator();

            while(var4.hasNext()) {
                EventHandler eventHandler = (EventHandler)var4.next();
                this.eventHandlers.put(eventHandler.getEventHandlerType(), eventHandler);
            }
        }

    }

    protected void initJpa() {
        if (this.jpaPersistenceUnitName != null) {
            this.jpaEntityManagerFactory = JpaHelper.createEntityManagerFactory(this.jpaPersistenceUnitName);
        }

        if (this.jpaEntityManagerFactory != null) {
            this.sessionFactories.put(EntityManagerSession.class, new EntityManagerSessionFactory(this.jpaEntityManagerFactory, this.jpaHandleTransaction, this.jpaCloseEntityManager));
            VariableType jpaType = this.variableTypes.getVariableType("jpa-entity");
            if (jpaType == null) {
                int serializableIndex = this.variableTypes.getTypeIndex("serializable");
                if (serializableIndex > -1) {
                    this.variableTypes.addType(new JPAEntityVariableType(), serializableIndex);
                } else {
                    this.variableTypes.addType(new JPAEntityVariableType());
                }
            }

            jpaType = this.variableTypes.getVariableType("jpa-entity-list");
            if (jpaType == null) {
                this.variableTypes.addType(new JPAEntityListVariableType(), this.variableTypes.getTypeIndex("jpa-entity"));
            }
        }

    }

    protected void initBeans() {
        if (this.beans == null) {
            this.beans = new HashMap();
        }

    }

    protected void initProcessValidator() {
        if (this.processValidator == null) {
            this.processValidator = (new ProcessValidatorFactory()).createDefaultProcessValidator();
        }

    }

    protected void initDatabaseEventLogging() {
        if (this.enableDatabaseEventLogging) {
            this.getEventDispatcher().addEventListener(new EventLogger(this.clock, this.objectMapper));
        }

    }

    public CommandConfig getDefaultCommandConfig() {
        return this.defaultCommandConfig;
    }

    public void setDefaultCommandConfig(CommandConfig defaultCommandConfig) {
        this.defaultCommandConfig = defaultCommandConfig;
    }

    public CommandConfig getSchemaCommandConfig() {
        return this.schemaCommandConfig;
    }

    public void setSchemaCommandConfig(CommandConfig schemaCommandConfig) {
        this.schemaCommandConfig = schemaCommandConfig;
    }

    public CommandInterceptor getCommandInvoker() {
        return this.commandInvoker;
    }

    public void setCommandInvoker(CommandInterceptor commandInvoker) {
        this.commandInvoker = commandInvoker;
    }

    public List<CommandInterceptor> getCustomPreCommandInterceptors() {
        return this.customPreCommandInterceptors;
    }

    public ProcessEngineConfigurationImpl setCustomPreCommandInterceptors(List<CommandInterceptor> customPreCommandInterceptors) {
        this.customPreCommandInterceptors = customPreCommandInterceptors;
        return this;
    }

    public List<CommandInterceptor> getCustomPostCommandInterceptors() {
        return this.customPostCommandInterceptors;
    }

    public ProcessEngineConfigurationImpl setCustomPostCommandInterceptors(List<CommandInterceptor> customPostCommandInterceptors) {
        this.customPostCommandInterceptors = customPostCommandInterceptors;
        return this;
    }

    public List<CommandInterceptor> getCommandInterceptors() {
        return this.commandInterceptors;
    }

    public ProcessEngineConfigurationImpl setCommandInterceptors(List<CommandInterceptor> commandInterceptors) {
        this.commandInterceptors = commandInterceptors;
        return this;
    }

    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }

    public ProcessEngineConfigurationImpl setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        return this;
    }

    public RepositoryService getRepositoryService() {
        return this.repositoryService;
    }

    public ProcessEngineConfigurationImpl setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
        return this;
    }

    public RuntimeService getRuntimeService() {
        return this.runtimeService;
    }

    public ProcessEngineConfigurationImpl setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
        return this;
    }

    public HistoryService getHistoryService() {
        return this.historyService;
    }

    public ProcessEngineConfigurationImpl setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
        return this;
    }

    public IdentityService getIdentityService() {
        return this.identityService;
    }

    public ProcessEngineConfigurationImpl setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
        return this;
    }

    public TaskService getTaskService() {
        return this.taskService;
    }

    public ProcessEngineConfigurationImpl setTaskService(TaskService taskService) {
        this.taskService = taskService;
        return this;
    }

    public FormService getFormService() {
        return this.formService;
    }

    public ProcessEngineConfigurationImpl setFormService(FormService formService) {
        this.formService = formService;
        return this;
    }

    public ManagementService getManagementService() {
        return this.managementService;
    }

    public DynamicBpmnService getDynamicBpmnService() {
        return this.dynamicBpmnService;
    }

    public ProcessEngineConfigurationImpl setManagementService(ManagementService managementService) {
        this.managementService = managementService;
        return this;
    }

    public ProcessEngineConfiguration getProcessEngineConfiguration() {
        return this;
    }

    public FlowableEventDispatcher getIdmEventDispatcher() {
        return this.idmEventDispatcher;
    }

    public ProcessEngineConfigurationImpl setIdmEventDispatcher(FlowableEventDispatcher idmEventDispatcher) {
        this.idmEventDispatcher = idmEventDispatcher;
        return this;
    }

    public Map<Class<?>, SessionFactory> getSessionFactories() {
        return this.sessionFactories;
    }

    public ProcessEngineConfigurationImpl setSessionFactories(Map<Class<?>, SessionFactory> sessionFactories) {
        this.sessionFactories = sessionFactories;
        return this;
    }

    public List<ProcessEngineConfigurator> getConfigurators() {
        return this.configurators;
    }

    public ProcessEngineConfigurationImpl addConfigurator(ProcessEngineConfigurator configurator) {
        if (this.configurators == null) {
            this.configurators = new ArrayList();
        }

        this.configurators.add(configurator);
        return this;
    }

    public ProcessEngineConfigurationImpl setConfigurators(List<ProcessEngineConfigurator> configurators) {
        this.configurators = configurators;
        return this;
    }

    public void setEnableConfiguratorServiceLoader(boolean enableConfiguratorServiceLoader) {
        this.enableConfiguratorServiceLoader = enableConfiguratorServiceLoader;
    }

    public List<ProcessEngineConfigurator> getAllConfigurators() {
        return this.allConfigurators;
    }

    public BpmnDeployer getBpmnDeployer() {
        return this.bpmnDeployer;
    }

    public ProcessEngineConfigurationImpl setBpmnDeployer(BpmnDeployer bpmnDeployer) {
        this.bpmnDeployer = bpmnDeployer;
        return this;
    }

    public BpmnParser getBpmnParser() {
        return this.bpmnParser;
    }

    public ProcessEngineConfigurationImpl setBpmnParser(BpmnParser bpmnParser) {
        this.bpmnParser = bpmnParser;
        return this;
    }

    public List<Deployer> getDeployers() {
        return this.deployers;
    }

    public ProcessEngineConfigurationImpl setDeployers(List<Deployer> deployers) {
        this.deployers = deployers;
        return this;
    }

    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }

    public ProcessEngineConfigurationImpl setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }

    public String getWsSyncFactoryClassName() {
        return this.wsSyncFactoryClassName;
    }

    public ProcessEngineConfigurationImpl setWsSyncFactoryClassName(String wsSyncFactoryClassName) {
        this.wsSyncFactoryClassName = wsSyncFactoryClassName;
        return this;
    }

    public ProcessEngineConfiguration addWsEndpointAddress(QName endpointName, URL address) {
        this.wsOverridenEndpointAddresses.put(endpointName, address);
        return this;
    }

    public ProcessEngineConfiguration removeWsEndpointAddress(QName endpointName) {
        this.wsOverridenEndpointAddresses.remove(endpointName);
        return this;
    }

    public ConcurrentMap<QName, URL> getWsOverridenEndpointAddresses() {
        return this.wsOverridenEndpointAddresses;
    }

    public ProcessEngineConfiguration setWsOverridenEndpointAddresses(ConcurrentMap<QName, URL> wsOverridenEndpointAdress) {
        this.wsOverridenEndpointAddresses.putAll(wsOverridenEndpointAdress);
        return this;
    }

    public Map<String, FormEngine> getFormEngines() {
        return this.formEngines;
    }

    public ProcessEngineConfigurationImpl setFormEngines(Map<String, FormEngine> formEngines) {
        this.formEngines = formEngines;
        return this;
    }

    public FormTypes getFormTypes() {
        return this.formTypes;
    }

    public ProcessEngineConfigurationImpl setFormTypes(FormTypes formTypes) {
        this.formTypes = formTypes;
        return this;
    }

    public ScriptingEngines getScriptingEngines() {
        return this.scriptingEngines;
    }

    public ProcessEngineConfigurationImpl setScriptingEngines(ScriptingEngines scriptingEngines) {
        this.scriptingEngines = scriptingEngines;
        return this;
    }

    public VariableTypes getVariableTypes() {
        return this.variableTypes;
    }

    public ProcessEngineConfigurationImpl setVariableTypes(VariableTypes variableTypes) {
        this.variableTypes = variableTypes;
        return this;
    }

    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }

    public ProcessEngineConfigurationImpl setExpressionManager(ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
        return this;
    }

    public BusinessCalendarManager getBusinessCalendarManager() {
        return this.businessCalendarManager;
    }

    public ProcessEngineConfigurationImpl setBusinessCalendarManager(BusinessCalendarManager businessCalendarManager) {
        this.businessCalendarManager = businessCalendarManager;
        return this;
    }

    public int getExecutionQueryLimit() {
        return this.executionQueryLimit;
    }

    public ProcessEngineConfigurationImpl setExecutionQueryLimit(int executionQueryLimit) {
        this.executionQueryLimit = executionQueryLimit;
        return this;
    }

    public int getTaskQueryLimit() {
        return this.taskQueryLimit;
    }

    public ProcessEngineConfigurationImpl setTaskQueryLimit(int taskQueryLimit) {
        this.taskQueryLimit = taskQueryLimit;
        return this;
    }

    public int getHistoricTaskQueryLimit() {
        return this.historicTaskQueryLimit;
    }

    public ProcessEngineConfigurationImpl setHistoricTaskQueryLimit(int historicTaskQueryLimit) {
        this.historicTaskQueryLimit = historicTaskQueryLimit;
        return this;
    }

    public int getHistoricProcessInstancesQueryLimit() {
        return this.historicProcessInstancesQueryLimit;
    }

    public ProcessEngineConfigurationImpl setHistoricProcessInstancesQueryLimit(int historicProcessInstancesQueryLimit) {
        this.historicProcessInstancesQueryLimit = historicProcessInstancesQueryLimit;
        return this;
    }

    public CommandContextFactory getCommandContextFactory() {
        return this.commandContextFactory;
    }

    public ProcessEngineConfigurationImpl setCommandContextFactory(CommandContextFactory commandContextFactory) {
        this.commandContextFactory = commandContextFactory;
        return this;
    }

    public TransactionContextFactory getTransactionContextFactory() {
        return this.transactionContextFactory;
    }

    public ProcessEngineConfigurationImpl setTransactionContextFactory(TransactionContextFactory transactionContextFactory) {
        this.transactionContextFactory = transactionContextFactory;
        return this;
    }

    public List<Deployer> getCustomPreDeployers() {
        return this.customPreDeployers;
    }

    public ProcessEngineConfigurationImpl setCustomPreDeployers(List<Deployer> customPreDeployers) {
        this.customPreDeployers = customPreDeployers;
        return this;
    }

    public List<Deployer> getCustomPostDeployers() {
        return this.customPostDeployers;
    }

    public ProcessEngineConfigurationImpl setCustomPostDeployers(List<Deployer> customPostDeployers) {
        this.customPostDeployers = customPostDeployers;
        return this;
    }

    public Map<String, JobHandler> getJobHandlers() {
        return this.jobHandlers;
    }

    public ProcessEngineConfigurationImpl setJobHandlers(Map<String, JobHandler> jobHandlers) {
        this.jobHandlers = jobHandlers;
        return this;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    public ProcessEngineConfigurationImpl setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }

    public DbSqlSessionFactory getDbSqlSessionFactory() {
        return this.dbSqlSessionFactory;
    }

    public ProcessEngineConfigurationImpl setDbSqlSessionFactory(DbSqlSessionFactory dbSqlSessionFactory) {
        this.dbSqlSessionFactory = dbSqlSessionFactory;
        return this;
    }

    public TransactionFactory getTransactionFactory() {
        return this.transactionFactory;
    }

    public ProcessEngineConfigurationImpl setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
        return this;
    }

    public List<SessionFactory> getCustomSessionFactories() {
        return this.customSessionFactories;
    }

    public ProcessEngineConfigurationImpl setCustomSessionFactories(List<SessionFactory> customSessionFactories) {
        this.customSessionFactories = customSessionFactories;
        return this;
    }

    public List<JobHandler> getCustomJobHandlers() {
        return this.customJobHandlers;
    }

    public ProcessEngineConfigurationImpl setCustomJobHandlers(List<JobHandler> customJobHandlers) {
        this.customJobHandlers = customJobHandlers;
        return this;
    }

    public List<FormEngine> getCustomFormEngines() {
        return this.customFormEngines;
    }

    public ProcessEngineConfigurationImpl setCustomFormEngines(List<FormEngine> customFormEngines) {
        this.customFormEngines = customFormEngines;
        return this;
    }

    public List<AbstractFormType> getCustomFormTypes() {
        return this.customFormTypes;
    }

    public ProcessEngineConfigurationImpl setCustomFormTypes(List<AbstractFormType> customFormTypes) {
        this.customFormTypes = customFormTypes;
        return this;
    }

    public List<String> getCustomScriptingEngineClasses() {
        return this.customScriptingEngineClasses;
    }

    public ProcessEngineConfigurationImpl setCustomScriptingEngineClasses(List<String> customScriptingEngineClasses) {
        this.customScriptingEngineClasses = customScriptingEngineClasses;
        return this;
    }

    public List<VariableType> getCustomPreVariableTypes() {
        return this.customPreVariableTypes;
    }

    public ProcessEngineConfigurationImpl setCustomPreVariableTypes(List<VariableType> customPreVariableTypes) {
        this.customPreVariableTypes = customPreVariableTypes;
        return this;
    }

    public List<VariableType> getCustomPostVariableTypes() {
        return this.customPostVariableTypes;
    }

    public ProcessEngineConfigurationImpl setCustomPostVariableTypes(List<VariableType> customPostVariableTypes) {
        this.customPostVariableTypes = customPostVariableTypes;
        return this;
    }

    public List<BpmnParseHandler> getPreBpmnParseHandlers() {
        return this.preBpmnParseHandlers;
    }

    public ProcessEngineConfigurationImpl setPreBpmnParseHandlers(List<BpmnParseHandler> preBpmnParseHandlers) {
        this.preBpmnParseHandlers = preBpmnParseHandlers;
        return this;
    }

    public List<BpmnParseHandler> getCustomDefaultBpmnParseHandlers() {
        return this.customDefaultBpmnParseHandlers;
    }

    public ProcessEngineConfigurationImpl setCustomDefaultBpmnParseHandlers(List<BpmnParseHandler> customDefaultBpmnParseHandlers) {
        this.customDefaultBpmnParseHandlers = customDefaultBpmnParseHandlers;
        return this;
    }

    public List<BpmnParseHandler> getPostBpmnParseHandlers() {
        return this.postBpmnParseHandlers;
    }

    public ProcessEngineConfigurationImpl setPostBpmnParseHandlers(List<BpmnParseHandler> postBpmnParseHandlers) {
        this.postBpmnParseHandlers = postBpmnParseHandlers;
        return this;
    }

    public ActivityBehaviorFactory getActivityBehaviorFactory() {
        return this.activityBehaviorFactory;
    }

    public ProcessEngineConfigurationImpl setActivityBehaviorFactory(ActivityBehaviorFactory activityBehaviorFactory) {
        this.activityBehaviorFactory = activityBehaviorFactory;
        return this;
    }

    public ListenerFactory getListenerFactory() {
        return this.listenerFactory;
    }

    public ProcessEngineConfigurationImpl setListenerFactory(ListenerFactory listenerFactory) {
        this.listenerFactory = listenerFactory;
        return this;
    }

    public BpmnParseFactory getBpmnParseFactory() {
        return this.bpmnParseFactory;
    }

    public ProcessEngineConfigurationImpl setBpmnParseFactory(BpmnParseFactory bpmnParseFactory) {
        this.bpmnParseFactory = bpmnParseFactory;
        return this;
    }

    public Map<Object, Object> getBeans() {
        return this.beans;
    }

    public ProcessEngineConfigurationImpl setBeans(Map<Object, Object> beans) {
        this.beans = beans;
        return this;
    }

    public List<ResolverFactory> getResolverFactories() {
        return this.resolverFactories;
    }

    public ProcessEngineConfigurationImpl setResolverFactories(List<ResolverFactory> resolverFactories) {
        this.resolverFactories = resolverFactories;
        return this;
    }

    public DeploymentManager getDeploymentManager() {
        return this.deploymentManager;
    }

    public ProcessEngineConfigurationImpl setDeploymentManager(DeploymentManager deploymentManager) {
        this.deploymentManager = deploymentManager;
        return this;
    }

    public ProcessEngineConfigurationImpl setDelegateInterceptor(DelegateInterceptor delegateInterceptor) {
        this.delegateInterceptor = delegateInterceptor;
        return this;
    }

    public DelegateInterceptor getDelegateInterceptor() {
        return this.delegateInterceptor;
    }

    public EventHandler getEventHandler(String eventType) {
        return (EventHandler)this.eventHandlers.get(eventType);
    }

    public ProcessEngineConfigurationImpl setEventHandlers(Map<String, EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
        return this;
    }

    public Map<String, EventHandler> getEventHandlers() {
        return this.eventHandlers;
    }

    public List<EventHandler> getCustomEventHandlers() {
        return this.customEventHandlers;
    }

    public ProcessEngineConfigurationImpl setCustomEventHandlers(List<EventHandler> customEventHandlers) {
        this.customEventHandlers = customEventHandlers;
        return this;
    }

    public FailedJobCommandFactory getFailedJobCommandFactory() {
        return this.failedJobCommandFactory;
    }

    public ProcessEngineConfigurationImpl setFailedJobCommandFactory(FailedJobCommandFactory failedJobCommandFactory) {
        this.failedJobCommandFactory = failedJobCommandFactory;
        return this;
    }

    public int getBatchSizeProcessInstances() {
        return this.batchSizeProcessInstances;
    }

    public ProcessEngineConfigurationImpl setBatchSizeProcessInstances(int batchSizeProcessInstances) {
        this.batchSizeProcessInstances = batchSizeProcessInstances;
        return this;
    }

    public int getBatchSizeTasks() {
        return this.batchSizeTasks;
    }

    public ProcessEngineConfigurationImpl setBatchSizeTasks(int batchSizeTasks) {
        this.batchSizeTasks = batchSizeTasks;
        return this;
    }

    public int getProcessDefinitionCacheLimit() {
        return this.processDefinitionCacheLimit;
    }

    public ProcessEngineConfigurationImpl setProcessDefinitionCacheLimit(int processDefinitionCacheLimit) {
        this.processDefinitionCacheLimit = processDefinitionCacheLimit;
        return this;
    }

    public DeploymentCache<ProcessDefinitionCacheEntry> getProcessDefinitionCache() {
        return this.processDefinitionCache;
    }

    public ProcessEngineConfigurationImpl setProcessDefinitionCache(DeploymentCache<ProcessDefinitionCacheEntry> processDefinitionCache) {
        this.processDefinitionCache = processDefinitionCache;
        return this;
    }

    public int getKnowledgeBaseCacheLimit() {
        return this.knowledgeBaseCacheLimit;
    }

    public ProcessEngineConfigurationImpl setKnowledgeBaseCacheLimit(int knowledgeBaseCacheLimit) {
        this.knowledgeBaseCacheLimit = knowledgeBaseCacheLimit;
        return this;
    }

    public DeploymentCache<Object> getKnowledgeBaseCache() {
        return this.knowledgeBaseCache;
    }

    public ProcessEngineConfigurationImpl setKnowledgeBaseCache(DeploymentCache<Object> knowledgeBaseCache) {
        this.knowledgeBaseCache = knowledgeBaseCache;
        return this;
    }

    public boolean isEnableSafeBpmnXml() {
        return this.enableSafeBpmnXml;
    }

    public ProcessEngineConfigurationImpl setEnableSafeBpmnXml(boolean enableSafeBpmnXml) {
        this.enableSafeBpmnXml = enableSafeBpmnXml;
        return this;
    }

    public FlowableEventDispatcher getEventDispatcher() {
        return this.eventDispatcher;
    }

    public void setEventDispatcher(FlowableEventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
    }

    public void setEnableEventDispatcher(boolean enableEventDispatcher) {
        this.enableEventDispatcher = enableEventDispatcher;
    }

    public ProcessValidator getProcessValidator() {
        return this.processValidator;
    }

    public void setProcessValidator(ProcessValidator processValidator) {
        this.processValidator = processValidator;
    }

    public boolean isEnableEventDispatcher() {
        return this.enableEventDispatcher;
    }

    public boolean isEnableDatabaseEventLogging() {
        return this.enableDatabaseEventLogging;
    }

    public ProcessEngineConfigurationImpl setEnableDatabaseEventLogging(boolean enableDatabaseEventLogging) {
        this.enableDatabaseEventLogging = enableDatabaseEventLogging;
        return this;
    }

    public int getMaxLengthStringVariableType() {
        return this.maxLengthStringVariableType;
    }

    public ProcessEngineConfigurationImpl setMaxLengthStringVariableType(int maxLengthStringVariableType) {
        this.maxLengthStringVariableType = maxLengthStringVariableType;
        return this;
    }

    public ProcessEngineConfigurationImpl setBulkInsertEnabled(boolean isBulkInsertEnabled) {
        this.isBulkInsertEnabled = isBulkInsertEnabled;
        return this;
    }

    public boolean isBulkInsertEnabled() {
        return this.isBulkInsertEnabled;
    }

    public int getMaxNrOfStatementsInBulkInsert() {
        return this.maxNrOfStatementsInBulkInsert;
    }

    public void setMaxNrOfStatementsInBulkInsert(int maxNrOfStatementsInBulkInsert) {
        this.maxNrOfStatementsInBulkInsert = maxNrOfStatementsInBulkInsert;
    }

    public DelegateExpressionFieldInjectionMode getDelegateExpressionFieldInjectionMode() {
        return this.delegateExpressionFieldInjectionMode;
    }

    public void setDelegateExpressionFieldInjectionMode(DelegateExpressionFieldInjectionMode delegateExpressionFieldInjectionMode) {
        this.delegateExpressionFieldInjectionMode = delegateExpressionFieldInjectionMode;
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    public ProcessEngineConfigurationImpl setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        return this;
    }

    public Flowable5CompatibilityHandler getFlowable5CompatibilityHandler() {
        return this.flowable5CompatibilityHandler;
    }

    public ProcessEngineConfigurationImpl setFlowable5CompatibilityHandler(Flowable5CompatibilityHandler flowable5CompatibilityHandler) {
        this.flowable5CompatibilityHandler = flowable5CompatibilityHandler;
        return this;
    }

    public long getAsyncExecutorThreadKeepAliveTime() {
        return this.asyncExecutorThreadKeepAliveTime;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorThreadKeepAliveTime(long asyncExecutorThreadKeepAliveTime) {
        this.asyncExecutorThreadKeepAliveTime = asyncExecutorThreadKeepAliveTime;
        return this;
    }

    public int getAsyncExecutorThreadPoolQueueSize() {
        return this.asyncExecutorThreadPoolQueueSize;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorThreadPoolQueueSize(int asyncExecutorThreadPoolQueueSize) {
        this.asyncExecutorThreadPoolQueueSize = asyncExecutorThreadPoolQueueSize;
        return this;
    }

    public BlockingQueue<Runnable> getAsyncExecutorThreadPoolQueue() {
        return this.asyncExecutorThreadPoolQueue;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorThreadPoolQueue(BlockingQueue<Runnable> asyncExecutorThreadPoolQueue) {
        this.asyncExecutorThreadPoolQueue = asyncExecutorThreadPoolQueue;
        return this;
    }

    public long getAsyncExecutorSecondsToWaitOnShutdown() {
        return this.asyncExecutorSecondsToWaitOnShutdown;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorSecondsToWaitOnShutdown(long asyncExecutorSecondsToWaitOnShutdown) {
        this.asyncExecutorSecondsToWaitOnShutdown = asyncExecutorSecondsToWaitOnShutdown;
        return this;
    }

    public int getAsyncExecutorMaxTimerJobsPerAcquisition() {
        return this.asyncExecutorMaxTimerJobsPerAcquisition;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorMaxTimerJobsPerAcquisition(int asyncExecutorMaxTimerJobsPerAcquisition) {
        this.asyncExecutorMaxTimerJobsPerAcquisition = asyncExecutorMaxTimerJobsPerAcquisition;
        return this;
    }

    public int getAsyncExecutorMaxAsyncJobsDuePerAcquisition() {
        return this.asyncExecutorMaxAsyncJobsDuePerAcquisition;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorMaxAsyncJobsDuePerAcquisition(int asyncExecutorMaxAsyncJobsDuePerAcquisition) {
        this.asyncExecutorMaxAsyncJobsDuePerAcquisition = asyncExecutorMaxAsyncJobsDuePerAcquisition;
        return this;
    }

    public int getAsyncExecutorDefaultTimerJobAcquireWaitTime() {
        return this.asyncExecutorDefaultTimerJobAcquireWaitTime;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorDefaultTimerJobAcquireWaitTime(int asyncExecutorTimerJobAcquireWaitTime) {
        this.asyncExecutorDefaultTimerJobAcquireWaitTime = asyncExecutorTimerJobAcquireWaitTime;
        return this;
    }

    public int getAsyncExecutorDefaultAsyncJobAcquireWaitTime() {
        return this.asyncExecutorDefaultAsyncJobAcquireWaitTime;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorDefaultAsyncJobAcquireWaitTime(int asyncExecutorDefaultAsyncJobAcquireWaitTime) {
        this.asyncExecutorDefaultAsyncJobAcquireWaitTime = asyncExecutorDefaultAsyncJobAcquireWaitTime;
        return this;
    }

    public int getAsyncExecutorDefaultQueueSizeFullWaitTime() {
        return this.asyncExecutorDefaultQueueSizeFullWaitTime;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorDefaultQueueSizeFullWaitTime(int asyncExecutorDefaultQueueSizeFullWaitTime) {
        this.asyncExecutorDefaultQueueSizeFullWaitTime = asyncExecutorDefaultQueueSizeFullWaitTime;
        return this;
    }

    public String getAsyncExecutorLockOwner() {
        return this.asyncExecutorLockOwner;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorLockOwner(String asyncExecutorLockOwner) {
        this.asyncExecutorLockOwner = asyncExecutorLockOwner;
        return this;
    }

    public int getAsyncExecutorTimerLockTimeInMillis() {
        return this.asyncExecutorTimerLockTimeInMillis;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorTimerLockTimeInMillis(int asyncExecutorTimerLockTimeInMillis) {
        this.asyncExecutorTimerLockTimeInMillis = asyncExecutorTimerLockTimeInMillis;
        return this;
    }

    public int getAsyncExecutorAsyncJobLockTimeInMillis() {
        return this.asyncExecutorAsyncJobLockTimeInMillis;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorAsyncJobLockTimeInMillis(int asyncExecutorAsyncJobLockTimeInMillis) {
        this.asyncExecutorAsyncJobLockTimeInMillis = asyncExecutorAsyncJobLockTimeInMillis;
        return this;
    }

    public int getAsyncExecutorLockRetryWaitTimeInMillis() {
        return this.asyncExecutorLockRetryWaitTimeInMillis;
    }

    public ProcessEngineConfigurationImpl setAsyncExecutorLockRetryWaitTimeInMillis(int asyncExecutorLockRetryWaitTimeInMillis) {
        this.asyncExecutorLockRetryWaitTimeInMillis = asyncExecutorLockRetryWaitTimeInMillis;
        return this;
    }
}
