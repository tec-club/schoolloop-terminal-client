public class GradeBookEntry {
    public final String className;
    public final int period;
    public final Double percent;
    public final String grade;

    public GradeBookEntry(String cn, int p, Double pe, String g) {
        className = cn;
        period = p;
        percent = pe;
        grade = g;
    }

    public String toString() {
        return period + " " + className + " -- " + grade + "/" + percent;
    }

}