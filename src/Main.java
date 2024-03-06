import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;



class Airport {
    private List<String> airports;
    private Map<String, List<String>> airportToAirplanes;


    public Airport() {
        airports = new ArrayList<>();
        airportToAirplanes = new HashMap<>();
    }

    public void createAirport(String airportName) {
        if (airports.contains(airportName)) {
            System.out.println("Error: Airport with the same name already exists.");
            return;
        }

        airports.add(airportName);
        airportToAirplanes.put(airportName, new ArrayList<>());
        System.out.println("Airport created: " + airportName);
    }

    public void deleteAirport(String airport) {
        airports.remove(airport);
        List<String> airplanes = airportToAirplanes.get(airport);
        if (airplanes != null) {
            for (String airplane : airplanes) {
                System.out.println("Deleting airplane: " + airplane);
            }
            airportToAirplanes.remove(airport);
        }
        System.out.println("Airport deleted: " + airport);
    }

    public void viewAirports() {
        System.out.println("Airports:");
        for (String airport : airports) {
            System.out.println("Airport: " + airport);
            List<String> airplanes = airportToAirplanes.get(airport);
            if (airplanes != null && !airplanes.isEmpty()) {
                System.out.println("  Associated Airplanes:");
                for (String airplane : airplanes) {
                    System.out.println("    " + airplane);
                }
            } else {
                System.out.println("  No airplanes associated.");
            }
        }
    }



    public void addAirplaneToAirport(String airport, String airplane) {
        List<String> airplanes = airportToAirplanes.get(airport);
        if (airplanes != null) {
            airplanes.add(airplane);
            System.out.println("Airplane associated with the airport.");
        } else {
            System.out.println("Error: Airport not found.");
        }
    }

    public boolean hasAirplanes(String airportName) {
        List<String> airplanes = airportToAirplanes.get(airportName);
        return airplanes != null && !airplanes.isEmpty();
    }

    public void removeAirplaneFromAirport(String airport, String airplane) {
        List<String> airplanes = airportToAirplanes.get(airport);
        if (airplanes != null) {
            airplanes.remove(airplane);
        } else {
            System.out.println("Error: Airport not found.");
        }
    }

    public boolean existsAirport(String airportName) {
        return airports.contains(airportName);
    }

    public List<String> getAirplanes(String airportName) {
        return airportToAirplanes.get(airportName);
    }
    public List<String> getAirports() {
        return airports;
    }

    public void clear() {
        airports.clear();
        airportToAirplanes.clear();
    }

}


class Route {
    private List<String> routes;
    private Airport airport;
    private Map<String, Integer> routeDistances;
    private Airplane airplane;
    private Scanner scanner;

    public Route(Airport airport, Airplane airplane, Scanner scanner) {
        routes = new ArrayList<>();
        routeDistances = new HashMap<>();
        this.airport = airport;
        this.airplane = airplane;
        this.scanner = scanner;
    }

    public void createRoute(String departureAirport, String arrivalAirport, int distance) {
        if (!airport.existsAirport(departureAirport) || !airport.existsAirport(arrivalAirport)) {
            System.out.println("Error: One or both airports do not exist.");
            return;
        }

        if (departureAirport.equals(arrivalAirport)) {
            System.out.println("Error: Departure airport and arrival airport cannot be the same.");
            return;
        }

        if (!airport.hasAirplanes(departureAirport)) {
            System.out.println("Error: There are no airplanes available at the departure airport.");
            return;
        }

        List<String> airplanes = airport.getAirplanes(departureAirport);
        if (airplanes == null || airplanes.isEmpty()) {
            System.out.println("Error: There are no airplanes available at the departure airport.");
            return;
        }

        System.out.println("Available airplanes at " + departureAirport + ":");
        for (int i = 0; i < airplanes.size(); i++) {
            String airplaneName = airplanes.get(i);
            Airplane.AirplaneDetails airplaneDetails = airplane.getAirplaneDetails(airplaneName);
            System.out.println((i + 1) + ". " + airplaneDetails);
        }

        System.out.print("Choose an airplane index: ");
        int selectedAirplaneIndex = scanner.nextInt();
        scanner.nextLine();

        if (selectedAirplaneIndex < 1 || selectedAirplaneIndex > airplanes.size()) {
            System.out.println("Error: Invalid airplane index.");
            return;
        }

        String selectedAirplane = airplanes.get(selectedAirplaneIndex - 1);
        Airplane.AirplaneDetails selectedAirplaneDetails = airplane.getAirplaneDetails(selectedAirplane);
        if (selectedAirplaneDetails.getRange() < distance) {
            System.out.println("Error: The selected airplane does not have sufficient range for this route.");
            return;
        }

        String route = departureAirport + " - " + arrivalAirport;
        routes.add(route);
        routeDistances.put(route, distance);
        System.out.println("Route created: " + route + ", Distance: " + distance + " km, Airplane: " + selectedAirplane);

    }
    public void deleteRoute(int index) {
        if (index < 1 || index > routes.size()) {
            System.out.println("Error: Invalid route index.");
            return;
        }

        String route = routes.get(index - 1);
        routes.remove(index - 1);
        System.out.println("Route deleted: " + route);
    }

