package pe.edu.vallegrande.vg_ms_attendance.domain.dto;

import lombok.Data;

@Data
public class StudentDTO {
    private String id;
    private String documentType;
    private String documentNumber;
    private String lastNamePaternal;
    private String lastNameMaternal;
    private String names;
}
