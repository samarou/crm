package com.itechart.security.business.model.dto.utils;

import com.itechart.security.business.model.dto.*;
import com.itechart.security.business.model.enums.EmailType;
import com.itechart.security.business.model.enums.TelephoneType;
import com.itechart.security.business.model.persistent.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

public class DtoConverter {
    public static List<ContactDto> convertContacts(List<Contact> contacts) {
        if (CollectionUtils.isEmpty(contacts)) {
            return Collections.emptyList();
        }
        return contacts.stream().map(DtoConverter::convertMainFields).collect(Collectors.toList());
    }

    public static ContactDto convert(Contact contact) {
        ContactDto dto = convertMainFields(contact);
        dto.setDateOfBirth(contact.getDateOfBirth());
        dto.setIsMale(contact.getIsMale());
        dto.setNationality(contact.getNationality());
        dto.setPhotoUrl(contact.getPhotoUrl());
//        dto.setOrders(convertOrders(contact.getOrders()));
        dto.setMessengers(convertMessengerAccounts(contact.getMessengers()));
        dto.setSocialNetworks(convertSocialNetworkAccounts(contact.getSocialNetworks()));
        dto.setWorkplaces(convertWorkplaces(contact.getWorkplaces()));
        dto.setIndustry(contact.getIndustry());
        dto.setSkills(convertSkills(contact.getSkills()));
        return dto;
    }

    public static ContactDto convertMainFields(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setPatronymic(contact.getPatronymic());
        dto.setAddresses(convertAddresses(contact.getAddresses()));
        dto.setTelephones(convertTelephones(contact.getTelephones()));
        dto.setEmails(convertEmails(contact.getEmails()));
        return dto;
    }

    public static Contact convert(ContactDto dto) {
        Contact contact = new Contact();
        contact.setId(dto.getId());
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setPatronymic(dto.getPatronymic());
        contact.setDateOfBirth(dto.getDateOfBirth());
        contact.setIsMale(dto.getIsMale());
        contact.setNationality(dto.getNationality());
        contact.setPhotoUrl(dto.getPhotoUrl());
//        contact.setOrders(convertOrderDtos(dto.getOrders()));
        contact.setAddresses(convertAddressDtos(dto.getAddresses()));
        contact.setTelephones(convertTelephoneDtos(dto.getTelephones()));
        contact.setEmails(convertEmailDtos(dto.getEmails()));
        contact.setMessengers(convertMessengerAccountDtos(dto.getMessengers()));
        contact.setSocialNetworks(convertSocialNetworkAccountDtos(dto.getSocialNetworks()));
        contact.setWorkplaces(convertWorkplaceDtos(dto.getWorkplaces()));
        contact.setIndustry(dto.getIndustry());
        contact.setSkills(convertSkillDtos(dto.getSkills()));
        return contact;
    }


    public static OrderDto convert(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setProduct(order.getProduct());
        dto.setCount(order.getCount());
        dto.setPrice(order.getPrice());
        return dto;
    }

    public static Order convert(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setProduct(dto.getProduct());
        order.setCount(dto.getCount());
        order.setPrice(dto.getPrice());
        return order;
    }

    public static Set<OrderDto> convertOrders(Set<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return emptySet();
        }
        return orders
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<Order> convertOrderDtos(Set<OrderDto> orderDtos) {
        if (CollectionUtils.isEmpty(orderDtos)) {
            return emptySet();
        }
        return orderDtos
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }


    public static EmailDto convert(Email email) {
        EmailDto dto = new EmailDto();
        dto.setId(email.getId());
        dto.setName(email.getName());
        dto.setType(email.getType() != null ? email.getType().name() : null);
        return dto;
    }

    public static Email convert(EmailDto dto) {
        Email email = new Email();
        email.setId(dto.getId());
        email.setName(dto.getName());
        email.setType(dto.getType() != null ? EmailType.findByName(dto.getType()) : null);
        return email;
    }

