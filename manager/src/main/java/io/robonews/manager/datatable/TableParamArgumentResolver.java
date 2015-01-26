/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.datatable;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class TableParamArgumentResolver implements WebArgumentResolver
{

    private static final String S_ECHO = "sEcho";
    private static final String I_DISPLAY_START = "iDisplayStart";
    private static final String I_DISPLAY_LENGTH = "iDisplayLength";
    private static final String I_SORTING_COLS = "iSortingCols";

    private static final String I_SORT_COLS = "iSortCol_";
    private static final String S_SORT_DIR = "sSortDir_";
    private static final String S_DATA_PROP = "mDataProp_";

    public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception
    {
        TableParam tableParamAnnotation = param.getParameterAnnotation(TableParam.class);

        if (tableParamAnnotation!=null)
        {
            HttpServletRequest httpRequest = (HttpServletRequest) request.getNativeRequest();

            String sEcho = httpRequest.getParameter(S_ECHO);
            String sDisplayStart = httpRequest.getParameter(I_DISPLAY_START);
            String sDisplayLength = httpRequest.getParameter(I_DISPLAY_LENGTH);
            String sSortingCols = httpRequest.getParameter(I_SORTING_COLS);

            Integer iEcho = sEcho == null ? 0 : Integer.parseInt(sEcho);
            Integer iDisplayStart = sDisplayStart == null ? 0 : Integer.parseInt(sDisplayStart);
            Integer iDisplayLength = sDisplayLength == null ? 0 : Integer.parseInt(sDisplayLength);
            Integer iSortingCols = sDisplayLength == null ? 0 : Integer.parseInt(sSortingCols);

            List sortFields = new ArrayList();
            for(int colCount=0; colCount<iSortingCols; colCount++)
            {
                String sSortCol = httpRequest.getParameter(I_SORT_COLS+colCount);
                String sSortDir = httpRequest.getParameter(S_SORT_DIR+colCount);
                String sColName = httpRequest.getParameter(S_DATA_PROP+sSortCol);
                sortFields.add(new SortField(sColName, sSortDir));
            }

            PagingCriteria pc = new PagingCriteria(iDisplayStart, iDisplayLength, iEcho, sortFields);

            return pc;
        }

        return WebArgumentResolver.UNRESOLVED;
    }
}