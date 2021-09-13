package com.jxblog.snowflake.snowflake.controller;

import com.jxblog.snowflake.snowflake.snowflake.SnowResponse;
import com.jxblog.snowflake.snowflake.snowflake.Snowflake;
import com.jxblog.snowflake.snowflake.snowflake.SnowflakeRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class SnowflakeController {

    private static final long SNOW_FLAKE_FAILED_ID = -1L;

    @RequestMapping(value = "/snowId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SnowResponse> asssignUid(@Valid @RequestBody SnowflakeRequest reqBody) {
        if (!reqBody.isPassed()) {
            return generateRes(null, SNOW_FLAKE_FAILED_ID, HttpStatus.BAD_REQUEST,
                    "Repeated Request, Please wait for the previous request finishes");
        }
        long workerId = reqBody.getWorkerId();
        long datacenterId = reqBody.getDataCenterId();
        Snowflake flake = new Snowflake(workerId, datacenterId);
        ResponseEntity<SnowResponse> res;
        try {
            res = generateRes(flake, flake.nextID(), HttpStatus.OK, "successful");
        } catch (IllegalStateException e) {
            res = generateRes(flake, SNOW_FLAKE_FAILED_ID, HttpStatus.SERVICE_UNAVAILABLE,
                    "Snow flake generation failed, please check cluster");
        }
        return res;
    }

    private ResponseEntity<SnowResponse> generateRes(Snowflake flake, long flakeId, HttpStatus status, String info) {
        if (flake == null) {
            flake = new Snowflake(2, 2);
        }
        return new ResponseEntity<>(new SnowResponse(flakeId, new Date(flake.currentTime()), info), status);
    }
}
