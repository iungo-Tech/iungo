package com.service;

import com.dao.IncidenceDao;
import com.model.Comment;
import com.model.Incidence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class IncidenceServiceImpl implements IncidenceService {

    @Autowired
    IncidenceDao incidenceDao;

    public void addIncidence(Incidence incidence){ incidenceDao.addIncidence(incidence); }

    public Incidence getIncidenceById(String id) { return incidenceDao.getIncidenceById(id); }

    public Incidence getIncidenceByProcedureId(String id) { return incidenceDao.getIncidenceByProcedureId(id); }

    public List<Incidence> getIncidenceByStudentId(String id) { return incidenceDao.getIncidenceByStudentId(id); }
    
    // COMMENT

    public void addComment(Comment comment){ incidenceDao.addComment(comment); }

    public Comment getCommentById(String id) { return incidenceDao.getCommentById(id); }
}
