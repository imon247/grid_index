public final class DataPoint {
	private double x;
	private double y;
	private int id;
	public DataPoint(double x, double y, int id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}
	public static DataPoint createPoint(double x, double y, int id) {
		return new DataPoint(x, y, id);
	}
	public boolean equals(Object obj) {
		if(this==obj)
			return true;
		if(obj==null)
			return false;
		if (getClass() != obj.getClass())
            return false;
		DataPoint other = (DataPoint) obj;
		return this.x==other.x && this.y==other.y;
	}
	public int hashCode() {
		return (int)(this.x*1000) + (int)(this.y*1000);
	}
	public String toString() {
		return String.format("(%s, %s)", this.x, this.y);
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public int getId() {
		return this.id;
	}
	public double distanceTo(DataPoint dp) {
		return Math.sqrt(Math.pow(this.x - dp.x, 2) + Math.pow(this.y - dp.y, 2));
	}
}
