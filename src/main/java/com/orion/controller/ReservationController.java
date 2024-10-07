package com.orion.controller;

import com.orion.dto.filter.BookingFilter;
import com.orion.dto.reservation.ReservationDto;
import com.orion.dto.vehicle.Available;
import com.orion.generics.ResponseObject;
import com.orion.security.CustomUserDetails;
import com.orion.service.ReservationService;
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
    @PostMapping
    public ResponseEntity<ResponseObject> createReservation(@RequestBody ReservationDto reservationDto) {
        String methodName = "createReservation";

        log.info("{} -> Create reservation", methodName);
        ResponseObject response = service.create(reservationDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/all")
    public ResponseEntity<ResponseObject> getReservations(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                          @RequestBody BookingFilter filter,
                                                          @RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer size,
                                                          @RequestParam(required = false) String search) {
        String methodName = "getReservations";
        log.info("{} -> Get reservations", methodName);
        ResponseObject response = service.getReservations(customUserDetails.getUsername(), filter, page, size, search);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<ResponseObject> getReservationByBooking(@PathVariable Long bookingId) {
        String methodName = "getReservation";
        log.info("{} -> Get reservation", methodName);
        ResponseObject response = service.getReservationFromBookingId(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/details/{bookingId}")
    public ResponseEntity<ResponseObject> getReservationDetails(@PathVariable Long bookingId) {
        String methodName = "getReservationDetails";
        log.info("{} -> Get reservation details", methodName);
        ResponseObject response = service.getDetails(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/cancel/{bookingId}")
    public ResponseEntity<ResponseObject> cancelReservation(@PathVariable Long bookingId) {
        String methodName = "cancelReservation";
        log.info("{} -> Cancel reservation", methodName);
        ResponseObject response = service.cancelReservation(bookingId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/availability")
    public ResponseEntity<ResponseObject> checkAvailability(@RequestBody Available available) {
        String methodName = "checkAvailability";
        log.info("{} -> Check availability", methodName);
        ResponseObject response = service.checkAvailability(available);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