    public void viewRoutes() {
        System.out.println("Routes:");
        for (int i = 0; i < routes.size(); i++) {
            System.out.println((i + 1) + ". " + routes.get(i));
        }
    }

    public String getRoute(int index) {
        if (index < 1 || index > routes.size()) {
            return null;
        }
        return routes.get(index - 1);
    }
    public List<String> getRoutes() {
        return routes;
    }

    public int getDistance(String route) {
        return routeDistances.getOrDefault(route, 0);
    }

    public void clear() {
        routes.clear();
        routeDistances.clear();
    }

    public void createRoute(String routeName, int distance) {
    }
}



class Flight {
    private List<String> flights;

    public Flight() {
        flights = new ArrayList<>();
    }

    public void createFlight(String flightName, String selectedRoute, String departureTime, String departureDate, String arrivalTime, String arrivalDate) {
        String flightDetails = flightName + " | Route: " + selectedRoute + " | Departure: " + departureTime + " " + departureDate + " | Arrival: " + arrivalTime + " " + arrivalDate;
        flights.add(flightDetails);
        System.out.println("Flight created: " + flightDetails);
    }

    public void deleteFlight(String flight) {
        flights.remove(flight);
        System.out.println("Flight deleted: " + flight);
    }
    public List<String> getFlights() {
        return flights;
    }

    public String getFlight(int index) {
        if (index >= 0 && index < flights.size()) {
            return flights.get(index);
        }
        return null;
    }
    public void viewFlights() {
        System.out.println("Flights:");
        for (String flight : flights) {
            System.out.println(flight);
        }
    }

    public void clear() {
        flights.clear();
    }

    public void createFlight(String flightDetails) {
    }
}
class Airplane {
    private List<AirplaneDetails> airplanes;
    private Airport airport;

    public Airplane(Airport airport) {
        airplanes = new ArrayList<>();
        this.airport = airport;
    }

    public void createAirplane(String name, int seatCount, int range, String airportName) {
        if (!airport.existsAirport(airportName)) {
            System.out.println("Error: The specified airport does not exist.");
            return;
        }

        AirplaneDetails airplane = new AirplaneDetails(name, seatCount, range);
        airplanes.add(airplane);
        airport.addAirplaneToAirport(airportName, name);
        System.out.println("Airplane created: " + airplane);
    }


    public void deleteAirplane(String name) {
        AirplaneDetails airplaneToDelete = null;
        for (AirplaneDetails airplane : airplanes) {
            if (airplane.getName().equals(name)) {
                airplaneToDelete = airplane;
                break;
            }
        }

        if (airplaneToDelete != null) {
            airplanes.remove(airplaneToDelete);
            System.out.println("Airplane deleted: " + airplaneToDelete);
        } else {
            System.out.println("Airplane not found.");
        }
    }

    public void viewAirplanes() {
        System.out.println("Airplanes:");
        for (AirplaneDetails airplane : airplanes) {
            System.out.println(airplane);
        }
    }

    public AirplaneDetails getAirplaneDetails(String airplaneName) {
        for (AirplaneDetails airplane : airplanes) {
            if (airplane.getName().equals(airplaneName)) {
                return airplane;
            }
        }
        return null;
    }

    public List<AirplaneDetails> getAirplanes() {
        return airplanes;
    }

    public class AirplaneDetails {
        private String name;
        private int seatCount;
        private int range;

        public AirplaneDetails(String name, int seatCount, int range) {
            this.name = name;
            this.seatCount = seatCount;
            this.range = range;
        }

