package com.smalwe.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class RestTemplateErrorHandler extends DefaultResponseErrorHandler {

    Logger logger = LoggerFactory.getLogger(RestTemplateErrorHandler.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {

        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                String httpBodyResponse = reader.lines().collect(Collectors.joining(""));
                logger.error("Error encountered while fetching the result from github:", httpBodyResponse);
                if (response.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
                    throw new UnProcessableEntityException("Input cannot be processed");
                }

                throw new RestTemplateException(httpBodyResponse);
            }
        }
    }
}
