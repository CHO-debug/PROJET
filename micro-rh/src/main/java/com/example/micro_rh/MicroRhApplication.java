package com.example.micro_rh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
@EnableFeignClients
public class MicroRhApplication implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String[] args) {
        SpringApplication.run(MicroRhApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Afficher toutes les collections existantes
        System.out.println("===========================================");
        System.out.println("Collections existantes dans la base 'gestion_rh' :");
        System.out.println("===========================================");

        mongoTemplate.getCollectionNames().forEach(collectionName -> {
            System.out.println("📁 " + collectionName);
        });

        System.out.println("===========================================");

        // Créer les collections si elles n'existent pas
        createCollectionIfNotExists("rh");
        createCollectionIfNotExists("emploitemps");
        createCollectionIfNotExists("demandeconge");
        createCollectionIfNotExists("absence");

        System.out.println("===========================================");
        System.out.println("✅ Initialisation terminée avec succès !");
        System.out.println("===========================================");
    }

    private void createCollectionIfNotExists(String collectionName) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
            System.out.println("✅ Collection '" + collectionName + "' créée avec succès !");
        } else {
            System.out.println("ℹ️  Collection '" + collectionName + "' existe déjà.");
        }
    }
}