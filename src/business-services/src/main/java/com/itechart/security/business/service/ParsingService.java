package com.itechart.security.business.service;

import com.itechart.security.business.model.dto.ContactDto;



/**
 * Created by anton.charnou on 24.05.2016.
 */
public interface ParsingService {
     ContactDto parse(String url);
}
