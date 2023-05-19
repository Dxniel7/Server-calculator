import java.io.IOException;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/calculate", new CalculationHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is listening on port " + port);
    }

    static class CalculationHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String operation = br.readLine();

                // Realizar el cálculo de la operación
                String result = evaluateOperation(operation);

                // Construir la respuesta
                String response = "{\"result\": \"" + result + "\"}";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(405, 0); // Method Not Allowed
                exchange.close();
            }
        }
    }

    private static String evaluateOperation(String operation) {
        // Implementa aquí tu lógica para evaluar la operación y obtener el resultado
        // Por ejemplo, puedes utilizar una librería de evaluación de expresiones matemáticas

        // Ejemplo básico de evaluación utilizando ScriptEngine
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        try {
            Object result = engine.eval(operation);
            return result.toString();
        } catch (ScriptException e) {
            e.printStackTrace();
            return "Error: No se pudo evaluar la operación.";
        }
    }
}
