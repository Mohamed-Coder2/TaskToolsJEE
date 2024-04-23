package app;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import ejb.Rest;

//import java.util.Collections;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.TypedQuery;

@Stateful
@Path("/rest")
public class RestService {

    //@PersistenceContext(unitName = "hello")
    //private EntityManager entityManager;
	@EJB
    Rest rest;
    ArrayList<Rest> history = new ArrayList<>();

    @GET
    @Path("/history")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rest> getHistory() {
    	return history;
    }


    @POST
    @Path("/calc")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addCalc(String json) {
        try {
            // Parse JSON string
            JsonObject jsonObject = Json.createReader(new StringReader(json)).readObject();
            int number1 = jsonObject.getInt("number1");
            int number2 = jsonObject.getInt("number2");
            String operation = jsonObject.getString("operation");

            // Calculate the result
            double result = calculate(number1, number2, operation);

            // Persist the calculation to the database
            //Rest calc = new Rest();
            //calc.setNumber1(number1);
            //calc.setNumber2(number2);
            //calc.setOperation(operation);
            //entityManager.persist(calc);

            return String.valueOf(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }



    private double calculate(int number1, int number2, String operation) {
        switch (operation) {
            case "+":
                return number1 + number2;
            case "-":
                return number1 - number2;
            case "*":
                return number1 * number2;
            case "/":
                if (number2 != 0) {
                    return number1 / number2;
                } else {
                    throw new ArithmeticException("Division by zero!");
                }
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }
    }
}
