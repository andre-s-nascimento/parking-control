package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3400)
@RequestMapping("/api/v1/parking-spot")
public class ParkingSpotController {

  // ponto de injecao do service no controller

  final ParkingSpotService parkingSpotService;

  public ParkingSpotController(ParkingSpotService parkingSpotService) {
    this.parkingSpotService = parkingSpotService;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping
  @Operation(summary = "Salva uma nova vaga",
      description = "Salva uma nova vaga de estacionamento")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "Retorna a vaga salva",
          content = @Content(mediaType = "application/json",
          schema = @Schema(implementation = ParkingSpotModel.class))
      ),
      @ApiResponse(responseCode = "409",
      description = "Conflitos: Placa do Carro ou Vaga já em uso ou Vaga já registrada para o Apartamento e Bloco "
      )
  }  )
  public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto) {
    if (parkingSpotService.existsByLicensePlateCar(parkingSpotDto.getLicensePlateCar())) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Conflict: License Plate Car is already in use!");
    }
    if (parkingSpotService.existsByParkingSpotNumber(parkingSpotDto.getParkingSpotNumber())) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Conflict: Parking Spot is already in use!");
    }
    if (parkingSpotService.existsByApartmentAndBlock(
        parkingSpotDto.getApartment(), parkingSpotDto.getBlock())) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body("Conflict: Parking Spot already registered for this apartment and block!");
    }

    var parkingSpotModel = new ParkingSpotModel();
    BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
    parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(parkingSpotService.save(parkingSpotModel));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
  @GetMapping
  @Operation(summary = "Retorna todas as Vagas de Estacionamento",
      description = "Retorna todas as Vagas de Estacionamento")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "OK",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ParkingSpotModel.class))
      ),
      @ApiResponse(responseCode = "404",
          description = "Não foi encontrada nenhuma vaga cadastrada"
      )
  }  )
  public ResponseEntity<Page<ParkingSpotModel>> getAllParkingSpots(
      @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
          Pageable pageable) {
    return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
  @GetMapping("/{id}")
  @Operation(summary = "Retorna uma Vaga de Estacionamento por id",
      description = "Retorna uma Vaga de Estacionamento, buscando por id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "OK",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ParkingSpotModel.class))
      ),
      @ApiResponse(responseCode = "404",
          description = "Não foi encontrada vaga para o id informado"
      )
  }  )
  public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") UUID id) {
    Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
    if (!parkingSpotModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found.");
    }
    return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/{id}")
  @Operation(summary = "Apaga uma Vaga de Estacionamento por id",
      description = "Apaga uma Vaga de Estacionamento que tenha o id informado")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "OK",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ParkingSpotModel.class))
      ),
      @ApiResponse(responseCode = "404",
          description = "Não foi encontrada vaga para o id informado"
      )
  }  )
  public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") UUID id) {
    Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
    if (!parkingSpotModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot Not Found.");
    }
    parkingSpotService.delete(parkingSpotModelOptional.get());
    return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PutMapping("/{id}")
  @Operation(summary = "Atualiza dados uma Vaga de Estacionamento por id",
      description = "Atualiza dados de uma Vaga de Estacionamento, buscando por id")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "OK",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ParkingSpotModel.class))
      ),
      @ApiResponse(responseCode = "404",
          description = "Não foi encontrada vaga para o id informado"
      )
  }  )
  public ResponseEntity<Object> updateParkingSpot(
      @PathVariable(value = "id") UUID id, @RequestBody @Valid ParkingSpotDto parkingSpotDto) {
    Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findById(id);
    if (!parkingSpotModelOptional.isPresent()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
    }
    var parkingSpotModel = new ParkingSpotModel();
    BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
    parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
    parkingSpotModel.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
    return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
  }
}
