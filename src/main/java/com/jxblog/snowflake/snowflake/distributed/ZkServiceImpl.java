package com.jxblog.snowflake.snowflake.distributed;

import java.util.List;

public class ZkServiceImpl implements ZkService {
    @Override
    public void reElectMaster() {
        
    }

    @Override
    public boolean maasterAlive() {
        return false;
    }

    @Override
    public boolean isMaster() {
        return false;
    }

    @Override
    public List<String> getLiveNodes() {
        return null;
    }

    @Override
    public void addListNode(String name, String data) {

    }
}
