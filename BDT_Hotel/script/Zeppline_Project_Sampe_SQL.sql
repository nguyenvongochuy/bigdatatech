
-- SQL Hive for First select as requirement
SELECT hotel, COUNT(*) AS count_reservation FROM hotel WHERE is_canceled=0 GROUP BY hotel;

-- SQL Hive for Second select as requirement
SELECT hotel, country, COUNT (is_canceled) AS count_reservation FROM hotel WHERE is_canceled=0 AND arrival_date_month ='December' GROUP BY hotel, country ORDER BY count_reservation DESC LIMIT 10;

