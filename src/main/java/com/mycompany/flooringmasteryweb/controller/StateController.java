/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.controller;

import com.mycompany.flooringmasteryweb.dao.OrderDao;
import com.mycompany.flooringmasteryweb.dao.ProductDao;
import com.mycompany.flooringmasteryweb.dao.StateDao;
import com.mycompany.flooringmasteryweb.dto.Product;
import com.mycompany.flooringmasteryweb.dto.ProductCommand;
import com.mycompany.flooringmasteryweb.dto.State;
import com.mycompany.flooringmasteryweb.dto.StateCommand;
import com.mycompany.flooringmasteryweb.utilities.StateUtilities;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
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
    public String index(Map model) {

        List<StateCommand> stateCommands = stateList();

        model.put("states", stateCommands);
        model.put("stateCommand", new StateCommand());

        return "state\\edit";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public State[] index() {
        List<State> states = stateDao.getListOfStates();
        return states.toArray(new State[states.size()]);
    }

    private List<StateCommand> stateList() {
        return stateDao.getListOfStates().stream()
                .map(state -> StateCommand.buildCommandState(state))
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/edit/{stateName}", method = RequestMethod.GET)
    public String edit(@PathVariable("stateName") String stateName, Map model) {
        State state = stateDao.get(stateName);

        model.put("states", stateList());
        model.put("stateCommand", StateCommand.buildCommandState(Objects.nonNull(state) ? state : new State()));

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
            State state = saveState(stateCommand);

            return "redirect:/state/";
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, headers = "Accept=application/json")
    @ResponseBody
    public State update(@Valid @RequestBody StateCommand stateCommand) {

        State state = saveState(stateCommand);

        return state;
    }

    private State saveState(StateCommand stateCommand) {
        State state = State.buildState(stateCommand);
        if (stateDao.get(state.getStateName()) == null) {
            state = stateDao.create(state);
        } else {
            state = stateDao.update(state);
        }
        return state;
    }

    @RequestMapping(value = "/delete/{stateName}", method = RequestMethod.GET)
    public String delete(@PathVariable("stateName") String stateName, Map model) {
        stateDao.delete(stateDao.get(stateName));

        return "redirect:/state/";
    }

    @RequestMapping(value = "/{stateName}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public State show(@PathVariable("stateName") String stateName) {
        return stateDao.get(stateName);
    }

    @RequestMapping(value = "/{stateName}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    @ResponseBody
    public State delete(@PathVariable("stateName") String stateName) {
        State stateToDelete = stateDao.get(stateName);
        return stateDao.delete(stateToDelete);
    }
}
