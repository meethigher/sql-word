package top.meethigher.export.utils;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.rtf.RtfWriter2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.meethigher.dbmonitor.model.structuremonitor.ColumnInfo;
import top.meethigher.export.model.Database;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 生成Word工具类
 *
 * @author chenchuancheng
 * @since 2023/3/11 1:54
 */
public class WordUtils {


    private static final Logger log = LoggerFactory.getLogger(WordUtils.class);

    /**
     * 转换为word
     *
     * @param database 数据库信息
     * @see <a href="https://github.com/zhengqingya/java-workspace">参考地址</a>
     */
    public static void toWord(Database database) {
        log.info("开始生成文档...");
        Document document = new Document(PageSize.A4);
        try {
            // 创建文件
            File file = new File(database.getExportFileName());
            if (file.exists() && file.isFile()) {
                file.delete();
            }
            file.createNewFile();
            // 写入文件信息
            RtfWriter2.getInstance(document, new FileOutputStream(database.getExportFileName()));
            document.open();
            Paragraph ph = new Paragraph();
            Font f = new Font();
            Paragraph p = new Paragraph(database.getExportTitle(), new Font(Font.NORMAL, 24, Font.BOLDITALIC, new Color(0, 0, 0)));
            p.setAlignment(1);
            document.add(p);
            ph.setFont(f);
            List<Database.Table> tables = database.getTableList();
            for (int i = 0; i < tables.size(); i++) {
                String table_name = tables.get(i).getTableName();
                String table_comment = null;
                String all = "" + (i + 1) + " 表名称:" + table_name + "（" + table_comment + "）";
                Table table = new Table(6);
                document.add(new Paragraph(""));
                table.setBorderWidth(1);
                table.setPadding(0);
                table.setSpacing(0);
                //添加表头的元素，并设置表头背景的颜色
                Color chade = new Color(176, 196, 222);
                Cell cell = new Cell("编号");
                addCell(table, cell, chade);
                cell = new Cell("字段名");
                addCell(table, cell, chade);
                cell = new Cell("类型");
                addCell(table, cell, chade);
                cell = new Cell("是否非空");
                addCell(table, cell, chade);
                cell = new Cell("是否主键");
                addCell(table, cell, chade);
                cell = new Cell("注释");
                addCell(table, cell, chade);
                table.endHeaders();
                // 表格的主体
                List<ColumnInfo> fileds = tables.get(i).getColumnInfos();
                for (int k = 0; k < fileds.size(); k++) {
                    addContent(table, cell, (k + 1) + "");
                    addContent(table, cell, fileds.get(k).getColumnName());
                    addContent(table, cell, fileds.get(k).getColumnType());
                    addContent(table, cell, fileds.get(k).isColumnNullable() ? "是" : "否");
                    addContent(table, cell, fileds.get(k).isPrimaryKey() ? "是" : "否");
                    addContent(table, cell, fileds.get(k).getColumnRemark());
                }
                Paragraph pheae = new Paragraph(all);
                //写入表说明
                document.add(pheae);
                //生成表格
                document.add(table);
            }
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成文档失败: {}", e.getMessage());
        }
        log.info("生成文档成功！");
    }

    /**
     * 添加表头到表格
     *
     * @param table
     * @param cell
     * @param chade
     */
    private static void addCell(Table table, Cell cell, Color chade) {
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(chade);
        table.addCell(cell);
    }

    /**
     * 添加内容到表格
     *
     * @param table
     * @param content
     */
    private static void addContent(Table table, Cell cell, String content) {
        cell = new Cell(content);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

}
