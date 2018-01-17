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
Użytkownik Windows: bazy <br/>
Hasło Windows: abc123 <br/>
Link do pobrania VMki: https://mega.nz/fm/Iix1lZxT <br/>
Adres repozutorium: https://github.com/DamianDex/bazydanych<br/>
Hasło do pgAdmin 4: bazy123 <br/>

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
<li>ilość produktów w magazynie jest większa niż 0,</li>
<li>ilość zamawianych produktów jest nie większa niż ilość dostępnych produktów,</li>
<li>ilość zamawianych produktów musi byc większa niż 0</li>
<li>wprowadzana wartość rabatu jest z przedziału <0,1)</li>
</ul>
<br/>
<b>Okno zamawiania produktów</b>
<img alt="Make an Order Window" src="https://github.com/DamianDex/bazydanych/blob/master/images/MakeOrderWindow.png"><br/>
<br/>
Jak widać na powyższym zrzucie, zaimplementowano interfejs użytkownika z wymaganą funkcjonalnością i komponentami.
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
Okno "Sales Report" umożliwia po wybraniu przycisku "Generate Report" wygenerowanie raportu, w którym znajdują się
informacje o sumie wartości wszystkich zamówień w danym miesiącu. Przycisk "Clear" umożliwia wyczyszczenie raportu.<br/>
Okno "Customer Report" działa w sposób analogiczny, z tą różnicą, że generuje raport pokazujący całkowitą wartość wszystkich
zamówień danego klienta w danym miesiącu. Lista klientów ułożona jest w kolejności od klienta z najwyższą wartością zamówień
w danym miesiącu.

<h1>Logi z wykonywanych operacji</h1>
<h3>Categories CRUD</h3>
<p>
Operacje CRUD na tabeli Categories pokazują najprostsze operacje wykonywane przez framework Hibernate.</br>
Zapytanie ograniczają się do najprostszych SELECT, DELETE, UPDATE i INSERT.</br>
Brak joinów.</br>
Tabela Categories używana jest przez tabelę Products - określa typ kategorii dla każdego z produktów.
<p>
<h4>Read All</h4>
<p> 
<b>Hibernate:</b> select categories0_.categoryId as category1_0_, categories0_.categoryName as category2_0_, categories0_.description as descript3_0_ from Categories categories0_
</p>

<h4>Add New</h4>
<p>
<b>Hibernate:</b> select max(categoryId) from Categories<br/>
<b>Hibernate:</b> insert into Categories (categoryName, description, categoryId) values (?, ?, ?)
</P>

<h4>Update</h4>
<p>
<b>Hibernate:</b> update Categories set categoryName=?, description=? where categoryId=?
</p>

<h4>Delete - successful</h4>
<p>
<b>Hibernate:</b> delete from Categories_Products where Categories_categoryId=?</br>
<b>Hibernate:</b> delete from Categories where categoryId=?
</p>

<h4>Delete - unsuccessful</h4>
<p>
<b>Hibernate:</b> delete from Categories_Products where Categories_categoryId=?<br/>
<b>Hibernate:</b> delete from Categories where categoryId=?<br/>
Jan 17, 2018 6:14:07 PM dao.CategoriesDAOImpl removeCategoryById<br/>
INFO: Category deleted successfully, Category details = Categories{categoryId=8, categoryName='Seafood', description='Seaweed and fish'}<br/>
Jan 17, 2018 6:14:07 PM org.hibernate.engine.jdbc.spi.SqlExceptionHelper logExceptions<br/>
WARN: SQL Error: 0, SQLState: 23503<br/>
Jan 17, 2018 6:14:07 PM org.hibernate.engine.jdbc.spi.SqlExceptionHelper logExceptions<br/>
ERROR: ERROR: update or delete on table "categories" violates foreign key constraint "fk_products_categories" on table "products"<br/>
Detail: Key (categoryid)=(8) is still referenced from table "products".<br/>
</p>

<h3>Suppliers CRUD</h3>
<h4>Read All</h4>
<p> 
<b>Hibernate</b>: select suppliers0_.supplierId as supplier1_7_, suppliers0_.address as address2_7_, suppliers0_.city as city3_7_, suppliers0_.companyName as companyN4_7_, suppliers0_.contactName as contactN5_7_, suppliers0_.contactTitle as contactT6_7_, suppliers0_.country as country7_7_, suppliers0_.fax as fax8_7_, suppliers0_.homePage as homePage9_7_, suppliers0_.phone as phone10_7_, suppliers0_.postalCode as postalC11_7_, suppliers0_.region as region12_7_ from Suppliers suppliers0_ </br>
</p>

