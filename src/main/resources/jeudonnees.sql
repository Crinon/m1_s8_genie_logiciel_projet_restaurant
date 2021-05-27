SET TIMEZONE='Europe/Paris';
-- Horaires du restaurant
INSERT INTO restaurant.restaurant(heurelimitediner, heureouverturediner, heurelimitedejeune, heureouverturedejeune) VALUES (79200, 68400, 54000, 43200);

-- Unique personne directeur, la personne 1
INSERT INTO restaurant.personne(nom,login)	VALUES ('directeur', 'directeur0');	
INSERT INTO restaurant.directeur(personne) VALUES (1);

-- Unique personne maître d'hôtel, la personne 2
INSERT INTO restaurant.personne(nom,login)	VALUES ('Andre', 'andre0');	
INSERT INTO restaurant.maitrehotel(personne)VALUES (2);

-- Deux personne assistant, les personnes 3 et 4
INSERT INTO restaurant.personne(nom, login)	VALUES ('Syvlie', 'sylvie0');	
INSERT INTO restaurant.personne(nom, login)	VALUES ('Eric', 'eric0');	
INSERT INTO restaurant.assistant(personne)VALUES (3);
INSERT INTO restaurant.assistant(personne)VALUES (4);

-- On veut 4 personnes serveurs, les personnes 5 6 7 8
INSERT INTO restaurant.personne(nom,login)	VALUES ('Jeremy', 'Jeremy0');	
INSERT INTO restaurant.personne(nom,login)	VALUES ('Anthony', 'anthony0');	
INSERT INTO restaurant.personne(nom,login)	VALUES ('Romain', 'romain0');	
INSERT INTO restaurant.personne(nom,login)	VALUES ('Alain', 'alain0');	
INSERT INTO restaurant.serveur(personne)VALUES (5);
INSERT INTO restaurant.serveur(personne)VALUES (6);
INSERT INTO restaurant.serveur(personne)VALUES (7);
INSERT INTO restaurant.serveur(personne)VALUES (8);
	
-- On veut 2 personnes cuisinier, les personnes 9 et 10	
INSERT INTO restaurant.personne(nom,login)	VALUES ('Evrard', 'evrad0');	
INSERT INTO restaurant.personne(nom,login)	VALUES ('Vilhjalmur', 'vilhjalmur0');	
INSERT INTO restaurant.cuisinier(personne) VALUES (9);
INSERT INTO restaurant.cuisinier(personne) VALUES (10);




-- Rez de chaussée (id = 1)
INSERT INTO restaurant.etage(niveau) VALUES (1);
-- Premier étage (id = 2)
INSERT INTO restaurant.etage(niveau) VALUES (2);
-- Second étage (id = 3)
INSERT INTO restaurant.etage(niveau) VALUES (3);

-- Tables 1 et 2 de l'étage au niveau 1 numéroTable 1 et 2 du serveur 1 Jérémy
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (2, 'Libre', 1, 1, 1);
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (4, 'Libre', 1, 2, 1);
-- Tables 3 et 4 de l'étage au niveau 2 numéroTable 3 et 4 du serveur 2 Anthony
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (6, 'Libre', 2, 3, 2);
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (10, 'Libre', 2, 4, 2);
-- Tables 5 et 6 de l'étage au niveau 3 numéroTable 5 et 6 du serveur 3 Romain
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (6, 'Libre', 3, 5, 3);
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (10, 'Libre', 3, 6, 3);
-- Tables 7 et 8 de des étages 2 et 3 numéroTable 7 et 8 du serveur 4 Alain
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (8, 'Libre', 2, 7, 4);
INSERT INTO restaurant.tables(capacite, etat, etage, numero, serveur) VALUES (8, 'Libre', 3, 8, 4);

-- Ingrédient 1
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('jambon', 250);
-- Ingrédient 2
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Endive', 128);
-- Ingrédient 3
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Béchamel', 128);
-- Plat 1
INSERT INTO restaurant.plat(nom, typeplat, prix, disponiblecarte, dureepreparation, typeingredient)	VALUES ('Endives enroulés dans du jambon sur son lit de béchamel', 'Plat', 166, 'true', 600, 'Viande');
-- Recette 1,2,3 pour le plat 1 avec les ingrédients 1, 2 et 3
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (99, 1, 1);
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (66, 2, 1);
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (33, 3, 1);


