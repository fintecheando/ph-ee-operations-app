package org.apache.fineract.paymenthub.domain;

import org.apache.fineract.organisation.parent.AbstractPersistableCustom;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "m_currency_rates")
public class CurrencyRate extends AbstractPersistableCustom {
    @Column(name = "from_currency")
    private String fromCurrency;

    @Column(name = "to_currency")
    private String toCurrency;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    public CurrencyRate(String fromCurrency, String toCurrency, BigDecimal rate, Date lastUpdated) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
        this.lastUpdated = lastUpdated;
    }

    public CurrencyRate() {
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public CurrencyRateLock getLock(String uniqueKey, Date expireBy) {
        return new CurrencyRateLock(uniqueKey, fromCurrency, toCurrency, rate, expireBy);
    }
}
