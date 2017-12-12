<h1>Temat projektu</h1>
Celem projektu jest zaimplementowanie systemu realizującego wybrane podstawowe operacje w przykładowej bazie Northwind 
w wybranej technologii.  
<br/><br/><b>  
Operacje CRUD na wybranych tabelach<br/>  
Operacje składania zamówień na produkty<br/>  
Operacje wyszukiwania informacji i raportowania (realizacja złożonych operacji SQL)<br/>  
</b><br/>  
Należy zwrócić uwagę na sposób i wydajność realizacji operacji bazodanowych (np. przedstawić wyniki pomiarów wydajnościowych)<br/>

Ważnym elementem projektu jest prezentacja technologii w której realizowany jest projekt, dokumentacja powinna mieć formę 
przewodnika (tutorialu) po danej technologii (przewodnik ilustrujący jak należy programować elementy systemu 
bazodanowego - na przykładzie bazy Northwind), przewodnik powinien zawierać opis wraz z elementami kodu.

Dokumentację należy prowadzić na bieżąco (wiki)</br>
Kod powinien być dostępny w repozytorium (svn, git)<br/>
Ostateczną wersję projektu należy oddać w formie w pełni skonfigurowanej maszyny wirtualnej (vmware lub virtualbox)<br/>
Technologia realizacji projektu do uzgodnienia z prowadzącym<br/>

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
Sławomir Gładyś <br/>
Piotr Gryta <br/><br/>
Studia inżynierskie<br/>
Rok czwarty

<h1>Instrukcja uruchomienia projektu</h1>
<h3>Uruchomienie bazy danych</h3>
Na pulpicie dostarczonej do projektu VMki znajduje się skrót do 
uruchomienia serwera PostgreSQL. Po uruchomieniu serwera należy zalogować się
jako uzytkownik <b>postgres</b> używając hasła <b>bazydanych</b>.<br/>
Baza danych Nortwhind będzie dostępna pod adresem: <b>jdbc:postgresql://localhost:5432/northwind</b>

<h3>Pobranie najnowszej wersji kodu z repozytorium</h3>
<b>Git bash</b><br/>
Uruchomić git bash - skrót znajduję się na pulpicie.
Wykonać kolejno w konsoli:<br/>
cd /c/Users/BazyDanych/Desktop/Projekt/bazydanych <br/>
git pull --rebase
<br/><br/>
<b>InteliJ Idea</b><br/>
Uruchomić IntelliJ IDEA Community Edition 2017.3.1 - skrót na pulpicie.<br/>
VCS -> Git -> Pull

<h3>Uruchomienie aplikacji</h3>
W InteliJ Idea odnaleźć klasę MainPanel i uruchomić jej metodę main.

<h1>Hasła</h1>
Adres repozutorium:https://github.com/DamianDex/bazydanych<br/>
Hasło do pgAdmin 4:bazydanych

<h1>Wybrane technologie</h1>
Java <br/>
Hibernate <br/>
PostgreSQL <br/>  
Jenkins <br/>  
Swing <br/>
AutoIT <br/>

<h1>Przebieg tworzenia projektu</h1>

Prace nad projektem rozpoczęliśmy od wygenerowania pliku konfiguracyjnego dla Mavena pom.xml oraz pliku konfiguracyjnego
 hibernate.cfg.xml dla Hibernate'a, w którym zostały podane informacje niezbędne do połączenia z wybraną bazą danych.<br/>
<img alt="Konfiguracja Mavena" src="https://github.com/DamianDex/bazydanych/blob/master/images/MavenKonfiguracja.png"> 
<br/><br/>
<img alt="Konfiguracja Hibernate'a" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateKonfiguracja.png">
<br/>

