package org.sobadfish.skywars.tools;

import cn.nukkit.utils.Config;

/**
 * 配置更新工具类
 *
 * @author LT_Name
 */
public class ConfigUpdateUtils {

    public static void updateConfig(Config config) {
        if (!config.exists("enable-Record")) {
            config.set("enable-Record", false);
            config.save();
        }
    }

}
