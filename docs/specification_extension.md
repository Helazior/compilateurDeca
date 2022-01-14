# Utilisation

Pour utiliser la compilation séparée, il faudra 3 choses:

1. Ajouter des imports au début des fichiers sources déca, de syntaxe `import <chemin_ficher>`, avec `<chemin_fichier>` étant un chemin valide (relatif ou absolu) vers le fichier `.deca` dont les classes sont à importer.

2. Pour le programme principal ainsi que les fichiers contenant des classes, lancer la commande `decac -c <nom_du_fichier>.deca`, ce qui va générer un fichier objet `<nom_du_fichier>.deco`. 

3. Rassembler tous les fichiers en un seul, à l'aide d'un éditeur de liens implémenté par nos soins. En fonction de nous choix futurs, il fera soit entièrement partie du compilateur, soit il sera un autre programme fourni avec le compilateur. Dans tous les cas, l'édition de lien se fera au moyen d'une commande ressemblant à `linker <main>.deco <objects>.deco...`, avec `<main>.deco` étant le fichier objet résultant du programme principal, et `<objects>.deco...` étant les fichiers objets dont on souhaite importer 

# Concecpt d'implémentation

Le but de l'extension link est de permettre d'avoir une compilation séparée en fichiers objets, puis ensuite de faire un seul exécutable à partir de ces fichiers objets.

Une appel à la commande `décac` va compiler un seul fichier déca en fichier objet (`.deco`) en effectuant les 3 étapes (A, B, C) dessus. Lors de l'étape A, la définition des classes de tous les fichiers importés seront parsés et ajoutés à l'arbre, permettant à l'étape B d'y avoir accès. Lors de l'étape B, les passes 1 et 2 seront faites non seulement sur les classes dans le fichier principal, mais également dans les définitions des classes importées. La passe 3 sera quand-à elle effectuée seulement dans le sous-arbre donné par le contenu du fichier principal (et va donc sauter la définition des classes importées).

Quand-à l'étape C, elle doit créer la table des méthodes de toutes les classes (dont les classes importées) et connait la disposition des attributs dans la mémoire, mais n'a pas à prévoir de label et de corps pour les méthodes importées. Avec une bonne convention de nommage des labels, on pourra savoir le label à choisir lors du remplissage de la table des classes sans soucis, et ainsi ne pas modifier plus que cela l'étape C.

Cela définit donc bien un fichier objet `.deco`, car il contient tout le code du fichier compilé, avec une possiblité de le compléter avec d'autres fichiers `.deco`.

Pour réunir tous ces fichiers, il ne nous restera plus qu'à concaténer tous les fichiers `.deco`, supprimer le code n'étant pas dans des méthodes dans les fichiers objets qui ne sont pas le programme principal, et vérifier que chaque label dont on a besoin existe.



# Étape A

## Modifications sur la syntaxe concrète

La syntaxe concrète pour lire les fichiers n'est presque pas modifiée. En effet, un fichier source ne se voit que des instructions `import <fichier_deca>` ajoutés, donnant la même grammaire, avec les deux règles suivantes en plus :

**import**  -> 'import' **fichier** EOL
**fichier** -> STRING

Cette syntaxe prend donc en compte les imports, et sera à appliqué une fois à chaque fichier source importé, en plus du fichier principal.

## Modifications sur la syntaxe abstraite

La syntaxe abstraite se voit fortement modifiée. Tout d'abord, lors de la compilation de `.deca` en `.deco`, nous avons une grammaire abstraite différente pour parser le code source principal de celui importé. Cependant, les terminaux de ces deux grammaires abstraites sont les mêmes que les terminaux de la grammaire abstraite "normale" (celle fournie, sans l'extension), avec seulement l'ajout d'un terminal EmptyBody. Cela permet de simplifier toutes les étapes (A, B et C) en n'ajoutant quasiment pas de classes à implémenter.

La grammaire principale n'est modifiée que sur la règle du non-terminal ***PROGRAM*** et le terminal Program, ainsi que deux nouvelles règles. Les différences par rapport à la grammaire du poly sont les suivantes :

***PROGRAM***
		-> Program[***LIST_DECL_IMPORT***, ***LIST_DECL_CLASSES***, ***MAIN***]

***LIST_DECL_IMPORT***
		->  (DECL_IMPORT)*

***DECL_IMPORT*** -> ***PROGRAM_IMPORT***

Où ***PROGRAM_IMPORT*** est l'axiome de la grammaire pour les fichiers importés. Le programme est donc le même que sans l'extension, avec des imports en plus.