        public String getName() {
            return name;
        }

        public int getSeatCount() {
            return seatCount;
        }

        public int getRange() {
            return range;
        }

        @Override
        public String toString() {
            return "Name: " + name + ", Seats: " + seatCount + ", Range: " + range + " km";
        }
    }

    public void clear() {
        airplanes.clear();
    }
}

class Client {
    private String name;
    private String phone;
    private boolean isCompany;
    private int airplaneCount;
    private List<Ticket> tickets;

    public Client(String name, String phone, boolean isCompany, int airplaneCount) {
        this.name = name;
        this.phone = phone;
        this.isCompany = isCompany;
        this.airplaneCount = airplaneCount;
        this.tickets = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }


    public boolean isCompany() {
        return isCompany;
    }

    public int getAirplaneCount() {
        return airplaneCount;
    }

    @Override
    public String toString() {
        String clientType = isCompany ? "Company" : "Individual";
        String details = "Name: " + name + ", Phone: " + phone;
        if (isCompany) {
            details += ", Airplane Count: " + airplaneCount;
        }
        return clientType + " " + details;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}

class Ticket {
    private String flight;
    private Client client;

    public Ticket(String flight, Client client) {
        this.flight = flight;
        this.client = client;
        client.addTicket(this);
        System.out.println("Ticket booked: " + flight + " - " + client.getName());
    }

    public String getFlight() {
        return flight;
    }

    public Client getClient() {
        return client;
    }
}


public class Main {

    private static final String FILE_NAME = "data.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Airport airport = new Airport();
        Airplane airplane = new Airplane(airport);
        Route route = new Route(airport, airplane, scanner);
        Flight flight = new Flight();
        List<Client> clients = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();

