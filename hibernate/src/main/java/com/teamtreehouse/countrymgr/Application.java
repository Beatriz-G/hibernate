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

import java.util.List;

public class Application {
    // Hold a reusable reference to a SessionFactory(only need one)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        // Create a StandardServiceRegistry
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        // Hibernate SessionFactory
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static void main(String[] args) {


    // Display the Country Data
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

















    /*
        // Builder pattern to Implement a Country Addition
       Country newCountryAdded = new CountryBuilder(code, newCountryAdded) {
           .withInternetUser("internetUsers");
           .withAdultLiteracyRate("adultLiteracyRate");
           .build();
       save(newCountryAdded);

       System.out.println("Country " + newCountryAdded + "added successfully!");
       };

        // Add country
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
    }


}






// Open Session

// Begin a transaction

//Use the session to save the contact

// Commit the transaction

// Close the session
