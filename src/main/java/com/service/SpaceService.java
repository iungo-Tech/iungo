package com.service;


import com.model.Space;

import java.util.List;

public interface SpaceService {

    void addSpace(Space space);

    List<Space> getQueryResults(String query);

    Space getByIdWithTimeline(String id);

    Space getById(String id);

    void deleteSpace(Space space);

    List<Space> getAll();
}
