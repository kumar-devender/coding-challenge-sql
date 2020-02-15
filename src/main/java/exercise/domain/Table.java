package exercise.domain;

import java.util.List;

public class Table<T> {
    private List<T> rows;

    public Table(List<T> rows) {
        this.rows = rows;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
