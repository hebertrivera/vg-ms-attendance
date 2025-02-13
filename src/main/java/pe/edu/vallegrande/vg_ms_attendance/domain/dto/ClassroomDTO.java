package pe.edu.vallegrande.vg_ms_attendance.domain.dto;import lombok.Data;
import java.util.List;

@Data
public class ClassroomDTO {
    private String classroomId;
    private String name;
    private String academicPeriodId;
    private String studyProgramId;
    private String didacticUnitId;
    private List<EnrollmentDetailDTO> enrollmentDetailId;
    private int capacity;
    private String status;

    private HeaderDTO header;

    @Data
    public static class HeaderDTO {
        private String academicPeriodId;
        private String academicPeriodName;
        private String academicPeriodStatus;
        private String programId;
        private String programName;
        private String programModule;
        private String programStatus;
        private String didacticId;
        private String didacticName;
        private String didacticProgramId;
        private String didacticStatus;
    }

    public void mapHeaderToClassroom() {
        if (header != null) {
            this.academicPeriodId = header.getAcademicPeriodId();
            this.studyProgramId = header.getProgramId();
            this.didacticUnitId = header.getDidacticId();
        }
    }
}