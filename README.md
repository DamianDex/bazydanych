<h1>Temat projektu</h1>
Celem projektu jest zaimplementowanie systemu realizujacego wybrane podstawowe operacje w przykladowej bazie Northwind
w wybranej technologii.
<br/><br/><b>
Operacje CRUD na wybranych tabelach<br/>
Operacje skladania zamówien na produkty<br/>
Operacje wyszukiwania informacji i raportowania (realizacja zlozonych operacji SQL)<br/>
</b><br/>
Nalezy zwrócic uwage na sposób i wydajnosc realizacji operacji bazodanowych (np. przedstawic wyniki pomiarów wydajnosciowych)<br/>

Waznym elementem projektu jest prezentacja technologii w której realizowany jest projekt, dokumentacja powinna miec forme
przewodnika (tutorialu) po danej technologii (przewodnik ilustrujacy jak nalezy programowac elementy systemu
bazodanowego - na przykladzie bazy Northwind), przewodnik powinien zawierac opis wraz z elementami kodu.

Dokumentacje nalezy prowadzic na biezaco (wiki)</br>
Kod powinien byc dostepny w repozytorium (svn, git)<br/>
Ostateczna wersje projektu nalezy oddac w formie w pelni skonfigurowanej maszyny wirtualnej (vmware lub virtualbox)<br/>
Technologia realizacji projektu do uzgodnienia z prowadzacym<br/>

Propozycje technologii:

Java, Hibernate
Ruby on Rails
Java, Play Framework
Python Django
ASP.NET
C#, ADO.NET, LINQ
PHP, Cake PHP
PHP Zend
Aplikacja mobilna, Android
Aplikacja mobilna, Windows Phone
Inne do uzgodnienia
Proponowane SZBD

MS SQL Server
Oracle
Postgres
MySql
MongoDB
Inne do uzgodnienia

<h1>Autorzy</h1>
Damian Fara <br/>
Slawomir Gladys <br/>
Piotr Gryta <br/><br/>
Studia inzynierskie<br/>
Rok czwarty

<h1>Instrukcja uruchomienia projektu</h1>
<h3>Uruchomienie bazy danych</h3>
Na pulpicie dostarczonej do projektu VMki znajduje sie skrót do
uruchomienia serwera PostgreSQL. Po uruchomieniu serwera nalezy zalogowac sie
jako uzytkownik <b>postgres</b> uzywajac hasla <b>bazydanych</b>.<br/>
Baza danych Nortwhind bedzie dostepna pod adresem: <b>jdbc:postgresql://localhost:5432/northwind</b>

<h3>Pobranie najnowszej wersji kodu z repozytorium</h3>
<b>Git bash</b><br/>
Uruchomic git bash - skrót znajduje sie na pulpicie.
Wykonac kolejno w konsoli:<br/>
cd /c/Users/BazyDanych/Desktop/Projekt/bazydanych <br/>
git pull --rebase
<br/><br/>
<b>InteliJ Idea</b><br/>
Uruchomic IntelliJ IDEA Community Edition 2017.3.1 - skrót na pulpicie.<br/>
VCS -> Git -> Pull

<h3>Uruchomienie aplikacji</h3>
W InteliJ Idea odnalezc klase MainPanel i uruchomic jej metode main.

<h1>Hasla</h1>
Uzytkownik Windows: bazy <br/>
Haslo Windows: abc123 <br/>
Link do pobrania VMki: https://mega.nz/fm/Iix1lZxT <br/>
Adres repozutorium: https://github.com/DamianDex/bazydanych<br/>
Haslo do pgAdmin 4: bazy123 <br/>

<h1>Wybrane technologie</h1>
Java <br/>
Hibernate <br/>
PostgreSQL <br/>
Jenkins <br/>
Swing <br/>
AutoIT <br/>

<h1>Przebieg tworzenia projektu</h1>

Prace nad projektem rozpoczelismy od wygenerowania pliku konfiguracyjnego dla Mavena pom.xml oraz pliku konfiguracyjnego
 hibernate.cfg.xml dla Hibernate'a, w którym zostaly podane informacje niezbedne do polaczenia z wybrana baza danych.<br/>
<img alt="Konfiguracja Mavena" src="https://github.com/DamianDex/bazydanych/blob/master/images/MavenKonfiguracja.png">
<br/><br/>
<img alt="Konfiguracja Hibernate'a" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateKonfiguracja.png">
<br/>

