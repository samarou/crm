package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.ContactDto;

import java.io.IOException;

/**
 * Created by anton.charnou on 24.05.2016.
 */
public interface ParsingService {
     ContactDto parse(String url) throws IOException;
}
