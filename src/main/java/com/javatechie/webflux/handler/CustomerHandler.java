package com.javatechie.webflux.handler;

import com.javatechie.webflux.dao.CustomerDao;
import com.javatechie.webflux.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerHandler {

    @Autowired
    private CustomerDao dao;


    public Mono<ServerResponse> loadCustomers(ServerRequest request){
        Flux<Customer> customerList = dao.getCustomerList();
        return ServerResponse.ok().body(customerList,Customer.class);
    }


    public Mono<ServerResponse> findCustomer(ServerRequest request){
        //Así se obtiene el valor de la variable de la url
      int customerId= Integer.valueOf( request.pathVariable("input"));
      //De las siguentes dos maneras se puede obterner un mono de un flux cuando se sabe 
      //que solo se va a abtener un elemento y se quiere un mono
       // dao.getCustomerList().filter(c->c.getId()==customerId).take(1).single();
        Mono<Customer> customerMono = dao.getCustomerList().filter(c -> c.getId() == customerId).next();
        return ServerResponse.ok().body(customerMono,Customer.class);
    }


    public Mono<ServerResponse> saveCustomer(ServerRequest request){
        Mono<Customer> customerMono = request.bodyToMono(Customer.class);
        Mono<String> saveResponse = customerMono.map(dto -> dto.getId() + ":" + dto.getName());
        return ServerResponse.ok().body(saveResponse,String.class);
    }



}
