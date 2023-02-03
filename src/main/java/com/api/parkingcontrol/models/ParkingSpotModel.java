package com.api.parkingcontrol.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TB_PARKING_SPOT")
@Getter
@Setter
@Schema(description="All details about the parking spot. ")
public class ParkingSpotModel implements Serializable {

  @Serial
  private static final long serialVersionUID = 7685126431483517296L;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Schema(description = "parkingSpotNumber should have less than 10 characters")
  @Column(nullable = false, unique = true, length = 10)
  private String parkingSpotNumber;

  @Column(nullable = false, unique = true, length = 7)
  private String licensePlateCar;

  @Column(nullable = false, length = 70)
  private String brandCar;

  @Column(nullable = false, length = 70)
  private String modelCar;

  @Column(nullable = false, length = 70)
  private String colorCar;

  @Column(nullable = false)
  private LocalDateTime registrationDate;

  @Column(nullable = false, length = 130)
  private String responsibleName;

  @Column(nullable = false, length = 30)
  private String apartment;

  @Column(nullable = false, length = 30)
  private String block;
}
