package br.ufsm.topicos.hibernate;


import br.ufsm.topicos.cbr.TrucoDescription;
import br.ufsm.topicos.deception.JogadaPlayer1;
import br.ufsm.topicos.deception.JogadaPlayer2;
import br.ufsm.topicos.log.Log;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Universidade Federal de Santa Maria
 * Pós-Graduação em Ciência da Computação
 * Tópicos em Computação Aplicada
 * Daniel Pinheiro Vargas
 * Criado em 16/08/2018.
 */

public class HibernateConfig {

    //private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    public static synchronized void buildSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = getConfiguration().buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration cfg = getConfiguration();
                sessionFactory = cfg.buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }


    public static Configuration getConfiguration() {

        Configuration cfg = new Configuration();
        cfg.addPackage("br.ufsm.topicos.cbr");
        cfg.addPackage("br.ufsm.topicos.log");
        //cfg.addPackage("br.ufsm.topicos.deception");
        cfg.addAnnotatedClass(TrucoDescription.class);
        cfg.addAnnotatedClass(Log.class);
        /*cfg.addAnnotatedClass(JogadaPlayer1.class);
        cfg.addAnnotatedClass(JogadaPlayer2.class);*/

        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/dbtrucocbr");
        //cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/dbdeception");
        cfg.setProperty("hibernate.connection.username", "root");
        cfg.setProperty("hibernate.connection.password", "desenvolvimento");
        cfg.setProperty("dialect", "org.hibernate.dialect.MySQLDialect");

        return cfg;
    }


}
