package pe.edu.vallegrande.vg_ms_attendance.application.service;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.vg_ms_attendance.domain.dto.ClassroomDTO;
import pe.edu.vallegrande.vg_ms_attendance.domain.dto.InstitucionalStaffDTO;
import reactor.core.publisher.Flux;


@Service
public class ExternalService {

    @Value("${services.classroom.url}")
    private String classroomUrl;

    @Value("${services.institucional-staff.url}")
    private String institucionalStaffUrl;

    private final WebClient.Builder webClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(ExternalService.class);  // Logger manual

    public ExternalService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Flux<ClassroomDTO> listActiveClassrooms() {
        return fetchDataList(classroomUrl, ClassroomDTO.class)
                .doOnNext(classroomDTO -> {
                    classroomDTO.mapHeaderToClassroom(); // Invocar el mapeo de Header a Classroom
                    log.info("Aula procesada: {}", classroomDTO);
                });
    }


    public Flux<InstitucionalStaffDTO> listActiveInstitutionalStaffs() {
        return fetchDataList(institucionalStaffUrl, InstitucionalStaffDTO.class)
                .doOnNext(staff -> log.info("Personal institucional activo encontrado: {}", staff));
    }

    public <T> Flux<T> fetchDataList(String baseUrl, Class<T> responseType) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl)
                .retrieve()
                .bodyToFlux(responseType)
                .doOnNext(item -> log.info("Elemento recibido: {}", item))
                .onErrorResume(e -> {
                    log.error("Error al obtener datos desde {}: {}", baseUrl, e.getMessage());
                    return Flux.empty();
                });
    }

}

