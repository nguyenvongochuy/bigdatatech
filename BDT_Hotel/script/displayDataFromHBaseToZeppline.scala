import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.{ConnectionFactory,HBaseAdmin,HTable,Put,Get}
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.spark.sql.execution.datasources.hbase._
import org.apache.spark.sql.SparkSession

def catalog =
     s"""{
    	|"table":{"namespace":"default", "name":"Hotel", "tableCoder":"PrimitiveType"},
    	|"rowkey":"key",
    	|"columns":{
     	|"rowkey":{"cf":"rowkey", "col":"key", "type":"string"},
     	|"hotel":{"cf":"CF_HOTEL", "col":"hotel", "type":"string"},
     	
     	|"is_canceled":{"cf":"CF_BOOKING", "col":"is_canceled", "type":"string"},
     	|"lead_time":{"cf":"CF_BOOKING", "col":"lead_time", "type":"string"},
     	|"arrival_date_year":{"cf":"CF_BOOKING", "col":"arrival_date_year", "type":"string"},
     	|"arrival_date_month":{"cf":"CF_BOOKING", "col":"arrival_date_month", "type":"string"},
     	|"arrival_date_week_number":{"cf":"CF_BOOKING", "col":"arrival_date_week_number", "type":"string"},
     	|"arrival_date_day_of_month":{"cf":"CF_BOOKING", "col":"arrival_date_day_of_month", "type":"string"},
     	|"stays_in_weekend_nights":{"cf":"CF_BOOKING", "col":"stays_in_weekend_nights", "type":"string"},
     	|"stays_in_week_nights":{"cf":"CF_BOOKING", "col":"stays_in_week_nights", "type":"string"},
     	|"adults":{"cf":"CF_BOOKING", "col":"adults", "type":"string"},
     	|"children":{"cf":"CF_BOOKING", "col":"children", "type":"string"},
     	|"babies":{"cf":"CF_BOOKING", "col":"babies", "type":"string"},
     	|"meal":{"cf":"CF_BOOKING", "col":"meal", "type":"string"},
     	     	
     	|"country":{"cf":"CF_BOOKING_DETAILS", "col":"country", "type":"string"},
     	|"market_segment":{"cf":"CF_BOOKING_DETAILS", "col":"market_segment", "type":"string"},
     	|"distribution_channel":{"cf":"CF_BOOKING_DETAILS", "col":"distribution_channel", "type":"string"},
     	|"is_repeated_guest":{"cf":"CF_BOOKING_DETAILS", "col":"is_repeated_guest", "type":"string"},
     	|"previous_cancellations":{"cf":"CF_BOOKING_DETAILS", "col":"previous_cancellations", "type":"string"},
     	|"previous_bookings_not_canceled":{"cf":"CF_BOOKING_DETAILS", "col":"previous_bookings_not_canceled", "type":"string"},
     	|"reserved_room_type":{"cf":"CF_BOOKING_DETAILS", "col":"reserved_room_type", "type":"string"},
     	|"assigned_room_type":{"cf":"CF_BOOKING_DETAILS", "col":"assigned_room_type", "type":"string"},
     	|"booking_changes":{"cf":"CF_BOOKING_DETAILS", "col":"booking_changes", "type":"string"},
     	|"deposit_type":{"cf":"CF_BOOKING_DETAILS", "col":"deposit_type", "type":"string"},
     	|"agent":{"cf":"CF_BOOKING_DETAILS", "col":"agent", "type":"string"},
     	|"company":{"cf":"CF_BOOKING_DETAILS", "col":"company", "type":"string"},
     	|"days_in_waiting_list":{"cf":"CF_BOOKING_DETAILS", "col":"days_in_waiting_list", "type":"string"},
     	|"customer_type":{"cf":"CF_BOOKING_DETAILS", "col":"customer_type", "type":"string"},
     	|"adr":{"cf":"CF_BOOKING_DETAILS", "col":"adr", "type":"string"},
     	|"required_car_parking_spaces":{"cf":"CF_BOOKING_DETAILS", "col":"required_car_parking_spaces", "type":"string"},
     	|"total_of_special_requests":{"cf":"CF_BOOKING_DETAILS", "col":"total_of_special_requests", "type":"string"},
     	|"reservation_status":{"cf":"CF_BOOKING_DETAILS", "col":"reservation_status", "type":"string"},
     	|"reservation_status_date":{"cf":"CF_BOOKING_DETAILS", "col":"reservation_status_date", "type":"string"}
    	|}	 	
	|}""".stripMargin
val spark: SparkSession = SparkSession.builder()
      .master("local[1]")
      .appName("HotelBookingSummary1")
      .getOrCreate()

import spark.implicits._

// Reading from HBase to DataFrame
val hbaseDF = spark.read
  .options(Map(HBaseTableCatalog.tableCatalog -> catalog))
  .format("org.apache.spark.sql.execution.datasources.hbase")
  .load()

//Display Schema from DataFrame
hbaseDF.printSchema()

//Collect and show Data from DataFrame
hbaseDF.show(false)

//Create Temporary Table on DataFrame
hbaseDF.createOrReplaceTempView("hotel")

//Run SQL
spark.sql("select * from hotel limit 10").show