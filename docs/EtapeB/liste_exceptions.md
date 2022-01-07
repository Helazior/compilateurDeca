# Liste des exceptions

## Général

### identifier

* *env_exp*(*name*) existe

### type

* *env_types*(*name*) existe et est un type

## Passe 1

### program

rien

### list_decl_class

rien

### decl_class

* union disjointe: erreur si la classe *name* est déjà défini
* *super* doit être une classe

## Passe 2

### program

rien

### list_decl_class

rien

### decl_class

* :warning: *super* doit être une classe
* union disjointe: les attributs et méthodes doivent être nommés différemment

### list_decl_field

* union disjointe: chaque <u>field</u> doit être nommé différemment

### decl_field

* *type* != <u>void</u>
* :warning: *super* doit être une classe
* Si le nom *name* est défini dans la classe, il doit être un <u>field</u>

### list_decl_method

* union disjointe: chaque méthode doit avoir un nom différent

### decl_method

* :warning: *super* doit être une classe
* Si le nom *name* est défini dans la classe, il doit être une méthode

### list_decl_param

rien

### decl_param

* *type* != <u>void</u>

## Passe 3

### program

rien

### list_decl_class

rien

### decl_cass

* :warning: *class* doit être une classe

### main

rien

### list_decl_field

rien

### decl_field

rien

### initialization

rien

### list_decl_method

rien

### decl_method

rien

### list_decl_param

* union disjointe: chaque param doit avoir un nom différent

### decl_param

rien

### method_body

rien

### list_decl_var

rien

### decl_var

* *type* != <u>void</u>
* union disjointe: la variable ne doit pas exister dans *env_exp*

### bloc

rien

### list_inst

rien

### inst

-> `expr`: rien

-> `print`: rien

-> <u>IfThenElse</u>: rien

-> <u>NoOperation</u>: rien

-> <u>Return</u>: *return* != <u>void</u>

-> <u>While</u>: rien

### print

rien

### rvalue

* on doit pouvoir assigner une valeur de type *type2* au type *type1*

### condition

* L'expression doit être de type <u>boolean</u>

### list_exp_print

rien

### exp_print

* l'expression doit être de type <u>int</u>, <u>float</u> ou <u>string</u>

### expr 

-> <u>Assign</u>: rien

-> `opbin`: type_binary_op(*op*, *type1*, *type2*) doit être définit, sans quoi on ne peut faire l'opération *op* entre *type1* et *type2*

-> `lvalue`: rien

-> <u>ReadInt</u>: rien

-> <u>ReadFloat</u>: rien

-> `op_un`: type_unary_op(*op*, *type1*) doit être définit

-> `literal`: rien

-> <u>Cast</u>: cast_compatible(*env_types*, *type2*, *type*) doit être définit

-> <u>InstanceOf</u>: type_instanceof_op(type1, type2) doit être définit

-> `method_call`: rien

-> <u>New</u>: *type* doit être une classe

-> <u>This</u>: *class* doit être une "vraie" classe (i.e. différent de 0, et :warning: class doit être dans *env_types*)

### literal

rien

### op_bin

rien

### op_un

rien

### lvalue

-> `lvalue_ident`: rien

-> <u>Selection</u> (le `field_ident` est <u>public</u>):

* `expr` doit donner un type de classe
* ce type de classe doit être dans *env_types*

-> <u>Selection</u> (le `field_ident` est <u>protected</u>):

* `expr` doit donner un type de classe
* ce type de classe doit être dans *env_types*
* la classe du `expr` doit être un sous-type de la classe actuelle
* la classe actuelle doit être un sous-type de la classe dans laquelle le `field_ident` est défini

## lvalue_ident

* l'identifiant doit être un <u>field</u>, <u>param</u> ou <u>var</u>

## field_ident

* l'identifiant doit être un <u>field</u>

## method_call

* `expr` doit être d'un type classe
* le dit type doit se trouver dans *env_exp2*

### method_ident

* le `indentifier` doit donner une méthode

### rvalue_star

rien

