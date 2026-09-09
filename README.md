Konzolna Java aplikacija, ki na podlagi statičnih GTFS podatkov javnega potniškega prometa poišče in izpiše prihajajoče avtobuse za izbrano postajo v oknu naslednjih 2 ur od trenutka poizvedbe.

Zahteve in tehnologije
----------------------

*   **Java**: OpenJDK 17 ali novejši

*   **Orodje za gradnjo**: Apache Maven 3.8+

*   **Knjižnice za testiranje**:

    *   JUnit 5 (Jupiter 5.10.2)

    *   AssertJ (3.25.3)


Prevajanje in zagon
-------------------

### 1\. Prevajanje in gradnja paketa (JAR)

V korenskem imeniku projekta (kjer se nahaja pom.xml) zaženite:mvn clean package

Ukaz prevede projekt, požene teste in v mapi target/ ustvari izvedljivo datoteko bus-trips-1.0.0.jar.

### 2\. Zagon aplikacije

Aplikacija se zažene prek terminala s tremi obveznimi parametri:java -jar target/bus-trips-1.0.0.jar

**Primeri uporabe:**

*   Relativni izpis za postajo 2 (do 3 prihodi na linijo):java -jar target/bus-trips-1.0.0.jar 2 3 relative

*   Absolutni izpis za postajo 10 (do 2 prihoda na linijo):java -jar target/bus-trips-1.0.0.jar 10 2 absolute


Testiranje
----------

Vsi testi se zaženejo z ukazom:mvn test

**Struktura testov:**

*   **Unit testi (TimeUtilsTest, TimetableServiceUnitTest)**:

    *   Pretvorba gtfs v localtime.

    *   Pretvorba v primeru ura>24.

    *   Izračun minut med dvema časoma znotraj istega dneva.
    
    *   Izračun minut ob prehodu čez polnoč (npr. 23:50 -> 00:15).
    
    *   Preverjanje 2-urnega intervala vključno z mejnimi vrednostmi.
    
    * Formatiranje v relativno (min) in absolutno (HH:mm) obliko.


*   **Integracijski testi (TimetableServiceIntegrationTest)**:

    *   Uspešno branje prihodov za postajo 2 ob 12:00.

    *   Neobstoječa postaja vrne Optional.empty.

    *   Prihodi so kronološko urejeni po času.

    *   Postaja obstaja, vendar v 2 urah ni nobenega avtobusa.


Arhitektura in optimizacija pomnilnika
--------------------------------------

Aplikacija je zasnovana modularno in ločena na posamezne odgovornosti:

*   model: Nemutabilne podatkovne strukture (records: Stop, StopTime, Route, Arrival).

*   parser: GtfsParser skrbi za branje in razčlenjevanje tekstovnih GTFS datotek.

*   service: TimetableService povezuje podatke iz parserja in izvaja poslovno logiko.

*   util: TimeUtils vsebuje statične pomožne metode za časovne izračune (java.time).

*   Main: Vstopna točka z validacijo vhodnih parametrov in končnim formatiranim izpisom.


**Pomnilniška učinkovitost:**

*   **Vrstično branje brez celotnega nalaganja**: Datoteke stop\_times.txt in trips.txt se obdelujejo vrstično prek BufferedReader, pri čemer se sproti filtrirajo le zapisi za izbrano postajo.

*   **Zgodnje filtriranje**: 2-urno časovno okno se preveri neposredno ob branju stop\_times.txt, zato se v pomnilnik shranijo le veljavni prihodi.

*   **Ciljno iskanje povezav**: Podatki o linijah se iz trips.txt in routes.txt poiščejo samo za tiste identifikatorje (trip\_id, route\_id), ki so bili predhodno potrjeni kot relevantni.


*   **Uporabljeno orodje**: Gemini (spletni pogovorni chatbot) – pri razvoju niso bili uporabljeni avtonomni agenti ali CLI orodja.

* **Samostojno načrtovanje in implementacija kode**:
  * **Arhitekturna zasnova in modeliranje**: Samostojna postavitev večplastne arhitekture (`model`, `service`, `parser`, `util`) ter definiranje nemutabilnih podatkovnih struktur (`records`: `Stop`, `StopTime`, `Route`, `Arrival`) za varen prenos podatkov med sloji.

  * **Razvoj celotne poslovne logike**: Samostojno programiranje večinskega dela logike aplikacije, ki ni zahtevala specifičnih pomnilniških optimizacij — to vključuje relacijsko povezovanje podatkov (postaja $\rightarrow$ časi $\rightarrow$ vožnje $\rightarrow$ linije), časovne preračune v `TimeUtils`, obravnavo prehodov čez polnoč ter avtomatizirane teste.
  
  * **Razmejitev vloge AI pri optimizaciji**: Ker se pri svojih dosedanjih izkušnjah z Javo še nisem poglobljeno srečeval z naprednimi tehnikami optimizacije pomnilnika ob obdelavi večjih datotek, sem AI uporabil namensko kot posvetovalno orodje za usmeritve glede dobrih praks (npr. vrstično procesiranje z `BufferedReader`, izogibanje celotnemu branju v pomnilnik in uporaba ustreznih podatkovnih zbirk `Set`/`Map`). Vso dejansko kodo, njeno prilagoditev specifikaciji in integracijo v sistem pa sem napisal samostojno.


*   Predlogi za nadgradnjo naloge 


*   **Integracija GTFS Realtime (GTFS-RT)**:

    *   Podpora za Protocol Buffers tokove v živo, ki omogočajo upoštevanje zamud in odpovedi avtobusov v realnem času.

*   **Načrtovalnik poti (Routing Engine)**:

    *   Implementacija algoritma za iskanje optimalne poti med poljubnima postajama s prestopanji.

*   **Razvoj grafičnega uporabniškega vmesnika**:
