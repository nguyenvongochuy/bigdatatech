package bdt.hotel.utils;

public final class Constants {
	
	public static final String MASTER_RUN = "local[2]";
	
	// Kafka
	public final class Kafka {
		public static final String TOPIC = "HotelBookingSummary";
	    public static final String BOOTSTRAP_SERVERS_KEY = "bootstrap.servers";

	    private Kafka() {}
	}

	private Constants() {}
}