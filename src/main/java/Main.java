/**
 * Created by Marcin on 2017-12-21.
 */
public class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.dropDatabase();

        solution.graphDatabase.generateSimpleGrapgh();
        solution.graphDatabase.generateSampleData();

        solution.runAllTests();
    }
}
