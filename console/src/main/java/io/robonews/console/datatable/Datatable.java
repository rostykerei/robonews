package io.robonews.console.datatable;

import java.util.ArrayList;
import java.util.List;

public class Datatable<E> {

    private long draw = 0;

    private long recordsTotal = 0;

    private long recordsFiltered = 0;

    private List<E> data = new ArrayList<E>();

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<E> getData() {
        return data;
    }

    public void setData(List<E> data) {
        this.data = data;
    }
}