        int choice = 0;
        while (choice != 18) {
            System.out.println("██████╗░███████╗██╗░░░░░░█████╗░██╗░░░██╗██╗░█████╗░\n" +
                    "██╔══██╗██╔════╝██║░░░░░██╔══██╗██║░░░██║██║██╔══██╗\n" +
                    "██████╦╝█████╗░░██║░░░░░███████║╚██╗░██╔╝██║███████║\n" +
                    "██╔══██╗██╔══╝░░██║░░░░░██╔══██║░╚████╔╝░██║██╔══██║\n" +
                    "██████╦╝███████╗███████╗██║░░██║░░╚██╔╝░░██║██║░░██║\n" +
                    "╚═════╝░╚══════╝╚══════╝╚═╝░░╚═╝░░░╚═╝░░░╚═╝╚═╝░░╚═╝\n");
            System.out.println("▒█▀▄▀█ ▒█▀▀▀ ▒█▄░▒█ ▒█░▒█ \n" +
                    "▒█▒█▒█ ▒█▀▀▀ ▒█▒█▒█ ▒█░▒█ \n" +
                    "▒█░░▒█ ▒█▄▄▄ ▒█░░▀█ ░▀▄▄▀\n");
            System.out.println("AIRPORT: 1-create, 2-delete, 3-view");
            System.out.println("ROUTE: 4-create, 5-delete, 6-view");
            System.out.println("FLIGHT: 7-create, 8-view");
            System.out.println("CLIENT: 9-create, 10-delete, 11-view");
            System.out.println("AIRPLANE: 12-create, 13-delete, 14-view");
            System.out.println("DATA: 15-save, 16-load");
            System.out.println("BUY TICKET: 17");
            System.out.println("EXIT: 18");
            System.out.print("\nEnter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter airport: ");
                    String airportName = scanner.nextLine();
                    airport.createAirport(airportName);
                    break;
                case 2:
                    System.out.print("Enter airport: ");
                    String airportToDelete = scanner.nextLine();
                    airport.deleteAirport(airportToDelete);
                    break;
                case 3:
                    airport.viewAirports();
                    break;
                case 4:
                    System.out.print("Enter departure airport: ");
                    String departureAirport = scanner.nextLine();

                    System.out.print("Enter arrival airport: ");
                    String arrivalAirport = scanner.nextLine();

                    System.out.print("Enter distance (in km) between departure and arrival airports: ");
                    int distance = scanner.nextInt();
                    scanner.nextLine();

                    route.createRoute(departureAirport, arrivalAirport, distance);
                    break;
                case 5:
                    route.viewRoutes();
                    System.out.print("Choose a route index to delete: ");
                    int routeToDeleteIndex = scanner.nextInt();
                    scanner.nextLine();
                    route.deleteRoute(routeToDeleteIndex);
                    break;
                case 6:
                    route.viewRoutes();
                    break;
                case 7:
                    System.out.print("Enter flight: ");
                    String flightName = scanner.nextLine();

                    System.out.println("Available routes:");
                    route.viewRoutes();
                    System.out.print("Choose a route index: ");
                    int selectedRouteIndex = scanner.nextInt();
                    scanner.nextLine();

                    String selectedRoute = route.getRoute(selectedRouteIndex);
                    if (selectedRoute == null) {
                        System.out.println("Error: Invalid route index.");
                        break;
                    }

                    System.out.print("Enter departure time: ");
                    String departureTime = scanner.nextLine();

                    System.out.print("Enter departure date: ");
                    String departureDate = scanner.nextLine();

                    System.out.print("Enter arrival time: ");
                    String arrivalTime = scanner.nextLine();

                    System.out.print("Enter arrival date: ");
                    String arrivalDate = scanner.nextLine();

                    flight.createFlight(flightName, selectedRoute, departureTime, departureDate, arrivalTime, arrivalDate);
                    break;
                case 8:
                    List<String> availableFlights = flight.getFlights();
                    System.out.println("Available flights:");
                    for (int i = 0; i < availableFlights.size(); i++) {
                        System.out.println(i + ". " + availableFlights.get(i));
                    }
                    System.out.print("Choose a flight index: ");
                    int selectedFlightIndex8 = scanner.nextInt();
                    scanner.nextLine();

                    if (selectedFlightIndex8 >= 0 && selectedFlightIndex8 < availableFlights.size()) {
                        String selectedFlight = availableFlights.get(selectedFlightIndex8);
                        System.out.println("Selected flight: " + selectedFlight);
                    } else {
                        System.out.println("Invalid flight index.");
                    }
                    break;
                case 9:
                    System.out.print("Enter client name: ");
                    String clientName = scanner.nextLine();

                    System.out.print("Enter client phone: ");
                    String clientPhone = scanner.nextLine();

                    System.out.print("Is the client a company? (1-yes/2-no): ");
                    String isCompanyInput = scanner.nextLine();
                    boolean isCompany = isCompanyInput.equalsIgnoreCase("1");

                    int airplaneCount = 0;
                    if (isCompany) {
                        System.out.print("Enter the number of airplanes for the company: ");
                        airplaneCount = scanner.nextInt();
                        scanner.nextLine();
                    }

                    Client client = new Client(clientName, clientPhone, isCompany, airplaneCount);
                    clients.add(client);
                    System.out.println("Client created: " + client);

                    break;
                case 10:
                    System.out.println("Contact us to delete your account (polibud@pb.edu.pl)");
                    break;
                case 11:
                    System.out.println("All Clients:");
                    for (Client existingClient : clients) {
                        System.out.println(existingClient);
                        List<Ticket> clientTickets = existingClient.getTickets();
                        if (!clientTickets.isEmpty()) {
                            System.out.println("  Associated Tickets:");
                            for (Ticket tickete : clientTickets) {
                                System.out.println("    " + tickete.getFlight());
                            }
                        } else {
                            System.out.println("  No tickets associated.");
                        }
                    }
                    break;
                case 12:
                    System.out.print("Enter airplane name: ");
                    String airplaneName = scanner.nextLine();

                    System.out.print("Enter seat count: ");
                    int seatCount = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter range: ");
                    int range = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter airport name to associate the airplane: ");
                    String associatedAirportName = scanner.nextLine();

                    airplane.createAirplane(airplaneName, seatCount, range, associatedAirportName);
                    break;
                case 13:
                    System.out.print("Enter airplane: ");
                    String airplaneToDelete = scanner.nextLine();
                    airplane.deleteAirplane(airplaneToDelete);
                    break;
                case 14:
                    airplane.viewAirplanes();
                    break;
                case 15:
                    saveData(airport, airplane, route, flight, clients, tickets);
                    break;
                case 16:
                    loadData(airport, airplane, route, flight, clients, tickets);
                    break;
                case 17:
                    System.out.println("Available flights:");
                    flight.viewFlights();
                    System.out.print("Choose a flight index: ");
                    int selectedFlightIndex = scanner.nextInt();
                    scanner.nextLine();

                    String selectedFlight = flight.getFlight(selectedFlightIndex);
                    if (selectedFlight == null) {
                        System.out.println("Error: Invalid flight index.");
                        break;
                    }

                    System.out.println("Available clients:");
                    int clientIndex = 0;
                    for (Client existingClient : clients) {
                        System.out.println(++clientIndex + ". " + existingClient.getName());
                    }
                    System.out.print("Choose a client index: ");
                    int selectedClientIndex = scanner.nextInt();
                    scanner.nextLine(); // Consume newline character

                    if (selectedClientIndex < 1 || selectedClientIndex > clients.size()) {
                        System.out.println("Error: Invalid client index.");
                        break;
                    }

                    Client selectedClient = clients.get(selectedClientIndex - 1);
                    Ticket ticket = new Ticket(selectedFlight, selectedClient);
                    tickets.add(ticket);
                    break;
                case 18:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            System.out.println();
        }
        scanner.close();
    }
    private static void saveData(Airport airport, Airplane airplane, Route route, Flight flight, List<Client> clients, List<Ticket> tickets) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME)))) {
            writer.println("Airports:");
            List<String> airports = airport.getAirports();
            for (String airportName : airports) {
                writer.println(airportName);
                List<String> airplanes = airport.getAirplanes(airportName);
                for (String airplaneName : airplanes) {
                    writer.println("  " + airplaneName);
                }
            }

            writer.println("Routes:");
            List<String> routes = route.getRoutes();
            for (String routeName : routes) {
                writer.println(routeName);
                writer.println(route.getDistance(routeName));
            }

            writer.println("Flights:");
            List<String> flights = flight.getFlights();
            for (String flightDetails : flights) {
                writer.println(flightDetails);
            }

            writer.println("Clients:");
            for (Client client : clients) {
                writer.println(client.getName());
                writer.println(client.getPhone());
                writer.println(client.isCompany());
                writer.println(client.getAirplaneCount());
            }

            writer.println("Tickets:");
            for (Ticket ticket : tickets) {
                writer.println(ticket.getFlight());
                writer.println(ticket.getClient().getName());
            }

            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while saving data: " + e.getMessage());
        }
    }

    private static void loadData(Airport airport, Airplane airplane, Route route, Flight flight, List<Client> clients, List<Ticket> tickets) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            airport.clear();
            airplane.clear();
            route.clear();
            flight.clear();
            clients.clear();
            tickets.clear();

            String line;
            String section = "";
            String airportName = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Airports:")) {
                    section = "Airports";
                } else if (line.startsWith("Routes:")) {
                    section = "Routes";
                } else if (line.startsWith("Flights:")) {
                    section = "Flights";
                } else if (line.startsWith("Clients:")) {
                    section = "Clients";
                } else if (line.startsWith("Tickets:")) {
                    section = "Tickets";
                } else {
                    switch (section) {
                        case "Airports":
                            if (line.startsWith("  ")) {
                                String airplaneName = line.trim();
                                airport.addAirplaneToAirport(airportName, airplaneName);
                            } else {
                                airportName = line.trim();
                                airport.createAirport(airportName);
                            }
                            break;
                        case "Routes":
                            String routeName = line.trim();
                            int distance = Integer.parseInt(reader.readLine().trim());
                            route.createRoute(routeName, distance);
                            break;
                        case "Flights":
                            String flightDetails = line.trim();
                            flight.createFlight(flightDetails);
                            break;
                        case "Clients":
                            String name = line.trim();
                            String phone = reader.readLine().trim();
                            boolean isCompany = Boolean.parseBoolean(reader.readLine().trim());
                            int airplaneCount = Integer.parseInt(reader.readLine().trim());
                            clients.add(new Client(name, phone, isCompany, airplaneCount));
                            break;
                        case "Tickets":
                            String flightName = line.trim();
                            String clientName = reader.readLine().trim();
                            Client client = findClient(clients, clientName);
                            if (client != null) {
                                tickets.add(new Ticket(flightName, client));
                            }
                            break;
                    }
                }
            }

            System.out.println("Data loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while loading data: " + e.getMessage());
        }
    }

    private static Client findClient(List<Client> clients, String name) {
        for (Client client : clients) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }
}