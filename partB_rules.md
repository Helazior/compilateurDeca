`identifier` *env_exp* |> *def*
		-> Identifier (|> *name*)
	**affect** *def* = *env_exp(name)*

`type` *env_types* |> *type*
		-> Identifier |> *name*
	**condition** *(_, type)* = *env_types(name)*

---

---

`program` |> *env_types*
		-> Program[`list_decl_class`(*env_types_predef*  |> *env_types*) ***MAIN*** ]



`list_decl_class` *env_types*  |> *env_types*
		-> [(`decl_class` env_types |> env_types)\*]



`decl_class` env_types |> ({*name ->* (class*(super, {}),* type_class*(name)*)} + env_types)
		-> DeclClass [
						Identifier |> *name* 
						Identifier |> *super*
						***LIST_DECL_FIELD*** ***LIST_DECL_METHOD***
		]
	**condition** *env_types(super)* = *(*class*(_), _)*

---

---

`program` *env_types* |> *env_types*
		-> Program[`list_decl_class`(*env_types* |> *env_types*) ***MAIN*** ]



`list_decl_class` *env_types*  |> *env_types*
		-> [(`decl_class` env_types |> env_types)\*]



`decl_class` env_types |> env_types
		-> DeclClass [
						Identifier |> *name* 
						Identifier |> *super*
						`list_decl_field` *env_types*, *super*, *name* |> *env_exp_f*
						`list_decl_method` *env_types*, *super* |> *env_exp_m*
		]
	**condition** *(*class*(_, env_exp_super), _)* = *env_types(super)*
	**affectation** *new_def* = *(*class*(super, (env_exp_f + env_exp_m)/env_exp_super),* type_class*(name))*
							 *env_types* = *{name -> new_def} / env_types*



`list_decl_field` *env_types*, *super*, *class* |> *env_exp*
		-> {*env_exp_r* = {}}
			 [( `decl_field` *env_types, super, class* |> *env_exp* 
			 		{*env_exp_r* = *env_exp_r* + *env_exp*} )\*]



`decl_field` *env_types, super, class* |> {*name -> (*field*(visib, class, type))*}
		-> DeclField |> *visib* [`type` *env_types* |> *type*, Identifier |> *name* ***INITIALIZATION***]
	**condition** *type* != void &&
							if (class*(_, env_exp_super), _*) = *env_types(super)* && *env_exp_super(name)* défini
							then *env_exp_super(name)* = (field*(_,_),_*)



`list_decl_method` *env_types*, *super* |> *env_exp_r*
		-> {*env_exp_r* = {}}
			 [( `decl_method` *env_types, super* |> *env_exp* 
			 		{*env_exp_r* = *env_exp_r* + *env_exp*} )\*]



`decl_method` *env_types, super* |> {*name -> (*method*(sig), type)*}
		-> DeclMethod [
				`type` *env_types* |> *type*
				Identifier |> *name* 
				`list_decl_param` env_types |> sig 
				***METHOD_BODY***
			]
	**condition** if (class*(_, env_exp_super), _*) = *env_types(super)* && *env_exp_super(name)* défini
							then (method *(sig2), type2)* = *env_exp_super(name)*
								&& *sig* == *sig2* && subtype*(env_types, type, type2)*



`list_decl_param` *env_types* |> *sig*
		-> {*sig* = []}
			 [( `decl_param` *env_types* |> *type*
			 		{*sig* = *sig* @ [type]} )\*]



`decl_param` *env_types* |> *type*
		-> DeclParam[`type` *env_types* |> *type* Identifier |> \_]
	**condition** *type* != *void*

---

---

`program` *env_types*
		-> Program[`list_decl_class` *env_types* `main` *env_types*]



`list_decl_class` *env_types*
		-> [(`decl_class` env_types)\*]



`main` *env_types*
		-> EmptyMain
		-> Main `bloc` *env_types*, {}, {}, 0, void



`decl_class` env_types
		-> DeclClass [
						Identifier |> *class* 
						Identifier |> _
						`list_decl_field` *env_types*, *env_exp*, *class*
						`list_decl_method` *env_types*, *env_exp*, *class*
		]
	**condition** *(*class*(_, env_exp), _)* = *env_types(class)*