<h4>Add New</h4>
<p>
<b>Hibernate:</b> insert into Suppliers (address, city, companyName, contactName, contactTitle, country, fax, homePage, phone, postalCode, region, supplierId) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
</p>

<h4>Update</h4>
<p>
<b>Hibernate:</b> update Suppliers set address=?, city=?, companyName=?, contactName=?, contactTitle=?, country=?, fax=?, homePage=?, phone=?, postalCode=?, region=? where supplierId=?
</p>

<h4>Delete</h4>
<p>
<b>Hibernate:</b> delete from Suppliers where supplierId=?
</p>

<h3>Products CRUD</h3>
<h4>Search</h4>
<p>
<b>Hibernate:</b> select products0_.productid as producti1_6_, products0_.categoryId as category9_6_, products0_.discontinued as disconti2_6_, products0_.productname as productn3_6_, products0_.quantityperunit as quantity4_6_, products0_.reorderlevel as reorderl5_6_, products0_.supplierId as supplie10_6_, products0_.unitprice as unitpric6_6_, products0_.unitsinstock as unitsins7_6_, products0_.unitsonorder as unitsono8_6_ from Products products0_ where categoryId=? and products0_.unitprice>=? and products0_.unitprice<=?</br>
<b>Hibernate:</b> select categories0_.categoryId as category1_0_0_, categories0_.categoryName as category2_0_0_, categories0_.description as descript3_0_0_ from Categories categories0_ where categories0_.categoryId=?
</p>

<h4>Add New</h4>
<p>
<b>Hibernate:</b> select max(productid) from Products</br>
<b>Hibernate:</b> insert into Products (categoryId, discontinued, productname, quantityperunit, reorderlevel, supplierId, unitprice, unitsinstock, unitsonorder, productid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
</p>

<h4>Update</h4>
<p>
<b>Hibernate:</b> update Products set categoryId=?, discontinued=?, productname=?, quantityperunit=?, reorderlevel=?, supplierId=?, unitprice=?, unitsinstock=?, unitsonorder=? where productid=?
</p>

<h4>Delete</h4>
<p>
<b>Hibernate:</b> delete from Products where productid=?
</p>

<h3>Make an order</h3>
<p>
<b>Hibernate:</b> select max(orderid) from Orders
<b>Hibernate:</b> select customers_.customerid, customers_.address as address2_2_, customers_.city as city3_2_, customers_.companyname as companyn4_2_, customers_.contactname as contactn5_2_, customers_.contacttitle as contactt6_2_, customers_.country as country7_2_, customers_.fax as fax8_2_, customers_.phone as phone9_2_, customers_.postalcode as postalc10_2_, customers_.region as region11_2_ from Customers customers_ where customers_.customerid=?</br>
<b>Hibernate:</b> insert into Orders (customerid, employeeId, freight, orderDate, requiredDate, shipAddress, shipCity, shipCountry, shipName, shipPostalCode, shipRegion, shipVia, shippedDate, orderid) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)</br>
<b>Hibernate:</b> insert into order_details (discount, quantity, unitprice, orderid, productid) values (?, ?, ?, ?, ?)</br>
<b>Hibernate:</b> insert into order_details (discount, quantity, unitprice, orderid, productid) values (?, ?, ?, ?, ?)</br>
<b>Hibernate:</b> insert into order_details (discount, quantity, unitprice, orderid, productid) values (?, ?, ?, ?, ?)</br>
</P>

<h3>Sales report generation</h3>
<p>
<b>Hibernate:</b> select extract(year from orders0_.orderDate) as col_0_0_, extract(month from orders0_.orderDate) as col_1_0_, sum(orderdetai1_.unitprice*orderdetai1_.quantity) as col_2_0_ from Orders orders0_ inner join order_details orderdetai1_ on (orders0_.orderid=orderdetai1_.orderid) group by extract(year from orders0_.orderDate) , extract(month from orders0_.orderDate) order by extract(year from orders0_.orderDate), extract(month from orders0_.orderDate) 
</p>

<h3>Customer report generation</h3>
<p>
<b>Hibernate:</b> select extract(year from orders0_.orderDate) as col_0_0_, extract(month from orders0_.orderDate) as col_1_0_, customers2_.companyname as col_2_0_, sum(orderdetai1_.unitprice*orderdetai1_.quantity) as col_3_0_ from Orders orders0_ inner join order_details orderdetai1_ on (orders0_.orderid=orderdetai1_.orderid) inner join Customers customers2_ on (orders0_.customerid=customers2_.customerid) where customers2_.companyname=? group by extract(year from orders0_.orderDate) , extract(month from orders0_.orderDate) , customers2_.companyname order by extract(year from orders0_.orderDate), extract(month from orders0_.orderDate), sum(orderdetai1_.unitprice*orderdetai1_.quantity) desc
</p>
