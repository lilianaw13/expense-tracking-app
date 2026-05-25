# expense-tracking-app

## Baza de date

Aplicatia JavaFX foloseste SQLite prin JDBC pentru salvarea cheltuielilor.
Integrarea nu modifica design pattern-urile existente: baza de date este adaugata ca o noua implementare Repository si este selectata prin Abstract Factory.

Fisierul bazei de date se creeaza automat la prima pornire:

```text
expense_tracker.db
```

Pentru rulare, adauga `sqlite-jdbc.jar` in librariile proiectului din IntelliJ.
URL-ul bazei de date poate fi schimbat cu proprietatea JVM:

```bash
-Dexpense.db.url=jdbc:sqlite:expense_tracker.db
```
