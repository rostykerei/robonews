/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.controllers;

import io.robonews.dao.NewsCategoryDao;
import io.robonews.domain.NewsCategory;
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
    private NewsCategoryDao newsCategoryDao;

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id", "level", "leftIndex", "rightIndex");
    }

    @ModelAttribute("parentCategories")
    public List<CategoryDto> parentCategoriesList(){
        List<CategoryDto> parents = new ArrayList<CategoryDto>();
        for (NewsCategory newsCategory : newsCategoryDao.getAll()) {
            CategoryDto parent = new CategoryDto();
            parent.setId(newsCategory.getId());
            parent.setName(
                new String(
                        new char[newsCategory.getLevel()]).
                        replace("\0", "- ") +
                        newsCategory.getName()
            );

            parents.add(parent);
        }

        return parents;
    }

    @RequestMapping(value = {"/category/list"}, method = RequestMethod.GET)
    public String showList(Model model) {
        model.addAttribute("categories", newsCategoryDao.getAll());

        return "category/list";
    }

    @RequestMapping(value = {"/category/create"}, method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute("category", new CategoryDto());

        return "category/create";
    }

    @RequestMapping(value = {"/category/{categoryId}/edit"}, method = RequestMethod.GET)
    public String showEditForm(@PathVariable int categoryId, Model model, RedirectAttributes attributes) {
        NewsCategory newsCategory = newsCategoryDao.getById(categoryId);

        if (newsCategory == null) {
            addRedirectErrorMessage(attributes, "Selected newsCategory does not exists");
            return "redirect:/category/list";
        }

        model.addAttribute("category", new CategoryDto(newsCategory));

        return "category/edit";
    }

    @RequestMapping(value = {"/category/create"}, method = RequestMethod.POST)
    public String saveAction(@Valid @ModelAttribute("category") CategoryDto categoryDto, BindingResult result,
                             SessionStatus status, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            addModelErrorMessage(model, "Failed to add newsCategory");
            return "category/create";
        }
        else {
            try {
                newsCategoryDao.create(categoryDto.toCategory(), categoryDto.getParentCategoryId());
            }
            catch (Exception e) {
                addModelErrorMessage(model, e, "Could not add newsCategory");
                return "category/create";
            }

            status.setComplete();
            addRedirectInfoMessage(attributes, "New newsCategory has been successfully saved");
            return "redirect:/category/list";
        }
    }

    @RequestMapping(value = {"/category/{categoryId}/delete"})
    public String deleteAction(@PathVariable int categoryId, RedirectAttributes attributes) {
        NewsCategory newsCategory = newsCategoryDao.getById(categoryId);

        if (newsCategory == null) {
            addRedirectErrorMessage(attributes, "Selected newsCategory does not exists");
            return "redirect:/category/list";
        }

        try {
            newsCategoryDao.delete(newsCategory);
        }
        catch (Exception e) {
            addRedirectErrorMessage(attributes, e, "Cannot delete selected newsCategory");
            return "redirect:/category/list";
        }

        addRedirectInfoMessage(attributes, "NewsCategory has been successfully deleted");
        return "redirect:/category/list";
    }
}