Kolejnym krokiem było mapowanie wybranych tabel z bazy danych. W tym celu dla każdej z tabel:
 <ol>
 <li>Tworzymy klasę o nazwie takiej jak wybrana tabela.</li>
 <li>Adnotujemy klasę jako klasę encji dodając adnotację @Entity.</li>
 <li>Dla poszczególnych klas tworzymy pola odzwierciedlające wybrane przez nas kolumny w danej tabeli. Dla wszystkich
  pól tworzymy gettery i settery.</li>
 <li>Dla każdej z klas tworzymy bezargumentowy konstruktor.</li>
 <li>Pole będące kluczem głównym oznaczamy z wykorzystaniem adnotacji @Id. Dodatkowo zamieszczając adnotacje @GeneratedValue 
 możemy generować jego wartość zgodnie z przyjęta strategią.</li>
 <li>Do pliku konfiguracyjnego hibernate.cfg.xml dodajemy wszystkie mapowane klasy.</li>
</ol>
<img alt="Tworzenie encji" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoriesEntity.png"><br/>
<img alt="Mapowanie klas" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateMapping.png"><br/>

Po utworzeniu encji stworzyliśmy relacje pomiędzy obiektami. Pomiędzy tabelami "Categories" i "Products" istnieje relacja 
jeden do wielu. W celu utworzenia tej relacji, w klasie "Categories" tworzymy zbiór produktów przynależących do kategori. 
Stworzone pole oznaczamy adnotacją @OneToMany.<br/>
<img alt="Relacja OneToMany" src="https://github.com/DamianDex/bazydanych/blob/master/images/RelacjaOneToMany.png"><br/>
W przypadku tworzenia relacji wiele do jeden pomiędzy tabelami "Products" i "Categories" należy w klasie "Products" 
utworzyć pole typy "Categories" i oznaczyć je adnotacją @ManyToOne. Należy również używając adnotacji @JoinColumn określić, 
 która kolumna ma stanowić klucz obcy.<br/>
<img alt="Relacja ManyToOne" src="https://github.com/DamianDex/bazydanych/blob/master/images/RelacjaManyToOne.png"><br/>
<b> EWENTUALNIE OPISAĆ TWORZENIE RELACJI DLA ORDER_DETAILS, PO CO FETCH CASCADE</b><br/>
Następnym krokiem jest przygotowanie klas realizujących dostęp do bazy danych (klasy DAO) w celu odwzorowania zmian w klasach 
Java na operacje bazodanowe. Etap ten rozpoczynamy od utworzenia interfejsu, gdzie znajdą się metody realizujące operacje
 bazodanowe. Będą to przedewszystkim operacje:
<ul>
<li>Dodanie informacji do bazy.</li>
<li>Usunięcie informacji z bazy.</li>
<li>Edycja istniejących informacji w bazie.</li>
<li>Odczytanie informacji z bazy na podstawie różnych kryteriów wyszukiwania.</li>
</ul>
<img alt="Interfejs kategorii" src="https://github.com/DamianDex/bazydanych/blob/master/images/Interfejs.png"><br/>
Po utworzeniu interfejsu, tworzymy klasę implementujące metody zawarte w naszym interfejsie.<br/>
<ul>
<li>Operacje dodawania informacji realizujemy z wykorzystaniem metody save() na otwartej sesji.
<img alt="Dodawanie obiektu" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaDodawania.png">
</li>
<li>Operacje aktualizacji informacji wykonujemy analogicznie do operacji dodawania, z tą różnicą, że wykorzystywana jest metoda update().
<img alt="Aktualizowanie obiektu" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaAktualizacji.png">
</li>
<li>Operacje pobierania realizujemy poprzez wczytanie do wcześniej utworzonego obiektu informacji z bazy z wykorzystaniem metody load().
<img alt="Pobieranie danych" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaPobieraniaDanych.png">
</li>
<li>Możliwe jest również pobranie wszystkich informacji z danej tabeli z wykorzystaniem metody createQuery(), gdzie jako 
argument podawana jest odpowiednia komenda sqlowa.
<img alt="Listowanie danych" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaListowania.png">
</li>
<li>Operacje usuwania realizowana jest z wykorzystaniem metody delete(), gdzie jako argument podaje się wcześniej wybrany obiekt przeznaczony do usunięcia.
<img alt="Usuwanie danych" src="https://github.com/DamianDex/bazydanych/blob/master/images/OperacjaUsuwania.png">
</li>
</ul>

