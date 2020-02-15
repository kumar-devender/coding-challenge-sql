package exercise.parser;

import exercise.domain.Table;

import java.io.BufferedReader;
import java.io.IOException;

public interface Parser {
    boolean canHandle(Class type);

    Table parse(BufferedReader br) throws IOException;
}
