# RSA - Explications et recommandations

Ce document explique de façon simple et pragmatique la génération de paires de clés RSA telle qu'elle apparaît dans `TokenUtils.java` du projet.
Il compare deux versions de la méthode de génération : V1 (ancienne) et V2 (recommandée), décrit pourquoi la V2 est plus portable et donne des conseils de tests et de bonnes pratiques.

## But
Générer une paire de clés (KeyPair) contenant une clé publique et une clé privée — la clé publique peut être partagée, la clé privée doit rester secrète.

## Résumé des deux versions

- V1 (generateKeyPairV1)
  - Utilise `SecureRandom.getInstance("NativePRNG")` pour la source d'aléa.
  - Risque : l'algorithme nommé "NativePRNG" peut ne pas exister sur toutes les plateformes (par exemple Windows) et provoquer une `NoSuchAlgorithmException`.
  - Avantage : pouvait être adaptée à certains environnements Linux/OpenJDK qui fournissent spécifiquement cette implémentation.

- V2 (generateKeyPairV2) — Recommandée
  - Essaie `SecureRandom.getInstanceStrong()` (meilleure source d'entropie si disponible), puis retombe sur `new SecureRandom()` comme fallback.
  - Ne propage pas d'exception checked à l'appelant : si RSA est absent (très improbable), lance une `IllegalStateException`.
  - Avantage : portable (fonctionne sur Windows, Linux, macOS) et robuste.

## Explication simple, étape par étape (analogie)

- KeyPairGenerator.getInstance("RSA")
  - Analogie : "je demande à un atelier de fabriquer des clés RSA". Sur une JVM standard, cet atelier existe.

- SecureRandom (source d'aléa)
  - Analogie : pour fabriquer une clé sécurisée, on a besoin d'un grand nombre imprévisible — comme tirer des numéros au hasard dans une urne sûre.
  - V1 demandait une machine précise ("NativePRNG") qui peut ne pas être disponible partout.
  - V2 demande d'abord la meilleure machine disponible (`getInstanceStrong()`), puis prend la machine par défaut (`new SecureRandom()`) si la meilleure n'est pas disponible.

- keyPairGenerator.initialize(2048, secureRandom)
  - On fixe la taille de la clé (2048 bits) et la source d'aléa. 2048 bits est aujourd'hui raisonnable pour la plupart des usages.

- keyPairGenerator.generateKeyPair()
  - L'atelier fabrique la paire de clés et on la récupère.

## Pourquoi V2 est préférable pour ce projet (en une phrase)
V2 évite les plantages liés à des implémentations de SecureRandom absentes sur certaines plateformes et privilégie la meilleure source d'entropie disponible, tout en gardant un fallback sûr.

## Tests simples à réaliser localement

1) Compiler le projet (depuis la racine du repo) :

```powershell
cd C:\dev\oidc-server; mvn -DskipTests package
```

2) Petit test Java (exemple) : ajouter temporairement un `main` pour appeler les deux méthodes et observer le comportement :

```java
public static void main(String[] args) throws Exception {
    // Tester V2 (recommandée)
    KeyPair kp2 = TokenUtils.generateKeyPairV2();
    System.out.println("V2 public algorithm: " + kp2.getPublic().getAlgorithm());

    // Tester V1 (peut lancer NoSuchAlgorithmException sur certaines plateformes)
    try {
        KeyPair kp1 = TokenUtils.generateKeyPairV1();
        System.out.println("V1 public algorithm: " + kp1.getPublic().getAlgorithm());
    } catch (NoSuchAlgorithmException e) {
        System.out.println("V1 failed: " + e.getMessage());
    }
}
```

Observations attendues :
- Sur Windows, V1 risque d'échouer avec `NoSuchAlgorithmException` (si "NativePRNG" absent), alors que V2 doit fonctionner.
- Sur la plupart des distributions Linux OpenJDK, les deux peuvent fonctionner.

## Bonnes pratiques et recommandations rapides

- Taille de clé : 2048 bits est acceptable; pour une sécurité à plus long terme considérez 3072 ou 4096 (conséquence : génération + opérations plus lentes).
- Alternatives modernes : pour de meilleures performances et clefs plus petites, évaluez ECDSA (p.ex. P-256) ou Ed25519 (EdDSA) — particulièrement utiles pour JWT/OPA.
- Stockage de la clé privée :
  - Ne jamais écrire la clé privée dans les logs.
  - Stocker la clé privée dans un KeyStore, HSM, ou service de coffre (Vault, Azure Key Vault, AWS KMS) pour la production.
- Fréquence de génération : générez une paire une fois, stockez-la et réutilisez-la ; évitez de générer une paire à chaque démarrage ou requête inutilement.

## Améliorations possibles

- Exposer la taille de clé en paramètre : `generateKeyPairV2(int keySize)` pour la flexibilité.
- Fournir une méthode pour générer des clés EC (ex: `generateECKeyPair("secp256r1")`).
- Ajouter des tests unitaires qui exécutent `generateKeyPairV2()` dans l'environnement CI pour vérifier la portabilité.
- Documenter les procédures de stockage sécurisées (ex : comment importer la paire dans un Java KeyStore/PKCS12).

## Prochaines actions proposées
- Si vous voulez, j'ajoute un petit test/un `main` et j'exécute la compilation (`mvn package`) pour vous montrer la sortie.
- Je peux aussi ajouter une méthode paramétrable et un exemple pour EC.

---

Si vous voulez que je modifie le document (niveau de détail, ajouter exemples concrets d'usage KeyStore ou commandes openssl), dites-moi ce que vous souhaitez ajouter.
