package com.itechart.scrapper.model.crm;

interface ACLable {
    UserDefaultAclDto convertToAcl(Boolean isAdmin);
}
