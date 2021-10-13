-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S7: Indexen
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------
-- LET OP, zoals in de opdracht op Canvas ook gezegd kun je informatie over
-- het query plan vinden op: https://www.postgresql.org/docs/current/using-explain.html


-- S7.1.
--
-- Je maakt alle opdrachten in de 'sales' database die je hebt aangemaakt en gevuld met
-- de aangeleverde data (zie de opdracht op Canvas).
--
-- Voer het voorbeeld uit wat in de les behandeld is:
-- 1. Voer het volgende EXPLAIN statement uit:
--    EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
--    Bekijk of je het resultaat begrijpt. Kopieer het explain plan onderaan de opdracht
-- 2. Voeg een index op stock_item_id toe:
--    CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
-- 3. Analyseer opnieuw met EXPLAIN hoe de query nu uitgevoerd wordt
--    Kopieer het explain plan onderaan de opdracht
-- 4. Verklaar de verschillen. Schrijf deze hieronder op.

--EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
--CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
--drop index ord_lines_si_id_idx

-- er word een bitmap index scan uitgevoerd bij een index


-- S7.2.
--
-- 1. Maak de volgende twee query’s:
-- 	  A. Toon uit de order tabel de order met order_id = 73590
-- 	  B. Toon uit de order tabel de order met customer_id = 1028
-- 2. Analyseer met EXPLAIN hoe de query’s uitgevoerd worden en kopieer het explain plan onderaan de opdracht
-- 3. Verklaar de verschillen en schrijf deze op
-- 4. Voeg een index toe, waarmee query B versneld kan worden
-- 5. Analyseer met EXPLAIN en kopieer het explain plan onder de opdracht
-- 6. Verklaar de verschillen en schrijf hieronder op

--"Index Scan using pk_sales_orders on orders  (cost=0.29..8.31 rows=1 width=155)"
--"  Index Cond: (order_id = 73590)"

--"Seq Scan on orders  (cost=0.00..1819.94 rows=107 width=155)"
--"  Filter: (customer_id = 1028)"

-- de een is een index scan en de ander een sequence scan

-- CREATE INDEX orders_costumer
-- ON orders(customer_id);

-- EXPLAIN SELECT * FROM orders where order_id=73590
-- EXPLAIN SELECT * FROM orders where customer_id=1028

--"Bitmap Heap Scan on orders  (cost=5.12..308.96 rows=107 width=155)"
--"  Recheck Cond: (customer_id = 1028)"
--"  ->  Bitmap Index Scan on orders_costumer  (cost=0.00..5.10 rows=107 width=0)"
--"        Index Cond: (customer_id = 1028)"

--er word nu een bitmap heap scan gedaan

-- S7.3.A
--
-- Het blijkt dat customers regelmatig klagen over trage bezorging van hun bestelling.
-- Het idee is dat verkopers misschien te lang wachten met het invoeren van de bestelling in het systeem.
-- Daar willen we meer inzicht in krijgen.
-- We willen alle orders (order_id, order_date, salesperson_person_id (als verkoper),
--    het verschil tussen expected_delivery_date en order_date (als levertijd),  
--    en de bestelde hoeveelheid van een product zien (quantity uit order_lines).
-- Dit willen we alleen zien voor een bestelde hoeveelheid van een product > 250
--   (we zijn nl. als eerste geïnteresseerd in grote aantallen want daar lijkt het vaker mis te gaan)
-- En verder willen we ons focussen op verkopers wiens bestellingen er gemiddeld langer over doen.
-- De meeste bestellingen kunnen binnen een dag bezorgd worden, sommige binnen 2-3 dagen.
-- Het hele bestelproces is er op gericht dat de gemiddelde bestelling binnen 1.45 dagen kan worden bezorgd.
-- We willen in onze query dan ook alleen de verkopers zien wiens gemiddelde levertijd 
--  (expected_delivery_date - order_date) over al zijn/haar bestellingen groter is dan 1.45 dagen.
-- Maak om dit te bereiken een subquery in je WHERE clause.
-- Sorteer het resultaat van de hele geheel op levertijd (desc) en verkoper.
-- 1. Maak hieronder deze query (als je het goed doet zouden er 377 rijen uit moeten komen, en het kan best even duren...)
SELECT orders.order_id, orders.order_date, orders.salesperson_person_id as verkoper , (orders.expected_delivery_date - orders.order_date) as levertijd, order_lines.quantity
FROM orders 
JOIN order_lines ON order_lines.order_id = orders.order_id 
WHERE (orders.expected_delivery_date - orders.order_date)>1.45 AND order_lines.quantity>250
ORDER BY orders.salesperson_person_id,(orders.expected_delivery_date - orders.order_date)

-- S7.3.B
--
-- 1. Vraag het EXPLAIN plan op van je query (kopieer hier, onder de opdracht)
-- 2. Kijk of je met 1 of meer indexen de query zou kunnen versnellen
-- 3. Maak de index(en) aan en run nogmaals het EXPLAIN plan (kopieer weer onder de opdracht) 
-- 4. Wat voor verschillen zie je? Verklaar hieronder.
EXPLAIN SELECT orders.order_id, orders.order_date, orders.salesperson_person_id as verkoper , (orders.expected_delivery_date - orders.order_date) as levertijd, order_lines.quantity
FROM orders 
JOIN order_lines ON order_lines.order_id = orders.order_id 
WHERE (orders.expected_delivery_date - orders.order_date)>1.45 AND order_lines.quantity>250
ORDER BY orders.salesperson_person_id,(orders.expected_delivery_date - orders.order_date);

-- CREATE INDEX onder_linde_quantity
-- ON order_lines(quantity);

-- CREATE INDEX onder_orders_date
-- ON orders(order_date);



-- CREATE INDEX orders_expected_delivery_date
-- ON orders(order_date);

-- er worden veel hashes gebruikt en een bitmap heat scan



-- S7.3.C
--
-- Zou je de query ook heel anders kunnen schrijven om hem te versnellen?
EXPLAIN SELECT orders.order_id, orders.order_date, orders.salesperson_person_id as verkoper , (orders.expected_delivery_date - orders.order_date) as levertijd, order_lines.quantity
FROM orders 
JOIN order_lines ON order_lines.order_id = orders.order_id AND order_lines.quantity>250 AND (orders.expected_delivery_date - orders.order_date)>1.45
ORDER BY orders.salesperson_person_id,(orders.expected_delivery_date - orders.order_date);

-- ik heb dit geprobeert maar lijkt geen verschil te hebben


