package com.gruppe43.moneymanager.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.javamoney.moneta.Money;

public record Group(String title,
                    List<Person> participants,
                    ArrayList<Expense> expenses,
                    Map<Person, Map<Person, Money>> debts) {

}