Kolejnym krokiem bylo mapowanie wybranych tabel z bazy danych. W tym celu dla kazdej z tabel:
 <ol>
 <li>Tworzymy klase o nazwie takiej jak wybrana tabela.</li>
 <li>Adnotujemy klase jako klase encji dodajac adnotacje @Entity.</li>
 <li>Dla poszczególnych klas tworzymy pola odzwierciedlajace wybrane przez nas kolumny w danej tabeli. Dla wszystkich
  pól tworzymy gettery i settery.</li>
 <li>Dla kazdej z klas tworzymy bezargumentowy konstruktor.</li>
 <li>Pole bedace kluczem glównym oznaczamy z wykorzystaniem adnotacji @Id. Dodatkowo zamieszczajac adnotacje @GeneratedValue
 mozemy generowac jego wartosc zgodnie z przyjeta strategia.</li>
 <li>Do pliku konfiguracyjnego hibernate.cfg.xml dodajemy wszystkie mapowane klasy.</li>
</ol>
<img alt="Tworzenie encji" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoriesEntity.png"><br/>
<img alt="Mapowanie klas" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateMapping.png"><br/>

Po utworzeniu encji stworzylismy relacje pomiedzy obiektami. Pomiedzy tabelami "Categories" i "Products" istnieje relacja
jeden do wielu. W celu utworzenia tej relacji, w klasie "Categories" tworzymy zbiór produktów przynalezacych do kategori.
Stworzone pole oznaczamy adnotacja @OneToMany.<br/>
<img alt="Relacja OneToMany" src="https://github.com/DamianDex/bazydanych/blob/master/images/RelacjaOneToMany.png"><br/>
W przypadku tworzenia relacji wiele do jeden pomiedzy tabelami "Products" i "Categories" nalezy w klasie "Products"
utworzyc pole typy "Categories" i oznaczyc je adnotacja @ManyToOne. Nalezy równiez uzywajac adnotacji @JoinColumn okreslic,
 która kolumna ma stanowic klucz obcy.<br/>
<img alt="Relacja ManyToOne" src="https://github.com/DamianDex/bazydanych/blob/master/images/RelacjaManyToOne.png"><br/>
<b> EWENTUALNIE OPISAC TWORZENIE RELACJI DLA ORDER_DETAILS, PO CO FETCH CASCADE</b><br/>
Nastepnym krokiem jest przygotowanie klas realizujacych dostep do bazy danych (klasy DAO) w celu odwzorowania zmian w klasach
Java na operacje bazodanowe. Etap ten rozpoczynamy od utworzenia interfejsu, gdzie znajda sie metody realizujace operacje
 bazodanowe. Beda to przedewszystkim operacje:
<ul>
<li>Dodanie informacji do bazy.</li>
<li>Usuniecie informacji z bazy.</li>
<li>Edycja istniejacych informacji w bazie.</li>
<li>Odczytanie informacji z bazy na podstawie róznych kryteriów wyszukiwania.</li>
</ul>
<img alt="Interfejs kategorii" src="https://github.com/DamianDex/bazydanych/blob/master/images/Interfejs.png"><br/>
Po utworzeniu interfejsu, tworzymy klase implementujace metody zawarte w naszym interfejsie.<br/>
<ul>
<li>Operacje dodawania informacji realizujemy z wykorzystaniem metody save() na otwartej sesji.
<img alt="Dodawanie obiektu" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaDodawania.png">
</li>
<li>Operacje aktualizacji informacji wykonujemy analogicznie do operacji dodawania, z ta róznica, ze wykorzystywana jest metoda update().
<img alt="Aktualizowanie obiektu" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaAktualizacji.png">
</li>
<li>Operacje pobierania realizujemy poprzez wczytanie do wczesniej utworzonego obiektu informacji z bazy z wykorzystaniem metody load().
<img alt="Pobieranie danych" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaPobieraniaDanych.png">
</li>
<li>Mozliwe jest równiez pobranie wszystkich informacji z danej tabeli z wykorzystaniem metody createQuery(), gdzie jako
argument podawana jest odpowiednia komenda sqlowa.
<img alt="Listowanie danych" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaListowania.png">
</li>
<li>Operacje usuwania realizowana jest z wykorzystaniem metody delete(), gdzie jako argument podaje sie wczesniej wybrany obiekt przeznaczony do usuniecia.
<img alt="Usuwanie danych" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaUsuwania.png">
</li>
</ul>