La grammaire abstraite des sources importées est quand-à elle similaire, mais ne supprime quelques informations inutiles pour la suite. En reprenant la grammaire abstraite du code source principal et en remplaçant ***PROGRAM*** par ***PROGRAM_IMPORT***, les seules modifications sont les suivantes :

***PROGRAM***
		-> Program[***LIST_DECL_IMPORT***, ***LIST_DECL_CLASSES***, EmptyMain]

***DECL_METHOD***
		->  DeclMethod[***IDENTIFIER***, ***IDENTIFIER***, ***LIST_DECL_PARAM***, EmptyMethodBody]

On a donc les mêmes informations, à l'exception des blocs de codes, qui ne sont pas évalués.

Avec cette structure de la grammaire, on est capable de construire un arbre contenant toutes les informations requises pour la bonne exécution de la suite de la compilation. En particulier, nous avons toutes les déclarations de classes et méthodes des fichiers importés dans la grammaire abstraite.

## Passage de la syntaxe concrète à la syntaxe abstraite

Pour passer de la syntaxe concrète, qui décrit chaque fichier individuellement, à la syntaxe abstraite, qui résume les données du code source principal ainsi que celui du code source importé, un regroupement des information doit avoir lieu. Cela se passera lors du parsing des tokens de la syntaxe concrète. Lorsque le Parseur lira les tokens 'import' et 'fichier', il lancera le lexer et le parseur pour fichiers importés sur le fichier donné. Cela lui retournera un terminal Program, qu'il peut mettre dans la liste d'imports, construisant ainsi l'arbre abstrait. De plus, le parseur ne lancera le lexer et le parseur que si le fichier n'est pas encore importé, il faudra donc fournir une liste des fichiers importées, pour assurer qu'un même fichier ne se trouve pas deux fois importé.

# Étape B

Pour l'étape B, des modifications auront lieu sur les passes 1 et 2, afin de répertorier les classes et méthodes importées dans les environnements. 

Additionnellement, les règles sur l'ordre de déclaration des classes est assoupli: pour éviter de nombreuses heures de débug sur l'ordre des imports aux futurs utilisateurs, une classe peut avoir comme classe parent une classe déclarée plus *tard*. Cela permet entre autres de pouvoir importer une classe fille dans un ficher source définissant sa classe mère. Ainsi, il y a l'ajout d'une passe 0, dont le seul but est de répertorier toutes les classes existantes, laissant ainsi la passe 1 vérifier que chaque classe a un père existant. Additionnellement, pour être sûr que chaque classe descend directement ou pas de Object, et ainsi éviter une boucle de dépendance dans la hiérarchie des classes, un parcours d'arbre partant de Object est effectué, en itérant récursivement sur leurs classes filles.

## Passe 0

Au lieu de se passer un environnement de types, les non-terminaux se passent un ensemble des classes qui répertoriera tous les noms de classe. La règle suivante est également modifiée pour répondre à cette modification:

**decl_class** ↓set_classes ↑({name} + set_classes)
	-> DeclClass [
		Identifier ↑*name*
		**IDENTIFIER**
		**LIST_DECL_FIELD LIST_DECL_METHOD**
	]

Cette passe peut également être remplacée par une collection des noms de méthodes en étape A.

## Passe 1

L'ensemble des classes créé à la passe 0 est ajoutée en argument de chaque non-terminal, 2 non terminaux sont ajoutés, et il y a une modification additionnelle des terminaux **program** et **decl_class**.

**program** ↓*set_classes* ↑*env_types_1*
		-> <u>Program</u>[
			**list_import** ↓*env_types_predef* ↓*set_classes* ↑*env_types_0*
			**list_decl_class** ↓*env_types_0* ↓*set_classes* ↑*env_types_1*
			***MAIN***
		]



**list_import** ↓*env_types* ↓*set_classes* ↑*env_types*
		-> [(**import** ↓*env_types* ↓*set_classes* ↑*env_types*)\*]



**import** ↓*env_types_in*, ↓*set_classes* ↑*env_types_out*
		-> <u>Program</u>[
			**list_import** ↓*env_types_in* ↓*set_classes*  ↑*env_types*
			**list_decl_class** ↓*env_types* ↓*set_classes*  ↑*env_types_out*
			***MAIN***
		]



**list_decl_class** ↓*env_types* ↓*set_classes* ↑*env_types*
		-> [(**decl_class** ↓*env_types* ↓*set_classes* ↑*env_types*)\*]



**decl_class** ↓*nv_types* ↓*set_classes* ↑({*name ->* (class*(super, {}),* type_class*(name)*)} + env_types)
		-> <u>DeclClass</u> [
						Identifier ↑*name* 
						Identifier ↑*super*
						***LIST_DECL_FIELD*** ***LIST_DECL_METHOD***
		]
	**condition** *super* in *set_classes*

