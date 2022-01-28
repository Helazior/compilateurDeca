all:	
	decac -c Morpion.deca Grid.deca Player.deca && linker Morpion.deco Grid.deco Player.deco


java:	
	\cp Morpion.java Morpion.deca && \cp Grid.java Grid.deca && \cp Player.java Player.deca
	decac -c Morpion.deca Grid.deca Player.deca && linker Morpion.deco Grid.deco Player.deco
	ima Morpion.ass


run:
	ima Morpion.ass

clean:
	rm *.deco *.ass

