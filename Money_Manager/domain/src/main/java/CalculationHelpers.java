import java.security.InvalidParameterException;
import org.javamoney.moneta.Money;


public class CalculationHelpers {

  static Money paymentShare(Money amount, int numberOfPeople) {
    if (numberOfPeople <= 1 || amount.isLessThan(Money.of(0, "EUR"))) {
      throw new InvalidParameterException();
    }
    return amount.divide(numberOfPeople);
  }

}
