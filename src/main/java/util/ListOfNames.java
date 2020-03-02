package util;

import java.util.ArrayList;
import java.util.List;

public class ListOfNames {
    private List<String> data = new ArrayList<>();

    public ListOfNames(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
