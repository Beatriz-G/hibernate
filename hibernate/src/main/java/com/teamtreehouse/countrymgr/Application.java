package com.teamtreehouse.countrymgr;

import com.teamtreehouse.countrymgr.model.Country;
import com.teamtreehouse.countrymgr.model.Country.CountryBuilder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static java.lang.System.out;

public class Application {
    // Hold a reusable reference to a SessionFactory(only need one)
    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        // Hibernate SessionFactory
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {
        run();
    }

    private static String promptAction() throws IOException {
        // Menu options
        Map<String, String> menu = new TreeMap<>();
        menu.put("Display", "Display country data");
        menu.put("Analysis", "View maximum and minimum values for each country");
        menu.put("Add", "Add country");
        menu.put("Edit", "Edit country");
        menu.put("Delete", "Delete country");
        menu.put("Quit", "Exit the program");

        System.out.println();
        System.out.println("--------*--*--*--*-Menu-*--*--*--*--------");
        System.out.println();
        for (Map.Entry<String, String> option : menu.entrySet()) {
            out.printf("%s --> %s%n", option.getKey(), option.getValue());
        }

        System.out.print("Select a menu option: ");
        String choice = reader.readLine();
        return choice.trim().toLowerCase();
    }

    public static void run() {
        String choice = "";
        do {
            try {
                choice = promptAction();
                switch (choice) {
                    case "display":
                        displayCountryData(fetchAllCountries());
                        break;
                    case "analysis":
                        displayAnalysis(fetchAllCountries());
                        break;
                    case "add":
                        addNewCountry();
                        break;
                    case "edit":
                        editCountry();
                        break;
                    case "delete":
                        deleteCountry();
                        break;
                    case "quit":
                        out.println("---*--*-End of Program-*--*---");
                        break;
                    default:
                        out.printf("Unknown choice: '%s'. Try again.%n%n%n", choice);
                }
            } catch (IOException ioe) {
                out.println("Problem with input.");
            }
        } while (!choice.equals("quit"));
    }

    // Display the Country Data
    // Fetch all countries with Hibernate
    private static List<Country> fetchAllCountries() {
        // Opening a session
        Session session = sessionFactory.openSession();
        // CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();
        //Create CriteriaQuery
        CriteriaQuery<Country> criteria = builder.createQuery(Country.class);
        // Specify criteria root
        criteria.from(Country.class);
        //Execute query
        List<Country> countries = session.createQuery(criteria).getResultList();
        // Close session
        session.close();

        return countries;
    }

    // Display the Country
    public static void displayCountryData(List<Country> countries) {
        System.out.println();
        System.out.println("-------------------*--*--*-*-View Country Data Here-*--*--*--*------------------");
        System.out.println();
        System.out.printf("%-10s %-30s %-25s %-20s%n", "Code", "Name", "Internet Users", "Literacy Rate");
        // Format country data
        for (Country country : countries) {
            System.out.printf(
                    "%-10s %-30s %-25s %-20s%n",
                    country.getCode(),
                    country.getName(),
                    NumberFormat(country.getInternetUsers()),
                    NumberFormat(country.getAdultLiteracyRate())
            );
        }
    }

    // Round numeric values and display NULL values as --
    private static String NumberFormat(Double value) {
        if (value == null) {
            return "--";
        } else {
            return String.format("%.2f", value);
        }
    }

    // Display the analysis
    private static void displayAnalysis(List<Country> countries) {
        // Calculate and displays max/min values for internetUsers and adultLiteracyRate
        // Max value for internetUsers
        Country maxInternet = countries.stream()
                .filter(country -> country.getInternetUsers() != null)
                .max(Comparator.comparing(Country::getInternetUsers))
                .orElse(null);

        // Max value for internetUsers
        Country minInternet = countries.stream()
                .filter(country -> country.getInternetUsers() != null)
                .min(Comparator.comparing(Country::getInternetUsers))
                .orElse(null);

        // Max value for adultLiteracyRate
        Country maxLiteracy = countries.stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .max(Comparator.comparing(Country::getAdultLiteracyRate))
                .orElse(null);

        // Min value for adultLiteracyRate
        Country minLiteracy = countries.stream()
                .filter(country -> country.getAdultLiteracyRate() != null)
                .min(Comparator.comparing(Country::getAdultLiteracyRate))
                .orElse(null);

        // Display the analysis
        System.out.println();
        System.out.println("--------*--*--*--*-Country Analysis-*--*--*--*--------");
        System.out.println();
        System.out.printf("Country with the highest percentage of internet users is %s with %.2f.%n", maxInternet.getName(), maxInternet.getInternetUsers());
        System.out.printf("Country with the lowest percentage of internet users is %s with %.2f.%n", minInternet.getName(), minInternet.getInternetUsers());
        System.out.printf("Country with the highest adult literacy is %s with %.2f.%n", maxLiteracy.getName(), maxLiteracy.getAdultLiteracyRate());
        System.out.printf("Country with the lowest adult literacy is %s with %.2f.%n", minLiteracy.getName(), minLiteracy.getAdultLiteracyRate());
        System.out.println("End of transaction.");
    }

