package exercise.parser;

import exercise.domain.Table;
import exercise.domain.User;
import exercise.util.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserParser implements Parser {

    private Function<String, User> mapToUser = (row) -> {
        String[] columns = row.split(Constant.COMMA);
        return new User(Integer.parseInt(columns[0]), columns[1], columns[2]);
    };

    @Override
    public boolean canHandle(Class type) {
        return User.class == type;
    }

    @Override
    public Table parse(BufferedReader br) throws IOException {
        Table table;
        List<User> users = br.lines()
                .map(mapToUser)
                .collect(Collectors.toList());
        table = new Table(users);
        br.close();
        return table;
    }
}
