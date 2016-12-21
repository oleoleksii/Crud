package com.bymdev.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by oleksii on 18.12.16.
 */
public class DailyIncome {
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date orderDate;

    private BigDecimal total;

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public DailyIncome(Date orderDate, BigDecimal total) {
        this.orderDate = orderDate;
        this.total = total;
    }
}
