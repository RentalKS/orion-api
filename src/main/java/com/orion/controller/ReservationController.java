package com.orion.controller;

import com.orion.dto.filter.BookingFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.Available;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.url}/reservation")
@RequiredArgsConstructor
@Log4j2
public class ReservationController {
    private final ReservationService service;

    @Operation(summary = "Create reservation", description = "Create reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping
    public ResponseEntity<ResponseObject> createReservation(@RequestBody ReservationDto reservationDto) {
        String methodName = "createReservation";

        log.info("{} -> Create reservation", methodName);
        ResponseObject response = service.create(reservationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get all reservations", description = "Get all reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/all")
    public ResponseEntity<ResponseObject> getReservations(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                          @RequestBody BookingFilter filter,
                                                          @RequestParam(defaultValue = "1") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(required = false) String search) {
        String methodName = "getReservations";
        log.info("{} -> Get reservations", methodName);
        ResponseObject response = service.getReservations(customUserDetails.getUsername(), filter, page, size, search);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get reservation by booking id", description = "Get reservation by booking id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("{bookingId}")
    public ResponseEntity<ResponseObject> getReservationByBooking(@PathVariable Long bookingId) {
        String methodName = "getReservation";
        log.info("{} -> Get reservation", methodName);
        ResponseObject response = service.getReservationFromBookingId(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Get reservation details", description = "Get reservation details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @GetMapping("/details/{bookingId}")
    public ResponseEntity<ResponseObject> getReservationDetails(@PathVariable Long bookingId) {
        String methodName = "getReservationDetails";
        log.info("{} -> Get reservation details", methodName);
        ResponseObject response = service.getDetails(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Cancel reservation", description = "Cancel reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<ResponseObject> cancelReservation(@PathVariable Long bookingId) {
        String methodName = "cancelReservation";
        log.info("{} -> Cancel reservation", methodName);
        ResponseObject response = service.cancelReservation(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Operation(summary = "Check availability", description = "Check availability")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = Available.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data supplied"),
            @ApiResponse(responseCode = "404", description = "Files not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
    })
    @PostMapping("/availability")
    public ResponseEntity<ResponseObject> checkAvailability(@RequestBody Available available) {
        String methodName = "checkAvailability";
        log.info("{} -> Check availability", methodName);
        ResponseObject response = service.checkAvailability(available);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
