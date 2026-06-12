package com.example;

/**
 * Gère l'inscription et la connexion des utilisateurs.
 */
public class AccountService {
    private final UserRepository userRepository;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Confirmation register(String email, String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new CompteExistantException(username);
        }
        User user = new User(email, username, password);
        userRepository.save(user);
        return new Confirmation("Inscription confirmée pour " + user.getUsername());
    }

    public Confirmation login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (!user.getPassword().equals(password)) {
            throw new AuthenticationException("Identifiants invalides");
        }
        return new Confirmation("Connexion réussie, redirection vers la page d'accueil pour " + username);
    }
}
