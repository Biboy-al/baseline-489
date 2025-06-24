package baseline.jobShop.simulation;

public class TestSimulation {

    public static void main(String[] args) {
        DynamicJobShopSimulation sim = new DynamicJobShopSimulation(10, 10);

        float meanFlow = sim.startSimulation();

        System.out.println("meanFlow = " + meanFlow);
    }
}
