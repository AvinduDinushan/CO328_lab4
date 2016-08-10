
package lk.ac.pdn.co328.restapi;
import lk.ac.pdn.co328.studentSystem.Student;
import lk.ac.pdn.co328.studentSystem.StudentRegister;
import org.jboss.resteasy.util.HttpResponseCodes;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lk.ac.pdn.co328.studentSystem.arraylistimplementation.ArraylistStudentRegister;
import lk.ac.pdn.co328.studentSystem.dbimplementation.DerbyStudentRegister;

@Path("rest")
public class StudentService
{
    //private static StudentRegister register = new ArraylistStudentRegister();
    private static  StudentRegister register = new DerbyStudentRegister();

    @GET
    @Path("student/{id}")
    // Uncommenting this will let the reciver know that you are sending a json
    @Produces( MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML )
    public Response viewStudent(@PathParam("id") int id) {
        try {
            Student st = register.findStudent(id);

            if (st == null) {
                return Response.status(HttpResponseCodes.SC_NOT_FOUND).build();
            }
            return Response.status(HttpResponseCodes.SC_OK).entity(st).build();
        }catch(Exception e){
            return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("student/{id}")
    @Consumes(MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML)
    public Response modifyStudent(@PathParam("id") int id, Student input)
    {
        Student std = null;
        if(input == null) {
            try {
                register.findStudent(id);
            } catch (Exception e) {
                e.printStackTrace();
                return Response.status(HttpResponseCodes.SC_FOUND).entity("error in id").build();
            }
            if(std == null){
                return Response.status(HttpResponseCodes.SC_NOT_FOUND).build();
            }else {
                try {
                    register.addStudent(input);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Response.status(HttpResponseCodes.SC_FOUND).entity("Updating Error").build();
                }
            }
        }else{
            return Response.status(HttpResponseCodes.SC_FOUND).entity("Updating Error").build();
        }
        return  Response.status(HttpResponseCodes.SC_OK).build();
    }

    @DELETE
    @Path("student/{id}")

    public Response deleteStudent(@PathParam("id") int id) {
        try{
            if ((register.findStudent(id) != (null))) {
                try {
                    register.removeStudent(id);
                    return Response.status(HttpResponseCodes.SC_OK).build();
                } catch (Exception e) {
                    return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
                }
            }else {
                return Response.status(HttpResponseCodes.SC_NOT_FOUND).build();
            }
        }catch (Exception e){
            return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("student/new")
    @Consumes(MediaType.APPLICATION_JSON + "," + MediaType.APPLICATION_XML)
    public Response addStudent(Student input) {
        Student std = null;
        if (input != (null)) {
            try{
               std = register.findStudent(input.getId());
            }catch(Exception e){
                e.printStackTrace();
                return Response.status(HttpResponseCodes.SC_FOUND).entity("Error finding the ID").build();
            }
            if(std == null){
                try {
                    register.addStudent(input);
                    return Response.status(HttpResponseCodes.SC_OK).build();
                } catch (Exception e) {
                    e.printStackTrace();
                    return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
                }
            }else{
                return Response.status(HttpResponseCodes.SC_INTERNAL_SERVER_ERROR).build();
            }
        }else{
            return  Response.status(HttpResponseCodes.SC_BAD_REQUEST).build();
        }
    }
}