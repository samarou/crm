package com.itechart.security.business.model.dto.utils;

import com.itechart.security.business.model.dto.HistoryEntryDto;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.model.dto.PublicUserDto;

/**
 * yauheni.putsykovich
 */
public class HistoryEntryConverter {
    public static HistoryEntryDto convert(HistoryEntry historyEntry, PublicUserDto creator, PublicUserDto editor) {
        HistoryEntryDto dto = new HistoryEntryDto();
        dto.setCreator(userToString(creator));
        dto.setCreatedDate(historyEntry.getCreatedDate());
        dto.setEditor(userToString(editor));
        dto.setLastModificationDate(historyEntry.getModificationDate());
        return dto;
    }

    public static String userToString(PublicUserDto user) {
        return user.getFirstName() + " " + user.getLastName() + "(" + user.getUserName() + ")";
    }
}
