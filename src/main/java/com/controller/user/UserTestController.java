package com.controller.user;

import com.model.*;
import com.model.encapsulators.GradesEncapsulator;
import com.model.enums.FaultType;
import com.model.enums.KeyRoleValue;
import com.model.enums.ProcedureStatus;
import com.model.enums.Role;
import com.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class UserTestController {

    private static final Logger logger = LogManager.getLogger(UserTestController.class);

    @Autowired
    ConversationService conversationService;

    @Autowired
    AntiBullyingReportService antiBullyingReportService;

    @Autowired
    UserService userService;

    @Autowired
    ProcedureService procedureService;

    @Autowired
    MessageService messageService;

    @Autowired
    ConversationUserService conversationUserService;

    @Autowired
    TaskService taskService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    IncidenceService incidenceService;

    /**
     * Processes the petition to get to the conversation page.
     *
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/user/messages")
    public ModelAndView messages(HttpServletRequest request) {
        Message message = new Message();

        try {

            User user = (User) request.getSession().getAttribute("user");
            List<Conversation> conversations;
            if (user == null) {
                conversations = conversationService.findAllConversationsByUserId("1");
                user = userService.getUserById("1");
            } else
                conversations = conversationService.findAllConversationsByUserId(user.getUserId());

            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] - Conversations loaded successfully");

            Collections.sort(conversations);

            for (Conversation conversation : conversations) {
                conversation.setUnread(conversationUserService.findUnread(user.getUserId(), conversation.getConversationId()));
            }

            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] - Unread conversations successfully found");


            ModelAndView model = new ModelAndView("/message");
            model.addObject("conversations", conversations);
            model.addObject("message", message);
            return model;
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error accessing the messaging page: " + e);

            return null;
        }
    }


    /**
     * Processes the creation of a new ticket by using all parameters from the "New Ticket" form.
     *
     * @param message the message with all its elements
     * @return returns the user to the main page with an url
     */
    @RequestMapping(value = "/message/creation", method = RequestMethod.POST)
    public String createMessage(@Valid @ModelAttribute(value = "message") Message message, BindingResult result, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        logger.info("[" + new Object() {
        }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

        try {
            if (user == null)
                message.setSender(userService.getUserById("1"));
            else
                message.setSender(user);

            message.setDate(new Date());

            messageService.addMessage(message);

            Conversation conversation = conversationService.getWithUsers(message.getConversation().getConversationId());
            conversation.setLastMessageDate(new Date());
            conversationService.addConversation(conversation);

            List<ConversationUser> conversationUsers = conversation.getUserConversations();

            for (ConversationUser cu : conversationUsers) {
                if (!cu.getUser().equals(user)) {
                    cu.newMessage();
                    conversationUserService.addConversationUser(cu);
                }
            }

            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error creating the message: " + e);

            return null;
        }

    }

    /**
     * Processes the creation of a new ticket by using all parameters from the "New Ticket" form.
     *
     * @param message the message with all its elements
     * @return returns the user to the main page with an url
     */
    @RequestMapping(value = "/message/creation/directMessage", method = RequestMethod.POST)
    public String createDirectMessage(@Valid @ModelAttribute(value = "message") Message message, BindingResult result, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            user = userService.getUserById("1"); // will be deleted soon

        logger.info("[" + new Object() {
        }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

        message.setSender(user);
        message.setDate(new Date());

        Conversation conversation = conversationService.findBy2Id(user.getUserId(), message.getReceiver());

        if (conversation == null) {
            User userReceiver = userService.getUserById(message.getReceiver());
            try {
                conversation = new Conversation();
                conversation.setTitle("Conversation " + user.getName() + " - " + userReceiver.getName());
                conversation.setDescription("This conversation is between " + user.getName() + " " + user.getSurname() + " " + user.getSecondSurname() + "" +
                        "& " + userReceiver.getName() + " " + userReceiver.getSurname() + " " + userReceiver.getSecondSurname());
                conversation.setLastMessageDate(new Date());
                logger.info("[" + new Object() {
                }.getClass().getEnclosingMethod().getName() + "] -  Conversation successfully created but not saved into the db yet");
                conversationService.addConversation(conversation);
                logger.info("[" + new Object() {
                }.getClass().getEnclosingMethod().getName() + "] -  Conversation successfully saved");
            } catch (Exception e) {
                logger.error("[" + new Object() {
                }.getClass().getEnclosingMethod().getName() + "] -  Conversation could not be created: " + e);
                return null;
            }


            ConversationUser conversationUser = new ConversationUser(user, conversation, new Date());
            conversationUserService.addConversationUser(conversationUser);
            conversation.addUserConversations(conversationUser);
            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Conversation user of loaded user created");


            conversationUser = new ConversationUser(userReceiver, conversation, new Date());
            conversationUserService.addConversationUser(conversationUser);
            conversation.addUserConversations(conversationUser);
            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Conversation user of receiver user created");

        }

        message.setConversation(conversation);

        try {
            System.out.println();
            messageService.addMessage(message);
            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  message saved: ");

            ConversationUser cu = conversationUserService.findByUserAndConversation(message.getReceiver(), conversation.getConversationId());
            conversationUserService.addConversationUser(cu);
            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Conversation user of receiver new message = true saved");


            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error creating the message: " + e);

            return null;
        }

    }

    /**
     * Processes the petition to get to the anti-bullying report creation page.
     *
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/user/antibullying")
    public ModelAndView antiBullyingAccess(@RequestParam Boolean observed) {

        try {

            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

            AntiBullyingReport newReport = new AntiBullyingReport();
            newReport.setObserved(observed);
            ModelAndView model = new ModelAndView("/createReport");
            model.addObject("report", newReport);

            return model;
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error accessing the anti-bullying report page: " + e);

            return null;
        }
    }

    /**
     * Processes the creation of a new anti-bullying report by using all parameters from the "Anti-Bullying Report" form.
     *
     * @param report the anti-bullying report with all its elements
     * @return returns the user to the main page with an url
     */
    @RequestMapping(value = "/user/antibullying/report", method = RequestMethod.POST)
    public String antiBullyingCreate(@Valid @ModelAttribute("report") AntiBullyingReport report, HttpServletRequest request) {

        try {

            if (!report.isAnonymous()) {
                User user = (User) request.getSession().getAttribute("user");
                if (user == null)
                    user = userService.getUserById("1");

                logger.info("[" + new Object() {
                }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

                report.setUser(user);
            }
            //Alertar responsable
            Procedure procedure = new Procedure("Anti bullying report", report.toString(), false, new Date(2099, 01, 01));
            User user = userService.getKeyRoleUser(KeyRoleValue.ANTIBULLYING);
            procedure.setUserP(userService.getKeyRoleUser(KeyRoleValue.ANTIBULLYING));
            procedureService.addProcedure(procedure);

            antiBullyingReportService.addAntiBullyingReport(report);
            return "redirect:/";
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error creating an anti-bullying report: " + e);

            return null;
        }
    }

    /**
     * Processes the petition to get to the procedure creation page.
     *
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/user/procedures")
    public ModelAndView proceduresAccess(HttpServletRequest request) {
        try {
            User userP = (User) request.getSession().getAttribute("user");
            if (userP == null) userP = userService.getUserById("1");
            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

            User user = userService.getAllUserProcedures(userP.getUserId());
            List<Procedure> procedures;
            List<Procedure> validProcedures = new ArrayList<>();

            if (user != null && user.getProcedures() != null) {
                procedures = user.getProcedures();
                Date actualDate = new Date();

                for (Procedure p : procedures) {
                    if (p.getLimitDate().compareTo(actualDate) < 0) {
                        p.setStatus(ProcedureStatus.CANCELLED);
                        procedureService.addProcedure(p);
                    } else {
                        validProcedures.add(p);
                    }
                }
            } else {
                logger.warn("[" + new Object() {
                }.getClass().getEnclosingMethod().getName() + "] -  User has no procedures");
            }
            ModelAndView model = new ModelAndView("/procedureUser");
            Collections.sort(validProcedures);
            model.addObject("procedures", validProcedures);

            return model;
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error accessing the procedures: " + e);

            return null;
        }
    }

    /**
     * Processes the response of a procedure.
     *
     * @param id       the id of the procedure we want to response to
     * @param decision boolean that indicates if the user has accepted or denied the online procedure
     * @return returns the user to last visited page with an url
     */
    @RequestMapping(value = "/user/procedure/response")
    public String procedureCreate(@Valid @ModelAttribute("id") String id, @ModelAttribute("decision") boolean decision) {
        try {
            Procedure procedure = procedureService.findById(id);
            if (decision == true) procedure.setStatus(ProcedureStatus.ACCEPTED);
            else procedure.setStatus(ProcedureStatus.DENIED);

            if (procedure.isNotify() && procedure.getCreator() != null) {
                User userP = procedure.getUserP();
                String title = decision == true ? userP.getFullName() + "has accepted your request" : userP.getFullName() + "has denied your request";
                Notification notification = new Notification(procedure.getCreator(), title, "this is the response of the procedure: " + procedure.getTitle(), true, new Date());
                userService.addNotification(notification);
                logger.info("[" + new Object() {
                }.getClass().getEnclosingMethod().getName() + "] - Notification has been created ");
            }

            procedureService.addProcedure(procedure);


            Incidence incidence = incidenceService.getIncidenceByProcedureId(procedure.getProcedureId());

            if(incidence != null){
                if(incidence.getFaultType().equals(FaultType.ATTENDANCE)){
                    incidence.setJustified(decision);

                    incidenceService.addIncidence(incidence);
                }
            }




            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;
        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error finding the procedure with id " + id + ": " + e);

            return null;
        }
    }

    @RequestMapping("/user/getNotifications")
    public @ResponseBody
    JSONArray loadNotifications(HttpServletRequest request) {

        try {
            User activeUser = (User) request.getSession().getAttribute("user");

            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

            User user;

            if (activeUser == null) //this is for testing, will be deleted
                user = userService.getAllUserNotifications("1");
            else
                user = userService.getAllUserNotifications(activeUser.getUserId());

            List<Notification> notificationList = user.getNotifications();
            Collections.sort(notificationList, Notification::compareTo);

            JSONArray data = new JSONArray();
            for (Notification n : notificationList) {
                JSONObject o = new JSONObject();
                o.put("title", n.getTitle());
                o.put("body", n.getDescription());
                o.put("pending", n.isPending());
                o.put("id", n.getNotificationId());

                n.setPending(false);
                userService.addNotification(n);

                data.add(o);
            }
            return data;

        } catch (Exception e) {
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error getting the notifications: " + e);

            return null;
        }
    }

    @RequestMapping("/user/eraseNotifications")
    public @ResponseBody
    JSONObject eraseNotifications(HttpServletRequest request) {
        JSONObject o = new JSONObject();
        try {
            User activeUser = (User) request.getSession().getAttribute("user");

            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Session user successfully loaded");

            User user;

            if (activeUser == null) //this is for testing, will be deleted
                userService.eraseNotifications("1");
            else
                userService.eraseNotifications(activeUser.getUserId());

            o.put("result", "success");
            logger.info("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Notifications successfully deleted");

        } catch (Exception e) {
            o.put("result", "fail");
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  Error deleting the notifications: " + e);
        }
        return o;
    }


    /**
     * Processes the petition to get to the procedure creation page.
     *
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/user/grades")
    public ModelAndView getGrades(@RequestParam String subjectId, HttpServletRequest request) {

        User user = null;

        try{
            user = (User) request.getSession().getAttribute("child");
        }catch (Exception e){
            logger.error("[" + new Object() {
            }.getClass().getEnclosingMethod().getName() + "] -  child does not exist " + e);
        }

        if(user == null){
            user = (User) request.getSession().getAttribute("user");
            if(user == null)
                user = userService.getUserById("1");
        }

        List<UserTask> userTasks = taskService.getUserTaskByUserAndSubject(user.getUserId(), subjectId);
        UserSubject userSubject = subjectService.getUserSubjectByUserAndSubject(user.getUserId(), subjectId);

        ModelAndView model = new ModelAndView("/user/grades");
        model.addObject("userTasks", userTasks);
        model.addObject("userSubject", userSubject);
        model.addObject("subjectId", subjectId);

        return model;

    }

    /**
     * Processes the petition to get to the procedure creation page.
     *
     * @return ModelAndView with the desired .jsp file and its required model & objects
     */
    @RequestMapping(value = "/user/allGrades")
    public ModelAndView getAllGrades(@RequestParam String userId, HttpServletRequest request) {

        System.out.printf("");
        User user;
        if("-1".equals(userId)) {
            user = (User) request.getSession().getAttribute("user");
            if(user == null)
                user = userService.getUserById("1");
        }else
            user = userService.getUserById(userId);

        List<Subject> subjects = subjectService.getByGroup(((RoleStudent) user.getRoles().get(Role.STUDENT)).getGroup().getGroupId());
        List<GradesEncapsulator> grades = new LinkedList<>();

        for (Subject subject : subjects){

            List<UserTask> userTasks = taskService.getUserTaskByUserAndSubject(user.getUserId(), subject.getSubjectId());
            UserSubject userSubject = subjectService.getUserSubjectByUserAndSubject(user.getUserId(), subject.getSubjectId());
            GradesEncapsulator gradesEncapsulator = new GradesEncapsulator(userTasks, userSubject, subject);

            grades.add(gradesEncapsulator);
        }

        List<Incidence> incidences = incidenceService.getIncidenceByStudentId(user.getRoles().get(Role.STUDENT).getRoleId());


        ModelAndView model = new ModelAndView("/user/gradesReport");
        model.addObject("grades", grades);
        model.addObject("incidences", incidences);

        return model;

    }


    @RequestMapping(value = "/user/subjects", method = RequestMethod.GET)
    public ModelAndView accessChildSubjectList(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) user = userService.getUserById("1");

        ModelAndView model = new ModelAndView("userSubjects");
        List<Subject> subjectList = subjectService.getByGroupNoTeachers(((RoleStudent) user.getRoles().get(Role.STUDENT)).getGroup().getGroupId());
        model.addObject("subjects", subjectList);
        return model;
    }

    @RequestMapping(value = "/user/task/{taskId}", method = RequestMethod.GET)
    public ModelAndView accessTask(@PathVariable("taskId") String taskId, HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) user = userService.getUserById("1");

        ModelAndView model = new ModelAndView("taskAccess");

        Task task = taskService.getTaskById(taskId);

        UserTask userTask = taskService.getUserTaskByUserAndTask(user.getUserId(),taskId);
        if(userTask.getGrade() == 0.0 ) {
            model.addObject("grade", "-");
            model.addObject("observations", "Not yet evaluated.");
        }else {
            model.addObject("grade", Float.toString(userTask.getGrade()));
            model.addObject("observations", userTask.getObservations());
        }
        if(task.getDeadline().compareTo(new Date()) != -1){
            model.addObject("solved", false);
        }else{

            model.addObject("solved", true);
        }
        model.addObject("task", task);

        return model;
    }

    @RequestMapping(value = "/user/task/{taskId}/report", method = RequestMethod.GET)
    public String reportTask(@PathVariable("taskId") String taskId) {

        Task task = taskService.getTaskById(taskId);
        task.setReports(task.getReports()+1);
        taskService.addTask(task);
        Set<RoleTeacher> roleTeachers = subjectService.getTeachersBySubjectId(task.getChapter().getSubject().getSubjectId());
        Notification notification = new Notification();
        notification.setPending(true);
        notification.setCreationDate(new Date());
        notification.setDescription("A user has reported an error in task "+task.getName());
        notification.setTitle("New task report");
        for(RoleTeacher rt : roleTeachers){
            Notification notificationAux = notification.clone();
            User u = new User();
            u.setUserId(rt.getUserR().getUserId());
            notificationAux.setUser(u);
            userService.addNotification(notificationAux);
        }
        //Getting the referer page
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

}