-- Ingrédient 4
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Carotte', 20);
-- Ingrédient 5
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Tomate', 25);
-- Plat 2
INSERT INTO restaurant.plat(nom, typeplat, prix, disponiblecarte, dureepreparation, typeingredient)	VALUES ('Tranches de tomates sur confît de carottes', 'Entree', 87, 'true', 300, 'Vegetarien');
-- Recette 4,5 pour le plat 2 avec les ingrédients 4 et 5
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (5, 4, 2);
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (5, 5, 2);


-- Ingrédient 6
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Tiramitsu', 10);
-- Plat 3
INSERT INTO restaurant.plat(nom, typeplat, prix, disponiblecarte, dureepreparation, typeingredient)	VALUES ('Du tiramitsu en un ingrédient', 'Dessert', 87, 'true', 120, 'Sucre');
-- Recette 6 pour le plat 3 avec les ingrédients 6
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (1, 6, 3);

-- Ingrédient 7
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Pâte à choux', 8);
-- Ingrédient 8
INSERT INTO restaurant.ingredient(nom, quantite) VALUES ('Saumon', 6);
-- Plat 4
INSERT INTO restaurant.plat(nom, typeplat, prix, disponiblecarte, dureepreparation, typeingredient)	VALUES ('Eclair au saumon', 'Dessert', 87, true, 120, 'Sale');
-- Recette 6 pour le plat 3 avec les ingrédients 6
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (1, 7, 4);
INSERT INTO restaurant.recette(quantite, ingredient, plat) VALUES (1, 8, 4);


INSERT INTO restaurant.affectation(datedebut, datefin, nombrepersonne, tableoccupe, facture) VALUES ('2021-05-26 19:01:02.78', '2021-05-26 20:30:02.78', 2, 1, 0);
INSERT INTO restaurant.affectation(datedebut, datefin, nombrepersonne, tableoccupe, facture) VALUES ('2021-05-22 19:10:02.78', '2021-05-22 20:45:02.78', 4, 2, 0);
INSERT INTO restaurant.affectation(datedebut, datefin, nombrepersonne, tableoccupe, facture) VALUES ('2021-05-26 19:14:02.78', '2021-05-26 20:30:02.78', 6, 3, 0);
INSERT INTO restaurant.affectation(datedebut, datefin, nombrepersonne, tableoccupe, facture) VALUES ('2021-05-26 19:22:02.78', '2021-05-26 20:33:02.78', 2, 4, 0);
INSERT INTO restaurant.affectation(datedebut, datefin, nombrepersonne, tableoccupe, facture) VALUES ('2021-05-26 19:33:02.78', '2021-05-26 20:30:02.78', 2, 5, 0);
INSERT INTO restaurant.affectation(datedebut, datefin, nombrepersonne, tableoccupe, facture) VALUES ('2021-05-26 21:01:02.78', '2021-05-26 21:50:02.78', 2, 6, 0);

-- Commandes 1 2 et 3 de l'affectation 1
-- Endives au jambon par un adulte
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 19:05:45.00', false, 1, 1, 'COMMANDEE');
-- Salade tomates par un adulte
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 19:05:00.00', false, 2, 1, 'COMMANDEE');
-- Tiramistu
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 19:01:02.78', false, 3, 1, 'COMMANDEE');

-- Commandes 4 et 5 de l'affectation 2
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 12:01:02.78', false, 1, 2, 'COMMANDEE');
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 12:01:02.78', false, 1, 2, 'COMMANDEE');

-- Commandes 5 et 6 de l'affectation 3
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 19:01:02.78', false, 2, 3, 'COMMANDEE');
INSERT INTO restaurant.commande(datedemande, estenfant, plat, affectation, etat) VALUES ('2021-05-26 19:01:02.78', false, 3, 3, 'COMMANDEE');

-- Réservation
INSERT INTO restaurant.reservation(dateappel, datereservation, nombrepersonne, valide, tablereserve) VALUES ('2021-05-26 19:01:02.78', '2024-05-26 19:01:02.78', 10, true, 6);