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
- bool - typ logiczny

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
Funkcja pozwala na wpisywanie wartości zmiennych wprowadzony przez użytkownika typu _int_, _real_, _bool_.

Składnia wywołania funkcji:
```$xslt
read(<zmienna wpisywana>)
```

## ETAP 2.

Instrukcje warunkowe

Instrukcja warunkowa pozwala na sterowanie przebiegiem działania programu. Operacje dozowlone w warunku to:
- == - równość
- != - nierówność
- < - mniejsze
- <= - mniejsze równe
- /> - większe
- />= - większe równe
```
Składnia instrukcji warunkowej:

```$xslt
if <warunek> begin
<zachowanie dla prawdziwego warunku>
endif else
<zachowanie dla fałszywego warunku>
endelse
```

Przykładowy kod:
```
if x < 5 begin
    if x == 4 begin
        print(x)
    endif else
        int y = 5
        print(y)
    endelse
endif else
x = 4
endelse
```
### Pętla
Składania pętli:
```$xslt
loop <warunek> begin
    <blok pętli>
endloop
```
Przykładowy kod:
```
loop x < 5 begin
    y = 0
    print(x)
    loop y < 3 begin
        print(s)
    endloop
endloop
```
Pętle wymagają podania zadeklarowanej wcześniej zmiennej, która będzie zwiększana
przed każdym przejściem pętli. Zmienna ta musi być typu `int`. Możliwe jest podanie dowolnego
warunku zakończenia pętli z tych dostępnych w instrukcji warunkowej.
### Funkcja
Składnia funkcji:
```$xslt
<typ zwracany> function <nazwa funkcji> (<typ argumentu> <argument>, ...) begin
    <blok funkcji>
    return <zwracana zmienna>
endfunction
```
Przykładowy kod:
```$xslt
real function testFun(int a, real b) begin
    print(a)
    print(b)
    real c
    c = 2.0 * b
    return c
endfunction

int x = 1
real y = 2.0
real z
z = testFun(x, y)
```
Funkcja wymaga podania typu zwracanej wartości (real lub int), oraz typów argumentów (również real lub int).
Przy wywoływaniu funkcji należy podać jako argumenty zadeklarowane wcześniej zmienne.

Zmienne zadeklarowane w funkcji są traktowane jako zmienne lokalne i nie ma do nich dostępu poza blokiem funckji.

