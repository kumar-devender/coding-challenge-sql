package exercise;

import exercise.domain.Purchase;
import exercise.domain.Table;
import exercise.domain.User;
import exercise.domain.UserPurchase;
import exercise.parser.Parser;
import exercise.util.Constant;
import exercise.util.TypeResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.*;

import static java.util.Arrays.stream;

public class Sql {

    private List<Parser> parsers;

    public Sql(List<Parser> parsers) {
        this.parsers = parsers;
    }

    public Table init(InputStream csvContent) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvContent))) {
            String header = br.readLine();
            String[] columns = header.split(Constant.COMMA);
            return getParser(TypeResolver.getType(columns[0]))
                    .map(parser -> parseToTable(br, parser))
                    .orElseThrow(() -> new IllegalStateException("Invalid file."));
        }
    }

    private Table parseToTable(BufferedReader br, Parser parser) {
        try {
            return parser.parse(br);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Table orderByDesc(Table table, String columnName) {
        if (table.getRows() == null || table.getRows().size() == 0) {
            return table;
        }

        if (table.getRows().get(0) instanceof User) {
            table.getRows().sort(TypeResolver.getUserComparator(columnName));
            return table;
        } else if (table.getRows().get(0) instanceof Purchase) {
            table.getRows().sort(TypeResolver.getPurchaseComparator(columnName));
            return table;
        }
        throw new IllegalStateException("Unsupported type table.");
    }

    public Table join(Table left, Table right, String joinColumnTableLeft, String joinColumnTableRight) {
        List<User> userList;
        List<Purchase> purchaseList;
        if (left.getRows().get(0) instanceof User) {
            userList = left.getRows();
            purchaseList = right.getRows();
        } else {
            userList = right.getRows();
            purchaseList = left.getRows();
        }
        Set<UserPurchase> join = new HashSet<>();
        for (User user : userList) {
            join(user, joinColumnTableLeft, join, purchaseList, joinColumnTableRight);
        }
        return new Table(new ArrayList<>(join));
    }

    private static Field apply(Field field) {
        field.setAccessible(true);
        return field;
    }

    private void join(User user, String userProperty, Set<UserPurchase> join, List<Purchase> purchaseList, String purchaseProperty) {
        purchaseList.stream()
                .forEach(purchase -> {
                    Class<?> userClazz = user.getClass();
                    Class<?> purchaseClazz = purchase.getClass();
                    try {
                        Field userField = makeFieldAccessible(userClazz, userProperty);
                        Field purchaseField = makeFieldAccessible(purchaseClazz, purchaseProperty);
                        String userValue = userField.get(user).toString();
                        String purchaseValue = purchaseField.get(purchase).toString();
                        if (userValue.equals(purchaseValue)) {
                            join.add(buildUserPurchase(user, purchase));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    private UserPurchase buildUserPurchase(User user, Purchase purchase) {
        UserPurchase userPurchase = new UserPurchase();
        userPurchase.setAdId(purchase.getAdId());
        userPurchase.setTitle(purchase.getTitle());
        userPurchase.setUserId(user.getUserId());
        userPurchase.setName(user.getName());
        userPurchase.setEmail(user.getEmail());
        return userPurchase;
    }

    private Field makeFieldAccessible(Class<?> clazz, String joinColumnTableRight) {
        Field[] purchaseFields = clazz.getDeclaredFields();
        return stream(purchaseFields)
                .filter(field -> field.getName().equals(joinColumnTableRight))
                .findFirst()
                .map(Sql::apply)
                .orElse(null);
    }


    private Optional<Parser> getParser(Class type) {
        return parsers.stream()
                .filter(parser -> parser.canHandle(type))
                .findFirst();
    }
}
