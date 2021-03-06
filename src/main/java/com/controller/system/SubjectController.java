package com.controller.system;

import com.model.*;
import com.model.enums.Role;
import com.service.*;
import javafx.animation.Timeline;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Controller
public class SubjectController {

    private static final Logger logger = LogManager.getLogger(SubjectController.class);


    @Autowired
    SubjectService subjectService;

    @Autowired
    SpaceService spaceService;

    @Autowired
    GroupService groupService;

    @Autowired
    TimeLineService timeLineService;

    @Autowired
    UserService userService;

    /**
     * Processes the petition to get to the subject creation page.
     *
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/subject/creation")
    public ModelAndView getSubjectCreationForm() {
        Subject subject = new Subject();
        subject.setSubjectGroup(new ClassGroup());
        List<ClassGroup> groups;
        try {
             groups = groupService.getAllClassGroup();
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  ClassGroups loaded successfully");
        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  ClassGroups could not be loaded: " + e);
            return null;
        }

        ModelAndView model = new ModelAndView("system/createSubject");
        model.addObject("subject", subject);
        model.addObject("groups", groups);
        return model;
    }

    /**
     * Processes the creation of a new subject by using all parameters from the "New Subject" form.
     *
     * @param subject the space with all its elements
     * @return returns the user to the main page with an url
     */
    @RequestMapping(value = "/subject/creation", method = RequestMethod.POST)
    public String createProcedure(@Valid @ModelAttribute("subject") Subject subject) {

        try {
            subjectService.addSubject(subject);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  New subject successfully created");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject could not be saved: " + e);
            return null;
        }
        return "redirect:/subject/relateTeacher?subjectId=" + subject.getSubjectId();
    }

    /**
     * Processes the petition to get to the subject modification page.
     *
     * @param subjectId the id of the specific subject to modify
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/subject/modify/{subjectId}", method = RequestMethod.GET)
    public ModelAndView getSubjectModify(@PathVariable String subjectId) {

        try {
            Subject subject = subjectService.getByIdWithAll(subjectId);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject loaded successfully");
            return new ModelAndView("system/updateSubject", "subject", subject);

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject could not be loaded: " + e);
            return null;
        }

    }

    /**
     * Processes the update of a specific space by using all parameters from the "Modify Space" form or processes the
     * petition to create a new time line for the space.
     *
     * @param subject the updated space with all its elements
     * @param selection the option that the user selects
     * @return returns the user to a page depending on the users previous choice
     */
    @RequestMapping(value = "/subject/modify", method = RequestMethod.POST)
    public ModelAndView updateSubjectModify(@Valid @ModelAttribute("subject") Subject subject, @ModelAttribute("buttonName") String selection) {

        try {
            ClassGroup group = groupService.getClassGroupById(subject.getGroupId());
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Group successfully loaded");
            subject.setSubjectGroup(group);

            subject.setTeachers(subjectService.getTeachersBySubjectId(subject.getSubjectId()));
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teachers successfully loaded");

            subjectService.addSubject(subject);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject successfully saved");
            subject = subjectService.getByIdWithAll(subject.getSubjectId());


        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject could not be saved: " + e);
            return null;
        }

        ModelAndView model;

        logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Selection value= " + selection);
        switch (selection) {
            case "add":
                model = new ModelAndView("system/addTimeline");
                List<Space> spaces;

                try {
                     spaces = spaceService.getAll();
                    logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  All spaces loaded");

                }catch (Exception e){
                    logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  All spaces could not be loaded: " + e);
                    return null;
                }

                TimeLine timeline = new TimeLine();
                timeline.setTimelineSubjectId(subject.getSubjectId());
                model.addObject("spaces", spaces);
                model.addObject("timeline", timeline);
                break;

            case "addTeacher":
                List<User> teachers;
                Set<RoleTeacher> SubjectTeachers = subject.getTeachers();
                try {
                    teachers = userService.getAllUsersWithRole(Role.TEACHER);
                    for (RoleTeacher teacher : SubjectTeachers){
                        teachers.remove(teacher.getUserR());
                    }
                    logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  All teachers loaded");

                }catch (Exception e){
                    logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teachers could not be loaded: " + e);
                    return null;
                }
                model = new ModelAndView("system/relateSubjectTeacher");
                model.addObject("subject", subject);
                model.addObject("teachers", teachers);
                break;

            default:
                model = new ModelAndView("system/updateSubject", "subject", subject);
                break;
        }

        return model;

    }

    /**
     * Processes the creation of a new time line by using all parameters from the "New TimeLine" form.
     *
     * @param timeLine the time line with all its elements
     * @return returns the user to the previous page with an url
     */
    @RequestMapping(value = "/subject/add/timeline", method = RequestMethod.POST)
    public String addTimeLine(@ModelAttribute("timeline") TimeLine timeLine){

        try {
            Space space = spaceService.getById(timeLine.getTimelineSpaceId());
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Space loaded successfully");
            Subject subject = subjectService.getById(timeLine.getTimelineSubjectId());
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject loaded successfully");
            RoleTeacher rt = userService.getTeacherById(timeLine.getTimelineTeacherId());
            timeLine.setTeacher(rt);
            timeLine.setSubjectTimeLine(subject);
            timeLine.setSpaceTimeLine(space);
            timeLineService.addTimeLine(timeLine);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Timeline saved successfully");
        }
        catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Timeline could not be saved: " + e);
        }

        return "redirect:/subject/modify/"+timeLine.getTimelineSubjectId()+".do";
    }

    /**
     * Method created in order to response an Ajax petition. It gets all booked hours in a space.
     *
     * @param id the id of the space in which we want to book a time line
     * @param weekday day of the week to book
     * and the other for the finishing hours
     */
    @RequestMapping("/subject/requestHours")
    public @ResponseBody
    JSONObject showAddTimeLine(@RequestParam("id") String id, @RequestParam("weekday") String weekday) {

        Space selectedSpace;

        try {
            selectedSpace = spaceService.getByIdWithTimeline(id);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Space loaded successfully");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Space could not be loaded: " + e);
            return null;
        }

        List<String> bookedHours = new ArrayList(); // Start and meantime booked hours
        List<String> endHours = new ArrayList(); // Meantime and finish booked hours

        endHours.add("8:00"); // We add this hour to the endHours as we can't finish a class at 8:00
        bookedHours.add("17:30"); // We add this hour to the bookedHours as we can't start a class at 17:30

        if (selectedSpace != null) {
            for (TimeLine t : selectedSpace.getTimelines()) { // For all the timelines in a specific space
                if ((t.getWeekday().toString().toLowerCase()).equals(weekday.toLowerCase())) { // If the time line is in within the weekday

                    // Start and finishing hours parse
                    String startHour = t.getStartingHour().split(":")[0];
                    String startMin = t.getStartingHour().split(":")[1];
                    int finishHour = Integer.parseInt(t.getFinishingHour().split(":")[0]);
                    int finishMin = Integer.parseInt(t.getFinishingHour().split(":")[1]);

                    bookedHours.add(startHour + ":" + startMin);
                    // If the starting hour is an o'clock we add the half past time to both lists
                    if("00".equals(startMin)) {
                        bookedHours.add(startHour + ":30");
                        endHours.add(startHour + ":30");
                    }
                    // Navigation in all in between hours
                    for (int index = Integer.parseInt(startHour)+1; index < finishHour; index = index + 1) {
                        bookedHours.add(index + ":00");
                        bookedHours.add(index + ":30");

                        endHours.add(index + ":00");
                        endHours.add(index + ":30");
                    }
                    // Same process as starting hour in reverse
                    if (finishMin == 30) {
                        bookedHours.add(finishHour + ":00");
                        endHours.add(finishHour + ":00");
                    }
                    endHours.add(finishHour+":"+finishMin);
                }
            }
        }
        JSONObject data = new JSONObject();
        data.put("start", bookedHours);
        data.put("end", endHours);
        return data;
    }

    @RequestMapping("/subject/requestAvailableTeachers")
    public @ResponseBody
    JSONArray requestTeachersAjax(@RequestParam("subjectId") String subjectId, @RequestParam("weekday") String weekday,
                               @RequestParam("start") String start, @RequestParam("end") String end) {
        JSONArray availableTeachers = new JSONArray();
        Set<RoleTeacher> teachers = subjectService.getTeachersBySubjectId(subjectId);
        int sh = Integer.parseInt(start.split("s")[0]+start.split("s")[1]);
        int fh = Integer.parseInt(end.split("s")[0]+end.split("s")[1]);
        for(RoleTeacher rt : teachers){
            Boolean available = true;
            List<TimeLine> timelines = userService.getTeacherByIdWithTimelines(rt.getUserR().getUserId()).getTimelines();
            for(TimeLine timeLine : timelines) {
                if (available){
                    if (timeLine.getWeekday().toString().equals(weekday)) {
                        int SH = Integer.parseInt(timeLine.getStartingHour().split(":")[0]+timeLine.getStartingHour().split(":")[1]);
                        int FH = Integer.parseInt(timeLine.getFinishingHour().split(":")[0]+timeLine.getFinishingHour().split(":")[1]);

                        if (!(SH > sh && SH > fh) || !(FH < sh && FH < fh)){
                            available = false;
                        }
                    }
                }else{
                    break;
                }
            }
            JSONObject data = new JSONObject();
            data.put("name", rt.getUserR().getName());
            data.put("surname", rt.getUserR().getSurname());
            data.put("secondsurname", rt.getUserR().getSecondSurname());
            data.put("userid", rt.getUserR().getUserId());
            data.put("available", available.toString());
            availableTeachers.add(data);

        }
        return availableTeachers;
    }

    /**
     * Processes the removal of a specific time line.
     *
     * @param timeLineId the id of the time line to delete
     * @return returns the user to the previous page with an url
     */
    @RequestMapping(value = "/subject/delete/timeline", method = RequestMethod.GET)
    public String deleteTimeline(@RequestParam String timeLineId, HttpServletRequest request){

        try {
            timeLineService.deleteTimeLine(timeLineService.getById(timeLineId));
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Timeline was removed successfully");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Timeline could not be removed: " + e);
            return null;
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    /**
     * Processes the removal of a specific time line.
     *
     * @param teacherId the id of the teacher to delete from subject with subjectId
     * @return returns the user to the previous page with an url
     */
    @RequestMapping(value = "/subject/delete/teacher", method = RequestMethod.GET)
    public String deleteTeacher(@RequestParam String teacherId, String subjectId, HttpServletRequest request){

        try {
            Subject subject = subjectService.getByIdWithAll(subjectId);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject loaded successfully");
            User teacher = userService.getUserById(teacherId);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teacher loaded successfully");
            subject.deleteTeacher((RoleTeacher) teacher.getRoleClass(Role.TEACHER));
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teacher removed from subject successfully");
            subjectService.addSubject(subject);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Timeline could not be saved");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teacher could not be removed of subject: " + e);
            return null;
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    /**
     * Processes the removal of a specific subject.
     *
     * @param subjectId the id of the subject to delete
     * @return returns the user to the previous page with an url
     */
    @RequestMapping(value = "/subject/delete", method = RequestMethod.GET)
    public String deleteSubject(@RequestParam String subjectId, HttpServletRequest request){

        try {
            subjectService.deleteSubject(subjectService.getById(subjectId));
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject was removed successfully");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject could not be removed: " + e);
            return null;
        }

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

    @RequestMapping(value = "/subject/setTeacher")
    public String setTeacherSubjectRelationship(@RequestParam String subjectId, @RequestParam String teachersId, HttpServletRequest request){

        //get responsibles
        String teachersSplitted[] = teachersId.split(",");

        Subject subject;

        try {
            subject = subjectService.getByIdWithAll(subjectId);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject loaded successfully");
        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject could not be loaded: " + e);
            return null;
        }

        for (String id : teachersSplitted){
            subject.addTeacher((RoleTeacher) userService.getUserById(id).getRoleClass(Role.TEACHER));
        }

        try {
            subjectService.addSubject(subject); // addUser = add or update
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject was saved successfully");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject could not be saved: "+ e );
        }

        String referer = request.getHeader("Referer");

        try{
            referer = referer.substring(referer.length()-15);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  referer: "+ referer);

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Could not cut substring of referer "+ e );
            return "redirect:/";
        }

        if("/subject/modify".equals(referer))
            return "redirect:/subject/modify/" + subjectId;

        logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  referer: "+ referer);

        return "redirect:/";
    }


    @RequestMapping(value = "/subject/relateTeacher", method = RequestMethod.GET)
    public ModelAndView relateSubjectTeacher(@RequestParam String subjectId){

        Subject subject;
        List<User> teachers;

        try{
            subject = subjectService.getById(subjectId);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject was loaded successfully");
            teachers = userService.getAllUsersWithRole(Role.TEACHER);
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teachers were loaded successfully");

        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Subject or Teachers could not be loaded: " + e);
            return null;
        }

        ModelAndView model = new ModelAndView("system/relateSubjectTeacher");

        model.addObject("subject", subject);
        model.addObject("teachers", teachers);


        return model;
    }

    @RequestMapping("/subject/requestTeachers")
    public @ResponseBody
    JSONArray showAddTimeLine(@RequestParam("department") String dept) {
        List<User> teachers;
        try{
            logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  department= " + dept);
            if(dept != null) {
                teachers = userService.getTeachersByDepartment(dept);
                logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teachers loaded by department successfully");
            }else{
                teachers = userService.getTeachers();
                logger.info("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  All teachers loaded successfully");
            }
        }catch (Exception e){
            logger.error("["+new Object(){}.getClass().getEnclosingMethod().getName()+"] -  Teachers could not be loaded: " + e);
            return null;
        }


        JSONArray data = new JSONArray();
        for(User t : teachers){
            JSONObject o = new JSONObject();
            o.put("id", t.getUserId());
            o.put("name", t.getName());
            o.put("surname", t.getSurname());
            o.put("secondSurname", t.getSecondSurname());

            data.add(o);
        }
        return data;
    }
}
