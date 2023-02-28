import java.util.List;
import org.javamoney.moneta.Money;

public record Expense(String title, Person payer, List<Person> participants, Money amount) {
}