Aby w ogóle mozliwe bylo wykonywanie operacji na naszej bazie, nalezy wczesniej utworzyc sesje odpowiadajaca za zarzadzanie
operacjami. W tym celu stworzylismy dwie klasy pomocnicze "ServiceHelper" i "SessionHelper". W klasie "ServiceHelper" mamy
metody umozliwiajace utworzenie fabryki sesji dla poszczególnych klas DAO. Natomiast w klasie "SessionHelper" metoda prepareSession()
umozliwia otwarcie sesji na utworzonej wczesniej fabryce sesji oraz rozpoczecie transakcji, co umozliwia przeprowadzenie operacji
 bazodanowych w klasach DAO. Natomiast metoda finishSession() umozliwia zatwierdzenie transakcji i zamkniecie utworznej sesji.<br/>
<img alt="SessionHelper" src="https://github.com/DamianDex/bazydanych/blob/master/images/SessionHelper.png"><br/>
<br/>
<img alt="ServiceHelper" src="https://github.com/DamianDex/bazydanych/blob/master/images/ServiceHelper.png"><br/>
Kolejnym krokiem bylo przygotowanie klas dostepu do uslug(klasy Service), które udostepniaja konkretne uslugi biznesowe dla aplikacji.
 Klasy serwisu zawieraja metody odpowiadajace metodom w klasach DAO. Metody te wywoluja metody klas DAO. Tak jak w przypadku
 klas DAO rozpoczynamy od utworzenia interfejsów z listami metod dla poszczególnych serwisów.<br/>
<img alt="Interfejs serwisu" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoryService.png"><br/>
Po utworzeniu interfejsów implementujemy nasze klasy serwisu. Dla kazdej z klas tworzymy pole odpowiedniego typu interfejsu.
 Pole to wykorzystujemy do wywolywania metod klas DAO.<br/>
<img alt="Implementacja serwisu" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoryServiceImpl.png"><br/>

<h1>Projekt interfejsu uzytkownika</h1>

<h3>Categories CRUD i Suppliers CRUD</h3>
<img alt="Categories CRUD" src="https://github.com/DamianDex/bazydanych/blob/master/images/Categories CRUD.png"><br/>
<img alt="Suppliers CRUD" src="https://github.com/DamianDex/bazydanych/blob/master/images/SuppliersCRUD.png"><br/>
W oknie "Categories CRUD" znajduje sie przycisk "Read All", który umozliwia wczytanie wszystkich kategori znajdujacych sie
w bazie i wyswietlenie ich w tabeli. Zaznaczenie jednej z kategori spowoduje zaladowanie informacji o niej do sekcji "Current Selection".
Wprowadzenie zmian w tej sekcji i wybranie przycisku "Update" spowoduje zaaktualizowanie danych kategori w bazie oraz w tabeli okna.
Jesli natomiast po wybraniu kategori wybierzemy przycisk "Delete" wybrana kategoria zostanie usunieta, o ile nie jest uzywana przez produkty.
W takim przypadku uzytkownik zobaczy odpowiednie ostrzezenie i kategoria nie zostanie usunieta. Wypelnienie pól "Category Name" i
"Category Description" oraz wybranie przycisku "Add New" spowoduje dodanie nowej kategori do bazy z danymi wprowadzonymi przez nas.<br/>
Okno "Suplliers CRUD" dziala w sposób analogiczny do "Categories CRUD" umozliwiajac przeprowadzanie operacji CRUD na dostawcach.
<h3>Products CRUD</h3>
<img alt="Products CRUD" src="https://github.com/DamianDex/bazydanych/blob/master/images/ProductsCRUD.png"><br/>
W oknie "Product CRUD" operacje dodawania, usuwania i aktualizowania dzialaja podobnie jak w oknach "Categories CRUD" i "Suppliers CRUD".
W sekcji "Current Selection" wystepuja dodatkowo dwa pola typu ComboBox umozliwiajace przypisanie produktu do kategori
i dostawcy istniejacych w bazie. W oknie tym zostala natomiast rozbudowana opcja wyszukiwania produktów. Przycisk "Search"
umozliwia wyszukiwanie produktów wedlug kombinacji czterech kryteriów. Mozliwe jest ustawienie jako kryterium wyszukiwania
dostawcy produktu, kategori produktu, minimalnej i maksymalnej ceny produktu.

<h3>Skladanie zamówien</h3>
<img alt="Make an Order Mock" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/MakeAnOrderMock.png"><br/>

