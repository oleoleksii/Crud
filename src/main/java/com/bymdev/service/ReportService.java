package com.bymdev.service;

import com.bymdev.pojo.DailyIncome;

import java.util.List;

/**
 * Created by oleksii on 18.12.16.
 */
public interface ReportService {
    List<DailyIncome> dailyIncome();
}
