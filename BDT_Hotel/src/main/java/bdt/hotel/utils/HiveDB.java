package bdt.hotel.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;



public class HiveDB {
	
	private static final String HIVE_DRIVER_NAME = "org.apache.hive.jdbc.HiveDriver";
	private static final String HIVE_URL = "jdbc:hive2://localhost:10000/default";
	private static final String HIVE_USERNAME = "cloudera";
	private static final String HIVE_PASSWORD = "cloudera";
	public static final String HIVE_TABLE_NAME = "hotel";
	private static Connection con;
	
	private static final String HiveSQLCreateTable = 
	"(	key string, "+
	  	"hotel string, "+
		"is_canceled int," +
		"lead_time int," +
		"arrival_date_year int," +
		"arrival_date_month string," +
		"arrival_date_week_number int," +
		"arrival_date_day_of_month int," +
		"stays_in_weekend_nights int," +
		"stays_in_week_nights int," +
		"adults int," +
		"children int," +
		"babies int," +
		"meal string," +
		"country string," +
		"market_segment string," +
		"distribution_channel string," +
		"is_repeated_guest int," +
		"previous_cancellations int," + 
		"previous_bookings_not_canceled int," +
		"reserved_room_type string," +
		"assigned_room_type string," +
		"booking_changes int," +
		"deposit_type string," +
		"agent int," +
		"company int," +
		"days_in_waiting_list int," +
		"customer_type string," +
		"adr double," +
		"required_car_parking_spaces int," +
		"total_of_special_requests int," +
		"reservation_status string," +
		"reservation_status_date date)"; 

	
	public static void viewDateUseSQL(SQLContext sqlContext, String tableName, int numLine) {
		sqlContext.sql("select * from " + tableName).show(numLine);
	}
	
	public static void createTableBySelectUseSQL(SQLContext sqlContext, String tableName, String sparkTempViewName) {
		sqlContext.sql("create table " + tableName + " using hive as select * from "+ sparkTempViewName);
	}
	
	public static void createTableUseSQL(SQLContext sqlContext, String tableName, String schemaFields) {
		sqlContext.sql("create table if not exists " + tableName + " using hive " + schemaFields);
	}
	
	public static void dropTableUseSQL(SQLContext sqlContext, String tableName) {
		sqlContext.sql("drop table if exists " + tableName);
	}
	
	public static void truncateTableUseSQL(SQLContext sqlContext, String tableName) {
		sqlContext.sql("truncate table if exists " + tableName);
	}
	
	public static void insertDataUseSQL(SQLContext sqlContext, String tableName, String sparkTempViewName) {
		//sqlContext.sql("select * from hotel").show(100);
		//sqlContext.sql("create table h1 as select * from hotel");
		sqlContext.sql("insert into " + tableName + " select * from " + sparkTempViewName);
		//IF (SELECT count(*)FROM information_schema.tables WHERE table_schema ='databasename'AND table_name ='tablename') > 0
		//THEN
		//INSERT statement 
		//END IF
	}
	
	
	private static void createConnection() throws ClassNotFoundException{
		// get connection
	    if (con==null) {
	    	Class.forName(HIVE_DRIVER_NAME);
	    	try {
				con = DriverManager.getConnection(HIVE_URL, HIVE_USERNAME, HIVE_PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void createTableUseJDBC(String tableName) throws ClassNotFoundException, SQLException{
	    // get connection
		createConnection();
		
	    Statement stmt = con.createStatement();
	    //stmt.execute("select * from h1");
	    stmt.execute("create table if not exists " + tableName + HiveSQLCreateTable);
	
	}
	
	public static void insertDataUseJDBC(String tableName, Dataset<Row> dataset) throws ClassNotFoundException, SQLException {
	
		// get connection
		createConnection();
	
	    // create statement
	    Statement stmt = con.createStatement();
	    
	    StringBuilder sb = new StringBuilder();
	    //List<HotelDTO> dataDTO = dataset.collectAsList();
	    		//(List<HotelDTO>) dataset.toDF().collect();
	    //dataset.show(5);
	    //int count = 1;
	    for (Row row:dataset.collectAsList()) {
	    	//System.out.println("Insert row: " + count);
	    		    	
	    	sb.append("insert into " + tableName);
	    	sb.append(" values(" + row.getString(0) + ",");
	    	sb.append("'"+row.getString(1) + "',");
		    sb.append(Integer.parseInt(row.getString(2)) + ",");
		    sb.append(Integer.parseInt(row.getString(3)) + ",");
		    sb.append(Integer.parseInt(row.getString(4)) + ",");
		    sb.append("'"+row.getString(5) + "',");
		    sb.append(Integer.parseInt(row.getString(6)) + ",");
		    sb.append(Integer.parseInt(row.getString(7)) + ",");
		    sb.append(Integer.parseInt(row.getString(8)) + ",");
		    sb.append(Integer.parseInt(row.getString(9)) + ",");
		    sb.append(Integer.parseInt(row.getString(10)) + ",");
		    sb.append("'"+row.getString(11) + "',");
		    sb.append(Integer.parseInt(row.getString(12)) + ",");
		    sb.append("'"+row.getString(13) + "',");
		    sb.append("'"+row.getString(14) + "',");
		    sb.append("'"+row.getString(15) + "',");
		    sb.append("'"+row.getString(16) + "',");
		    sb.append(Integer.parseInt(row.getString(17)) + ",");
		    sb.append(Integer.parseInt(row.getString(18)) + ",");
		    sb.append(Integer.parseInt(row.getString(19)) + ",");
		    sb.append("'"+row.getString(20) + "',");
		    sb.append("'"+row.getString(21) + "',");
		    sb.append(Integer.parseInt(row.getString(22)) + ",");
		    sb.append("'"+row.getString(23) + "',");
		    sb.append("'"+row.getString(24) + "',");
		    sb.append("'"+row.getString(25) + "',");
		    sb.append(Integer.parseInt(row.getString(26)) + ",");
		    sb.append("'"+row.getString(27) + "',");
		    sb.append(Double.parseDouble(row.getString(28)) + ",");
		    sb.append(Integer.parseInt(row.getString(29)) + ",");
		    sb.append(Integer.parseInt(row.getString(30)) + ",");
		    sb.append("'"+row.getString(31) + "',");
		    sb.append("'"+row.getString(32) + "'");
		    sb.append(")");
	    
		   
		    
		    	//System.out.println(st.toString());
		    	stmt.execute(sb.toString());
		    	sb.setLength(0);
		    	
		    
		    
		    //count++;
	    }
	    
	    
	    
	    
		}
	
	public static void insertDataSetRowUseJDBC(String tableName, Dataset<Row> dataset) throws ClassNotFoundException, SQLException {
	
		// Saving data to a JDBC source
		Class.forName(HIVE_DRIVER_NAME);
		
		dataset.write()
		  .format("jdbc")
		  .mode("saveMode")
		  //.mode("overwrite")
		  //.mode("append")
		  .option("url", HIVE_URL)
		  .option("dbtable", tableName)
		  .option("user", HIVE_USERNAME)
		  .option("password", HIVE_PASSWORD)
		  
		  .save();
	}
	
}


