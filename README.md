# Hibernate search, Elasticsearch et Java

## Introduction

Vous connaissez JPA ou hibernate ? Oui ? Alors vous allez adorez hibernate-search. Hibernate search permet d'ajouter des fonctionnalités de recherche plein texte et d'en finir avec les requêtes SQL (ou JPQL) compliquées à base de _LIKE %%_ et de _toLower()_.

Dans ce TP, vous allez exploiter hibernate-search pour ajouter des fonctionnalités de recherche plein texte à vos entités JPA. C'est ici toute la puissance de Lucene et d'Elasticsearch que vous pourrez inclure à vos projets JPA.

Pour cela, vous allez partir d'un projet JPA très simple permettant de gérer des livres et d'effectuer des recherche dans le contenu de ces livres. A l'issu de ce TP, vous serez donc capable :

- d'ajouter des fonctionnalités de RI à vos projets JPA
- de personnaliser votre indexation et vos recherches
- de déléguer l'indexation et les recherche à Eleasticsearch

**Evaluation** : L'évaluation portera sur le projet complété de vos réponse que vous déposez sur Moodle avant la **date indiquée sur Moodle** et en **ayant pris soin de supprimer le dossier data**.

## Pré-requis

- Java 8
- Une base de données relationnelle (Postgresql ou Mariadb) ⇒ Images docker fournies

## Récupération et importation du projet squelette

Récupérer le projet de base sur Moodle puis l'importer dans l'IDE de votre choix (projet pré paramétré pour Eclipse)

Étudier ensuite ses différents éléments et notamment le fichier _persistence.xml_ et les classes _Book.java_ et _IndexSearchAPI.java_.

Note : à chaque exécution de la classe _Main_, la base de données est recréée et les index reconstruits. Notez la rapidité du processus...

## Prise en main d'hibernate-search

Site officiel : [http://hibernate.org/search/](http://hibernate.org/search/)

Documentation : [https://docs.jboss.org/hibernate/stable/search/reference/en-US/html\_single/](https://docs.jboss.org/hibernate/stable/search/reference/en-US/html_single/)

### Requêtes

1. En vous inspirant de la méthode _searchInTitle,_ écrire une méthode permettant d'effectuer une recherche de mots-clés dans le contenu des livres.
2. Ecrire une méthode permettant d'effectuer une recherche exacte (_phrase_)
3. Ecrire une méthode à deux paramètres _q1_ et _q2_ permettant d'effectuer une recherche exacte de _q1_ dans le titre et une recherche approximative de _q2_ dans le contenu.

### Gestion de plusieurs entités

Afin d'améliorer notre projet, ajoutons lui quelques entités afin de pouvoir effectuer des recherches plus précises et de mieux répondre aux besoins des utilisateurs.

1. Pour cela, créer les classes suivantes et annotez-les pour en faire des entités :

1. Auteur (nom, prénom, biographie)
2. Genre (type, description)

2. Ajouter ensuite les relations entre les différentes entités (un livre a un auteur et un genre) puis créer quelques instances d'entités (vous pouvez utiliser des données factices).

3. Annoter les deux entités pour les rendre indexables. Vous pouvez utiliser l' **Analyzer** si vous en avez défini un.

4. Ajouter une fonctionnalité permettant de filtrer la recherche d'un livre sur un auteur

5. Ajouter une fonctionnalité permettant de filtrer la recherche d'un livre sur un genre particulier. La recherche devra être tolérante aux fautes.

## Indexation dans Elasticsearch

Elasticsearch est un moteur de recherche distribué, alors profitons de sa puissance pour stocker nos index à l'intérieur et déporter les recherches à Elasticsearch.