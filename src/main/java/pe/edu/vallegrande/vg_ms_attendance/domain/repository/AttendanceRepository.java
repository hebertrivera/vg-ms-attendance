package pe.edu.vallegrande.vg_ms_attendance.domain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vg_ms_attendance.domain.model.Attendance;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AttendanceRepository extends ReactiveMongoRepository<Attendance, String> {
    Flux<Attendance> findByClassroomIdAndDate(String classroomId, LocalDate date);
}
