package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.domain.User;
import com.devsuperior.workshopmongo.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private final UserRepository rep;

    public DataSeeder(UserRepository rep) {
        this.rep = rep;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);
        if (rep.count() > 0) {
            System.out.println("-> Banco de dados já possui registros. Deseja limpar o banco de dados? (s/n)");
            String answer = sc.next();
            if (answer.equalsIgnoreCase("s")) {
                rep.deleteAll();
                System.out.println("-> Banco de dados limpo com sucesso!");
            } else {
                System.out.println("-> Operação cancelada. Nenhum dado foi alterado.");
                return;
            }
        }

        System.out.println("-> Iniciando a geração de dados falsos com Datafaker...");
        Faker faker = new Faker(new Locale("pt", "BR"));
        List<User> users = new ArrayList<>();

        int nUsers = 10;
        for (int i = 0; i < nUsers; i++) {
            String name = faker.name().fullName();
            String email = faker.internet().emailAddress();

            users.add(new User(name, email));
        }
        rep.saveAll(users);
        System.out.println(nUsers + " usuários inseridos com sucesso! Verifique no MongoDB Compass.");

    }
}
