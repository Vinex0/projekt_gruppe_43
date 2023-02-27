import java.util.ArrayList;
import org.javamoney.moneta.Money;

public record Expense(String title, Person payer, ArrayList<Person> participants, Money amount) {
}
