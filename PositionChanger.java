package molcom;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

public class PositionChanger implements Callable<PositionChanger> {
    private ArrayList<Molecule> movingMoleculesSubset;
    private ArrayList<NanoMachine> nanoMachinesSubset;
    private final ReentrantLock lock = new ReentrantLock();
    private int leftXmargin, rightXmargin;
    private MolComSim sim;

    public List<Molecule> getMovingMoleculesSubset() {
        return movingMoleculesSubset;
    }

    public List<NanoMachine> getNanoMachinesSubset() {
        return nanoMachinesSubset;
    }

    public PositionChanger(int left, int right){
        this.movingMoleculesSubset = new ArrayList<>();
        this.nanoMachinesSubset = new ArrayList<>();
        this.leftXmargin = left;
        this.rightXmargin = right;
        this.sim = MolComSim.createInstance();
        // getNanoMachin();
    }

    private void getNanoMachin() {
        System.out.println(sim.getNanoMachines().size());
        for (NanoMachine nm : sim.getNanoMachines()) {
            if ((nm.getPosition().getX() >= this.leftXmargin) && (nm.getPosition().getX() < this.rightXmargin)) {
                this.nanoMachinesSubset.add(nm);
            }
        }
    }

    private void update() {
        movingMoleculesSubset.clear();
        for (Molecule m : sim.getMovingMolecules()) {
            if ((m.getPosition().getX() >= this.leftXmargin) && (m.getPosition().getX() < this.rightXmargin)) {
                this.movingMoleculesSubset.add(m);
            }
        }


    }

    @Override
    public PositionChanger call() throws Exception {
        lock.lock();
        try {
            update();
            for (Molecule m : this.movingMoleculesSubset) {
                m.move();
            }
        }  catch (Exception e) {
        } finally {
            lock.unlock();
        }

        return this;
    }
}