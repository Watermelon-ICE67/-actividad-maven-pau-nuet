# ComputerCenterShop v0 starter

Projecte inicial visible per a la practica integradora de RA1 del modul MP0486 - Acces a dades.

## Finalitat

Aquest starter serveix per treballar la gestio d'informacio en fitxers: rutes relatives, lectura, conversio de text a objectes, modificacio en memoria, escriptura, persistencia entre execucions i tractament d'errors.

No es una solucio completa. El treball de l'alumnat consisteix a evolucionar el projecte seguint el backlog RA1.

## Estructura

- `model`: classes de domini.
- `view`: interfície grafica Swing existent.
- `main`: coordinacio principal de l'aplicacio i menu de consola.
- `dao`: acces a dades basat en fitxers.
- `files`: dades de treball en text.

## Dades principals

- `files/users.txt`: usuaris per al login simulat.
- `files/items.txt`: productes inicials de la botiga.

Credencials de prova:

- usuari `1001`, contrasenya `admin`
- usuari `1002`, contrasenya `repair`

## Tasques esperades de RA1

El backlog indica quines parts s'han de completar, corregir o demostrar:

- lectura real de `items.txt`;
- conversio robusta de linies de text a `Product`;
- modificacio de l'inventari;
- guardat de l'inventari actualitzat;
- comprovacio de persistencia entre execucions;
- gestio de fitxer absent o linia mal formada;
- README i proves finals.

## Execucio

Requisits:

- JDK 25.
- Maven 3.9.x, si es vol compilar des de linia d'ordres.

Comandes utils:

```bash
mvn compile
```

Classe principal de consola:

```text
main.Shop
```

Classe principal grafica Swing:

```text
view.LoginView
```

## Limits RA1

Aquest projecte no utilitza JDBC, SQL, ORM, MongoDB, serveis externs ni bases de dades. La persistencia ha de quedar limitada a fitxers locals.

## Proves RA1

| Issue | Cas provat | Resultat observat |
|---|---|---|
| ISSUE-02 | Rutes de `files/items.txt` i `files/users.txt` | Ruta relativa (`user.dir` + `files/`), sense rutes absolutes; fitxers a l'arrel del projecte |
| ISSUE-03 | Lectura de `files/items.txt` línia a línia amb `BufferedReader` i try-with-resources | Les línies es llegeixen com a text i el fitxer es tanca automàticament |
| ISSUE-04 | Línia vàlida `Item:SSD 1TB;Wholesaler Price:60.00;Stock:8;` | Es crea un `Product` amb nom, preu majorista 60.00 i estoc 8 |
| ISSUE-05 | Línia mal formada `Item:Ratoli;Wholesaler Price:abc;Stock:5;` | Avís amb el número de línia; l'aplicació continua i carrega els altres productes |
| ISSUE-06 | Afegir `Ratoli gaming`, preu 25.50, estoc 7 (opció 2) | Apareix a l'opció 5 durant la mateixa execució, sense reiniciar |
| ISSUE-07 | Sumar 5 a l'estoc de `SSD 1TB` (opció 3) | L'estoc passa de 8 a 13; nom i preus sense canvis |
| ISSUE-08 | Eliminar `Teclat mecanic` (opció 4) | Ja no apareix a l'opció 5; la resta de productes continuen accessibles |
| ISSUE-09 | Desar l'inventari (opció 10) | `files/items.txt` conté l'inventari actualitzat en el mateix format de línia |
| ISSUE-10 | Afegir `Pantalla OLED` (75.00, estoc 10), desar i reiniciar | Després del reinici, l'opció 5 mostra `Pantalla OLED`: el canvi persisteix |