`list_decl_field` *env_types*, *env_exp*, *class*
		-> [( `decl_field` *env_types*, *env_exp*, *class* )\*]



`decl_field` *env_types*, *env_exp*, *class*
		-> DeclField |> *visib* [
				`type` *env_types* |> *type*
				Identifier |> \_
				`initialization` *env_types, env_exp, class, type*
			]



`initialization` *env_types, env_exp, class, type*
		-> Initialization [`rvalue` *env_types, env_exp, class, type*]
		-> NoInitialization



`list_decl_method` *env_types*, *env_exp*, *class*
			 [( `decl_method` *env_types*, *env_exp*, *class* )\*]



`decl_method` *env_types*, *env_exp*, *class*
		-> DeclMethod [
				`type` *env_types* |> *return*
				Identifier |> \_ 
				`list_decl_param` env_types |> env_exp_params
				`method_body` *env_types*, *env_exp*, *env_exp_params*, *class*, *return*
			]



`list_decl_param` *env_types* |> *env_exp_r*
		-> {*env_exp_r* = []}
			 [( `decl_param` *env_types* |> *env_exp*
			 		{*env_exp_r* = *env_exp_r* + *env_exp*} )\*]



`decl_param` *env_types* |> {*name* -> (param, *type*)}
		-> DeclParam[`type` *env_types* |> *type* Identifier |> *name*]



`method_body` *env_types*, *env_exp*, *env_exp_params*, *class*, *return*
		-> MethodBody `bloc` *env_types*, *env_exp*, *env_exp_params*, *class*, *return*
		-> MethodAsmBody [StringLiteral]



`list_decl_var` *env_types*, *env_exp_sup*, *env_exp*, *class* |> *env_exp*
		-> [
			 		(`decl_var` *env_types*, *env_exp_sup*, *env_exp*, *class*, *env_exp*)\*
			 	]



`decl_var` *env_types*, *env_exp_sup*, *env_exp*, *class*, {*name* -> (var, *type*) + *env_exp*}
		-> DeclVar [
				`type` *env_types* |> *type*
				Identifier |> *name*
				`initialization` *env_types*, *env_exp/env_exp_sup*, *class*, *type*
			]
	**condition** *type* != void

`bloc` *env_types*, *env_exp_sup*, *env_exp*, *class*, *return*
		-> [
				`list_decl_var` *env_types*, *env_exp_sup*, *env_exp*, *class* |> *env_exp_r*
				`list_inst` *env_types*, *env_exp_r/env_exp_sup*, *class*, *return*
			]



`list_inst` *env_types*, *env_exp*, *class*, *return*
		-> [(`inst` *env_types*, *env_exp*, *class*, *return*)\*]



`inst` *env_types*, *env_exp*, *class*, *return*
		-> `expr` *env_types*, *env_exp*, *class* |> _
		-> `print` [`list_exp_print` *env_types*, *env_exp*, *class*]
		-> IfThenElse [
				`condition` *env_types*, *env_exp*, *class*
				`list_inst` *env_types*, *env_exp*, *class*, *return*
				`list_inst` *env_types*, *env_exp*, *class*, *return*
			]
		-> NoOperation
		-> Return [`rvalue` *env_types*, *env_exp*, *class*, *return*]
	**condition** return != void
		-> While [
				`condition` *env_types*, *env_exp*, *class*
				`list_inst` *env_types*, *env_exp*, *class*, *return*
		]



`print`
		-> Print
		-> Println



`rvalue` *env_types*, *env_exp*, *class*, *type1*
		-> `expr` *env_types*, *env_exp*, *class* |> *type2*
	**condition** assign_compatible(*env_types*, *type1*, *type2*)



`condition` *env_types*, *env_exp*, *class*
		-> `expr` *env_types*, *env_exp*, *class* |> boolean



`list_exp_print` *env_types*, *env_exp*, *class*
		-> [(`exp_print` *env_types*, *env_exp*, *class*)\*]



`exp_print` *env_types*, *env_exp*, *class*
		`expr` *env_types*, *env_exp*, *class* |> *type*
	**condition** *type* in [int, float, string]