## Passe 2

Pour cette passe, les mêmes non-terminaux sont ajoutés à la grammaire que sur la passe 1, et **program** est modifié pour permettre de passer par les classes importées.

**program** ↓*env_types* ↑*env_types_r*
		-> Program[
			**list_import** ↓*env_types* ↑*env_types_0*
			**list_decl_class** ↓*env_types_0* ↑*env_types_r*
			***MAIN*** ]



**list_import** ↓*env_types*  ↑*env_types*
		-> [(**import** ↓*env_types* ↑*env_types*)\*]



**import** ↓*env_types_in* ↑*env_types_out*
		-> Program[
			**list_import** ↓*env_types_in*  ↑*env_types*
			**list_decl_class** ↓*env_types*  ↑*env_types_out*
			***MAIN*** ]



**list_decl_class** ↓*env_types*  ↑*env_types*
		-> [(**decl_class** ↓*env_types* ↑*env_types*)\*]



**decl_class** ↓*env_types* ↑*env_types*
		-> DeclClass [
						Identifier ↑*name* 
						Identifier ↑*super*
						**list_decl_field** ↓*env_types* ↓*super* ↓*name* ↑*env_exp_f*
						**list_decl_method** ↓*env_types* ↓*super* ↑*env_exp_m*
		]
	**condition** *(*class*(_, env_exp_super), _)* = *env_types(super)*
	**affectation** *new_def* = *(*class*(super, (env_exp_f + env_exp_m)/env_exp_super),* type_class*(name))*
							 *env_types* = *{name -> new_def} / env_types*



**list_decl_field** ↓*env_types* ↓*super* ↓*class* ↑*env_exp*
		-> {*env_exp_r* = {}}
			 [( **decl_field** ↓*env_types* ↓*super* ↓*class* ↑*env_exp* 
			 		{*env_exp_r* = *env_exp_r* + *env_exp*} )\*]



**decl_field** ↓*env_types* ↓*super* ↓*class* ↑{*name -> (*field*(visib, class, type))*}
		-> DeclField ↑*visib* [
			**type** ↓*env_types* ↑*type*
			Identifier ↑*name* 
			***INITIALIZATION***]
	**condition** *type* != void &&
							if (class*(_, env_exp_super), _*) = *env_types(super)* && *env_exp_super(name)* défini
							then *env_exp_super(name)* = (field*(_,_),_*)



**list_decl_method** ↓*env_types* ↓*super* ↑*env_exp_r*
		-> {*env_exp_r* = {}}
			 [( **decl_method** ↓*env_types* ↓*super* ↑*env_exp* 
			 		{*env_exp_r* = *env_exp_r* + *env_exp*} )\*]



**decl_method** ↓*env_types* ↓*super* ↑{*name -> (*method*(sig), type)*}
		-> DeclMethod [
				**type** ↓*env_types* ↑*type*
				Identifier ↑*name* 
				**list_decl_param** ↓*env_types* ↑*sig* 
				***METHOD_BODY***
			]
	**condition** if (class*(_, env_exp_super), _*) = *env_types(super)* && *env_exp_super(name)* défini
							then (method *(sig2), type2)* = *env_exp_super(name)*
								&& *sig* == *sig2* && subtype*(env_types, type, type2)*



**list_decl_param** ↓*env_types* ↑*sig*
		-> {*sig* = []}
			 [( **decl_param** ↓*env_types* ↑*type*
			 		{*sig* = *sig* @ [type]} )\*]



**decl_param** ↓*env_types* ↑*type*
		-> DeclParam[
			**type** ↓*env_types* ↑*type*
			Identifier |> \_]
	**condition** *type* != *void*

## Passe 3

Dans cette passe, les imports sont ignorés, et le comportement de la passe n'est pas changée ou delà de cela.

# Étape C

L'étape C ne se voit pas beaucoup changée. La génération de la table des méthodes doit cependant inclure les classes importées. Il faut ainsi faire attention au nommage des labels, pour qu'il n'y ait pas de collision, mais également que lors de chaque compilation, les mêmes méthodes portent le même label. 

# Étape D

Dans notre compilation séparée, on rajoute une 4e étape pour l'édition de liens. Lors de cette édition, chaque fichier `.deco` est concaténé afin d'avoir la définition de toutes les méthodes, et le corps du programme principal de chaque fichier est enlevé, à l'exception de celui du premier fichier spécifié. Il nous faut également une vérification de tous les labels existants, et qu'une méthode référencée est bien définie. 