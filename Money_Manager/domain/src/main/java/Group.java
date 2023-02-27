import java.util.ArrayList;
import java.util.Map;
import org.javamoney.moneta.Money;

public record Group(String title,
                    ArrayList<Person> participants,
                    Map<Person, Map<Person, Money>> debts) {

}
