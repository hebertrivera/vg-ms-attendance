package pe.edu.vallegrande.vg_ms_attendance.domain.dto;


import lombok.Data;
import java.util.List;

@Data
public class EnrollmentDetailDTO {
    private String id;
    private StudentDTO student;
    private List<DidacticUnitDTO> didacticUnit;
    private String status;
}

