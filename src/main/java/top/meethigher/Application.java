package top.meethigher;

import top.meethigher.export.Args;
import top.meethigher.export.model.Database;
import top.meethigher.export.utils.WordUtils;

/**
 * 程序入口
 *
 * @author chenchuancheng
 * @since 2023/3/11 1:53
 */
public class Application {

    public static void main(String... args) {
        Database database = Args.toArgsObject(args);
        WordUtils.toWord(database);
    }
}
