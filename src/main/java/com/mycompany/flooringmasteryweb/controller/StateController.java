/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author apprentice
 */
@Controller
@RequestMapping(value = "/state")
public class StateController {

    ProductDao productDao;
    StateDao stateDao;
    OrderDao orderDao;

    @Inject
    public StateController(
            ProductDao productDao,
            StateDao stateDao,
            OrderDao orderDao
    ) {
        this.productDao = productDao;
        this.stateDao = stateDao;
        this.orderDao = orderDao;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String blank(Map model) {

        List<StateCommand> stateCommands = stateList();

        model.put("states", stateCommands);
        model.put("stateCommand", new StateCommand());

        return "state\\edit";
    }

    private List<StateCommand> stateList() {
        return stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/edit/{stateName}", method = RequestMethod.GET)
    public String edit(@PathVariable("stateName") String stateName, Map model) {

        model.put("states", stateList());
        model.put("stateCommand", StateCommand.buildCommandState(stateDao.get(stateName)));

        return "state\\edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@ModelAttribute StateCommand stateCommand, BindingResult bindingResult, Map model) {

        boolean stateValid = StateUtilities.validStateInput(stateCommand.getStateName());
        boolean taxValid = true;

        if (!stateValid) {
            bindingResult.rejectValue("stateName", "error.user", "That State Does Not Exist.");
        }

        if (stateCommand.getStateTax() < 0) {
            bindingResult.rejectValue("stateTax", "error.user", "The Tax Can Not Be Less Than Zero.");
            taxValid = false;
        }

        if (stateCommand.getStateTax() > 100) {
            bindingResult.rejectValue("stateTax", "error.user", "The Tax Can Not Be That High.");
            taxValid = false;
        }

        if (bindingResult.hasErrors()) {

            model.put("stateCommand", stateCommand);
            model.put("states", stateList());

            model.put("stateError", !stateValid);
            model.put("taxError", !taxValid);

            return "state\\edit";

        } else {
            String enteredName = stateCommand.getStateName();
            String guessedName = StateUtilities.bestGuessStateName(enteredName);
            String stateName = StateUtilities.abbrFromState(guessedName);

            State state = new State();
            state.setStateTax(stateCommand.getStateTax());
            state.setStateName(stateName);

            if (stateDao.get(state.getStateName()) == null) {
                stateDao.create(state);
            } else {
                stateDao.update(state);
            }

            return "redirect:/state/";
        }
    }

    @RequestMapping(value = "/delete/{stateName}", method = RequestMethod.GET)
    public String delete(@PathVariable("stateName") String stateName) {

        stateDao.delete(stateDao.get(stateName));

        return "redirect:/state/";
    }
}
