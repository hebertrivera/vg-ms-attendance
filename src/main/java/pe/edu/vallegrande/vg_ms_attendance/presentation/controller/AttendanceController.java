package pe.edu.vallegrande.vg_ms_attendance.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vg_ms_attendance.application.service.AttendanceService;
import pe.edu.vallegrande.vg_ms_attendance.domain.dto.*;
import pe.edu.vallegrande.vg_ms_attendance.domain.model.Attendance;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("teacher/${api.version}/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }


    @GetMapping("/classrooms")
    public Flux<ClassroomDTO> getActiveClassrooms() {
        return attendanceService.listActiveClassrooms();
    }


    @GetMapping("/institutional-staff")
    public Flux<InstitucionalStaffDTO> getActiveInstitutionalStaff() {
        return attendanceService.listActiveInstitutionalStaff();
    }


    @PostMapping("/register-students")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Attendance> registerStudentAttendance(@RequestBody RegisterStudentAttendanceDTO attendanceRequest) {
        return attendanceService.registerStudentAttendance(attendanceRequest);
    }

    @GetMapping("/history")
    public Flux<Attendance> getAttendanceHistory(
            @RequestParam String classroomId,
            @RequestParam String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return attendanceService.getAttendanceHistory(classroomId, parsedDate);
    }

    @PutMapping("/update/{idAttendance}")
    public Mono<Attendance> updateAttendance(
            @PathVariable String idAttendance,
            @RequestBody UpdateAttendanceDTO updateRequest) {
        return attendanceService.updateAttendance(idAttendance, updateRequest)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al actualizar la asistencia: " + e.getMessage())));
    }

    @GetMapping("/list-detail")
    public Flux<ListDetailAttendanceDTO> getListDetailAttendances() {
        return attendanceService.listDetailAttendances();
    }

    @PutMapping("/activate/{idAttendance}")
    public Mono<Attendance> activateAttendance(@PathVariable String idAttendance) {
        return attendanceService.activateAttendance(idAttendance)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al activar la asistencia: " + e.getMessage())));
    }

    @PutMapping("/inactivate/{idAttendance}")
    public Mono<Attendance> inactivateAttendance(@PathVariable String idAttendance) {
        return attendanceService.inactivateAttendance(idAttendance)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error al inactivar la asistencia: " + e.getMessage())));
    }
}
