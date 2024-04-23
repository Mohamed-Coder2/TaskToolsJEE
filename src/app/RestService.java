package app;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import ejb.Rest;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
@Path("/rest")
public class RestService {

    @PersistenceContext(unitName="hello")
    private EntityManager entityManager;

    @EJB
    Rest rest;

    @GET
    @Path("/history")
    @Produces(MediaType.TEXT_PLAIN)
    public List<Rest> getHistory() {
        String simpleQuery = "SELECT Number1,Number2,Operation FROM Rest";
        Query query = entityManager.createQuery(simpleQuery);
        List<Rest> list = query.getResultList();
        return list;
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
