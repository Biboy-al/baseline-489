package baseline.jobShop.simulation;

public class TestSimulation {

    public static void main(String[] args) {
        DynamicJobShopSimulation sim = new DynamicJobShopSimulation( null, null, null, 0, 0, 2500, 50);

        double meanFlow = sim.startSimulation();

        System.out.println("meanFlow = " + meanFlow);
    }
}
