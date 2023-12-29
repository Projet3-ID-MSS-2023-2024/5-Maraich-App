package be.helha.maraichapp.controllers;

import be.helha.maraichapp.dto.ReservationDTO;
import be.helha.maraichapp.dto.ReservationUpdateDTO;
import be.helha.maraichapp.models.Reservation;
import be.helha.maraichapp.services.ReservationService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @RequestMapping("/getAll")
    public List<Reservation> getAllReservations(){
        return this.reservationService.getAllReservations();
    }

    @GetMapping
    @RequestMapping("/getShoppingCartUser/{idUser}")
    public List<Reservation> getShoppingCart(@PathVariable("idUser") int idUser){
        return this.reservationService.getShoppingCart(idUser);
    }

    @PutMapping
    @RequestMapping("/update")
    public Reservation updateReservationQuantity(@RequestBody ReservationUpdateDTO reservationUpdateDTO){
        return reservationService.updateReservationQuantity(reservationUpdateDTO.idReservation(), reservationUpdateDTO.newQuantity());
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReservationById(@PathVariable int id) {
        reservationService.deleteReservationById(id);
    }

    @DeleteMapping("/deleteShoppingCart/{idUser}")
    public void deleteShoppingCartByUserId(@PathVariable int idUser) {
        reservationService.deleteShoppingCartByUserId(idUser);
    }

    @GetMapping
    @RequestMapping("/existShoppingCart/{idUser}/{idShop}")
    public Map<String, String> existShoppingCart(@PathVariable("idUser") int idUser, @PathVariable("idShop") int idShop) {
        return this.reservationService.existShoppingCart(idUser, idShop);
    }

}
