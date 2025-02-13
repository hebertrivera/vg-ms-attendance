package pe.edu.vallegrande.vg_ms_attendance.domain.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class InstitucionalStaffDTO {
    private String id;
    private String fatherLastname;
    private String motherLastname;
    private String name;
    private LocalDate birthdate;
    private String documentType;
    private String documentNumber;
    private String sex;
    private String country;
    private UbigeoDTO ubigeo;
    private String email;
    private String phone;
    private String civilStatus;
    private String instructionGrade;
    private String disabilityType;
    private String disability;
    private String workCondition;
    private String occupation;
    private String nativeLanguage;
    private String status;
    private String address;
    private String rol;
}


