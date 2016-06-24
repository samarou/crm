package com.itechart.security.service.impl;

import com.itechart.scraper.model.SmgProfile;
import com.itechart.scraper.parser.Parser;
import com.itechart.scraper.parser.impl.SmgProfileParser;
import com.itechart.security.model.dto.PublicUserDto;
import com.itechart.security.service.SmgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SmgServiceImpl implements SmgService {
    private static final Logger log = LoggerFactory.getLogger(SmgServiceImpl.class);

    @Override
    public PublicUserDto parseSMG(String profileUrl) throws IOException {
        Parser parser = new SmgProfileParser();
        SmgProfile smgProfile = parser.parse(profileUrl);
        return convert(smgProfile);
    }

    private PublicUserDto convert(SmgProfile smgProfile) {
        PublicUserDto publicUserDto = new PublicUserDto();
        publicUserDto.setFirstName(smgProfile.getFirstName());
        publicUserDto.setLastName(smgProfile.getLastName());
        publicUserDto.setEmail(smgProfile.getEmail());
        publicUserDto.setUserName(smgProfile.getUserName());
        return publicUserDto;
    }




}
