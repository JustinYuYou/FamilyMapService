package util;

import model.Event;

import java.util.ArrayList;
import java.util.List;

public class ListOfEvents {
    private List<Event> data = new ArrayList<>();

    public ListOfEvents(List<Event> data) {
        this.data = data;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }
}
