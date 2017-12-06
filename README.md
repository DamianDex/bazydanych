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

<h1>Przebieg tworzenia projektu</h1>
Prace nad projektem rozpoczęliśmy od utworzenia pliku konfiguracyjnego dla Hibernate'a, w którym zostały podane informacje niezbędne do połączenia z wybraną bazą danych<br/>
<img alt="Konfiguracja Hibernate'a" src="https://github.com/DamianDex/bazydanych/blob/master/images/HibernateKonfiguracja.png">


<h1>Wybrane technologie</h1>
Java <br/>
Hibernate <br/>
PostgreSQL <br/>  
Jenkins <br/>  
Swing <br/>
AutoIT <br/>

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