ProjectMinecraft
================

Projet S6: Génération de monde Minecraft

Les outils Map, MapUI et ModMinecraft contenus dans le dossier utils sont nécessaires au fonctionnement et peuvent être considérés comme des projets séparés.

Mode d'emploi
-------------

Pré-requis:
  * Savoir comment fonctionne craftbukkit, c'est à dire comment le lancer et le configurer.

Coté serveur:

  1. Copier le plugin Mapminecraft.jar dans le dossier plugin du serveur,
  2. Editer le fichier bukkit.yml pour qu'il prenne en compte le plugin, ajouter ces lignes a la fin du fichier
 
  worlds:
    TESTMAP:
      generator: Mapminecraft

  3. Editer le fichier server.properties pour changer la ligne level-name en level-name:TESTMAP

Coté générateur:

  1. Lancer la compilation de la classe Luncher qui se trouve dans generator,

  Pour générer une map aléatoire:
  
  2. Choisir la taille de la map et des régions,
  3. Choisir le seed,
  4. Générer la map.

  Pour générer une map avec OpenStreetMap:
  
  2. Aller sur OpenStreetMap.org,
  3. Choisir une zone et exporter en fichier OSM,
  4. Sur le generator, choisir la map a générer et le nom de la map,
  5. Générer la map.

Une fois la map généré, il faut la mettre dans le dossier server, pour l'instant le nom de la map doit etre TESTMAP.
Vous pouvez maintenant lancer le serveur et visiter ^^