    public static Set<EmailDto> convertEmails(Set<Email> emails) {
        if (CollectionUtils.isEmpty(emails)) {
            return emptySet();
        }
        return emails
            .stream()
            .filter(email -> email.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<Email> convertEmailDtos(Set<EmailDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return emptySet();
        }
        return dtos
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }


    public static CountryDto convert(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        return dto;
    }

    public static Country convert(CountryDto dto) {
        Country country = new Country();
        country.setId(dto.getId());
        country.setName(dto.getName());
        return country;
    }

    public static List<CountryDto> convertCountries(List<Country> countries) {
        if (CollectionUtils.isEmpty(countries)) {
            return Collections.emptyList();
        }
        return countries.stream().map(DtoConverter::convert).collect(Collectors.toList());
    }

    public static AddressDto convert(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setAddressLine(address.getAddressLine());
        dto.setZipcode(address.getZipcode());
        dto.setCity(address.getCity());
        dto.setRegion(address.getRegion());
        dto.setCountry(address.getCountry() != null ? address.getCountry().getId() : null);
        return dto;
    }

    public static Address convert(AddressDto dto) {
        Address address = new Address();
        address.setId(dto.getId());
        address.setAddressLine(dto.getAddressLine());
        address.setZipcode(dto.getZipcode());
        address.setCity(dto.getCity());
        address.setRegion(dto.getRegion());
        if (dto.getCountry() != null && dto.getCountry() != 0) {
            Country country = new Country();
            country.setId(dto.getCountry());
            address.setCountry(country);
        }
        return address;
    }

    public static Set<AddressDto> convertAddresses(Set<Address> addresses) {
        if (CollectionUtils.isEmpty(addresses)) {
            return emptySet();
        }
        return addresses
            .stream()
            .filter(address -> address.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<Address> convertAddressDtos(Set<AddressDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return emptySet();
        }
        return dtos
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }


    public static TelephoneDto convert(Telephone telephone) {
        TelephoneDto dto = new TelephoneDto();
        dto.setId(telephone.getId());
        dto.setNumber(telephone.getNumber());
        dto.setType(telephone.getType() != null ? telephone.getType().name() : null);
        return dto;
    }

    public static Telephone convert(TelephoneDto dto) {
        Telephone telephone = new Telephone();
        telephone.setId(dto.getId());
        telephone.setNumber(dto.getNumber());
        telephone.setType(dto.getType() != null ? TelephoneType.findByName(dto.getType()) : null);
        return telephone;
    }

    public static Set<TelephoneDto> convertTelephones(Set<Telephone> telephones) {
        if (CollectionUtils.isEmpty(telephones)) {
            return emptySet();
        }
        return telephones
            .stream()
            .filter(telephone -> telephone.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<Telephone> convertTelephoneDtos(Set<TelephoneDto> telephones) {
        if (CollectionUtils.isEmpty(telephones)) {
            return emptySet();
        }
        return telephones
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }


    public static MessengerDto convert(Messenger messenger) {
        MessengerDto dto = new MessengerDto();
        dto.setId(messenger.getId());
        dto.setName(messenger.getName());
        return dto;
    }

    public static Messenger convert(MessengerDto dto) {
        Messenger messenger = new Messenger();
        messenger.setId(dto.getId());
        messenger.setName(dto.getName());
        return messenger;
    }

    public static List<MessengerDto> convertMessengers(List<Messenger> messengers) {
        if (CollectionUtils.isEmpty(messengers)) {
            return Collections.emptyList();
        }
        return messengers.stream().map(DtoConverter::convert).collect(Collectors.toList());
    }

    public static MessengerAccountDto convert(MessengerAccount messengerAccount) {
        MessengerAccountDto dto = new MessengerAccountDto();
        dto.setId(messengerAccount.getId());
        dto.setMessenger(messengerAccount.getMessenger() != null ? messengerAccount.getMessenger().getId() : null);
        dto.setUsername(messengerAccount.getUsername());
        return dto;
    }

    public static MessengerAccount convert(MessengerAccountDto dto) {
        MessengerAccount messengerAccount = new MessengerAccount();
        messengerAccount.setId(dto.getId());
        if (dto.getMessenger() != null && dto.getMessenger() != 0) {
            Messenger messenger = new Messenger();
            messenger.setId(dto.getMessenger());
            messengerAccount.setMessenger(messenger);
        }
        messengerAccount.setUsername(dto.getUsername());
        return messengerAccount;
    }

    public static Set<MessengerAccountDto> convertMessengerAccounts(Set<MessengerAccount> messengers) {
        if (CollectionUtils.isEmpty(messengers)) {
            return emptySet();
        }
        return messengers
            .stream()
            .filter(messengerAccount -> messengerAccount.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<MessengerAccount> convertMessengerAccountDtos(Set<MessengerAccountDto> messengers) {
        if (CollectionUtils.isEmpty(messengers)) {
            return emptySet();
        }
        return messengers
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }


    public static SocialNetworkDto convert(SocialNetwork socialNetwork) {
        SocialNetworkDto dto = new SocialNetworkDto();
        dto.setId(socialNetwork.getId());
        dto.setName(socialNetwork.getName());
        return dto;
    }

    public static SocialNetwork convert(SocialNetworkDto dto) {
        SocialNetwork socialNetwork = new SocialNetwork();
        socialNetwork.setId(dto.getId());
        socialNetwork.setName(dto.getName());
        return socialNetwork;
    }

    public static List<SocialNetworkDto> convertSocialNetworks(List<SocialNetwork> socialNetworks) {
        if (CollectionUtils.isEmpty(socialNetworks)) {
            return Collections.emptyList();
        }
        return socialNetworks.stream().map(DtoConverter::convert).collect(Collectors.toList());
    }

    public static SocialNetworkAccountDto convert(SocialNetworkAccount socialNetworkAccount) {
        SocialNetworkAccountDto dto = new SocialNetworkAccountDto();
        dto.setId(socialNetworkAccount.getId());
        dto.setSocialNetwork(socialNetworkAccount.getSocialNetwork() != null ? socialNetworkAccount.getSocialNetwork().getId() : null);
        dto.setUrl(socialNetworkAccount.getUrl());
        return dto;
    }

    public static SocialNetworkAccount convert(SocialNetworkAccountDto dto) {
        SocialNetworkAccount socialNetworkAccount = new SocialNetworkAccount();
        socialNetworkAccount.setId(dto.getId());
        if (dto.getSocialNetwork() != null && dto.getSocialNetwork() != 0) {
            SocialNetwork socialNetwork = new SocialNetwork();
            socialNetwork.setId(dto.getSocialNetwork());
            socialNetworkAccount.setSocialNetwork(socialNetwork);
        }
        socialNetworkAccount.setUrl(dto.getUrl());
        return socialNetworkAccount;
    }

    public static Set<SocialNetworkAccountDto> convertSocialNetworkAccounts(Set<SocialNetworkAccount> socialNetworks) {
        if (CollectionUtils.isEmpty(socialNetworks)) {
            return emptySet();
        }
        return socialNetworks
            .stream()
            .filter(s -> s.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<SocialNetworkAccount> convertSocialNetworkAccountDtos(Set<SocialNetworkAccountDto> socialNetworks) {
        if (CollectionUtils.isEmpty(socialNetworks)) {
            return emptySet();
        }
        return socialNetworks
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }


    public static WorkplaceDto convert(Workplace workplace) {
        WorkplaceDto dto = new WorkplaceDto();
        dto.setId(workplace.getId());
        dto.setName(workplace.getName());
        dto.setComment(workplace.getComment());
        dto.setPosition(workplace.getPosition());
        dto.setStartDate(workplace.getStartDate());
        dto.setEndDate(workplace.getEndDate());
        return dto;
    }

    public static Workplace convert(WorkplaceDto dto) {
        Workplace workplace = new Workplace();
        workplace.setId(dto.getId());
        workplace.setName(dto.getName());
        workplace.setComment(dto.getComment());
        workplace.setPosition(dto.getPosition());
        workplace.setStartDate(dto.getStartDate());
        workplace.setEndDate(dto.getEndDate());
        return workplace;
    }

    public static Set<WorkplaceDto> convertWorkplaces(Set<Workplace> workplaces) {
        if (CollectionUtils.isEmpty(workplaces)) {
            return emptySet();
        }
        return workplaces
            .stream()
            .filter(workplace -> workplace.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<Workplace> convertWorkplaceDtos(Set<WorkplaceDto> workplaces) {
        if (CollectionUtils.isEmpty(workplaces)) {
            return emptySet();
        }
        return workplaces
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Attachment convert(AttachmentDto dto) {
        Attachment attachment = new Attachment();
        attachment.setId(dto.getId());
        attachment.setName(dto.getName());
        attachment.setComment(dto.getComment());
        Contact contact = new Contact();
        contact.setId(dto.getContactId());
        attachment.setContact(contact);
        attachment.setDateUpload(dto.getDateUpload());
        return attachment;
    }

    public static AttachmentDto convert(Attachment attachment) {
        AttachmentDto dto = new AttachmentDto();
        dto.setId(attachment.getId());
        dto.setName(attachment.getName());
        dto.setComment(attachment.getComment());
        dto.setContactId(attachment.getContact().getId());
        dto.setDateUpload(attachment.getDateUpload());
        return dto;
    }

    public static List<AttachmentDto> convertAttachments(List<Attachment> attachments) {
        if (CollectionUtils.isEmpty(attachments)) {
            return Collections.emptyList();
        }
        return attachments.stream().map(DtoConverter::convert).collect(Collectors.toList());
    }

    public static List<Attachment> convertAttachmentDtos(List<AttachmentDto> attachmentsDto) {
        if (CollectionUtils.isEmpty(attachmentsDto)) {
            return Collections.emptyList();
        }
        return attachmentsDto.stream().map(DtoConverter::convert).collect(Collectors.toList());
    }

    public static EmailTypeDto convert(EmailType type) {
        EmailTypeDto dto = new EmailTypeDto();
        dto.setName(type.name());
        dto.setValue(type.getValue());
        return dto;
    }

    public static List<EmailTypeDto> convertEmailTypes(EmailType[] types) {
        if (types.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(types)
            .stream()
            .map(DtoConverter::convert)
            .collect(Collectors.toList());
    }


    public static TelephoneTypeDto convert(TelephoneType type) {
        TelephoneTypeDto dto = new TelephoneTypeDto();
        dto.setName(type.name());
        dto.setValue(type.getValue());
        return dto;
    }

    public static List<TelephoneTypeDto> convertTelephoneTypes(TelephoneType[] types) {
        if (types.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.asList(types)
            .stream()
            .map(DtoConverter::convert)
            .collect(Collectors.toList());
    }

    public static Skill convert(SkillDto dto) {
        Skill skill = new Skill();
        skill.setId(dto.getId());
        skill.setName(dto.getName());
        return skill;
    }

    public static SkillDto convert(Skill skill) {
        SkillDto dto = new SkillDto();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        return dto;
    }

    public static Set<SkillDto> convertSkills(Set<Skill> skills) {
        if (CollectionUtils.isEmpty(skills)) {
            return emptySet();
        }
        return skills
            .stream()
            .filter(skill -> skill.getDateDeleted() == null)
            .map(DtoConverter::convert)
            .collect(toSet());
    }

    public static Set<Skill> convertSkillDtos(Set<SkillDto> skills) {
        if (CollectionUtils.isEmpty(skills)) {
            return emptySet();
        }
        return skills
            .stream()
            .map(DtoConverter::convert)
            .collect(toSet());
    }
}
