package com.jxblog.snowflake.snowflake;

import com.jxblog.snowflake.snowflake.snowflake.SnowResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.sql.SQLException;

@ControllerAdvice
public class ExceptionNotifier {
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<SnowResponse> handleSqlException() {
        SnowResponse res = new SnowResponse();
        res.setErrorMsg("Sorry, the db doesn't permit your current operation!");
        return new ResponseEntity<>(res, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
