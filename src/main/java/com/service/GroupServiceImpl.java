package com.service;

import com.dao.GroupDao;
import com.model.ClassGroup;
import com.model.enums.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupDao groupDao;

    public void addGroup(ClassGroup group){
        groupDao.addGroup(group);
    }

    public List<ClassGroup> getQueryResults(String query) { return groupDao.getQueryResults(query); }

    public List<ClassGroup> getAllClassGroup(){ return groupDao.getAllClassGroup(); }

    public ClassGroup getClassGroupById(String id){ return groupDao.getClassGroupById(id); }

    public ClassGroup getGroupBySubjectId(String id){ return  groupDao.getGroupBySubjectId(id); }

    public List<Integer> getLevelsByStage(Stage stage) { return groupDao.getLevelsByStage(stage); }

    public List<String> getGroupsByStageAndLevel(Stage stage, int level) { return groupDao.getGroupsByStageAndLevel(stage, level); }

    public ClassGroup getGroupsByStageAndLevelAndGroupAndCourse(ClassGroup groupTmp) { return groupDao.getGroupsByStageAndLevelAndGroupAndCourse(groupTmp); }
}
