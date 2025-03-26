package com.testing.rest;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
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

    //id 
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") String id) {
        Optional<Users> user = usersRepository.findById(new ObjectId(id));
        if (user != null) {
            return Response.ok(user).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario no encontrado con ID: " + id)
                    .build();
        }
    }

    // POST: Crear un nuevo usuario
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(Users user) {
        usersRepository.save(user);
        return Response.status(Response.Status.CREATED)
                .entity("Usuario creado: " + user.getEmail())
                .build();
    }

    // DELETE: Eliminar un usuario por correo electrónico
    @DELETE
    @Path("/{email}")
    @RolesAllowed({ "admin" })
    public Response deleteUserByEmail(@PathParam("email") String email) {
        Users user = usersRepository.findByEmail(email);
        if (user != null) {
            usersRepository.delete(user);
            return Response.ok("Usuario eliminado: " + email).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario no encontrado con email: " + email)
                    .build();
        }
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
