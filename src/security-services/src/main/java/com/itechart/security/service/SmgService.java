package com.itechart.security.service;

import com.itechart.security.model.dto.PublicUserDto;

import java.io.IOException;

/**
 * Created by anton.charnou on 07.06.2016.
 */
public interface SmgService {
    PublicUserDto parseSMG(String profileUrl) throws IOException;
}
