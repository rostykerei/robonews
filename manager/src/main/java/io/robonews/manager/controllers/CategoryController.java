/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.controllers;

import io.robonews.dao.TopicDao;
import io.robonews.domain.Topic;
import io.robonews.manager.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes(value = "category", types = CategoryDto.class)
public class CategoryController extends AbstractController {

    @Autowired
    private TopicDao topicDao;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id", "level", "leftIndex", "rightIndex");
    }

    @ModelAttribute("parentCategories")
    public List<CategoryDto> parentCategoriesList(){
        List<CategoryDto> parents = new ArrayList<CategoryDto>();
        for (Topic topic : topicDao.getAll()) {
            CategoryDto parent = new CategoryDto();
            parent.setId(topic.getId());
            parent.setName(
                new String(
                        new char[topic.getLevel()]).
                        replace("\0", "- ") +
                        topic.getName()
            );

            parents.add(parent);
        }

        return parents;
    }

    @RequestMapping(value = {"/category/list"}, method = RequestMethod.GET)
    public String showList(Model model) {
        model.addAttribute("categories", topicDao.getAll());

        return "category/list";
    }

    @RequestMapping(value = {"/category/create"}, method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDto());

        return "category/create";
    }

    @RequestMapping(value = {"/category/{categoryId}/edit"}, method = RequestMethod.GET)
    public String showEditForm(@PathVariable int categoryId, Model model, RedirectAttributes attributes) {
        Topic topic = topicDao.getById(categoryId);

        if (topic == null) {
            addRedirectErrorMessage(attributes, "Selected topic does not exists");
            return "redirect:/category/list";
        }

        model.addAttribute("category", new CategoryDto(topic));

        return "category/edit";
    }

    @RequestMapping(value = {"/category/create"}, method = RequestMethod.POST)
    public String saveAction(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult result,
                             SessionStatus status, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            addModelErrorMessage(model, "Failed to add topic");
            return "category/create";
        }
        else {
            try {
                topicDao.create(categoryDto.toCategory(), categoryDto.getParentCategoryId());
            }
            catch (Exception e) {
                addModelErrorMessage(model, e, "Could not add topic");
                return "category/create";
            }

            status.setComplete();
            addRedirectInfoMessage(attributes, "New topic has been successfully saved");
            return "redirect:/category/list";
        }
    }

    @RequestMapping(value = {"/category/{categoryId}/delete"})
    public String deleteAction(@PathVariable int categoryId, RedirectAttributes attributes) {
        Topic topic = topicDao.getById(categoryId);

        if (topic == null) {
            addRedirectErrorMessage(attributes, "Selected topic does not exists");
            return "redirect:/category/list";
        }

        try {
            topicDao.delete(topic);
        }
        catch (Exception e) {
            addRedirectErrorMessage(attributes, e, "Cannot delete selected topic");
            return "redirect:/category/list";
        }

        addRedirectInfoMessage(attributes, "Topic has been successfully deleted");
        return "redirect:/category/list";
    }
}
