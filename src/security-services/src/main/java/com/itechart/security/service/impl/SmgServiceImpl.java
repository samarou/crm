package com.itechart.security.service.impl;

import com.itechart.scraper.model.Person;
import com.itechart.scraper.parser.Parser;
import com.itechart.scraper.parser.impl.SmgPersonParser;
import com.itechart.security.model.dto.PublicUserDto;
import com.itechart.security.service.SmgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by anton.charnou on 07.06.2016.
 */
@Service
public class SmgServiceImpl implements SmgService {
    private static final Logger log = LoggerFactory.getLogger(SmgServiceImpl.class);

    @Override
    public PublicUserDto parseSMG(String profileUrl) throws IOException {
        Parser parser = new SmgPersonParser();
        Person person = parser.parse(profileUrl);
        return convert(person);
    }

    private PublicUserDto convert(Person person) {
        PublicUserDto publicUserDto = new PublicUserDto();
        publicUserDto.setFirstName(person.getFirstName());
        publicUserDto.setLastName(person.getLastName());
        return publicUserDto;
    }




}
