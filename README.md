<h1>Temat projektu</h1>
Celem projektu jest zaimplementowanie systemu realizującego wybrane podstawowe operacje w przykładowej bazie Northwind w wybranej technologii.  
<br/><br/><b>  
Operacje CRUD na wybranych tabelach<br/>  
Operacje składania zamówień na produkty<br/>  
Operacje wyszukiwania informacji i raportowania (realizacja złożonych operacji SQL)<br/>  
</b><br/>  
Należy zwrócić uwagę na sposób i wydajność realizacji operacji bazodanowych (np. przedstawić wyniki pomiarów wydajnościowych)<br/>

Ważnym elementem projektu jest prezentacja technologii w której realizowany jest projekt, dokumentacja powinna mieć formę przewodnika (tutorialu) po danej technologii (przewodnik ilustrujący jak należy programować elementy systemu bazodanowego - na przykładzie bazy Northwind), przewodnik powinien zawierać opis wraz z elementami kodu

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

<h1>Hasła</h1>
Hasło do pgAdmin 4:bazydanych

<h1>Wybrane technologie</h1>
Java <br/>
Hibernate <br/>
PostgreSQL <br/>  
Jenkins <br/>  
Swing <br/>
AutoIT <br/>

<h1>Przebieg tworzenia projektu</h1>

Prace nad projektem rozpoczęliśmy od wygenerowania pliku konfiguracyjnego dla Mavena pom.xml oraz pliku konfiguracyjnego hibernate.cfg.xml dla Hibernate'a, w którym zostały podane informacje niezbędne do połączenia z wybraną bazą danych.<br/>
<img alt="Konfiguracja Mavena" src="https://github.com/DamianDex/bazydanych/blob/master/images/MavenKonfiguracja.png"> 
<br/>
<img alt="Konfiguracja Hibernate'a" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateKonfiguracja.png">
<br/>

Kolejnym krokiem było mapowanie wybranych tabel z bazy danych. W tym celu dla każdej z tabel:
 <ol>
 <li>Tworzymy klasę o nazwie takiej jak wybrana tabela.</li>
 <li>Adnotujemy klasę jako klasę encji dodając adnotację @Entity.</li>
 <li>Dla poszczególnych klas tworzymy pola odzwierciedlające wybrane przez nas kolumny w danej tabeli. Dla wszystkich pól tworzymy gettery i settery.</li>
 <li>Dla każdej z klas tworzymy bezargumentowy konstruktor.</li>
 <li>Pole będące kluczem głównym oznaczamy z wykorzystaniem adnotacji @Id. Dodatkowo zamieszczając adnotacje @GeneratedValue możemy generować jego wartość zgodnie z przyjęta strategią.</li>
 <li>Do pliku konfiguracyjnego hibernate.cfg.xml dodajemy wszystkie mapowane klasy.</li>
</ol>
<img alt="Tworzenie encji" src="https://github.com/DamianDex/bazydanych/blob/master/images/CategoriesEntity.png"><br/>
<img alt="Mapowanie klas" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateMapping.png"><br/>

Po utworzeniu encji stworzyliśmy relacje pomiędzy obiektami. Pomiędzy tabelami "Categories" i "Products" istnieje relacja jeden do wielu. W celu utworzenia tej relacji, w klasie "Categories" tworzymy zbiór produktów przynależących do kategori. Stworzone pole oznaczamy adnotacją @OneToMany.<br/>
<img alt="Relacja OneToMany" src="https://github.com/DamianDex/bazydanych/blob/master/images/RelacjaOneToMany.png"><br/>
W przypadku tworzenia relacji wiele do jeden pomiędzy tabelami "Products" i "Categories" należy w klasie "Products" należy utworzyć pole typy "Categories" i oznaczyć je adnotacją @ManyToOne. Należy również używając adnotacji @JoinColumn określić, która kolumna ma stanowić klucz obcy.<br/>
<img alt="Relacja ManyToOne" src="https://github.com/DamianDex/bazydanych/blob/master/images/RelacjaManyToOne.png"><br/>
<b> EWENTUALNIE OPISAĆ TWORZENIE RELACJI DLA ORDER_DETAILS, PO CO FETCH CASCADE</b><br/>
Następnym krokiem jest przygotowanie klas realizujących dostęp do bazy danych w celu odwzorowania zmian w klasach Java na operacje bazodanowe. Etap ten rozpoczynamy od utworzenia interfejsu, gdzie znajdą się metody realizujące operacje bazodanowe. Będą to przedewszystkim operacje:
<ul>
<li>Dodanie informacji do bazy.</li>
<li>Usunięcie informacji z bazy.</li>
<li>Edycja istniejących informacji w bazie.</li>
<li>Odczytanie informacji z bazy na podstawie różnych kryteriów wyszukiwania.</li>
</ul>
<img alt="Interfejs kategorii" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/Interfejs.png"><br/>
Po utworzeniu interfejsu, tworzymy klasę implementujące metody zawarte w naszym interfejsie.<br/>
<ul>
<li>Operacje dodawania informacji realizujemy z wykorzystaniem metody save() na otwartej sesji.
<img alt="Dodawanie obiektu" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/OperacjaDodawania.png">
</li>
<li>Operacje aktualizacji informacji wykonujemy analogicznie do operacji dodawania, z tą różnicą, że wykorzystywana jest metoda update().
<img alt="Aktualizowanie obiektu" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/OperacjaAktualizacji.png">
</li>
<li>Operacje pobierania realizujemy poprzez wczytanie do wcześniej utworzonego obiektu informacji z bazy z wykorzystaniem metody load().
<img alt="Pobieranie danych" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/OperacjaPobieraniaDanych.png">
</li>
<li>Możliwe jest również pobranie wszystkich informacji z danej tabeli z wykorzystaniem metody createQuery(), gdzie jako argument podawana jest odpowiednia komenda sqlowa.
<img alt="Listowanie danych" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/OperacjaListowania.png">
</li>
<li>Operacje usuwania realizowana jest z wykorzystaniem metody delete(), gdzie jako argument podaje się wcześniej wybrany obiekt przeznaczony do usunięcia.
<img alt="Usuwanie danych" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/OperacjaUsuwania.png">
</li>
</ul>

Aby w ogóle możliwe było wykonywanie operacji na naszej bazie, należy wcześniej utworzyć sesję odpowiadającą za zarządzanie operacjami. W tym celu stworzyliśmy dwie klasy pomocnicze "ServiceHelper" i "SessionHelper".<br/>
<img alt="SessionHelper" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/SessionHelper.png"><br/>
<img alt="ServiceHelper" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/ServiceHelper.png"><br/>
<h1>PostgreSQL</h1>

<h1>Hibernate</h1>

<h1>Projekt interfejsu użytkownika</h1>

<h3>Składanie zamówień</h3>
<img alt="Make an Order Mock" src="https://github.com/DamianDex/bazydanych/blob/master/mocks/MakeAnOrderMock.png">
<br/>

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
<b>Wymagane sprawdzenia podczas dodawania produktów do aktualnie tworzonego zamówienia:<b><br/>
<ul>
<li>TODO !!!</li>
<li></li>
<li></li>
</ul>

<h4>Order details</h4>