package com.jxblog.snowflake.snowflake.distributed;

import java.util.List;

public interface ZkService {
    void reElectMaster();
    boolean maasterAlive();
    boolean isMaster();
    List<String> getLiveNodes();
    void addListNode(String name, String data);
}
