package com.controller;

import com.model.Procedure;
import com.model.User;
import com.model.enums.ProcedureStatus;
import com.model.enums.Role;
import com.service.ProcedureService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;


@Controller
public class ProcedureController {

	@Autowired
	ProcedureService procedureService;
	@Autowired
	UserService userService;

	/**
	 * Processes the petition to get to the procedure creation page.
	 *
	 * @return ModelAndView with the desired .jsp file and its required model & objects
	 */
	@RequestMapping(value = "/procedure/creation")
	public ModelAndView getProcedureCreationForm() {
		ModelAndView model = new ModelAndView("createProcedure");
		Procedure procedure = new Procedure();
		procedure.setUserP(new User());
		model.addObject("procedure", procedure);
		model.addObject("users", userService.getAllUsers());

		return model;
	}

	/**
	 * Processes the creation of a new procedure by using all parameters from the "New Procedure" form.
	 *
	 * @param procedure the procedure with all its elements
	 * @return returns the user to the main page with an url
	 */
	@RequestMapping(value = "/procedure/creation", method = RequestMethod.POST)
	public String createProcedure(@Valid @ModelAttribute("procedure") Procedure procedure, BindingResult result, ModelMap model) throws ParseException {
		procedure.setStatus(ProcedureStatus.CREATED);
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		procedure.setCreationDate(sdf.parse(sdf.format(date)));
		procedure.setLimitDate(sdf.parse(sdf.format(date)));
		procedure.setUserP(userService.getUserById(procedure.getUserP().getUserId()));

		procedureService.addProcedure(procedure);
		return "redirect:/";
	}
}
