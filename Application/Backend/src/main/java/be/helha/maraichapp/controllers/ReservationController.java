package be.helha.maraichapp.controllers;

import be.helha.maraichapp.dto.ReservationDTO;
import be.helha.maraichapp.models.Reservation;
import be.helha.maraichapp.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    @RequestMapping("/addReservation")
    public Reservation addReservation(@RequestBody ReservationDTO reservationDTO){
        return this.reservationService.addReservation(reservationDTO);
    }

    @GetMapping
    @RequestMapping("getAll")
    public List<Reservation> getAllReservations(){
        return this.reservationService.getAllReservations();
    }


}
