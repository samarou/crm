package com.itechart.scraper.model.smg.response;

import com.itechart.scraper.model.smg.SmgShortProfile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class SmgProfilesResponse extends SmgResponse {
    public List<SmgShortProfile> Profiles;

    public List<Integer> getProfileIds() {
        if (isEmpty(getProfiles())) {
            return emptyList();
        }
        return getProfiles().stream().map(SmgShortProfile::getProfileId).collect(Collectors.toList());
    }
}
