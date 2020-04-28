package com.dao;

import com.model.ClassGroup;
import com.model.RoleTeacher;
import com.model.Subject;
import com.model.UserSubject;

import java.util.List;
import java.util.Set;

public interface SubjectDao {

    void addSubject(Subject subject);

    void deleteSubject(Subject subject);

    List<Subject> getQueryResults(String query);

    Subject getByIdWithAll(String id);

    Subject getByIdWithChapters(String id);

    Subject getById(String id);

    Set<RoleTeacher> getTeachersBySubjectId(String id);

    List<Subject> getByGroup(String groupId);

    UserSubject getUserSubjectByUserAndSubject(String userId, String subjectId);

    List<Subject> getByGroupNoTeachers(String groupId);

}