`expr` *env_types*, *env_exp*, *class* |> *type*
		-> Assign [
				`lvalue` *env_types*, *env_exp*, *class* |> *type1*
				`rvalue` *env_types*, *env_exp*, *class*, *type1*
			]
		-> `op_bin` |> *op* [
				`expr` *env_types*, *env_exp*, *class* |> *type1*
				`expr` *env_types*, *env_exp*, *class* |> *type2*
			]
	**affectation** *type* = type_binary_op(*op*, *type1*, *type2*)
		-> `lvalue` *env_types*, *env_exp*, *class* |> *type1*
		-> ReadInt
	**affectation** *type* = int
		-> ReadFloat
	**affectation** *type* = float
		-> `op_un` |> *op* [
				`expr` *env_types*, *env_exp*, *class* |> *type1*
			]
	**affectation** *type* = type_unary_op(*op*, *type1*)
		-> `literal` |> *type*
		-> Cast [
				`type` *env_types* |> *type*
				`expr` *env_types*, *env_exp*, *class* |> *type2*
		]
	**condition** cast_compatible(*env_types*, *type2*, *type*)
		-> InstanceOf [
				`expr` *env_types*, *env_exp*, *class* |> *type1*
				`type` *env_types* |> *type2*
		]
	**affectation** type = type_instanceof_op(type1, type2)
		-> `method_call` *env_types*, *env_exp*, *class* |> *type*
		-> New [`type` *env_types* |> *type*]
	**condition** *type* = type_class(\_)
		-> This
	**condition** *class* != 0
	**affectation** *type* = type_class(*class*)



`literal` \|> int -> IntLitteral
`literal` |> float -> FloatLitteral
`literal` |> string -> StringLitteral
`literal` |> boolean -> BooleanLitteral
`literal` |> null -> Null

`op_bin` \|> divide -> Divide
`op_bin` |> minus -> Minus
`op_bin` |> modulo -> Modulo
`op_bin` |> multiply -> Multiply
`op_bin` |> plus -> Plus
`op_bin` |> and -> And
`op_bin` |> or -> Or
`op_bin` |> eq -> Equals
`op_bin` |> neq -> NotEquals
`op_bin` |> gt -> Greater
`op_bin` |> geq -> GreaterOrEquals
`op_bin` |> lt -> Lower
`op_bin` |> leq -> LowerOrEquals

`op_un` \|> minus -> UnaryMinus
`op_un` |> not -> Not



`lvalue` *env_types*, *env_exp*, *class* |> *type*
		-> `lvalue_ident` *env_exp* |> *type*
		-> Selection [
				`expr` *env_types*, *env_exp*, *class* |> type_class(*class2*)
				`field_ident` *env_exp2* |> public, \_, *type*
			]
	**condition** (class*(\_, env_exp2),\_*) = *env_types*(*class2*)
		-> Selection [
				`expr` *env_types*, *env_exp*, *class* |> type_class(*class2*)
				`field_ident` *env_exp2* |> protected, *class_field*, *type*
			]
	**condition** (class*(\_, env_exp2),\_*) = *env_types*(*class2*)
							&& subtype(*env_types*, type_class(*class2*), type_class(*class*))
							&& subtype(*env_types*, type_class(*class*), type_class(*class_field*))



`lvalue_ident` *env_exp* |> *type*
		-> `identifier` *env_exp* |> (field(\_, \_), *type*)
		-> `identifier` *env_exp* |> (param, *type*)
		-> `identifier` *env_exp* |> (var, *type*)



`field_ident` *env_exp* |> *visib*, *class*, *type*
		-> `identifier` *env_exp* |> (field(*visib*,*class*), *type*)



`method_call` *env_types*, *env_exp*, *class* |> *type*
		-> MethodCall [
				`expr` *env_types*, *env_exp*, *class* |> type_class(*class2*)
				`method_ident` *env_exp2* |> *sig*, *type*
				[`rvalue_star` *env_types*, *env_exp*, *class*, *sig*]
			]
	**condition** (class(\_, *env_exp2*), \_) = *env_types*(*class2*)



`method_ident` *env_exp* |> *sig*, *type*
		-> `identifier` *env_exp* |> (method(*sig*), *type*)



`rvalue_star` *env_types*, *env_exp*, *class*, []
		->

`rvalue_star` *env_types*, *env_exp*, *class*, *type . sig*
		-> `rvalue` *env_types*, *env_exp*, *class*, *type*
		-> `rvalue_star` *env_types*, *env_exp*, *class*, *sig*