Aby w ogóle możliwe było wykonywanie operacji na naszej bazie, należy wcześniej utworzyć sesję odpowiadającą za zarządzanie 
operacjami. W tym celu stworzyliśmy dwie klasy pomocnicze "ServiceHelper" i "SessionHelper". W klasie "ServiceHelper" mamy 
metody umożliwiające utworzenie fabryki sesji dla poszczególnych klas DAO. Natomiast w klasie "SessionHelper" metoda prepareSession() 
umożliwia otwarcie sesji na utworzonej wcześniej fabryce sesji oraz rozpoczęcie transakcji, co umożliwia przeprowadzenie operacji
 bazodanowych w klasach DAO. Natomiast metoda finishSession() umożliwia zatwierdzenie transakcji i zamknięcie utworznej sesji.<br/>
<img alt="SessionHelper" src="https://github.com/DamianDex/bazydanych/blob/master/images/SessionHelper.png"><br/>
<br/>
<img alt="ServiceHelper" src="https://github.com/DamianDex/bazydanych/blob/master/images/ServiceHelper.png"><br/>
Kolejnym krokiem było przygotowanie klas dostępu do usług(klasy Service), które udostępniają konkretne usługi biznesowe dla aplikacji.
 Klasy serwisu zawierają metody odpowiadające metodom w klasach DAO. Metody te wywołują metody klas DAO. Tak jak w przypadku 
 klas DAO rozpoczynamy od utworzenia interfejsów z listami metod dla poszczególnych serwisów.<br/>
<img alt="Interfejs serwisu" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoryService.png"><br/>
Po utworzeniu interfejsów implementujemy nasze klasy serwisu. Dla każdej z klas tworzymy pole odpowiedniego typu interfejsu.
 Pole to wykorzystujemy do wywoływania metod klas DAO.<br/>
<img alt="Implementacja serwisu" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoryServiceImpl.png"><br/>

<h1>Projekt interfejsu użytkownika</h1>

<h3>Categories CRUD i Suppliers CRUD</h3>
<img alt="Categories CRUD" src="https://github.com/DamianDex/bazydanych/blob/master/images/Categories CRUD.png"><br/>
<img alt="Suppliers CRUD" src="https://github.com/DamianDex/bazydanych/blob/master/images/SuppliersCRUD.png"><br/>
W oknie "Categories CRUD" znajduje się przycisk "Read All", który umożliwia wczytanie wszystkich kategori znajdujących się 
w bazie i wyświetlenie ich w tabeli. Zaznaczenie jednej z kategori spowoduje załadowanie informacji o niej do sekcji "Current Selection".
Wprowadzenie zmian w tej sekcji i wybranie przycisku "Update" spowoduje zaaktualizowanie danych kategori w bazie oraz w tabeli okna.
Jeśli natomiast po wybraniu kategori wybierzemy przycisk "Delete" wybrana kategoria zostanie usunięta, o ile nie jest używana przez produkty.
W takim przypadku użytkownik zobaczy odpowiednie ostrzeżenie i kategoria nie zostanie usunięta. Wypełnienie pól "Category Name" i 
"Category Description" oraz wybranie przycisku "Add New" spowoduje dodanie nowej kategori do bazy z danymi wprowadzonymi przez nas.<br/>
Okno "Suplliers CRUD" działa w sposób analogiczny do "Categories CRUD" umożliwiając przeprowadzanie operacji CRUD na dostawcach.
<h3>Products CRUD</h3>
<img alt="Products CRUD" src="https://github.com/DamianDex/bazydanych/blob/master/images/ProductsCRUD.png"><br/>
W oknie "Product CRUD" operacje dodawania, usuwania i aktualizowania działają podobnie jak w oknach "Categories CRUD" i "Suppliers CRUD".
W sekcji "Current Selection" występują dodatkowo dwa pola typu ComboBox umożliwiające przypisanie produktu do kategori 
i dostawcy istniejących w bazie. W oknie tym została natomiast rozbudowana opcja wyszukiwania produktów. Przycisk "Search"
umożliwia wyszukiwanie produktów według kombinacji czterech kryteriów. Możliwe jest ustawienie jako kryterium wyszukiwania
dostawcy produktu, kategori produktu, minimalnej i maksymalnej ceny produktu.

