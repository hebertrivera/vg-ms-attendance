package pe.edu.vallegrande.vg_ms_attendance.application.service;

import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vg_ms_attendance.domain.dto.*;
import pe.edu.vallegrande.vg_ms_attendance.domain.model.Attendance;
import pe.edu.vallegrande.vg_ms_attendance.domain.repository.AttendanceRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;


@Service
public class AttendanceService {

    private final ExternalService externalService;
    private final AttendanceRepository attendanceRepository;

    public AttendanceService(ExternalService externalService, AttendanceRepository attendanceRepository) {
        this.externalService = externalService;
        this.attendanceRepository = attendanceRepository;
    }


    public Flux<ClassroomDTO> listActiveClassrooms() {
        return externalService.listActiveClassrooms();
    }


    public Flux<InstitucionalStaffDTO> listActiveInstitutionalStaff() {
        return externalService.listActiveInstitutionalStaffs();
    }


    public Mono<Attendance> registerStudentAttendance(RegisterStudentAttendanceDTO attendanceRequest) {
        Attendance attendance = new Attendance();
        attendance.setClassroomId(attendanceRequest.getClassroomId());
        attendance.setInstitucionalStaffId(attendanceRequest.getInstitucionalStaffId());
        attendance.setDate(attendanceRequest.getDate());
        attendance.setStatus("A");


        List<Attendance.StudentAttendance> students = attendanceRequest.getStudents().stream()
                .map(student -> {
                    Attendance.StudentAttendance studentAttendance = new Attendance.StudentAttendance();
                    studentAttendance.setStudentId(student.getStudentId());
                    studentAttendance.setAttendanceStatus(student.getAttendanceStatus());
                    studentAttendance.setObservation(student.getObservation());
                    return studentAttendance;
                })
                .toList();

        attendance.setStudents(students);
        return attendanceRepository.save(attendance);
    }

    public Flux<Attendance> getAttendanceHistory(String classroomId, LocalDate date) {
        return attendanceRepository.findAll()
                .filter(attendance -> attendance.getClassroomId().equals(classroomId)
                        && attendance.getDate().equals(date))
                .switchIfEmpty(Flux.error(new RuntimeException("No se encontraron asistencias para la fecha y aula especificadas")));
    }

    public Mono<Attendance> updateAttendance(String idAttendance, UpdateAttendanceDTO updateRequest) {
        return attendanceRepository.findById(idAttendance)
                .flatMap(existingAttendance -> {
                    existingAttendance.setClassroomId(updateRequest.getClassroomId());
                    existingAttendance.setInstitucionalStaffId(updateRequest.getInstitucionalStaffId());
                    existingAttendance.setDate(updateRequest.getDate());


                    if (updateRequest.getStatus() != null) {
                        existingAttendance.setStatus(updateRequest.getStatus());
                    }


                    List<Attendance.StudentAttendance> updatedStudents = updateRequest.getStudents().stream()
                            .map(student -> {
                                Attendance.StudentAttendance studentAttendance = new Attendance.StudentAttendance();
                                studentAttendance.setStudentId(student.getStudentId());
                                studentAttendance.setAttendanceStatus(student.getAttendanceStatus());
                                studentAttendance.setObservation(student.getObservation());
                                return studentAttendance;
                            })
                            .toList();

                    existingAttendance.setStudents(updatedStudents);

                    return attendanceRepository.save(existingAttendance);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró asistencia con el ID especificado")));
    }


    public Flux<ListDetailAttendanceDTO> listDetailAttendances() {
        return attendanceRepository.findAll()
                .flatMap(attendance ->
                        Mono.zip(
                                        externalService.listActiveClassrooms()
                                                .filter(classroom -> classroom.getClassroomId().equals(attendance.getClassroomId()))
                                                .singleOrEmpty()
                                                .defaultIfEmpty(new ClassroomDTO()),
                                        externalService.listActiveInstitutionalStaffs()
                                                .filter(staff -> staff.getId().equals(attendance.getInstitucionalStaffId()))
                                                .singleOrEmpty()
                                                .defaultIfEmpty(new InstitucionalStaffDTO())
                                )
                                .map(tuple -> {
                                    ClassroomDTO classroom = tuple.getT1();
                                    InstitucionalStaffDTO staff = tuple.getT2();

                                    ListDetailAttendanceDTO dto = new ListDetailAttendanceDTO();
                                    dto.setIdAttendance(attendance.getIdAttendance());
                                    dto.setClassroomId(classroom.getClassroomId() != null ? List.of(classroom) : List.of());
                                    dto.setInstitucionalStaffId(staff.getId() != null ? List.of(staff) : List.of());
                                    dto.setDate(attendance.getDate());
                                    dto.setStatus(attendance.getStatus()); // Nuevo campo incluido
                                    dto.setStudents(attendance.getStudents().stream()
                                            .map(student -> {
                                                ListDetailAttendanceDTO.StudentAttendanceDTO studentDTO = new ListDetailAttendanceDTO.StudentAttendanceDTO();
                                                studentDTO.setStudentId(student.getStudentId());
                                                studentDTO.setAttendanceStatus(student.getAttendanceStatus());
                                                studentDTO.setObservation(student.getObservation());
                                                return studentDTO;
                                            }).toList());
                                    return dto;
                                })
                );
    }

    public Mono<Attendance> activateAttendance(String idAttendance) {
        return attendanceRepository.findById(idAttendance)
                .flatMap(attendance -> {
                    attendance.setStatus("A"); // Cambiar estado a Activo
                    return attendanceRepository.save(attendance);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró asistencia con el ID especificado")));
    }


    public Mono<Attendance> inactivateAttendance(String idAttendance) {
        return attendanceRepository.findById(idAttendance)
                .flatMap(attendance -> {
                    attendance.setStatus("I"); // Cambiar estado a Inactivo
                    return attendanceRepository.save(attendance);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No se encontró asistencia con el ID especificado")));
    }

}
