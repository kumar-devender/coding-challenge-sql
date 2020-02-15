package exercise;

import exercise.domain.Purchase;
import exercise.domain.Table;
import exercise.domain.User;
import exercise.domain.UserPurchase;
import exercise.parser.PurchaseParser;
import exercise.parser.UserParser;
import exercise.util.Constant;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SqlTest {
    private static FileReader CSV_READER;
    private static Sql target;

    @BeforeClass
    public static void init() {
        CSV_READER = new FileReader();
        target = new Sql(Arrays.asList(new UserParser(), new PurchaseParser()));
    }

    @Test
    public void testInitForPurchase() throws IOException {
        Table<Purchase> table = target.init(CSV_READER.getFileAsStream(Constant.PURCHASE_FILE));
        Assert.assertNotNull(table.getRows());
        Assert.assertSame(table.getRows().size(), 8);
    }

    @Test(expected = NullPointerException.class)
    public void testInitWithInvalidFileName() throws IOException {
        Table<Purchase> table = target.init(CSV_READER.getFileAsStream("invalid.csv"));
        Assert.assertNotNull(table.getRows());
        Assert.assertSame(table.getRows().size(), 8);
    }


    @Test
    public void testInitForUsers() throws IOException {
        Table<User> table = target.init(CSV_READER.getFileAsStream(Constant.USER_FILE));
        Assert.assertNotNull(table.getRows());
        Assert.assertSame(table.getRows().size(), 5);
    }

    @Test
    public void testUserTableOrderByUserIdDesc() {
        List<User> userList = getUsers();
        Table<User> userTable = new Table(userList);
        Table<User> afterSort = target.orderByDesc(userTable, "userId");
        Assert.assertSame(afterSort.getRows().get(0).getUserId(), 5);
        Assert.assertSame(afterSort.getRows().get(1).getUserId(), 4);
        Assert.assertSame(afterSort.getRows().get(2).getUserId(), 3);
        Assert.assertSame(afterSort.getRows().get(3).getUserId(), 2);
        Assert.assertSame(afterSort.getRows().get(4).getUserId(), 1);
    }

    @Test(expected = IllegalStateException.class)
    public void testUserTableOrderByInvalidColumnName() {
        List<User> userList = getUsers();
        Table<User> userTable = new Table(userList);
        target.orderByDesc(userTable, "INVALID");
    }

    @Test(expected = IllegalStateException.class)
    public void testOrderByUnSupportedType() {
        List<Object> userList = new ArrayList<>();
        userList.add(new Object());
        userList.add(new Object());
        userList.add(new Object());
        Table<User> userTable = new Table(userList);
        target.orderByDesc(userTable, "userId");
    }

    @Test
    public void testJoinWithCommonJoinColumn() {
        List<User> userList = getUsers();
        List<Purchase> purchaseList = getPurchases();

        Table<User> userTable = new Table(userList);
        Table<User> purchaseTable = new Table(purchaseList);
        Table<UserPurchase> userPurchaseTable = target.join(userTable, purchaseTable, "userId", "userId");
        Assert.assertSame(userPurchaseTable.getRows().size(), 8);
        boolean noUserWithIdFive = userPurchaseTable.getRows().stream().filter(item -> 5 == item.getUserId()).findFirst().isPresent();
        long countUserWithIdOne = userPurchaseTable.getRows().stream().filter(item -> 1 == item.getUserId()).count();
        Assert.assertFalse(noUserWithIdFive);
        Assert.assertSame(4L, countUserWithIdOne);
    }

    @Test
    public void testJoinWithNonCommonJoinColumn() {
        List<Purchase> purchaseList = getPurchases();
        List<User> userList = getUsers();
        Table<User> userTable = new Table(userList);
        Table<User> purchaseTable = new Table(purchaseList);
        Table<UserPurchase> userPurchaseTable = target.join(userTable, purchaseTable, "name", "userId");
        Assert.assertSame(userPurchaseTable.getRows().size(), 0);
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(2, "manuel", "manuel@foo.de"));
        userList.add(new User(1, "andre", "andre@bar.de"));
        userList.add(new User(3, "swen", "swen@foo.de"));
        userList.add(new User(5, "paul", "paul@foo.de"));
        userList.add(new User(4, "lydia", "lydia@bar.de"));
        return userList;
    }

    private List<Purchase> getPurchases() {
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(new Purchase(1, "car", 1));
        purchaseList.add(new Purchase(2, "car", 1));
        purchaseList.add(new Purchase(3, "car", 1));
        purchaseList.add(new Purchase(4, "guitar", 2));
        purchaseList.add(new Purchase(5, "guitar", 3));
        purchaseList.add(new Purchase(6, "table", 4));
        purchaseList.add(new Purchase(7, "table", 4));
        purchaseList.add(new Purchase(9, "chair", 1));
        return purchaseList;
    }

}