<h3>Składanie zamówień</h3>
<img alt="Make an Order Mock" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/MakeAnOrderMock.png"><br/>

<h4>Customer details</h4>
Istnieją dwie możliwości dla wyboru klienta podczas składania nowego zamówienia: <br/>
<ol>
<li>Stworzenie nowego klietna</li>
<li>Użycie istniejącego klietna</li>
</ol><br/>
W przypadku wyboru pierwszej opcji Combobox 'CustomerID' pozostanie <b>nieaktywny</b>, a wszystkie pozostałe pola w sekcji 'Customer details'
będą dostępne do edycji (wyświetlone będą jedynie odpowiednie placeholders).<br/><br/>
W przypadku wyboru drugiej opcji Combobox 'CustomerID' będzie aktywny i będziemy mogli wybrać klienta używając jego 'ID'.
Pozostałe pola w tej sekcji pozostaną nieaktywne i po wyborze klietna zostaną uzupełnione jego danymi w formie jedynie do odczytu.<br/><br/>

<h4>Products</h4>
W sekcji o nazwie 'Products' dostępna będzie wyszukiwarka, która po wpisaniu odpowiedniej frazy i kliknięciu przycisku szukaj wyświetli jedynie
pasujące do kryterium wyszukiwania (nazwa) produkty.<br/>
Produkty będą prezentowane w tabeli a jej kolejne kolumny przedstawią takie dane jak:
<ul>
<li>Product Id</li>
<li>Product Name</li>
<li>Quantity per Unit</li>
<li>Unit Price</li>
<li>Unit in Stock</li>
<li>Units on Order</li>
</ul><br/>
Dostępna będzie także kolumna <b>Actions</b>. Dla każdego produktu będziemy w niej mogli podać ilość produktu jaką chcemy
dodać do zamówienia i wybrać przycisk <b>Order</b>. Po kliknięciu tego przycisku produkty dodadzą się do aktualnie
tworzonego zamówienia. Nastąpi również seria walidacji (nie możemy zamówić więcej niż znajduje się w magazynie).
<br/>
<b>Wymagane sprawdzenia podczas dodawania produktów do aktualnie tworzonego zamówienia:</b><br/>
<ul>
<li>TODO !!!</li>
<li></li>
<li></li>
</ul>

<h4>Order details</h4>

<h3>Sales Report i Customer Report</h3>
<img alt="Sales Report" src="https://github.com/DamianDex/bazydanych/blob/master/images/SalesReport.png"><br/><br/>
<img alt="Customer Report" src="https://github.com/DamianDex/bazydanych/blob/master/images/CustomerReport.png"><br/>
Okno "Sales Report" umożliwia po wybraniu przycisku "Generate Report" wygenerowanie raportu, w którym znajdują się
informacje o sumie wartości wszystkich zamówień w danym miesiącu. Przycisk "Clear" umożliwia wyczyszczenie raportu.<br/>
Okno "Customer Report" działa w sposób analogiczny, z tą różnicą, że generuje raport pokazujący całkowitą wartość wszystkich
zamówień danego klienta w danym miesiącu. Lista klientów ułożona jest w kolejności od klienta z najwyższą wartością zamówień
w danym miesiącu.
