package com.itechart.scrapper.model.crm.contact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessengerAccountDto {
    private Long id;

    private Long messenger;

    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessengerAccountDto that = (MessengerAccountDto) o;

        return username != null ? username.equals(that.username) : that.username == null;

    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
