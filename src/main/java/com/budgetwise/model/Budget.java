
package com.budgetwise.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public class Budget {
    private YearMonth month;
    private BigDecimal monthlyLimit;

    public Budget(YearMonth month, BigDecimal monthlyLimit) {
        this.month = month;
        this.monthlyLimit = monthlyLimit;
    }

    public YearMonth getMonth() { return month; }
    public BigDecimal getMonthlyLimit() { return monthlyLimit; }

    public void setMonth(YearMonth month) { this.month = month; }
    public void setMonthlyLimit(BigDecimal monthlyLimit) {
        this.monthlyLimit = monthlyLimit;
    }
}
