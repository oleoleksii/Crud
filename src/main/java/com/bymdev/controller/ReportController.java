package com.bymdev.controller;

import com.bymdev.pojo.DailyIncome;
import com.bymdev.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by oleksii on 18.12.16.
 */
@Api
@RestController
public class ReportController {
    @Autowired
    ReportService reportService;

    //Daily income
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not found")
    })
    @RequestMapping(value = "/report/dailyIncome", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DailyIncome>> getDailyIncome() {
        List<DailyIncome> dailyIncomes = reportService.dailyIncome();

        if (dailyIncomes.isEmpty())
        {
            return new ResponseEntity<List<DailyIncome>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<DailyIncome>>(dailyIncomes, HttpStatus.OK);
    }
}
