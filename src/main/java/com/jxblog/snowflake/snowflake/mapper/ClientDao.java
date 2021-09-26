package com.jxblog.snowflake.snowflake.mapper;

import com.jxblog.snowflake.snowflake.snowflake.SnowflakeMachineInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientDao {
    String COLUMN_LIST = "" +
            "s_machine_id, s_datacenter_id, " +
            "s_datacenter_name, s_machine_name, " +
            "s_description, s_deleted, s_ip";
    @Select("select * FROM s_machines")
    @Results({
            @Result(column = "s_machine_id", property = "machineId"),
            @Result(column = "s_datacenter_id", property = "dataCenterId"),
            @Result(column = "s_datacenter_name", property = "description"),
            @Result(column = "s_machine_name", property = "machineName"),
            @Result(column = "s_description", property = "description"),
            @Result(column = "s_deleted", property = "deleted"),
            @Result(column = "s_ip", property = "ip")
    })
    List<SnowflakeMachineInfo> listMachineInfo();

    @Insert("INSERT INTO s_machines (" + COLUMN_LIST + ")" +
            " VALUES(#{machineId}, #{dataCenterId}, \"\n" +
            "            + \"#{dataCenteName}, #{machineName}, \"\n" +
            "            + \"#{description}, #{deleted}, #{ip})")
    void insertMachineInfo(SnowflakeMachineInfo info);

}