<h4>Customer details</h4>
Istnieja dwie mozliwosci dla wyboru klienta podczas skladania nowego zamówienia: <br/>
<ol>
<li>Stworzenie nowego klietna</li>
<li>Uzycie istniejacego klietna</li>
</ol><br/>
W przypadku wyboru pierwszej opcji Combobox 'CustomerID' pozostanie <b>nieaktywny</b>, a wszystkie pozostale pola w sekcji 'Customer details'
beda dostepne do edycji (wyswietlone beda jedynie odpowiednie placeholders).<br/><br/>
W przypadku wyboru drugiej opcji Combobox 'CustomerID' bedzie aktywny i bedziemy mogli wybrac klienta uzywajac jego 'ID'.
Pozostale pola w tej sekcji pozostana nieaktywne i po wyborze klietna zostana uzupelnione jego danymi w formie jedynie do odczytu.<br/><br/>

<h4>Products</h4>
W sekcji o nazwie 'Products' dostepna bedzie wyszukiwarka, która po wpisaniu odpowiedniej frazy i kliknieciu przycisku szukaj wyswietli jedynie
pasujace do kryterium wyszukiwania (nazwa) produkty.<br/>
Produkty beda prezentowane w tabeli a jej kolejne kolumny przedstawia takie dane jak:
<ul>
<li>Product Id</li>
<li>Product Name</li>
<li>Quantity per Unit</li>
<li>Unit Price</li>
<li>Unit in Stock</li>
<li>Units on Order</li>
</ul><br/>
Dostepna bedzie takze kolumna <b>Actions</b>. Dla kazdego produktu bedziemy w niej mogli podac ilosc produktu jaka chcemy
dodac do zamówienia i wybrac przycisk <b>Order</b>. Po kliknieciu tego przycisku produkty dodadza sie do aktualnie
tworzonego zamówienia. Nastapi równiez seria walidacji (nie mozemy zamówic wiecej niz znajduje sie w magazynie).
<br/>
<b>Wymagane sprawdzenia podczas dodawania produktów do aktualnie tworzonego zamówienia:</b><br/>
<ul>
<li>ilosc produktów w magazynie jest wieksza niz 0,</li>
<li>ilosc zamawianych produktów jest nie wieksza niz ilosc dostepnych produktów,</li>
<li>ilosc zamawianych produktów musi byc wieksza niz 0</li>
<li>wprowadzana wartosc rabatu jest z przedzialu <0,1)</li>
</ul>
<br/>
<b>Okno zamawiania produktów</b>
<img alt="Make an Order Window" src="https://github.com/DamianDex/bazydanych/blob/master/images/MakeOrderWindow.png"><br/>
<br/>
Jak widac na powyższym zrzucie, zaimplementowano interfejs użytkownika z wymaganą funkcjonalnością i komponentami.
W polu tekstowym 'Total Price' jest na bieżąco aktualizowana wartość sumaryczna dodanych produktów. Jest możliwość usunięcia
dodanych już produktów z listy produktów zamawianych. Po wciśnięciu przycisku 'Make an Order' zostaje wyświetlone okno dialogowe
z podsumowaniem utworzonego zamówenia.
<img alt="Order summary widow" src="https://github.com/DamianDex/bazydanych/blob/master/images/OrderSummary.png"><br/>
Po zatwierdzeniu zamówienia zostają wykonane zapytania mające na celu wypełnienie bazy danych nowymi danymi, w przeciwnym wypadku
okno zostaje ukryte.

<h4>Order details</h4>

<h3>Sales Report i Customer Report</h3>
<img alt="Sales Report" src="https://github.com/DamianDex/bazydanych/blob/master/images/SalesReport.png"><br/><br/>
<img alt="Customer Report" src="https://github.com/DamianDex/bazydanych/blob/master/images/CustomerReport.png"><br/>
Okno "Sales Report" umozliwia po wybraniu przycisku "Generate Report" wygenerowanie raportu, w którym znajduja sie
informacje o sumie wartosci wszystkich zamówien w danym miesiacu. Przycisk "Clear" umozliwia wyczyszczenie raportu.<br/>
Okno "Customer Report" dziala w sposób analogiczny, z ta róznica, ze generuje raport pokazujacy calkowita wartosc wszystkich
zamówien danego klienta w danym miesiacu. Lista klientów ulozona jest w kolejnosci od klienta z najwyzsza wartoscia zamówien
w danym miesiacu.