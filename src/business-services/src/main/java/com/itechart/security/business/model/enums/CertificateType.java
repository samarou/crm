package com.itechart.security.business.model.enums;

import lombok.Getter;


@Getter
public enum CertificateType {
    WITH_HONORS("With honors"),
    SIMPLE("Simple"),
    NOT_COMPLETE("Not completed");
    private String value;

    public static CertificateType findByName(String certificateType){
        for(CertificateType type: CertificateType.values()){
            if(type.name().equals(certificateType)){
                return type;
            }
        }
        throw new RuntimeException("Certificate type was not found for name " + certificateType);
    }

    CertificateType(String value){this.value = value;}
}
