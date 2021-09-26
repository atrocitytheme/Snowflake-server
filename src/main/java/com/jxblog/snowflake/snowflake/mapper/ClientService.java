package com.jxblog.snowflake.snowflake.mapper;

import com.jxblog.snowflake.snowflake.snowflake.SnowflakeMachineInfo;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClientService implements ClientDao {

    @Autowired
    private SqlSessionTemplate tmplt;

    @Override
    public List<SnowflakeMachineInfo> listMachineInfo() {
        return tmplt.selectList("listMachineInfo");
    }

    @Override
    public void insertMachineInfo(SnowflakeMachineInfo info) {
        tmplt.insert("insertMachineInfo", info);
    }
}
