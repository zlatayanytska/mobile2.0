package com.yanytska.mobileapp.data.repository;

import com.yanytska.mobileapp.data.model.Fare;

import java.util.ArrayList;
import java.util.List;

public class FareRepository implements IRepository {

    private List<Fare> fareList;

    public FareRepository(List<Fare> list) {
        this.fareList = list;
    }

    public List<Fare> getByName(final String name) {
        List<Fare> fares = new ArrayList<>();

        for (Fare fare : fareList) {
            if (fare.getUuid().equals(name)) {
                fares.add(fare);
            }
        }

        return fares;
    }

    @Override
    public List<Fare> getList() {
        return fareList;
    }

    @Override
    public void create(Object item) {
        fareList.add((Fare) item);
    }
}
