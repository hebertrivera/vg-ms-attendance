package pe.edu.vallegrande.vg_ms_attendance.domain.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateAttendanceDTO {
    private String classroomId;
    private String institucionalStaffId;
    private LocalDate date;
    private List<StudentAttendanceDTO> students;
    private String status;

    @Data
    public static class StudentAttendanceDTO {
        private String studentId;
        private String attendanceStatus;
        private String observation;
    }
}

