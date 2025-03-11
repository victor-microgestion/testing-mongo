package com.testing.rest;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/mock")
public class ControllerApi {

    @Inject
    UsersRepository usersRepository;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMock() {
        List<Users> users = usersRepository.findAll().toList();
        return Response.ok(users).build();
    }

    @PUT
    @Path("/{email}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("email") String email, Users updatedUser) {
        Users user = usersRepository.findByEmail(email);
        user.setName(updatedUser.getName());
        user.setEmail(email);
        user.setProvider(updatedUser.getProvider());
        user.setRole(updatedUser.getRole());
        usersRepository.save(user);
        return Response.ok(user).build();
    }
}
