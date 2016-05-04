package com.itechart.security.business.model.dto;

import com.itechart.security.business.model.persistent.Contact;
import com.itechart.security.business.model.persistent.Order;
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
}
