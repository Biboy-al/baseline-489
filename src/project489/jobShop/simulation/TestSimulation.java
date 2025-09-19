package project489.jobShop.simulation;

public class TestSimulation {

    public static void main(String[] args) {
        DynamicJobShopSimulation sim = new DynamicJobShopSimulation( null, null, null, 0, 0, 2500, 50, 5, 500);

        sim.run();

        System.out.println("meanFlow = " + sim.getMeanFlowTime());
    }
}
