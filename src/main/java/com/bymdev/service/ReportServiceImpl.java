package com.bymdev.service;

import com.bymdev.pojo.DailyIncome;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oleksii on 18.12.16.
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DailyIncome> dailyIncome() {
        Query q = entityManager.createNativeQuery("select o.order_date, sum(o.total) " +
                "from t_order o " +
                "group by o.order_date " +
                "order by o.order_date desc");
        List<Object[]> resultList = q.getResultList();
        List<DailyIncome> report = new ArrayList<DailyIncome>();

        for(Object[] o : resultList) {
            report.add(new DailyIncome((Date)o[0], (BigDecimal) o[1]));
        }

        return report;
    }
}
