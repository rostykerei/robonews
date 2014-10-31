/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.datatable;

import io.robonews.console.controller.error.BadRequestException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class DatatableCriteriaMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        DatatableParams parameterAnnotation = parameter.getParameterAnnotation(DatatableParams.class);
        if (parameterAnnotation != null) {
            return (DatatableCriteria.class.isAssignableFrom(parameter.getParameterType()));
        }

        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        DatatableCriteria criteria = new DatatableCriteria();

        int draw = Integer.parseInt(request.getParameter("draw"));
        int length = Integer.parseInt(request.getParameter("length"));
        int start = Integer.parseInt(request.getParameter("start"));

        String search = request.getParameter("search[value]");
        String sortField = request.getParameter("columns[" + request.getParameter("order[0][column]") + "][data]");

        DatatableParams parameterAnnotation = parameter.getParameterAnnotation(DatatableParams.class);

        if (length < 0 || length > parameterAnnotation.maxLength()) {
            throw new BadRequestException("Datatable length arg is not correct");
        }

        if (start < 0) {
            throw new BadRequestException("Datatable start arg is not correct");
        }

        if (!isValidSortField(sortField, parameterAnnotation.sortFields())) {
            throw new BadRequestException("Datatable sort field is not correct");
        }

        criteria.setDraw(draw);
        criteria.setLength(length);
        criteria.setStart(start);
        criteria.setSearch(search);
        criteria.setSortField(sortField);

        criteria.setSortDirection(
            "desc".equals(request.getParameter("order[0][dir]")) ? DatatableCriteria.SortDirection.DESC : DatatableCriteria.SortDirection.ASC
        );

        return criteria;
    }

    private boolean isValidSortField(String sortField, String[] validFields) {
        for (String f : validFields) {
            if (f.equals(sortField)) {
                return true;
            }
        }

        return false;
    }
}