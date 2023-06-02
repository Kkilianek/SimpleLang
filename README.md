# Język _SimpleLang_
__Autorzy__: Kacper Kilianek, Agata Biernacka

## WSTĘP
Język został napisany w ramach projektu z przedmiotu _Języki formalne i kompilatory_. Do analizy składnowej wykorzystano ANTLR, czyli narzędzie do generowania analizatorów składniowych. Jest on odpowiedzialny za interpretację gramatyki, tworzenie parsera, lexera oraz drzewa wyprowadzenia AST.
Za kompilację drzewa wyprowadzenia do postaci kodu maszynowego i dalszą optymalizację odpowiada LLVM. 

## ETAP 1.
Język pozwala na tworzenie prostych programów z rozszerzeniem _sl_. Umożliwa tworzenie zmiennych, wykonywanie operacji arytmetycznych na zmiennych oraz posiada zaimplementowane podstawowe metody wejścia i wyjścia. 
W razie problemów język posiada rozbudowany modułu wskazywania błędów podczas analizy leksykalno-składniowej.

### Zmienne

Język wspiera 3 typy zmiennych:
- int - typ całkowity
- real - typ zmiennoprzecinkowy
- long - próba typu 64bitowego
- bool - typ logiczny (działa!)

Język wspiera również podstawowe operacje algebraiczne:
- "\+" - dodawanie
- "\-" - odejmowanie
- "\*" - mnożenie
- "\/" - dzielenie

Możliwe są 3 główne operacje na zmiennych:
- deklaracja
- deklaracja z przypisaniem
- przypisanie

Składnia wymienionych operacji:
```$xslt
// deklaracja
<typ> <nazwa zmiennej>
// deklaracja z przypisaniem
<typ> <nazwa zmiennej> = <wartość>
// przypisanie
<nazwa zmiennej> = <wartość>
```

Przykładowy kod:
```$xslt
int z = 4
z = z + 2 * 3 - 3
real a
a = 1.1 * 1.1 + 5.4 / 2.7
```

### Funkcje wbudowane
Dostępne są 2 funkcje wbudowane:
- print() - odpowiadający za wypisywanie wartości zmiennych 
- read() - odpowiadający za wczytywanie wartości zmiennych

#### Funkcja _print_
Funkcja pozwala na wypisywanie wartości zmiennych typu _int_, _real_, _bool_ oraz ich odpowiedników tablicowych (pojedyńcze elementy).

Składnia wywołania funkcji:
```$xslt
print(<zmienna wypisywana>)
``` 
#### Funkcja _read_
Funkcja pozwala na wpisywanie wartości zmiennych wprowadzony przez użytkownika typu _int_, _real_, (_bool_)*.

Składnia wywołania funkcji:
```$xslt
read(<zmienna wpisywana>)
``` 