    // Get country by code
    private static Country fetchCountryCode(String code) {
        Session session = sessionFactory.openSession();
        Country country = session.get(Country.class, code);
        session.close();
        return country;
    }

    // Retrieve existing data and display for user
    private static String userCountryCode() throws IOException {
        displayCountryData(fetchAllCountries());
        // Allow user to pick country by code
        System.out.println("Provide the 3 character code of the country here: ");
        return reader.readLine().trim().toUpperCase();
    }

    // Country editing function
    public static void editCountry() throws IOException {
        String code = userCountryCode();
        Country country = fetchCountryCode(code);

        // If user picks country code that does not exist, message will notify them
        if (country == null) {
            System.out.println("Sorry, that code is not connected to a country.\nReturn to menu and try again.");
            return;
        }
        // Shows country picked with current internet user rate and literacy rate
        System.out.println();
        System.out.println("-----*--*--*--*-Current Country Data-*--*--*--*-----");
        System.out.println();
        System.out.printf("You picked: %s %nWith %.2f percent of Internet Users, and a Literacy Rate of %.2f.%n", country.getName(), country.getInternetUsers(), country.getAdultLiteracyRate());
        System.out.println();

        // Edit country name, interUser data and adultLiteracy data
        System.out.println("Enter new country name: ");
        String newName = reader.readLine().trim();

        System.out.println("Enter new internet user rate: ");
        double newInternetUsers = Double.parseDouble(reader.readLine().trim());

        System.out.println("Enter new adult literacy rate: ");
        double newAdultLiteracyRate = Double.parseDouble(reader.readLine().trim());

        country.setName(newName);
        country.setInternetUsers(newInternetUsers);
        country.setAdultLiteracyRate(newAdultLiteracyRate);

        update(country);
        System.out.println();
        System.out.println("Updated information!");
        System.out.println("End of transaction.");
    }

    // Update data
    private static void update(Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(country);
        session.getTransaction().commit();
        session.close();
    }

    // Save data
    public static void save(Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(country);
        session.getTransaction().commit();
        session.close();
    }

    // Country addition
    private static void addNewCountry() throws IOException {
        System.out.println();
        System.out.println("-----*--*--*--*-Enter data for new country-*--*--*--*----- ");
        System.out.println();

        System.out.println("What is the new country code: (3 characters only) ");
        String code = reader.readLine().trim().toUpperCase();
        if (code.length() != 3) {
            System.out.println("Please keep code to 3 letters only.\nReturn to menu and try again.");
            return;
        }

        System.out.println("What is the name of the country? ");
        String name = reader.readLine().trim();

        System.out.println("Enter percentage of Internet Users: ");
        String internetUsers = reader.readLine().trim();

        System.out.println("Enter adult literacy percentage: ");
        String adultLiteracyRate = reader.readLine().trim();

        // Save data to database
        Country country = new CountryBuilder(name, code)
                .withInternetUsers(Double.parseDouble(internetUsers))
                .withAdultLiteracyRate(Double.parseDouble(adultLiteracyRate))
                .build();
        save(country);
        System.out.println();
        System.out.println("Country added successfully!");
        System.out.println("End of transaction.");
    }

    // Country deletion
    public static void deleteCountry() throws IOException {
        String code = userCountryCode();
        Country country = fetchCountryCode(code);
        System.out.println("You picked " + code + " to be deleted.");
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(country);
        session.getTransaction().commit();
        System.out.println("Country has been deleted.");
        System.out.println("End of transaction.");
        session.close();
    }
}