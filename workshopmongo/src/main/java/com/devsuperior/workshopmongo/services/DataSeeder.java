package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.domain.Post;
import com.devsuperior.workshopmongo.domain.User;
import com.devsuperior.workshopmongo.dto.AuthorDTO;
import com.devsuperior.workshopmongo.repository.PostRepository;
import com.devsuperior.workshopmongo.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@Component
public class DataSeeder implements CommandLineRunner {
    @Autowired
    private final UserRepository rep;

    @Autowired
    private final PostRepository repo;

    public DataSeeder(UserRepository rep, PostRepository repo) {
        this.rep = rep;
        this.repo = repo;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner sc = new Scanner(System.in);

        boolean shouldGenerateUsers = true;
        boolean shouldGeneratePosts = true;

        // Validação para apagar usuários
        if (rep.count() > 0) {
            System.out.println("-> Banco de dados já possui registros de usuários. Deseja limpar o banco de dados? (s/n)");
            String answer = sc.next();
            if (answer.equalsIgnoreCase("s")) {
                rep.deleteAll();
                System.out.println("-> Banco de dados de usuários limpo com sucesso!");
            } else {
                System.out.println("-> Operação de limpeza de usuários cancelada. O estado salvo do banco de dados foi mantido!");
                shouldGenerateUsers = false;
            }
        }

        // Validação para apagar posts
        if (repo.count() > 0) {
            System.out.println("-> Banco de dados já possui registros de posts. Deseja limpar os posts? (s/n)");
            String answer = sc.next();
            if (answer.equalsIgnoreCase("s")) {
                repo.deleteAll();
                System.out.println("-> Banco de dados de posts limpo com sucesso!");
            } else {
                System.out.println("-> Operação de limpeza de posts cancelada. O estado salvo do banco de dados foi mantido!");
                shouldGeneratePosts = false;
            }
        }

        // Geração de dados apenas se permitido
        if (shouldGenerateUsers) {
            System.out.println("-> Iniciando a geração de usuários com Datafaker...");
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

        if (shouldGeneratePosts) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
            List<User> users = new ArrayList<>(rep.findAll());

            Post post1 = new Post(null, sdf.parse("21/03/2018"), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", new AuthorDTO(users.getFirst()));
            Post post2 = new Post(null, sdf.parse("21/03/2018"), "Bom dia", "Acordei feliz hoje!", new AuthorDTO(users.getFirst()));
            List<Post> posts = new ArrayList<>(List.of(post1, post2));

            repo.saveAll(posts);
            System.out.println("Posts inseridos com sucesso! Verifique no MongoDB Compass.");
        }
    }
}
