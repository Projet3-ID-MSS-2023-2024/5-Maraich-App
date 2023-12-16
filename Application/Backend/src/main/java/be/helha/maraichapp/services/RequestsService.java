package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Requests;

import java.util.List;

public interface RequestsService {

    Requests addRequests(Requests requests);
    List<Requests> getAllRequests();
    Requests getRequestsById (int id);
    Requests updateRequests(Requests requests);
    void deleteRequestsById(int id);


}
