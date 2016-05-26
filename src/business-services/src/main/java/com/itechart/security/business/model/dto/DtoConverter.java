package com.itechart.security.business.model.dto;

import com.itechart.security.business.model.persistent.Attachment;
import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.HistoryEntry;
import com.itechart.security.business.model.persistent.Order;
import com.itechart.security.model.persistent.dto.PublicUserDto;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DtoConverter {
    public static List<ContactDto> convertContacts(List<Contact> contacts) {
        if (CollectionUtils.isEmpty(contacts)) {
            return Collections.emptyList();
        }
        return contacts.stream().map(DtoConverter::convert).collect(Collectors.toList());
    }

    public static ContactDto convert(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setId(contact.getId());
        dto.setFirstName(contact.getFirstName());
        dto.setLastName(contact.getLastName());
        dto.setEmail(contact.getEmail());
        dto.setAddress(contact.getAddress());
        return dto;
    }

    public static Contact convert(ContactDto dto) {
        Contact contact = new Contact();
        contact.setId(dto.getId());
        contact.setFirstName(dto.getFirstName());
        contact.setLastName(dto.getLastName());
        contact.setEmail(dto.getEmail());
        contact.setAddress(dto.getAddress());
        return contact;
    }

    public static Set<OrderDto> convertOrders(Set<Order> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return Collections.emptySet();
        }
        return orders.stream().map(DtoConverter::convert).collect(Collectors.toSet());
    }

    public static OrderDto convert(Order order){
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setProduct(order.getProduct());
        dto.setCount(order.getCount());
        dto.setPrice(order.getPrice());
        return dto;
    }


    public static Attachment convert(AttachmentDto dto){
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

    public static AttachmentDto convert(Attachment attachment){
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
