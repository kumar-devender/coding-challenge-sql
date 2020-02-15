package exercise.parser;

import exercise.domain.Purchase;
import exercise.domain.Table;
import exercise.util.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PurchaseParser implements Parser {
    private Function<String, Purchase> mapToPurchase = (row) -> {
        String[] columns = row.split(Constant.COMMA);
        return new Purchase(Integer.parseInt(columns[0]), columns[1], Integer.parseInt(columns[2]));

    };

    @Override
    public boolean canHandle(Class type) {
        return Purchase.class == type;
    }

    @Override
    public Table parse(BufferedReader br) throws IOException {
        List<Purchase> purchases = br.lines()
                .map(mapToPurchase)
                .collect(Collectors.toList());
        Table table = new Table(purchases);
        br.close();
        return table;
    }
}
