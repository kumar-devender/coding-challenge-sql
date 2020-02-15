package exercise.util;

import exercise.domain.Purchase;
import exercise.domain.User;

import java.util.Comparator;

public class TypeResolver {

    public static Class getType(String firstColumn) {
        switch (firstColumn) {
            case "USER_ID":
                return User.class;
            case "AD_ID":
                return Purchase.class;
            default:
                throw new IllegalStateException("Invalid file.");
        }
    }

    public static Comparator<Purchase> getPurchaseComparator(String columnName) {
        switch (columnName) {
            case Constant.AD_ID:
                return Comparator.comparingInt(Purchase::getAdId).reversed();
            case Constant.TITLE:
                return Comparator.comparing(Purchase::getTitle).reversed();
            case Constant.USER_ID:
                return Comparator.comparingInt(Purchase::getUserId).reversed();
            default:
                throw new IllegalStateException("Invalid column name.");
        }
    }

    public static Comparator<User> getUserComparator(String columnName) {
        switch (columnName) {
            case Constant.EMAIL:
                return Comparator.comparing(User::getEmail).reversed();
            case Constant.NAME:
                return Comparator.comparing(User::getName).reversed();
            case Constant.USER_ID:
                return Comparator.comparingInt(User::getUserId).reversed();
            default:
                throw new IllegalStateException("Invalid column name.");
        }
    }

}
