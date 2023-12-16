package be.helha.maraichapp.controllers;

import be.helha.maraichapp.models.Requests;
import be.helha.maraichapp.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    RequestsService requestsService;

    @GetMapping("/{id}")
    public Requests getRequestById(@PathVariable int id) {
        return requestsService.getRequestsById(id);
    }
    @GetMapping
    public List<Requests> getRequests() {
        return requestsService.getAllRequests();
    }
    @PostMapping("newRequest")
    public Requests addRequest(@RequestBody Requests requests) {
        return requestsService.addRequests(requests);
    }
    @PutMapping("/update/request")
    public Requests updateRequest(@RequestBody Requests requests) {
        return requestsService.updateRequests(requests);
    }
    @DeleteMapping("/{id}")
    public void deleteRequestById(@PathVariable int id) {
        requestsService.deleteRequestsById(id);
    }
}
