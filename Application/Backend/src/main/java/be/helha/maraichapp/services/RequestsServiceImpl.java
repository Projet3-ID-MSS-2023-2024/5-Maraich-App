package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Requests;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.RequestsRepository;
import be.helha.maraichapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestsServiceImpl implements RequestsService{

    @Autowired
    private RequestsRepository requestsRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Requests addRequests(Requests requests) {
        ExampleMatcher userMatcher = ExampleMatcher.matching();
        // Verify if the customer already exists in the database
        Users user = userRepository.findById(requests.getUser().getIdUser()).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        requests.setUser(user);
        requests = this.requestsRepository.save(requests);
        user.setRequests(requests);
        userRepository.save(user);
        return requests;
    }

    @Override
    public List<Requests> getAllRequests() {
        return this.requestsRepository.findAll();
    }

    @Override
    public Requests getRequestsById(int id) {
        Optional<Requests> requestsOptional = this.requestsRepository.findById(id);
        return requestsOptional.orElse(null);
    }

    @Override
    public Requests updateRequests(Requests requests) {
        if (this.requestsRepository.existsById(requests.getId())) {
            return this.requestsRepository.save(requests);
        }
        return null;
    }

    @Override
    public void deleteRequestsById(int id) {
        this.requestsRepository.deleteById(id);
    }
}
