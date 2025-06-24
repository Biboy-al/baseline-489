package baseline.jobShop.simulation;

public class TestSimulation {

    public static void main(String[] args) {
        DynamicJobShopSimulation sim = new DynamicJobShopSimulation(1000, 10);

        double meanFlow = sim.startSimulation();

        System.out.println("meanFlow = " + meanFlow);
    }
}
