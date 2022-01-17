exit_value=0

if decac -h > tmp.out; then
	if ! [ -s tmp.out ]; then
		echo "Erreur: il n'y a pas d'affichage d'usage sur l'option -h"
		exit_value=1
	fi
else
	echo "Erreur: l'option -h fait une erreur"
	exit_value=1
fi

if decac > tmp.out; then
	if ! [ -s tmp.out ]; then
		echo "Erreur: il n'y a pas d'affichage d'usage sans argument"
		exit_value=1
	fi
else
	echo "Erreur: decac sans argument fait une erreur"
	exit_value=1
fi

if decac -r 2 src/test/deca/codegen/valid/provided/ecrit0.deca 2> tmp.out; then
	echo "Erreur: decac n'échoue pas avec un nombre de registes invalide"
fi

if decac -r 18 src/test/deca/codegen/valid/provided/ecrit0.deca 2> tmp.out; then
	echo "Erreur: decac n'échoue pas avec un nombre de registes invalide"
fi


echo "Options testées avec succès"

exit $exit_value
