package top.meethigher.export;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import org.slf4j.Logger;
import top.meethigher.dbmonitor.constant.DBDriver;
import top.meethigher.dbmonitor.model.DBMonitorProperty;
import top.meethigher.dbmonitor.model.sizemonitor.DBSize;
import top.meethigher.dbmonitor.model.sizemonitor.TableSize;
import top.meethigher.dbmonitor.model.structuremonitor.ColumnInfo;
import top.meethigher.dbmonitor.model.structuremonitor.TableStructure;
import top.meethigher.dbmonitor.service.DBMonitor;
import top.meethigher.dbmonitor.service.DefaultDBMonitor;
import top.meethigher.export.model.Database;

import java.util.List;

/**
 * 参数
 *
 * @author chenchuancheng
 * @since 2023/3/11 1:55
 */
public class Args {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Args.class);


    private static final DBMonitor<DBSize, TableStructure> monitor = new DefaultDBMonitor();


    @Parameter(names = {"--host", "-h"}, description = "目标主机host")
    public String host = "192.168.110.138";

    @Parameter(names = {"--port", "-p"}, description = "目标主机port", validateWith = PositiveInteger.class)
    public int port = 5432;

    @Parameter(names = "-u", description = "数据库用户名")
    public String user = "postgres";

    @Parameter(names = "--passwd", description = "密码", password = true)
    public String password = "postgres";

    @Parameter(names = "--db", description = "数据库名")
    public String db = "ccc";

    @Parameter(names = "-f", description = "导出文件名称")
    public String exportFileName = "数据库结构说明.doc";

    @Parameter(names = "--title", description = "文件标题")
    public String exportTitle = "数据库结构说明";

    @Parameter(names = "-t", description = "数据库类型 0-postgresql 1-mysql 2-oracle 3-sqlserver 4-db2")
    public int type = 0;

    @Parameter(names = "--help", help = true)
    public boolean help;


    public static Database toArgsObject(String... args) {
        Args argsObj = new Args();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(argsObj)
                .build();
        jCommander.parse(args);
        if (argsObj.help) {
            jCommander.usage();
            System.exit(0);
        }
        Database db = new Database();
        db.setExportFileName(argsObj.exportFileName);
        db.setExportTitle(argsObj.exportTitle);
        DBMonitorProperty property = new DBMonitorProperty();
        property.setUsername(argsObj.user);
        property.setPassword(argsObj.password);
        switch (argsObj.type) {
            case 1:
                property.setDriver(DBDriver.MYSQL8.driverClass);
                property.setJdbcUrl(String.format(DBDriver.MYSQL5.jdbcUrl, argsObj.host, argsObj.port, argsObj.db));
                break;
            case 2:
                property.setDriver(DBDriver.ORACLE.driverClass);
                property.setJdbcUrl(String.format(DBDriver.ORACLE.jdbcUrl, argsObj.host, argsObj.port, argsObj.db));
                break;
            case 3:
                property.setDriver(DBDriver.SQLSERVER.driverClass);
                property.setJdbcUrl(String.format(DBDriver.SQLSERVER.jdbcUrl, argsObj.host, argsObj.port, argsObj.db));
                break;
            default:
                property.setDriver(DBDriver.PSQL.driverClass);
                property.setJdbcUrl(String.format(DBDriver.PSQL.jdbcUrl, argsObj.host, argsObj.port, argsObj.db));
        }
        DBSize dbSize = monitor.queryDBSize(property);
        List<TableSize> detailSize = dbSize.getDetailSize();
        log.info("查询该数据库共有{}张表", detailSize.size());
        for (int i = 0; i < detailSize.size(); i++) {
            TableSize tableSize = detailSize.get(i);
            Database.Table table = new Database.Table();
            table.setTableName(tableSize.getTableName());
            TableStructure tableStructure = monitor.queryTableStructure(property, tableSize.getTableName());
            List<ColumnInfo> columnInfos = tableStructure.getColumnInfos();
            if (columnInfos != null && columnInfos.size() != 0) {
                for (ColumnInfo columnInfo : columnInfos) {
                    table.getColumnInfos().add(columnInfo);
                }
                db.getTableList().add(table);
                log.info("第{}张表数据成功获取", i);
            } else {
                log.warn("第{}张表没有数据", i);
            }
        }
        return db;
    }
}
