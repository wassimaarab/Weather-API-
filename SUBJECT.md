---
documentclass: book
papersize: a4
fontsize: 10pt
header-includes: |
    \hypersetup{
        colorlinks = true,
        linkbordercolor = {pink},
    }
    \usepackage{newunicodechar}
    \usepackage{./twemojis/twemojis}
    \newunicodechar{🌤}{\twemoji{sun behind cloud}}
    \newunicodechar{🌧}{\twemoji{cloud with rain}}
---

# Projet Programmation Orientée Objet (PG203)

L’objectif de ce projet est de réaliser un agrégateur de flux météo en
ligne de commande. Il permet de récupérer automatiquement depuis le
web les prévisions météo d’une ville spécifiée et de les afficher dans
la console. Bien entendu, l'objectif principal de ce projet est de
mettre en oeuvre les principes fondamentaux de la programmation
orientée objet: encapsulation, délégation, héritage, polymorphisme. Le
développement d'un programme qui implémente les fonctionnalités
demandées est secondaire.

## Organisation

Les groupes de projet seront découpés en binômes. Pour faciliter le
travail, le projet est découpé en trois itérations successives :

- la première et la seconde itération feront l’objet d’une démo et
  d’une rapide revue de code durant le début de chaque séance de suivi
  de projet;

- la troisième itération sera complétée et rendue avec un ensemble de
  livrables à la fin du projet.

### Starter-kit

Pour faciliter le développement, un starter-kit qui contient un
squelette de projet est fourni.

Il utilise l'outil de build `Gradle` pour gérér les dépendances aux
bibliothèques externes, pour compiler le projet, pour lancer les tests
et pour lancer le client.

### Première itération

L’objectif de cette première livraison est de développer un premier
client qui récupère des informations sur la météo provenant de l’API
[WeatherAPI](https://www.weatherapi.com/). Une API (*Application
Programming Interface*) permet d’avoir accès aux fonctionnalités
proposées par une application. Une API REST n’est rien d’autre qu’une
API accessible depuis le web via les [commandes
standard](https://developer.mozilla.org/en-US/docs/Web/HTTP/Overview)
du protocole HTTP.

Dans le cadre de notre projet, utiliser une API revient donc à
simplement faire une requête HTTP et analyser sa réponse JSON :

- La classe
[`java.net.HttpUrlConnection`](https://docs.oracle.com/javase/8/docs/api/java/net/HttpURLConnection.html)
de la bibliothèque standard de Java permet de réaliser une requête
HTTP. Cette requête renvoie un
[code](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status) ainsi
qu'une réponse de type `String`.

- La plupart des APIs retournent leurs réponses par défaut au format
  JSON (*JavaScript Object Notation*). Pour extraire les données de la
  réponse obtenue, il est donc nécessaire de la parser. Pour ce faire,
  il suffit d'utiliser
  [`JSON-java`](https://stleary.github.io/JSON-java), disponible dans
  le starter-kit, afin de produire un
  [`JSONObject`](https://stleary.github.io/JSON-java/org/json/JSONObject.html).

Le client devra être capable de produire les températures d'une ville
donnée. Il fonctionnera de la manière suivante (le style de
l'affichage est laissé libre) :

```bash
shell$ java -jar weather.jar -l Bordeaux
              +-----+-----+-----+-----+
              | J+0 | J+1 | J+2 | J+3 |
+-------------+-----+-----+-----+-----+
|             | 10° | 12° | 8°  | 15° |
+-------------+-----+-----+-----+-----+
```

### Seconde itération

Pour la réalisation de notre aggrégateur, nous avons sélectionné une
liste d’APIs gratuites.

La liste est la suivante :

- [WeatherAPI](https://www.weatherapi.com/) (implémentée dans l'itération 1)
- [Open-Meteo](https://open-meteo.com/)
- [OpenWeatherMap](https://openweathermap.org/price)

Chaque API possède sa propre documentation à laquelle vous pouvez
avoir accès sur le site correspondant. L’objectif de la deuxième
livraison est de rajouter ces API dans l'agrégateur.

En plus d'afficher la température, vous devrez afficher le temps qu'il
fait (ensoleillé, nuageux, pluvieux, etc.) et le vent (vitesse et
direction). Vous devez aussi gérer la possibilité d'avoir des données
manquantes (jours ou types d'information) dans les résultats d'une API
donnée.

Le client devra fonctionner de cette manière (pour simplifier nous
n'affichons qu'un jour mais un tableau de trois jours devra être
affiché):

```bash
shell$ java -jar weather.jar -l Bordeaux
              +---------------+
              | J+0           |
+-------------+---------------+
| WeatherAPI  | 10° 🌤 03km/h |
+-------------+---------------+
| OpenMeteo   | 11° 🌤 24km/h |
+-------------+---------------+
| Open WM     | 10° 🌧 10km/h |
+-------------+---------------+
```

### Troisième itération

Pour la troisième itération nous allons améliorer la gestion des erreurs et introduire un cache pour éviter de faire des requêtes inutiles.

Le cache devra être implémenté dans un fichier JSON. Les fichiers devront être stockés dans un dossier cache à la racine du projets.
Le format de fichier du cache est le suivant:
```json
[
{
    "city": "Bordeaux",
    "api": "WeatherAPI",
    "timestamp": 1616425200,
    "value": {
        ...
    }
},
{
    "city": "Bordeaux",
    "api": "OpenMeteo",
    "timestamp": 1616425860,
    "value": {
        ...
    }
},
]
```
Le cache devra être mis à jour toutes les 24 heures.

Pour la gestion des erreurs, l'idée est de disposer d'un client
robuste qui ne plante pas à la première erreur d'une API donnée. Pour
ce faire, vous devrez gérer les erreurs réseaux et les erreurs de
parsing. En cas d'erreur, le client devra afficher un message d'erreur
explicite et continuer à fonctionner sur les APIs restantes. Il est
explicitement demandé d'introduire une exception contrôlée de type
`WeatherFetchingException` qui sera utilisée pour gérer les erreurs
sur les API sous-jacentes.

Exemple :

```bash
shell$ java -jar weather.jar -l Bordeaux -j 0
Erreur: Impossible de récupérer les données de OpenMeteo

              +-------------------+
              | J+0               |
+-------------+-------------------+
| WeatherAPI  | 10° 🌤 69% 3km/h  |
+-------------+-------------------+
| Open WM     | 10° 🌤 71% 10km/h |
+-------------+-------------------+
```

## Livrables

Pour la dernière itération, les livrables sont les suivants :

- Le code source intégral du projet, utilisant une indentation claire,
  des noms de variables explicites et des commentaires pertinents. La
  lisibilité du code sera un des critères de notation.

- Outre la lisibilité, le code fourni devra pouvoir être compilé et
  exécuté sans erreurs en utilisant les commandes de bases décrites
  dans le starter-kit :

  ```
  gradlew build
  gradlew run
  gradlew test
  ```

- Un document de conception qui explique les choix de conception utilisé dans votre programme. Vous trouverez un template de ce document dans le fichier `DESIGN.md`. Ce document rentrera aussi en compte dans la notation.

- Un jeu de tests unitaires qui couvre toutes les parties importantes
  du code. L'outil `Jacoco` installé dans le starter-kit permet de
  calculer et visualiser la couverture de code des tests.
