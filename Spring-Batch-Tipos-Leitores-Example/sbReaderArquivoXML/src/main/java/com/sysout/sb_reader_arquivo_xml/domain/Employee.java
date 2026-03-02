package com.sysout.sb_reader_arquivo_xml.domain;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "employee")
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Employee {

    private int id;
    private String name;
    private String department;


}
