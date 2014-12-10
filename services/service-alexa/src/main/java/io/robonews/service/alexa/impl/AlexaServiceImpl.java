/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.alexa.impl;

import io.robonews.service.alexa.AlexaService;
import io.robonews.service.alexa.exception.AlexaServiceException;
import io.robonews.service.http.HttpRequest;
import io.robonews.service.http.HttpResponse;
import io.robonews.service.http.HttpService;
import io.robonews.service.http.impl.HttpRequestImpl;
import io.robonews.service.http.impl.HttpServiceUtils;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.robonews.service.text.tools.TextSanitizer;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AlexaServiceImpl implements AlexaService {

    private HttpService httpService;

    private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private static final String ALEXA_API_BASE_URL = "http://data.alexa.com/data?cli=10&url=";

    public AlexaServiceImpl(HttpService httpService) {
        this.httpService = httpService;
    }

    @Override
    public AlexaServiceResult query(String url) throws AlexaServiceException {

        HttpResponse httpResponse = null;

        try {
            String apiUrl = ALEXA_API_BASE_URL + HttpServiceUtils.urlEncode(url);
            HttpRequest httpRequest = new HttpRequestImpl(apiUrl);

            httpRequest.setAccept("application/xml");

            httpResponse = httpService.execute(httpRequest);

            if (httpResponse.getHttpStatus() == HttpResponse.STATUS_OK) {

                Document doc = documentBuilderFactory.newDocumentBuilder().parse(httpResponse.getStream());

                Element element = doc.getDocumentElement();

                String rootUrl = element.getAttribute("URL");

                if (StringUtils.isEmpty(rootUrl) || "404".equalsIgnoreCase(rootUrl)) {
                    throw new AlexaServiceException("not found");
                }

                NodeList popularityNodeList = element.getElementsByTagName("POPULARITY");

                if (popularityNodeList.getLength() != 1) {
                    throw new AlexaServiceException("Could not find <POPULARITY> tag (or more than 1 found) in Alexa response");
                }

                Element elementAttribute = (Element) popularityNodeList.item(0);

                String rankAttr = elementAttribute.getAttribute("TEXT");
                String urlAttr = elementAttribute.getAttribute("URL");

                int rank;

                try {
                    rank = Integer.parseInt(rankAttr);
                }
                catch (NumberFormatException e) {
                    throw new AlexaServiceException("Could not parse RANK");
                }

                if (StringUtils.isEmpty(urlAttr)) {
                    throw new AlexaServiceException("Alexa returned empty URL attribute");
                }

                if (urlAttr.endsWith("/")) {
                    urlAttr = urlAttr.substring(0, urlAttr.length() - 1);
                }

                // Taking site title

                NodeList siteNodeList = element.getElementsByTagName("SITE");

                String title = null;
                String description = null;

                if (siteNodeList.getLength() > 0) {
                    Element siteElementAttribute = (Element) siteNodeList.item(0);
                    title = siteElementAttribute.getAttribute("TITLE");

                    if (title != null) {
                        title = TextSanitizer.sanitizeText(title, 255);
                    }

                    description = siteElementAttribute.getAttribute("DESC");

                    if (description != null) {
                        description = TextSanitizer.sanitizeText(description, 255);
                    }
                }

                return new AlexaServiceResult(rank, urlAttr, title, description);
            }
            else {
                throw new AlexaServiceException("Alexa returned HTTP error: " + httpResponse.getHttpStatus());
            }
        }
        catch (ParserConfigurationException | IOException | SAXException e) {
            throw new AlexaServiceException(e);
        } finally {
            if (httpResponse != null) {
                httpResponse.releaseConnection();
            }
        }
    }
}
