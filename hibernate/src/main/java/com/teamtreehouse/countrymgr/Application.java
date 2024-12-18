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
        List<Country> countries = fetchAllCountries();
        run();

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

    // Format data in columns with headers
    public static void displayCountryData(List<Country> countries) {
        System.out.println("------------------------------View Country Data Here------------------------------");
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

    // Display the Analysis
    // Calculate and display max/min values for internetUsers and adultLiteracyRate
    private static void displayAnalysis(List<Country> countries) {
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
        System.out.println("---------------Country Analysis---------------");
        System.out.printf("Country with the highest percentage of internet users is %s with %.2f.%n",
                maxInternet.getName(),
                maxInternet.getInternetUsers());
        System.out.printf("Country with the lowest percentage of internet users is %s with %.2f.%n", minInternet.getName(), minInternet.getInternetUsers());
        System.out.printf("Country with the highest adult literacy is %s with %.2f.%n", maxLiteracy.getName(), maxLiteracy.getAdultLiteracyRate());
        System.out.printf("Country with the lowest adult literacy is %s with %.2f.%n", minLiteracy.getName(), maxLiteracy.getAdultLiteracyRate());
    }

/*
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
        System.out.println("What country do you want to edit? Provde the code here: ");
        String code = reader.readLine().trim().toUpperCase();
        return code;
    }


    // Country Editing Function
    public static void editCountry(List<Country> countries) {
        String code = fetchCountryCode();
        Country country = fetchCountryCode(country);




    }

    // Update country
    private static void update(Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(country);
        session.getTransaction().commit();
        session.close();
    }


    // Country Addition
    private static void addNewCountry() throws IOException {
        System.out.println("Enter data for new country: ");
        String name = reader.readLine().trim();

        String code="";
        while (code.length() != 3) {
            System.out.printf("Please give a country code with three characters: ");
            code = reader.readLine().trim();
        }

        System.out.printf("Please enter percentage of Internet Users: ");
        String internetUsers = reader.readLine().trim();

        System.out.printf("Please enter adult literacy percentage: ");
        String adultLiteracyRate = reader.readLine().trim();

        // Save data to database
        Country country = new CountryBuilder(code, name)
                .withInternetUsers(internetUsers)
                .withAdultLiteracyRate(adultLiteracyRate)
                .build();
            save(country);
            System.out.println("Country added successfully!");
    }

    // Country deletion capability
    public static void deleteCountry(Country country) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.delete(country);
        session.getTransaction();
        session.close();
        System.out.printf("You deleted %f%s from the list.", country.getName());
    }
*/
    private static String promptAction() throws IOException {
        // Menu Options
        Map<String, String> menu = new TreeMap<>();
        menu.put("View", "View country data");
        menu.put("Analysis", "View maximum and minumum values for each country");
        menu.put("Add", "Add data");
        menu.put("Edit", "Edit data");
        menu.put("Delete", "Delete data");
        menu.put("Quit", "Exits the program");

        System.out.printf("Menu: %n");
        for (Map.Entry<String, String> option : menu.entrySet()) {
            out.printf("%s --> %s%n", option.getKey(), option.getValue());
        }

        System.out.printf("Select an option: ");
        String choice = reader.readLine();
        return choice.trim().toLowerCase();
    }

    public static void run() {
        String choice = "";
        do {
            try {
                choice = promptAction();
                switch (choice) {
                    case "view":
                        displayCountryData(fetchAllCountries());
                        break;
                    case "analysis":
                        displayAnalysis(fetchAllCountries());
                        break;
                    case "add":
                        //addNewCountry();
                        break;
                    case "edit":
                        //editCountry(fetchAllCountries());
                        break;
                    case "delete":
                        //deleteCountry();
                        break;
                    case "quit":
                        out.println("End of Program");
                        break;
                    default:
                        out.printf("Unknown choice: '%s'. Try again.%n%n%n", choice);
                }
            } catch (IOException ioe) {
                out.println("Problem with input.");
            }
        } while (!choice.equals("quit"));
    }
}


 /*
 session example
           // Open Session
            Session session = sessionFactory.openSession();
            // Begin a transaction
            session.beginTransaction();
            //Use the session to save the contact
            session.save(country);
            // Commit the transaction
            session.getTransaction().commit();
            // Close the session
            session.close();
            */