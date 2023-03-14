package com.gruppe43.moneymanager.domain;

import java.security.InvalidParameterException;
import java.util.Map;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CalculationHelperTest {

  @Test
  void paymentShareTestValid() {
    Money amount = Money.of(12.4, "EUR");
    assertThat(CalculationHelpers.paymentShare(amount, 4)).isEqualTo(Money.of(3.1, "EUR"));
  }

  @Test
  void paymentShareTestInvalidPeopleCount() {
    Money amount = Money.of(12.4, "EUR");
    assertThrows(InvalidParameterException.class, () -> CalculationHelpers.paymentShare(amount, 0));
  }

  @Test
  void paymentShareTestInvalidAmount() {
    Money amount = Money.of(-1, "EUR");
    assertThrows(InvalidParameterException.class, () -> CalculationHelpers.paymentShare(amount, 3));
  }

}
