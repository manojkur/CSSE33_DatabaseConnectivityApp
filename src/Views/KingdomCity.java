package Views;

import java.sql.Date;

public class KingdomCity {
	public String Name;
	public String ShortName;
	public Date DateConquered;
	public long GDP;
	public String Succession;
	public String Type;
	public String cityName;
	public String Coordinates;
	public int population;

	public Object[] getRow() {
		Object[] o = { Name, ShortName, DateConquered, new Long(GDP), Succession, Type, cityName, Coordinates,
				new Integer(population) };
		return o;
	}
}