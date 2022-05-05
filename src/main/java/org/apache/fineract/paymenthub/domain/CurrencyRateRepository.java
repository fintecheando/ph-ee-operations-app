package org.apache.fineract.paymenthub.domain;

import org.springframework.data.repository.CrudRepository;

public interface CurrencyRateRepository extends CrudRepository<CurrencyRate, Long> {

    CurrencyRate findOneByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);
}
