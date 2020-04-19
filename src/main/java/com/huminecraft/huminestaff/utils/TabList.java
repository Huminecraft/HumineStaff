package com.huminecraft.huminestaff.utils;


import com.huminecraft.huminestaff.main.StaffMain;

public class TabList {

    private String header;
    private String footer;

    public TabList() {
        this.header = "§d>>§5§lHumine§fCraft§r§d§r<<\n§5=§0=§5=§0=§5=§0=§5=§0=§5=§0=§5=§0=§5=§0=§5=§0=§5=§0=";
        this.footer = "";
    }

    public String getHeader() {
        return this.header;
    }

    public void updateFooter() {
        this.footer = "§5=§0=§5=§0=§5=§0=§5=§0= §5Connectés: §f" + StaffMain.getInstance().getServer().getOnlinePlayers().size() + " §5=§0=§5=§0=§5=§0=§5=§0=";
    }

    public String getFooter() {
        return this.footer;
    }
}
