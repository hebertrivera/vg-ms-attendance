package pe.edu.vallegrande.vg_ms_attendance.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "attendances")
public class Attendance {
    @Id
    private String idAttendance;           // ID generado por MongoDB
    private String classroomId;
    private String institucionalStaffId;
    private LocalDate date;
    private List<StudentAttendance> students;
    private String status = "A";

    @Data
    public static class StudentAttendance {
        private String studentId;
        private String attendanceStatus;
        private String observation;
    }
}
