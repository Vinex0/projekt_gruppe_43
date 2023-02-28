import java.security.InvalidParameterException;
import org.javamoney.moneta.Money;


public class CalculationHelpers {

  static Money paymentShare(Money amount, int numberOfPeople) {
    if (numberOfPeople <= 1 || amount.isLessThan(Money.of(0, "EUR"))) {
      throw new InvalidParameterException();
    }
    return amount.divide(numberOfPeople);
  }

  static Money difference(Money amount_person_a, Money amount_person_b) {
    return amount_person_a.subtract(amount_person_b);
  }

}
