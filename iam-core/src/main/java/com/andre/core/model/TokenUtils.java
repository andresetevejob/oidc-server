package com.andre.core.model;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TokenUtils {
    // Empêche l'instanciation de la classe utilitaire
    private TokenUtils() {
    }

    /**
     * V1 - Version originale : utilise explicitement "NativePRNG" pour SecureRandom.
     * Attention : ce nom d'algorithme n'est pas garanti sur toutes les plateformes (p.ex. Windows)
     * et peut lancer NoSuchAlgorithmException.
     * Gardée ici pour compatibilité / comparaison.
     */
    public static KeyPair generateKeyPairV1() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom secureRandom = SecureRandom.getInstance("NativePRNG");
        keyPairGenerator.initialize(2048, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * V2 - Version recommandée : plus robuste et portable.
     * - Essaie SecureRandom.getInstanceStrong() puis retombe sur new SecureRandom() si indisponible.
     * - Ne lance pas NoSuchAlgorithmException en sortie (encapsulée si RSA absent).
     */
    public static KeyPair generateKeyPairV2() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom secureRandom;
            try {
                secureRandom = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                secureRandom = new SecureRandom();
            }
            keyPairGenerator.initialize(2048, secureRandom);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA KeyPairGenerator not available", e);
        }
    }
}
