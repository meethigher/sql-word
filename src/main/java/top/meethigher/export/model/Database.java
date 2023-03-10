package top.meethigher.export.model;

import top.meethigher.dbmonitor.model.structuremonitor.ColumnInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * 数据库信息
 *
 * @author chenchuancheng
 * @since 2023/3/11 1:53
 */
public class Database {


    private String exportFileName;

    private String exportTitle;

    private final List<Table> tableList = new LinkedList<>();

    public List<Table> getTableList() {
        return tableList;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String getExportTitle() {
        return exportTitle;
    }

    public void setExportTitle(String exportTitle) {
        this.exportTitle = exportTitle;
    }


    /**
     * 表信息
     *
     * @author chenchuancheng
     * @since 2023/3/11 1:54
     */
    public static final class Table {

        private String tableName;

        private final List<ColumnInfo> columnInfos = new LinkedList<>();


        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public List<ColumnInfo> getColumnInfos() {
            return columnInfos;
        }
    }
}




